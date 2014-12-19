package br.com.whack.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import br.com.guilhermehps.whack.R;
import br.com.whack.core.MainThread;

public class MainView extends SurfaceView implements
SurfaceHolder.Callback {

	private MainThread thread;
	private static final String TAG = MainThread.class.getSimpleName();
	
	// Display metrics usdadas para converter pixels em dp
	private static DisplayMetrics dm;
	Context context;

	public MainView(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		// Get display metrics
		context.getResources();
		dm = Resources.getSystem().getDisplayMetrics();
		
		this.context = context;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (event.getY() > getHeight() - 50) {
				thread.setRunning(false);
				((Activity)getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
			}
		}

		Toast.makeText(context,"Width = "+dm.widthPixels+"\nHeight = "+dm.heightPixels, Toast.LENGTH_LONG).show();
		return super.onTouchEvent(event);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img480), 0, 0, null);
		
		/*Bitmap redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
		redBall = Bitmap.createScaledBitmap(redBall, convertPixelsToDp(100), convertPixelsToDp(100), true);
		
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueball), 100, 100, null);
		canvas.drawBitmap(redBall, 100, 100, null);*/
		
		Bitmap redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
		redBall = Bitmap.createScaledBitmap(redBall, convertPercentageToPixelsWidth(1), convertPercentageToPixelsHeight(1), true);
		canvas.drawBitmap(redBall, 0, 0, null);
	}
	
	public static int convertPixelsToDp(int px){
	    int dp = (int) (px / (dm.densityDpi / 160f));
	    return dp;
	}
	
	public static int convertPercentageToPixelsWidth(float percentage){
	    return (int) (dm.widthPixels*percentage);
	}
	
	public static int convertPercentageToPixelsHeight(float percentage){
	    return (int) (dm.heightPixels*percentage);
	}
}
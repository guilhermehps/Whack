package br.com.whack.core;

import br.com.whack.view.MainView;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	// flag to hold game state
	private boolean running;

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface
	private MainView gamePanel;

	private static final String TAG = MainThread.class.getSimpleName();

	// Construtor
	public MainThread(SurfaceHolder surfaceHolder, MainView gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@SuppressLint("WrongCall") @Override
	public void run() {
		Canvas canvas;
		long tickCount = 0L;
		Log.d(TAG, "Starting game loop");

		while (running) {
			canvas = null;
			tickCount++;
			// update game state
			// render state to the screen

			try {
				canvas = this.surfaceHolder.lockCanvas();
				if (canvas != null) {
					synchronized (surfaceHolder) {
						// update game state
						// draws the canvas on the panel
						this.gamePanel.onDraw(canvas);
					}
				}
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			} // end finally
		}
		Log.d(TAG, "Game loop executed " + tickCount + " times");
	}
}
package br.com.criandojogosandroid.cap03ex02_surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    Tela tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        tela = new Tela(this);
        setContentView(tela);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tela.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tela.pause();
    }

    public class Tela extends SurfaceView implements Runnable {

        Thread surfaceThread = null;
        SurfaceHolder holder;
        boolean running = false;

        public Tela(Context context) {
            super(context);
            holder = getHolder();
        }

        public void resume() {
            running = true;
            surfaceThread = new Thread(this);
            surfaceThread.start();
        }

        public void pause() {
            running = false;
            while (true) {
                try {
                    surfaceThread.join();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            surfaceThread = null;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (running) {
                if (!holder.getSurface().isValid())
                    continue;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        canvas = holder.getSurface().lockHardwareCanvas();
                    } else {
                        canvas = holder.lockCanvas();
                    }

                    //Códigos de desenho vão aqui

                } finally {
                    if (canvas != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            holder.getSurface().unlockCanvasAndPost(canvas);
                        } else {
                            holder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }
    }
}

package cn.org.orchid.aircraftwar2024;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class gameSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private boolean isDrawing;
    public gameSurfaceView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        isDrawing = false;

    }

    @Override
    public void run() {
        while(isDrawing) {

        }
    }

    //绘图操作
    private void draw() {
        try {
            canvas = surfaceHolder.lockCanvas();
            //TODO draw过程

        } catch (Exception e) {

        } finally {
            if(canvas != null){
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

}

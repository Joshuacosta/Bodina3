package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 07/10/2015.
 */
/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.View.OnTouchListener;

        import java.util.ArrayList;
        import java.util.List;

public class SimpleDrawView extends View implements OnTouchListener {

    private static final float STROKE_WIDTH = 5f;

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    public SimpleDrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Path path = new Path();
        boolean first = true;
        for (Point point : points) {
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }

        canvas.drawPath(path, paint);
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                Point point = new Point();
                point.x = event.getHistoricalX(i);
                point.y = event.getHistoricalY(i);
                points.add(point);
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void clear() {
        points.clear();
    }

    class Point {
        float x, y;
        float dx, dy;

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
*/

import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by it00046 on 26/01/2015.
 */
//public class DrawingView extends View {
public class SimpleDrawView extends RelativeLayout {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    // Experiment
    private PointF startPoint = null, endPoint = null;
    public boolean isDrawing;

    private ArrayList<Linia> Linies = new ArrayList<Linia>();
    class Linia{
        public PointF Inici, Final;
        public void Linia(){
        }
    }

    private float brushSize, lastBrushSize;

    private boolean erase=false;

    public SimpleDrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        //get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        brushSize = 2;
        lastBrushSize = brushSize;
        drawPaint.setStrokeWidth(brushSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    // La definida inicialment

    @Override
    protected void onDraw(Canvas canvas) {
        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        if (endPoint != null) {
            //drawPath.moveTo(startPoint.x, startPoint.y);
            //drawPath.lineTo(endPoint.x, endPoint.y);
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, drawPaint);
        }
        //canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //
                startPoint = new PointF(event.getX(), event.getY());
                //drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                endPoint = new PointF(event.getX(), event.getY());
                //drawPath.lineTo(touchX, touchY);
                //
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
    /*
    // Prova per dibuixar linies
    @Override
    protected void onDraw(Canvas canvas)
    {
        if(isDrawing)
        {
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, drawPaint);
            canvas.drawCircle(endPoint.x, endPoint.y, 25, drawPaint);
            // Pinto la resta de linies
            for (int i=0; i<Linies.size(); i++){
                Linia Aux = Linies.get(i);
                canvas.drawLine(Aux.Inici.x, Aux.Inici.y, Aux.Final.x, Aux.Final.y, drawPaint);
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // Validem si estem damunt d
                startPoint = new PointF(event.getX(), event.getY());
                endPoint = new PointF();
                isDrawing = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDrawing){
                    endPoint.x = event.getX();
                    endPoint.y = event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isDrawing){
                    endPoint.x = event.getX();
                    endPoint.y = event.getY();
                    isDrawing = false;
                    // Nova linia
                    Linia Lin = new Linia();
                    Lin.Inici = startPoint;
                    Lin.Final = endPoint;
                    Linies.add(Lin);
                }
                break;
            default:
                return false;
        }
        return true;
    }
    // Fi experiment
       */
    public void setColor(String newColor){
        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize){
        //update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);

    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    // Drag and drop

}

package ru.asdivov.aviahorizont;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class AviahorizontView extends View {

    private Paint paint;
    private Paint paintSky;
    private Paint paintGround;
    private Paint paintPanel;

    private float yaw = 0;
    private float tangage = 0;
    private float pan = 0;

    private Bitmap mBitmapArrow;

    public AviahorizontView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

        paintSky = new Paint();
        paintSky.setAntiAlias(true);
        paintSky.setStrokeWidth(2);
        paintSky.setStyle(Paint.Style.FILL);
        paintSky.setColor(Color.parseColor("#19b2c0"));

        paintGround = new Paint();
        paintGround.setAntiAlias(true);
        paintGround.setStrokeWidth(2);
        paintGround.setStyle(Paint.Style.FILL);
        paintGround.setColor(Color.parseColor("#aa5d39"));

        paintPanel = new Paint();
        paintPanel.setAntiAlias(true);
        paintPanel.setStrokeWidth(2);
        paintPanel.setStyle(Paint.Style.FILL);
        paintPanel.setColor(Color.DKGRAY);

        Resources res = this.getResources();
        mBitmapArrow = BitmapFactory.decodeResource(res, R.drawable.arrow);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int xPoint = getMeasuredWidth() / 2;
        int yPoint = getMeasuredHeight() / 2;

        float radius = (float) (Math.min(xPoint, yPoint) * 0.75);
        canvas.drawCircle(xPoint, yPoint, radius, paint);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        // 3.143 is a good approximation for the circle
        canvas.drawLine(
                (float) (xPoint + radius
                        * Math.sin((double) (-yaw + 90 - tangage) / 180 * 3.143)),
                (float) (yPoint - radius
                        * Math.cos((double) (-yaw + 90 - tangage) / 180 * 3.143)),
                (float) (xPoint - radius
                        * Math.sin((double) (-yaw + 90 + tangage) / 180 * 3.143)),
                (float) (yPoint + radius
                        * Math.cos((double) (-yaw + 90 + tangage) / 180 * 3.143)), paint);

        canvas.drawCircle(xPoint, yPoint, radius * 1.2f, paintPanel);
        canvas.drawCircle(xPoint, yPoint, radius, paintSky);

        final RectF rectf = new RectF();
        rectf.set(xPoint - radius, yPoint - radius, xPoint + radius,
                yPoint + radius);

        Float startAngle = - yaw - tangage / 2;
        Float sweepAngle = 180 + tangage;

        canvas.drawArc(rectf, startAngle, sweepAngle, false, paintGround);

        canvas.save();
        canvas.rotate(-pan,
                (float) (xPoint + radius
                        * Math.sin((double) (-pan) / 180 * 3.143)),
                (float) (yPoint - radius
                        * Math.cos((double) (-pan) / 180 * 3.143)));

        canvas.drawBitmap(mBitmapArrow,
                (float) (xPoint + radius
                        * Math.sin((double) (-pan) / 180 * 3.143)),
                (float) (yPoint - radius
                        * Math.cos((double) (-pan) / 180 * 3.143)),
                paint);
        canvas.restore();


        for(int i = 0; i < 360; i = i + 30){
            canvas.save();
            canvas.rotate(-i,
                    (float) (xPoint + radius * 1.05f
                    * Math.sin((double) (-i) / 180 * 3.143)),
                    (float) (yPoint - radius * 1.05f
                    * Math.cos((double) (-i) / 180 * 3.143)));

            canvas.drawText(
                    String.valueOf(i),
                    (float) (xPoint + radius * 1.05f
                    * Math.sin((double) (-i) / 180 * 3.143)),
                    (float) (yPoint - radius * 1.05f
                            * Math.cos((double) (-i) / 180 * 3.143)),
                    paint);
            canvas.restore();
        }

        canvas.drawText("YAW: " + String.format("%.1f",- yaw), xPoint, yPoint, paint);
        canvas.drawText("TAN: " + String.format("%.1f", -tangage), xPoint, yPoint+30, paint);
        canvas.drawText("PAN: " + String.format("%.1f", pan), xPoint, yPoint+60, paint);
    }

    public void updateData(float yaw, float tangage, float pan) {
        this.yaw = yaw;
        this.tangage = tangage;
        this.pan = pan;
        invalidate();
    }

}
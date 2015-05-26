package ru.asdivov.aviahorizont;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class AviahorizontView extends View {

    private Paint paint;
    private Paint paintSky;
    private Paint paintGround;

    private float yaw = 0;
    private float tangage = 0;

    public AviahorizontView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int xPoint = getMeasuredWidth() / 2;
        int yPoint = getMeasuredHeight() / 2;

        float radius = (float) (Math.min(xPoint, yPoint) * 0.95);
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

        canvas.drawCircle(xPoint, yPoint, radius, paintSky);

        final RectF rectf = new RectF();
        rectf.set(xPoint - radius, yPoint - radius, xPoint + radius,
                yPoint + radius);

        Float startAngle = - yaw - tangage / 2;
        Float sweepAngle = 180 + tangage;

        canvas.drawArc(rectf, startAngle, sweepAngle, false, paintGround);

        canvas.drawText("YAW: " + String.valueOf(yaw), xPoint, yPoint, paint);
        canvas.drawText("TAN: " + String.valueOf(tangage), xPoint, yPoint+30, paint);
    }

    public void updateData(float yaw, float tangage) {
        this.yaw = yaw;
        this.tangage = tangage;
        invalidate();
    }

}
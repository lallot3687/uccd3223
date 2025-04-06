package my.edu.utar.individual;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Menu extends View {
    private final Paint paint;
    private final Activity activity;
    public Menu(Activity activity){
        super(activity);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(50);
        Path path = new Path();
        this.activity = activity;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas){
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float margin = 30;

        canvas.drawColor(Color.WHITE);

        //1 Draw header background
        Bitmap curveImage = BitmapFactory.decodeResource(getResources(),R.drawable.curve);
        canvas.drawBitmap(curveImage, 6,-30,null);

        //2 Background Bee
        ImageView grass = ImageDecode(R.drawable.grass);
        setImage(grass,width,height/2,0,1700);

        //4 Menu text
        ImageView menu = new ImageView(getContext());
        menu.setImageResource(R.drawable.menu);
        setImage(menu,500,250,475,100);

        //6 Topics
        drawText(canvas,paint,Color.BLACK,110,true,"Topics",565,650);

        //7 Bee
        ImageView bee = ImageDecode(R.drawable.bee);
        setImage(bee,600,600,800,320);
        //8 Compare Button
        ImageView compare = ImageDecode(R.drawable.compare);
        setImage(compare,1500,700,-20,700);
        compare.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
               scaleAnimation(compare);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setContentView(new CompareActivity(activity));
                    }
                }, 500); // Delay time (same as animation duration)
            }
        });

        //9 Order Button
        ImageView order = ImageDecode(R.drawable.order);
        setImage(order,1500,700,-20,1410);
        order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleAnimation(order);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setContentView(new OrderActivity(activity));
                    }
                }, 500); // Delay time (same as animation duration)
            }
        });

        //10 Compose Button
        ImageView compose = ImageDecode(R.drawable.compose);
        setImage(compose, 1250,700,100,2130);
        compose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleAnimation(compose);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setContentView(new ComposeActivity(activity));
                    }
                }, 500); // Delay time (same as animation duration)

            }
        });
    }

    public void setImage(ImageView components, int width, int height, int X, int Y){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.leftMargin = X;
        params.topMargin = Y;
        components.setLayoutParams(params);
        ((ViewGroup) getParent()).addView(components);
    }

    public void drawText(Canvas canvas,Paint paint,int color,int size,boolean status,String text,int x , int y){
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(status);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        paint.setTextSize(size);
        canvas.drawText(text,x,y,paint);

    }

    public ImageView ImageDecode (int image){

        ImageView iv = new ImageView(getContext());
        Bitmap bm = BitmapFactory.decodeResource(getResources(),image);
        iv.setImageBitmap(bm);
        return iv;
    }


    private void scaleAnimation(ImageView iv){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv,"scaleX",1.0f,1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv,"scaleY",1.0f,1.1f);

        scaleX.setDuration(0);
        scaleY.setDuration(0);

        scaleX.start();
        scaleY.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(iv, "scaleX", 1.1f, 1.0f);
                ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(iv, "scaleY", 1.1f, 1.0f);
                scaleXBack.setDuration(0);
                scaleYBack.setDuration(0);
                scaleXBack.start();
                scaleYBack.start();
            }
        }, 300); // 1000ms (1-second delay before shrinking back)

    }


}

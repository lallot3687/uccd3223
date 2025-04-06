package my.edu.utar.individual;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.Random;


public class CompareActivity extends View {
    private final Activity activity;
    private TextView num1,num2,ctR,ctW;
    private ImageView wrongIndicator;
    private ImageView correctIndicator;
    private ImageView wrong;
    private ImageView check;
    private ImageView refresh;
    private int number1, number2,countRight,countWrong;
    private MediaPlayer mediaPlayer;
    private boolean isAnimationRunning = false; // Flag to prevent multiple clicks

    public CompareActivity(Activity activity){

        super(activity);
        this.activity = activity;

    }



    @Override
    protected void onDraw(@NonNull Canvas canvas){

        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        //Background color
        canvas.drawColor(Color.parseColor("#E5e5e5"));

        //birds
        ImageView birds1 = ImageDecode(R.drawable.birds);
        setImage(birds1,600,600,50,500);
        birds1.setZ(0);

        ImageView birds2 = ImageDecode(R.drawable.birds);
        setImage(birds2,600,600,700,700);

        //woodstand
        ImageView woodstand = ImageDecode(R.drawable.woodstand);
        setImage(woodstand,800,780,-100,2100);

        //grass background
        ImageView grass = ImageDecode(R.drawable.grasscompare);
        setImage(grass,width +550,1000,0,2040);

        //Header Background
        ImageView headercurve = ImageDecode(R.drawable.curvecompare);
        setImage(headercurve,width,900,0,-250);

        //back icon
        ImageView back = ImageDecode(R.drawable.back);
        setImage(back,250,250,100,100);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                scaleAnimation(back);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setContentView(new Menu(activity));
                    }
                }, 500); // Delay time (same as animation duration)

            }
        });


        //compare text
        ImageView compare = ImageDecode(R.drawable.comparetext);
        setImage(compare,700,430,375,7);

        //refresh icon
        refresh = ImageDecode(R.drawable.refresh);
        setImage(refresh,250,250,1100,100);
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                num1.setText("");
                num2.setText("");
                generateNumbers();
            }
        });

        //rectangle
        ImageView rectangle = ImageDecode(R.drawable.rectangle);
        setImage(rectangle,1440,1300,0,850);
        rectangle.setAlpha(0.8f);
        rectangle.setZ(0);

        //backgro
        ImageView backgro = ImageDecode(R.drawable.backgro);
        setImage(backgro,900,700,400,1380);
        backgro.setZ(0);
        backgro.setAlpha(0.2f);

        //Text
        TextView text = new TextView(getContext());
        setText(text,"Choose the correct symbol.",25,Color.BLACK,50,950,3);

        //Check and wrong icon
        check = new ImageView(getContext());
        check.setImageResource(R.drawable.checkcricle);
        setImage(check,200,200,1230,1880);
        check.setZ(11);
        check.setVisibility(View.INVISIBLE);

        wrong = new ImageView(getContext());
        wrong.setImageResource(R.drawable.cancel);
        setImage(wrong,200,200, 1230,1880);
        wrong.setZ(11);
        wrong.setVisibility(View.INVISIBLE);

        //Number Containers
        View container1 = new View(getContext());
        setShape(container1,"#FBCF03",350,350,120,1100,0,1,50,0);
        View container2 = new View(getContext());
        setShape(container2,"#FBCF03",350,350,980,1100,0,1,50,0);
        View container3 = new View(getContext());
        setShape(container3,"#D9D9D9",350,300,550,1125,0,1,20,0);

        //greater lesser Button
        View grtbtn =  new View (getContext());
        setShape(grtbtn,"#F24668",350,300,350,1600,3,1,20,100);
        ImageView icon1 = new ImageView(getContext());
        icon1.setImageResource(R.drawable.arrow);
        setImage(icon1,250,250,440,1625);
        icon1.setZ(11);
        grtbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setEnabled(false);
                moveImage(grtbtn,icon1,660,1150,550,1125,500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAnswers("<");
                    }
                }, 500); // Delay time (same as animation duration)

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setEnabled(true);
                    }
                }, 500); // Delay time (same as animation duration)

            }
        });
        icon1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setEnabled(false);
                moveImage(grtbtn,icon1,660,1150,550,1125,500);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAnswers("<");
                    }
                }, 500); // Delay time (same as animation duration)
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setEnabled(true);
                    }
                }, 500); // Delay time (same as animation duration)

            }
        });



        View lstbtn =  new View (getContext());
        setShape(lstbtn,"#F24668",350,300,750,1600,3,1,20,100);
        ImageView icon2 = new ImageView(getContext());
        icon2.setImageResource(R.drawable.arrow);
        setImage(icon2,250,250,750,1625);
        icon2.setZ(10);
        icon2.setRotation(180);
        lstbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setEnabled(false);
                moveImage(lstbtn,icon2,550,1150,550,1125,500);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAnswers(">");
                    }
                }, 500); // Delay time (same as animation duration)

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setEnabled(true);
                    }
                }, 500); // Delay time (same as animation duration)
            }
        });
        icon2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setEnabled(false);
                moveImage(lstbtn,icon2,550,1150,550,1125,500);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAnswers(">");

                    }
                }, 500); // Delay time (same as animation duration)

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setEnabled(true);
                    }
                }, 500); // Delay time (same as animation duration)

            }

        });


        //Bird image
        ImageView bird = ImageDecode(R.drawable.bird);
        setImage(bird,600,600,20,1600);

        //Generate Number
        generateNumbers();

        //wrong and correct indicator
        correctIndicator = new ImageView(getContext());
        correctIndicator = ImageDecode(R.drawable.correct);
        setImage(correctIndicator,700,310,650,2200);
        correctIndicator.setZ(10);
        correctIndicator.setVisibility(View.INVISIBLE);

        wrongIndicator = new ImageView(getContext());
        wrongIndicator = ImageDecode(R.drawable.wrong);
        setImage(wrongIndicator,700,310,650,2200);
        wrongIndicator.setVisibility(View.INVISIBLE);

        //Record
        ImageView rightIcon = new ImageView(getContext());
        rightIcon.setImageResource(R.drawable.checkcricle);
        setImage(rightIcon,150,150,200,2180);

        ImageView wrongIcon = new ImageView(getContext());
        wrongIcon.setImageResource(R.drawable.cancel);
        setImage(wrongIcon,150,150,200,2330);

        recordCount();



    }

    public void setImage(ImageView components, int width, int height, int leftMargin, int topMargin){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        params.width = width;
        params.height = height;
        components.setLayoutParams(params);
        ((ViewGroup) getParent()).addView(components);
    }

    public void setText(TextView components,String text,int size,int color,int x,int y,int z){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        components.setText(text);
        components.setTextSize(size);
        components.setTextColor(color);
        components.setTypeface(Typeface.DEFAULT_BOLD);
        params.leftMargin = x;
        params.topMargin = y;
        components.setZ(z);
        components.setLayoutParams(params);

        ((ViewGroup) getParent()).addView(components);

    }

    public ImageView ImageDecode (int image){

        ImageView iv = new ImageView(getContext());
        Bitmap bm = BitmapFactory.decodeResource(getResources(),image);
        iv.setImageBitmap(bm);
        return iv;
    }
    public void setShape(View view,String colorcode, int width, int height, int x, int  y,int  z ,float alpha, int corner,float shadow){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(colorcode));
        drawable.setCornerRadius(corner);
        view.setBackground(drawable);
        view.setElevation(shadow);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.leftMargin = x;
        params.topMargin = y;

        view.setLayoutParams(params);

        view.setZ(z);
        view.setAlpha(alpha);

        ((ViewGroup) getParent()).addView(view);

    }

    private int getSize1(int number) {
        int digits = (number == 0) ? 1 : (int) Math.log10(number) + 1;

        if (digits == 1) {
            return 250;
        }
        else if (digits == 2) {
            return 225;
        }
        else {
            return 200;
        }

    }

    private int getSize2(int number) {
        int digits = (number == 0) ? 1 : (int) Math.log10(number) + 1;

        if (digits == 1) {
            return 1120;
        }
        else if (digits == 2) {
            return 1090;
        }
        else {
            return 1060;
        }
    }

    private void generateNumbers(){
        num1 = new TextView(getContext());
        num2 = new TextView(getContext());

        Random random = new Random();
        number1 = random.nextInt(100) + 1;
        number2 = random.nextInt(100) + 1;

        while (number1 == number2 ){
            number2 = random.nextInt(100) + 1;
        }

        int size1 = getSize1(number1);
        int size2 = getSize2(number2);

        setText(num1,String.valueOf(number1),35,Color.BLACK,size1,1190,3);
        setText(num2,String.valueOf(number2),35,Color.BLACK,size2,1190,3);

    }

    private void recordCount(){
        ctR =  new TextView(getContext());
        ctW =  new TextView(getContext());

        setText(ctR,String.valueOf(countRight),30,Color.BLACK,370,2180,3);
        setText(ctW,String.valueOf(countWrong),30,Color.BLACK,370,2330,3);
    }

    private void checkAnswers(String operator){
        boolean isCorrect = (operator.equals("<") && number1 < number2) || (operator.equals(">") && number1 > number2);

        if (isCorrect){
            correctIndicator.setVisibility(View.VISIBLE);
            wrongIndicator.setVisibility(View.INVISIBLE);
            check.setVisibility(View.VISIBLE);
            countRight++;
        }
        else {
            correctIndicator.setVisibility(View.INVISIBLE);
            wrongIndicator.setVisibility(View.VISIBLE);
            wrong.setVisibility(View.VISIBLE);
            countWrong++;

            showImageOverlay(R.drawable.catlaugh);
            playSound(getContext(),R.raw.laughingcat);

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                correctIndicator.setVisibility(View.INVISIBLE);
                wrongIndicator.setVisibility(View.INVISIBLE);
                num1.setText("");
                num2.setText("");
                check.setVisibility(View.INVISIBLE);
                wrong.setVisibility(View.INVISIBLE);
                ctR.setText("");
                ctW.setText("");
                recordCount();
                generateNumbers();// Hide image after 5s

            }
        }, 1100); // 5000 milliseconds = 5 seconds


    }

    private void moveImage(View v,ImageView iv,int x1,int y1,int x2, int y2, int duration){
        if (isAnimationRunning){
            return;
        }
        isAnimationRunning = true;
        float originX1 = v.getX();
        float originY1 = v.getY();

        float originX2 = iv.getX();
        float originY2 = iv.getY();

        ObjectAnimator animatorX1 = ObjectAnimator.ofFloat(iv, "x", x1);
        ObjectAnimator animatorY1 = ObjectAnimator.ofFloat(iv,"y",y1);
        ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(v,"x",x2);
        ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(v,"y",y2);

        animatorX1.setDuration(duration);
        animatorY1.setDuration(duration);
        animatorX2.setDuration(duration);
        animatorY2.setDuration(duration);

        animatorX1.start();
        animatorY1.start();
        animatorX2.start();
        animatorY2.start();

        animatorX1.addListener(new Animator.AnimatorListener() {


            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        returnImage(v, iv, originX1, originY1, originX2, originY2, duration);
                        isAnimationRunning = false;
                    }
                }, duration); //

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
                isAnimationRunning = false;
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {}
        });




    }

    private void returnImage (View v, ImageView iv,float x1, float y1,float x2,float y2,int duration){
        ObjectAnimator returnX1 = ObjectAnimator.ofFloat(v,"x",x1);
        ObjectAnimator returnY1 = ObjectAnimator.ofFloat(v,"y",y1);
        ObjectAnimator returnX2 = ObjectAnimator.ofFloat(iv,"x",x2);
        ObjectAnimator returnY2 = ObjectAnimator.ofFloat(iv,"y",y2);

        returnX1.setDuration(duration);
        returnY1.setDuration(duration);
        returnX2.setDuration(duration);
        returnY2.setDuration(duration);

        returnX1.start();
        returnY1.start();
        returnX2.start();
        returnY2.start();


    }

    private void scaleAnimation(ImageView iv){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv,"scaleX",1.0f,1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv,"scaleY",1.0f,1.2f);

        scaleX.setDuration(400);
        scaleY.setDuration(400);

        scaleX.start();
        scaleY.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(iv, "scaleX", 1.2f, 1.0f);
                ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(iv, "scaleY", 1.2f, 1.0f);
                scaleXBack.setDuration(200);
                scaleYBack.setDuration(200);
                scaleXBack.start();
                scaleYBack.start();
            }
        }, 400); // 1000ms (1-second delay before shrinking back)

    }
    private void showImageOverlay(int image) {
        Context context = getContext(); // Get the context from View
        if (!(context instanceof Context)) return; // Ensure it's valid

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Create a layout for the dialog
        FrameLayout container = new FrameLayout(context);
        container.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Create an ImageView
        ImageView catImage = new ImageView(context);
        catImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),image));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(600, 600);
        params.gravity = Gravity.CENTER;
        catImage.setLayoutParams(params);

        // Add ImageView to container
        container.addView(catImage);

        // Set the container as the dialog view
        builder.setView(container);
        AlertDialog dialog = builder.create();

        // Make the dialog background transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(dialog::dismiss, 3000);

    }



    public void playSound(Context context, int soundResource) {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release old instance
        }
        mediaPlayer = MediaPlayer.create(context, soundResource);
        mediaPlayer.start();
    }

    public void stopSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



}

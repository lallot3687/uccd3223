package my.edu.utar.individual;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import android.widget.TextView;

import java.util.Random;

public class ComposeActivity extends View {
    private final Activity activity;
    private TextView ctR;
    private TextView ctW;
    private TextView targetNum;
    private ImageView wrongIndicator;
    private ImageView correctIndicator;
    private ImageView wrong;
    private ImageView check;
    private int countRight,countWrong;
    private int indexNum;
    private int targetNumber;
    private int clickCount;
    private ObjectAnimator animatorX,returnX;
    private boolean isAnimationRunning = false; // Flag to prevent multiple clicks
    private FrameLayout container;
    private FrameLayout.LayoutParams containerParams;
    private final String[] operators = {"+","-","*"};
    private Random random;
    private TextView[] numArr;
    private int[] numberArr,numChosenIndex,numClick;
    private boolean[] textMoved;

    private final int[] operatorImages = {R.drawable.add,R.drawable.minus,R.drawable.multiple};

    public ComposeActivity(Activity activity){
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
        setImage(birds1,600,600,50,500,true);
        birds1.setZ(0);

        ImageView birds2 = ImageDecode(R.drawable.birds);
        setImage(birds2,600,600,700,700,true);

        //woodstand
        ImageView woodstand = ImageDecode(R.drawable.woodstand);
        setImage(woodstand,800,780,-100,2100,true);

        //grass background
        ImageView grass = ImageDecode(R.drawable.grasscompare);
        setImage(grass,width +550,1000,0,2040,true);

        //Header Background
        ImageView headercurve = ImageDecode(R.drawable.curvecompare);
        setImage(headercurve,width,900,0,-250,true);

        //back icon
        ImageView back = ImageDecode(R.drawable.back);
        setImage(back,250,250,100,100,true);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                scaleAnimation(back);

                playSound(getContext(),R.raw.popsound);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setContentView(new Menu(activity));
                    }
                }, 500); // Delay time (same as animation duration)

            }
        });

        //compose text
        ImageView compare = ImageDecode(R.drawable.composetext);
        setImage(compare,700,430,375,7,true);

        //refresh icon
        ImageView refresh = ImageDecode(R.drawable.refresh);
        setImage(refresh,250,250,1100,100,true);
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               resetGame();
            }
        });

        //rectangle
        ImageView rectangle = ImageDecode(R.drawable.rectangle);
        setImage(rectangle,1440,1300,0,850,true);
        rectangle.setAlpha(0.8f);
        rectangle.setZ(0);

        //backgro
        ImageView backgro = ImageDecode(R.drawable.backgro);
        setImage(backgro,900,700,400,1380,true);
        backgro.setZ(0);
        backgro.setAlpha(0.2f);

        //Text
        TextView text = new TextView(getContext());
        setText(text,"Compose the numbers.",FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,20,Color.BLACK,50,950,3,true,"#D9D9D9");

        //Check and wrong icon
        check = new ImageView(getContext());
        check.setImageResource(R.drawable.checkcricle);
        setImage(check,200,200,1230,1880,true);
        check.setZ(11);
        check.setVisibility(View.INVISIBLE);

        wrong = new ImageView(getContext());
        wrong.setImageResource(R.drawable.cancel);
        setImage(wrong,200,200, 1230,1880,true);
        wrong.setZ(11);
        wrong.setVisibility(View.INVISIBLE);


        //Bird image
        ImageView bird = ImageDecode(R.drawable.bird);
        setImage(bird,600,600,20,1600,true);

        //Record
        ImageView rightIcon = new ImageView(getContext());
        rightIcon.setImageResource(R.drawable.checkcricle);
        setImage(rightIcon,150,150,200,2180,true);

        ImageView wrongIcon = new ImageView(getContext());
        wrongIcon.setImageResource(R.drawable.cancel);
        setImage(wrongIcon,150,150,200,2330,true);


        //wrong and correct indicator
        correctIndicator = new ImageView(getContext());
        correctIndicator = ImageDecode(R.drawable.correct);
        setImage(correctIndicator,700,310,650,2200,true);
        correctIndicator.setZ(10);
        correctIndicator.setVisibility(View.INVISIBLE);

        wrongIndicator = new ImageView(getContext());
        wrongIndicator = ImageDecode(R.drawable.wrong);
        setImage(wrongIndicator,700,310,650,2200,true);
        wrongIndicator.setVisibility(View.INVISIBLE);

        //initiate the variables
        init();

        recordCount();

        //generate layout
        layoutGenerate();


        //generate numbers
        generateNumbers();

        //generate symbol
        generateOperator();

        //user onclick
        userOnClick();


    }

    public void init(){
        random = new Random();
        TextView num1 = new TextView(getContext());
        TextView num2 = new TextView(getContext());
        TextView num3 = new TextView(getContext());
        ctR =  new TextView(getContext());
        ctW =  new TextView(getContext());
        targetNum = new TextView(getContext());
        numArr = new TextView[4];
        numberArr = new int[4];
        numChosenIndex = new int[2];
        numClick = new int[2];
        clickCount = 0;
        textMoved = new boolean[4];

        for (int i =0 ;i<4;i++){
            numberArr[i] = -1;
            textMoved[i] = false;

        }

        for (int i =0 ;i<2;i++){
            numChosenIndex[i] = -1;
            numClick[i] = -1;
        }
    }

    public void layoutGenerate(){
        ViewGroup parent = (ViewGroup) getParent();


        // Remove previous FrameLayout if exists
        View existingContainer = parent.findViewWithTag("number_container");
        if (existingContainer != null) {
            parent.removeView(existingContainer);
        }

        container = new FrameLayout(getContext());
        container.setTag("number_container");  // Set a tag for easy reference
        containerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 1000
        );
        containerParams.topMargin = 1100;
        container.setLayoutParams(containerParams);
        container.setZ(10);

        parent.addView(container);

        View container1 = new View(getContext());
        setShape(container1,"#FBCF03",250,250,150,30,2,1,15,1,false);
        View container2 = new View(getContext());
        setShape(container2,"#FBCF03",250,250,600,30,2,1,15,1,false);
        View container3 = new View(getContext());
        setShape(container3,"#F24668",250,250,1050,30,2,1,30,1,false);


    }

    public void generateNumbers(){

        int[][] positions = {{600,400},{900,400},{600,700,},{900,700}};

        for (int i=0;i<4;i++){
            numArr[i] = new TextView(getContext());
            numArr[i].setBackgroundColor(Color.parseColor("#17E100"));
            if (i == 0){
                numberArr[i] = random.nextInt(30) +1;
            }
            else {
                do{
                    numberArr[i] = random.nextInt(30) +1;
                }while(numberArr[i] == numberArr[i-1]);

            }
            setText(numArr[i],String.valueOf(numberArr[i]),200,200,25,Color.WHITE,positions[i][0],positions[i][1],5,false,"#17E100");
        }

        numChosenIndex[0] = random.nextInt(4);
        numChosenIndex[1] = random.nextInt(4);
        while(numChosenIndex[0] == numChosenIndex[1]){
            numChosenIndex[1] = random.nextInt(4);
        }
        indexNum = random.nextInt(3);

        switch (operators[indexNum]) {
            case "+":
                targetNumber = numberArr[numChosenIndex[0]] + numberArr[numChosenIndex[1]];
                break;
            case "-":
                targetNumber = numberArr[numChosenIndex[0]] - numberArr[numChosenIndex[1]];
                break;
            case "*":
                targetNumber = numberArr[numChosenIndex[0]] * numberArr[numChosenIndex[1]];
                break;
        }


        setText(targetNum,String.valueOf(targetNumber),250,250,25,Color.WHITE,1050,30,10,false,"#D9D9D9");
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#F24668"));
        drawable.setCornerRadius(30);
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        targetNum.setBackground(drawable);
    }


    public void generateOperator(){
        ImageView equal = ImageDecode(R.drawable.equal);
        setImage(equal,100,100,900,100,false);

        ImageView math = ImageDecode(operatorImages[indexNum]);

        if (operators[indexNum].equals("-")){
            setImage(math,150,150,420,40,false);
            math.setZ(10);
        }
        else{
            setImage(math,150,150,420,70,false);
            math.setZ(10);

        }


    }

    public void userOnClick(){
        for (int i=0;i<4;i++){
            final int index = i;
            numArr[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!textMoved[index]){

                        numClick[clickCount] = Integer.parseInt(numArr[index].getText().toString());
                        moveImage(numArr[index],clickCount,index,250);
                    }
                    else {
                        returnImage(numArr[index],index);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkAnswers(clickCount);
                        }
                    },250);
                }
            });
        }
    }

    public ImageView ImageDecode (int image){

        ImageView iv = new ImageView(getContext());
        Bitmap bm = BitmapFactory.decodeResource(getResources(),image);
        iv.setImageBitmap(bm);
        return iv;
    }

    public void setImage(ImageView components, int width, int height, int leftMargin, int topMargin,boolean viewgroup){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        params.width = width;
        params.height = height;
        components.setLayoutParams(params);

        if (viewgroup){
            ((ViewGroup) getParent()).addView(components);
        }
        else{
            container.addView(components);
        }
    }

    public void setText(TextView components,String text,int width,int height,int size,int color,int x,int y,int z,boolean viewgroup,String colorcode){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        components.setText(text);

        components.setTextSize(size);
        components.setTextColor(color);
        components.setGravity(Gravity.CENTER);
        components.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        params.leftMargin = x;
        params.topMargin = y;
        components.setZ(z);
        components.setLayoutParams(params);

        //Check if the Textview exist in the view group and remove

        ViewParent parent = components.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(components);
        }

        if (viewgroup) {
            ((ViewGroup) getParent()).addView(components);
        }
        else{
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor(colorcode));
            drawable.setCornerRadius(20);
            drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);


            components.setBackground(drawable);
            container.addView(components);
        }
    }
    public void setShape(View view,String colorcode, int width, int height, int x, int  y,int  z ,float alpha, int corner,float shadow,boolean viewgroup){
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

        if (viewgroup){
            ((ViewGroup) getParent()).addView(view);
        }
        else{
            container.addView(view);
        }
    }

    private void recordCount(){



        setText(ctR,String.valueOf(countRight),FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,30,Color.BLACK,370,2180,3,true,"D9D9D9");
        setText(ctW,String.valueOf(countWrong),FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,30,Color.BLACK,370,2330,3,true,"D9D9D9");

    }

    private void checkAnswers(int index){
        boolean isCorrect = false;
        Log.d("Operator",String.valueOf(operators[indexNum]));
        if (index == 2){
            switch (operators[indexNum]) {
                case "+":
                    isCorrect = numClick[0] + numClick[1] == targetNumber;
                    break;
                case "*":
                    isCorrect = numClick[0] * numClick[1] == targetNumber;
                    break;
                case "-":
                    isCorrect = numClick[0] - numClick[1] == targetNumber;
                    break;
            }

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


            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetGame();
                }
            }, 1000); // 5000 milliseconds = 5 seconds

        }
    }
    public void resetGame(){
        correctIndicator.setVisibility(View.INVISIBLE);
        wrongIndicator.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);
        wrong.setVisibility(View.INVISIBLE);


        clickCount = 0;

        //Re-generate Text and number as well as addOnListener to the number
        layoutGenerate();
        generateNumbers();// Hide image after 5s
        generateOperator();
        userOnClick();
        recordCount();


        //Reset the textMoved to false and set default to the number
        for (int i =0;i<4;i++){
            textMoved[i] = false;
            numberArr[i] = -1;
        }

    }

    private void moveImage(TextView tv,int clickcount,int index,int duration){
        ObjectAnimator animatorY = null;

        if (clickcount == 0){
            animatorX = ObjectAnimator.ofFloat(tv,"x",175);
            animatorY = ObjectAnimator.ofFloat(tv,"y",55);
            tv.setBackgroundColor(Color.TRANSPARENT);
            tv.setTextColor(Color.BLACK);

        }
        else if (clickcount == 1){
            animatorX = ObjectAnimator.ofFloat(tv,"x",625);
            animatorY = ObjectAnimator.ofFloat(tv,"y",55);
            tv.setBackgroundColor(Color.TRANSPARENT);
            tv.setTextColor(Color.BLACK);
        }
        else{
            return;
        }

        if (!textMoved[index]){
            clickCount++;
        }
        textMoved[index] = true;

        animatorX.start();
        animatorY.start();

    }

    private void returnImage (TextView tv,int index){

        int[][] positions = {{600,400},{900,400},{600,700,},{900,700}};
        ObjectAnimator returnY;

        if (textMoved[index]){
            returnX = ObjectAnimator.ofFloat(tv,"x",positions[index][0]);
            returnY = ObjectAnimator.ofFloat(tv,"y",positions[index][1]);

            returnX.setDuration(250);
            returnY.setDuration(250);

            returnX.start();
            returnY.start();

            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor("#17E100"));
            drawable.setCornerRadius(20);
            drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);

            tv.setTextColor(Color.WHITE);
            tv.setBackground(drawable);

            if (Integer.parseInt(numArr[index].getText().toString()) == numClick[0]){
                clickCount = 0;
            }
            else{
                clickCount = 1;
            }
            textMoved[index] = false;
        }
    }


    private void scaleAnimation(ImageView iv) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv, "scaleX", 1.0f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv, "scaleY", 1.0f, 1.2f);

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

    private MediaPlayer mediaPlayer;

    public void playSound(Context context, int soundResource) {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release old instance
        }

        mediaPlayer = MediaPlayer.create(context, soundResource);
        mediaPlayer.setVolume(1,1);
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

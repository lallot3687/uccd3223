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
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class OrderActivity extends View {
    private final Activity activity;

    private TextView ctR;
    private TextView ctW;
    private TextView text;
    private TextView[] numArr;
    private ImageView wrongIndicator;
    private ImageView correctIndicator;
    private ImageView wrong;
    private ImageView check;
    private int countRight,  countWrong, clickCount, indexText;
    private int[] numberArr,numberUser;
    private final String [] words = {"ascending order.","descending order."};
    private Random random;
    private boolean []textMoved ;

    public OrderActivity(Activity activity){
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas){
        super.onDraw(canvas);
        int width = getWidth();

        //initial the variable
        init();

        //Background color
        canvas.drawColor(Color.parseColor("#E5e5e5"));

        //birds
        ImageView birds1 = ImageDecode(R.drawable.birds);
        setImage(birds1,600,600,50,500);
        birds1.setZ(0);

        ImageView birds2 = ImageDecode(R.drawable.birds);
        setImage(birds2,600,600,700,700);

        //woodstand
        ImageView woodStand = ImageDecode(R.drawable.woodstand);
        setImage(woodStand,800,780,-100,2100);

        //grass background
        ImageView grass = ImageDecode(R.drawable.grasscompare);
        setImage(grass,width +550,1000,0,2040);

        //Header Background
        ImageView headerCurve = ImageDecode(R.drawable.curvecompare);
        setImage(headerCurve,width,900,0,-250);

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

        //order text
        ImageView compare = ImageDecode(R.drawable.ordertext);
        setImage(compare,700,430,375,7);
        //refresh icon
        ImageView refresh = ImageDecode(R.drawable.refresh);

        setImage(refresh,250,250,1100,100);
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
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
        generateText();

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

        //Bird image
        ImageView bird = ImageDecode(R.drawable.bird);
        setImage(bird,600,600,20,1600);

        //Record
        ImageView rightIcon = new ImageView(getContext());
        rightIcon.setImageResource(R.drawable.checkcricle);
        setImage(rightIcon,150,150,200,2180);

        ImageView wrongIcon = new ImageView(getContext());
        wrongIcon.setImageResource(R.drawable.cancel);
        setImage(wrongIcon,150,150,200,2330);
        recordCount();

        //container
        View container1 = new View(getContext());
        setShape(container1,"#D9D9D9",200,200,180,1100,1,1,30,1);
        View container2 = new View(getContext());
        setShape(container2,"#D9D9D9",200,200,410,1100,1,1,30,1);
        View container3 = new View(getContext());
        setShape(container3,"#D9D9D9",200,200,640,1100,1,1,30,1);
        View container4 = new View(getContext());
        setShape(container4,"#D9D9D9",200,200,870,1100,1,1,30,1);
        View container5 = new View(getContext());
        setShape(container5,"#D9D9D9",200,200,1100,1100,1,1,30,1);


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

        //generate numbers
        generateNumbers();

        //Useronclick
        userOnClick();
    }

    public void init(){
        numberArr =  new int [5];
        numArr = new TextView[5];
        textMoved = new boolean[5];
        numberUser = new int[5];
        random = new Random();

        text = new TextView(getContext());
        ctR =  new TextView(getContext());
        ctW =  new TextView(getContext());

        for (int i =0;i<5;i++){
            textMoved[i] = false;
            numberArr[i] = -1;
        }

    }


    public ImageView ImageDecode (int image){

        ImageView iv = new ImageView(getContext());
        Bitmap bm = BitmapFactory.decodeResource(getResources(),image);
        iv.setImageBitmap(bm);
        return iv;
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

    public void setText(TextView components,String text,int width,int height,int size,int color,int x,int y,int z,boolean viewgroup, FrameLayout param){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        components.setText(text);
        components.setTextSize(size);
        components.setTextColor(color);
        components.setGravity(Gravity.CENTER);
        components.setTypeface(Typeface.DEFAULT_BOLD);
        params.leftMargin = x;
        params.topMargin = y;
        components.setZ(z);
        components.setLayoutParams(params);

        //Check if the Textview exist in the view group and remove
        ViewGroup parent = (ViewGroup) components.getParent();
        if (parent != null) {
            parent.removeView(components);
        }

        if (viewgroup) {
            ((ViewGroup) getParent()).addView(components);
        }
        else{
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor("#F8CF03"));
            drawable.setCornerRadius(20);
            drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);


            components.setBackground(drawable);
            param.addView(components);
        }
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

    public void generateText(){
        indexText  = random.nextInt(2) ;
        setText(text,"Arrange the numbers in " + words[indexText],FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,20,Color.BLACK,50,950,3,true,null);
    }


    private void generateNumbers(){

        ViewGroup parent = (ViewGroup) getParent();
        // Remove previous FrameLayout if exists
        View existingContainer = parent.findViewWithTag("number_container");
        if (existingContainer != null) {
            parent.removeView(existingContainer);
        }

        FrameLayout container = new FrameLayout(getContext());
        container.setTag("number_container");  // Set a tag for easy reference
        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 1000
        );
        containerParams.topMargin = 1100;
        container.setLayoutParams(containerParams);
        container.setZ(10);

        Random random  =  new Random();

        int [][] positions = {{600,400},{850,400},{1100,400},{725,650},{975,650}};

        for (int i=0 ; i< 5;i++){
            numArr[i] = new TextView(getContext());
            numArr[i].setBackgroundColor(Color.parseColor("#D9D9D9"));
            if (i == 0){
                numberArr[i] = random.nextInt(999) +1;
            }
            else {
                do{
                    numberArr[i] = random.nextInt(999) +1;
                }while(numberArr[i] == numberArr[i-1]);

            }
            setText(numArr[i],String.valueOf(numberArr[i]),200,200,25,Color.BLACK,positions[i][0],positions[i][1],5,false,container);


        }
        if (words[indexText].equals("ascending order.")){
            Arrays.sort(numberArr);
        }
       else{
            Integer[] numArray = Arrays.stream(numberArr).boxed().toArray(Integer[]::new);
           Arrays.sort(numArray, (a, b)->b-a);
           numberArr = Arrays.stream(numArray).mapToInt(Integer::intValue).toArray();
        }

        ((ViewGroup) getParent()).addView(container);
    }

    public void userOnClick(){

        for (int i = 0 ; i < 5 ; i++){
            final int index = i;
            numArr[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!textMoved[index]){

                        numberUser[clickCount] = Integer.parseInt(numArr[index].getText().toString());
                        moveImage(numArr[index],clickCount,index,250);
                    }
                    else {
                        returnImage(numArr[index], index);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkAnswers(Integer.parseInt(numArr[index].getText().toString()),clickCount);
                        }
                    },250);

                }
            });
        }
    }
    private void recordCount(){

        setText(ctR,String.valueOf(countRight),FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,30,Color.BLACK,370,2180,3,true,null);
        setText(ctW,String.valueOf(countWrong),FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,30,Color.BLACK,370,2330,3,true,null);

    }

    private void checkAnswers(int checkNum,int index){
        boolean isCorrect = false;
       if (index == 5){
           if (words[indexText].equals("ascending order.")){
               for (int i = 0 ;i < 5;i++){

                   if (numberUser[i] != numberArr[i]){
                       isCorrect = false;
                       break;
                   }
                   else{
                       isCorrect = true;
                   }
               }
           }
           else{
               for (int i =4;i>=0;i--){
                   if (numberUser[i] != numberArr[i]){
                       isCorrect = false;
                       break;
                   }
                   else{
                       isCorrect = true;
                   }
               }
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

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       showImageOverlay();
                   }
               },1000);



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

        generateText();
        generateNumbers();// Hide image after 5s
        userOnClick();
        recordCount();

        //Reset the textMoved to false and set default to the number
        for (int i =0;i<5;i++){
            textMoved[i] = false;
            numberUser[i] = -1;
        }

    }

    private void moveImage(TextView tv,int clickcount,int index,int duration){
        ObjectAnimator animatorY = null;

        ObjectAnimator animatorX;
        if (clickcount == 0){
            animatorX = ObjectAnimator.ofFloat(tv,"x",180);
            animatorY = ObjectAnimator.ofFloat(tv,"y",0);
        }
        else if (clickcount == 1){
            animatorX = ObjectAnimator.ofFloat(tv,"x",410);
            animatorY = ObjectAnimator.ofFloat(tv,"y",0);
        }
        else if (clickcount == 2){
            animatorX = ObjectAnimator.ofFloat(tv,"x",640);
            animatorY = ObjectAnimator.ofFloat(tv,"y",0);
        }
        else if (clickcount ==3) {
            animatorX = ObjectAnimator.ofFloat(tv,"x",870);
            animatorY = ObjectAnimator.ofFloat(tv,"y",0);
        }
        else if (clickcount == 4){
            animatorX = ObjectAnimator.ofFloat(tv,"x",1100);
            animatorY = ObjectAnimator.ofFloat(tv,"y",0);
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
        int [][] positions = {{600,400},{850,400},{1100,400},{725,650},{975,650}};
        int numUserIndex = -1;
        ObjectAnimator returnY;

        if (textMoved[index]){
            ObjectAnimator returnX = ObjectAnimator.ofFloat(tv, "x", positions[index][0]);
            returnY = ObjectAnimator.ofFloat(tv,"y",positions[index][1]);
            returnX.setDuration(250);
            returnY.setDuration(250);
            returnX.start();
            returnY.start();

            for (int i = 0 ; i<5;i++){
                if (numberUser[i] == Integer.parseInt(String.valueOf(tv.getText()))){
                    numUserIndex = i;
                    break;
                }
            }
            for (int i = numUserIndex + 1;i<5;i++){

                for (int j = 0;j<5;j++){

                    if (numberUser[i] == Integer.parseInt(String.valueOf(numArr[j].getText()))){
                        moveImage(numArr[j],i-1, j, 250);
                    }
                }
            }
            for (int i =numUserIndex ;i<5 ;i++){
               if (numberUser[i] != -1 && i<4){
                   numberUser[i] = numberUser[i+1];
               }

               if (numberUser[i] != -1 && i==4){
                   numberUser[i] = -1;
               }
            }
            clickCount--;
            textMoved[index] = false;
        }
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

    private void showImageOverlay() {
        Context context = getContext(); // Get the context from View
        if (context == null) return; // Ensure it's valid

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Create a layout for the dialog
        FrameLayout container = new FrameLayout(context);
        container.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        //Create Answer view

        FrameLayout ansContainer = new FrameLayout(getContext());
        FrameLayout.LayoutParams ansParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,1000);
        ansParams.gravity = Gravity.CENTER;
        ansContainer.setLayoutParams(ansParams);
        ansContainer.setBackgroundColor(Color.WHITE);

        int[][] positions = {{140,250},{370,250},{605,250},{835,250},{1065,250}};

        TextView[] numCorrect = new TextView[5];
        TextView[] numUser = new TextView[5];

        //Text
        TextView tv = new TextView(getContext());
        setText(tv,"Correct answers:", FrameLayout.LayoutParams.MATCH_PARENT,150,25,Color.BLACK,0,50,3,false,container);

        //Show correct answer
        for (int i =0;i<5;i++){

            numCorrect[i] = new TextView(getContext());
            numCorrect[i].setBackgroundColor(Color.parseColor("#D9D9D9"));
            setText(numCorrect[i],String.valueOf(numberArr[i]),200,200,25,Color.BLACK,positions[i][0],positions[i][1],5,false,ansContainer);
        }

        //Text
        TextView tv1 = new TextView((getContext()));
        setText(tv1,"Your answers:", FrameLayout.LayoutParams.MATCH_PARENT,150,25,Color.BLACK,0,550,3,false,container);

        //Show user's answer
        for (int i =0;i<5;i++){

            numUser[i] = new TextView(getContext());
            numUser[i].setBackgroundColor(Color.parseColor("#D9D9D9"));
            setText(numUser[i],String.valueOf(numberUser[i]),200,200,25,Color.BLACK,positions[i][0], 750,5,false,ansContainer);
        }

        // Add ImageView to container
        container.addView(ansContainer);

        // Set the container as the dialog view
        builder.setView(container);
        AlertDialog dialog = builder.create();

        // Make the dialog background transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
    }
}

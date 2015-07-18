package com.example.baekdaehyun.amuguna;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener {
//    public static ImageView backBtn;
    public static ImageView refreshBtn;
    public static ImageButton selectFirstBtn;
    public static ImageButton selectSecondBtn;
    DisplayMetrics displaymetrics = new DisplayMetrics();
    private int tournamentIndex = 1;
    private int level;
    List<String> foodList;
    public static TextView levelTxt;
    private boolean animFinFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        backBtn = (ImageView)findViewById(R.id.backBtn);
        refreshBtn = (ImageView)findViewById(R.id.refreshBtn);
        selectFirstBtn = (ImageButton)findViewById(R.id.firstBtn);
        selectSecondBtn = (ImageButton)findViewById(R.id.secondBtn);

        levelTxt = (TextView)findViewById(R.id.level_text);

//        backBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        selectFirstBtn.setOnClickListener(this);
        selectSecondBtn.setOnClickListener(this);


        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        foodList = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.food)));

        Collections.shuffle(foodList);
        foodList = foodList.subList(0,32);
        level = foodList.size();
        setImageView(selectFirstBtn, foodList.get(0));
        setImageView(selectSecondBtn, foodList.get(1));



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.refreshBtn:
                break;
            case R.id.firstBtn:

                selectFirstBtn.animate().setStartDelay(300).setDuration(300).translationX(-displaymetrics.widthPixels).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
//                        selectFirstBtn.setVisibility(View.GONE);
                        setImageView(selectFirstBtn, foodList.get(tournamentIndex +1));
                        selectFirstBtn.setTranslationX(0);
                        selectFirstBtn.animate().setDuration(300).alpha(1.0f);
                        animFinFlag = true;
                    }
                });
                selectSecondBtn.animate().setDuration(300).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setImageView(selectSecondBtn, foodList.get(tournamentIndex + 2));
                        selectSecondBtn.animate().setStartDelay(300).setDuration(300).alpha(1.0f);



                    }
                });
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        foodList.remove(tournamentIndex++);
                        if(tournamentIndex >= (foodList.size()-1)){
                            tournamentIndex = 1;
                            level /= 2;
                            levelTxt.setText(Integer.toString(level) + "강");
                            if(level == 1){
                                finalFood();
                            }
                        }
                    }
                }, 400);
                break;
            case R.id.secondBtn:

                selectSecondBtn.animate().setStartDelay(300).setDuration(300).translationX(-displaymetrics.widthPixels).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
//                        selectFirstBtn.setVisibility(View.GONE);

                        setImageView(selectSecondBtn, foodList.get(tournamentIndex+1));
                        selectSecondBtn.setTranslationX(0);
                        selectSecondBtn.animate().setDuration(300).alpha(1.0f);
                    }
                });
                selectFirstBtn.animate().setDuration(300).alpha(0.0f).setListener(new AnimatorListenerAdapter() {


                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setImageView(selectFirstBtn, foodList.get(tournamentIndex + 2));
                        selectFirstBtn.animate().setStartDelay(300).setDuration(300).alpha(1.0f);


                    }
                });
                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        foodList.remove(tournamentIndex - 1);
                        tournamentIndex++;
                        if (tournamentIndex == (foodList.size() - 1)) {
                            tournamentIndex = 1;
                            level /= 2;


                            levelTxt.setText(Integer.toString(level) + "강");
                            if (level == 1) {
                                finalFood();
                            }
                        }
                    }
                }, 400);
                break;
        }
    }


    public void setImageView(ImageView v, String name) {
        Context context = v.getContext();
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());

        Drawable dr = context.getResources().getDrawable(id);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 500, 500, true));

        v.setImageDrawable(d);
    }

    public void finalFood(){
        foodList.get(0);
    }
}

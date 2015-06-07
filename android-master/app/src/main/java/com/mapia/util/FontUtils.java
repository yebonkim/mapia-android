package com.mapia.util;

import android.graphics.Typeface;

import com.mapia.MainApplication;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class FontUtils {
    private static MainApplication mainApplication = MainApplication.getInstance();
    private static Typeface nanumBold;
    private static Typeface nanumRegular;

    public static Typeface getNanumBold(){
        if(nanumBold == null){
            nanumBold = Typeface.createFromAsset(mainApplication.getAssets(), "fonts/NanumBarunGothicOTFBold.otf");
        }
        return nanumBold;
    }

    public static Typeface getNanumRegular(){
        if(nanumRegular == null){
            if(nanumRegular == null){
                nanumRegular = Typeface.createFromAsset(mainApplication.getAssets(), "fonts/NanumBarunGothicOTF.otf");
            }
            return nanumRegular;
        }
        return nanumRegular;
    }
}

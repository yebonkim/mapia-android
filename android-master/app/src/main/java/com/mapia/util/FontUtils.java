package com.mapia.util;

import android.graphics.Typeface;

import com.mapia.MainApplication;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class FontUtils {
    private static MainApplication mainApplication;
    private static Typeface nanumBold;
    private static Typeface nanumRegular;

    static{
        FontUtils.mainApplication = MainApplication.getInstance();
    }
    public static Typeface getNanumBold(){
        if(nanumBold == null){
            nanumBold = Typeface.createFromAsset(MainApplication.getContext().getAssets(), "fonts/NanumBarunGothicOTFBold.otf");
        }
        return nanumBold;
    }

    public static Typeface getNanumRegular(){
        if(nanumRegular == null){
            if(nanumRegular == null){
                nanumRegular = Typeface.createFromAsset(MainApplication.getContext().getAssets(), "fonts/NanumBarunGothicOTF.otf");
            }
            return nanumRegular;
        }
        return nanumRegular;
    }
}

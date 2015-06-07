package com.mapia.splash;

/**
 * Created by daehyun on 15. 6. 5..
 */
public class SplashUtils {

    private static boolean isShowSplash = false;

    public static boolean getIsShowSplash(){
        return SplashUtils.isShowSplash;
    }

    public static void setIsShowSplash(final boolean isShowSplash){
        SplashUtils.isShowSplash = isShowSplash;
    }

}

package com.mapia;

import android.app.Application;
import android.content.Context;

import com.mapia.home.HomeActivity;
import com.mapia.search.SearchActivity;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class MainApplication extends Application {

    private static Context context;
    private static boolean isMenubarClicked = false;
    private static MainApplication mainApplication;
    private HomeActivity homeActivity;
    private SearchActivity searchActivity;

    public MainApplication(){
        super();

    }
    public static MainApplication getInstance(){
        synchronized (MainApplication.class) {
            return MainApplication.mainApplication;
        }
    }

    public HomeActivity getHomeActivity(){
        return this.homeActivity;
    }
    public void setHomeActivity(HomeActivity paramHomeActivity){
        this.homeActivity = paramHomeActivity;

    }

    public void onCreate(){
        super.onCreate();
        MainApplication.mainApplication = this;
        MainApplication.context = this.getApplicationContext();


    }
    public void setSearchActivity(SearchActivity searchActivity){
        this.searchActivity = searchActivity;
    }
    public SearchActivity getSearchActivity(){
        return this.searchActivity;
    }
    public static Context getContext(){
        return context;
    }
}

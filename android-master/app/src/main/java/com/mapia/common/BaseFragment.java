package com.mapia.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.mapia.MainActivity;
import com.mapia.MainApplication;
import com.mapia.R;

import java.util.Map;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class BaseFragment extends Fragment {
    protected boolean defaultMenuBarShow = true;
    protected boolean dragFlag;
    protected float endY;
    protected boolean firstDragFlag;
    protected InputMethodManager inputMethodManager;
    protected MainActivity mainActivity;
    protected MainApplication mainApplication;

    public BaseFragment(){
        super();
        this.dragFlag = false;
        this.firstDragFlag = false;
        this.defaultMenuBarShow = true;
    }


    public void hideSoftKeyboard(){

    }
    @Override
    public void onCreate(final Bundle bundle){
        super.onCreate(bundle);
        this.mainActivity = (MainActivity)this.getActivity();
        this.mainApplication = new MainApplication();
        this.inputMethodManager = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle){
        return layoutInflater.inflate(R.layout.fragment_base, viewGroup, false);
    }
    public boolean getDefaultMenuBarShow(){
        return this.defaultMenuBarShow;
    }

    public void onBuriedAfterAnimation(){

    }
    public void onBuriedBeforeAnimation(){

    }


    public void onEvent(final Map<String, Object> map) {
    }

    public void onFadeInAfterAnimation() {
    }

    public void onFadeInBeforeAnimation() {
    }

    public void onFadeOutBeforeAnimation() {
    }

    public void refreshInfo() {
    }

    public void refreshList() {
    }

    public void scrollToTop() {
    }

    public void setConnectionCondition(final boolean b) {
    }
}

package com.mapia.search;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapia.R;
import com.mapia.common.BaseFragment;
import com.mapia.custom.FontSettableEditText;
import com.mapia.custom.MapiaPageTransformer;
import com.mapia.search.pic.SearchPicResultFragment;
import com.mapia.search.tag.SearchTagResultFragment;
import com.mapia.search.user.SearchUserResultFragment;

import java.lang.reflect.Field;

//import org.apache.commons.lang3.StringUtils;


public class SearchFragment extends BaseFragment
{
    private SearchPagerAdapter adapter;
//    private SearchHistoryCacheWrapper cacheWrapper;
    private FontSettableEditText editText;
    private RelativeLayout layout;
    private int[] nodataImageResourceIdArray;
    private LinearLayout picButton;
    private LinearLayout pickerButton;
    private LinearLayout removeButton;
    private LinearLayout searchIcon;
    private LinearLayout tagButton;
    private ViewPager viewPager;

    public SearchFragment() {
        super();
        this.nodataImageResourceIdArray = new int[] { 2130837735, 2130837736, 2130837737, 2130837738, 2130837739 };
        this.defaultMenuBarShow = false;
    }

    private void checkSearchResultCount(final String s) {
        String s2 = null;
//        switch (this.viewPager.getCurrentItem()) {
//            case 0: {
//                s2 = QueryManager.makeSearchTagApiUrl(s, 1, 20);
//                break;
//            }
//            case 1: {
//                s2 = QueryManager.makeSearchPicApiUrl(s, 1, 20);
//                break;
//            }
//            case 2: {
//                s2 = QueryManager.makeSearchUserApiUrl(s, 1, 20);
//                break;
//            }
//        }
//        this.mainActivity.makeRequest(s2, new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                if (jsonObject == null) {
//                    return;
//                }
//                try {
//                    if ("success".equals(jsonObject.getString("resultStatus"))) {
//                        if (jsonObject.getJSONObject("result").getInt("totalCount") == 0) {
//                            SearchFragment.this.adapter.showNoSearchResult(SearchFragment.this.viewPager.getCurrentItem());
//                            SearchFragment.this.showKeyboard();
//                        }
//                        else {
//                            SearchFragment.this.openSearchResult(s);
//                        }
//                    }
//                    SearchFragment.this.adapter.hideProgress(SearchFragment.this.viewPager.getCurrentItem());
//                }
//                catch (Exception ex) {}
//            }
//        });
    }

    private void inputHistory(final String s) {
//        switch (this.viewPager.getCurrentItem()) {
//            default: {}
//            case 0: {
//                this.cacheWrapper.insertTag(s);
//            }
//            case 1: {
//                this.cacheWrapper.insertPic(s);
//            }
//        }
    }

    private void openSearchResult(final String s) {
        final Bundle arguments = new Bundle();
        arguments.putString("query", s);
        switch (this.viewPager.getCurrentItem()) {
            default: {}
            case 0: {
//                AceUtils.nClick(NClicks.SEARCH_LIST_TAG);
                SearchTagResultFragment searchTagResultFragment = new SearchTagResultFragment();
                searchTagResultFragment.setArguments(arguments);
                this.mainActivity.addFragment(searchTagResultFragment);
            }
            case 1: {
//                AceUtils.nClick(NClicks.SEARCH_LIST_PIC);
                SearchPicResultFragment searchPicResultFragment = new SearchPicResultFragment();
                searchPicResultFragment.setArguments(arguments);
                this.mainActivity.addFragment(searchPicResultFragment);
            }
            case 2: {
//                AceUtils.nClick(NClicks.SEARCH_LIST_USER);
                final SearchUserResultFragment searchUserResultFragment = new SearchUserResultFragment();
                searchUserResultFragment.setArguments(arguments);
                this.mainActivity.addFragment(searchUserResultFragment);
            }
        }
    }

    private void setIndicator() {
        switch (this.viewPager.getCurrentItem()) {
            default: {}
            case 0: {
                this.tagButton.setAlpha(1.0f);
                this.picButton.setAlpha(0.3f);
                this.pickerButton.setAlpha(0.3f);
                this.editText.setHint((CharSequence)this.mainActivity.getString(R.string.search_tag_title));
                break;
            }
            case 1: {
                this.tagButton.setAlpha(0.3f);
                this.picButton.setAlpha(1.0f);
                this.pickerButton.setAlpha(0.3f);
                this.editText.setHint((CharSequence)this.mainActivity.getString(R.string.search_place_title));
                break;
            }
            case 2: {
                this.tagButton.setAlpha(0.3f);
                this.picButton.setAlpha(0.3f);
                this.pickerButton.setAlpha(1.0f);
                this.editText.setHint((CharSequence)this.mainActivity.getString(R.string.search_user_title));
                break;
            }
        }
    }

    private void showAutoComplete() {
        this.adapter.showAutoComplete(this.editText.getText().toString());
    }

    private void showHistory() {
        this.adapter.showHistory();
    }

    public int[] getNodataImageResourceIdArray() {
        return this.nodataImageResourceIdArray;
    }

    public String getQuery() {
        return this.editText.getText().toString();
    }

    public void hideKeyboard() {
        this.inputMethodManager.hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
    }

    @Override
    public void hideSoftKeyboard() {
        this.hideKeyboard();
    }


    public boolean onBackPressed() {
        this.hideKeyboard();
        return false;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.layout = (RelativeLayout)layoutInflater.inflate(R.layout.fragment_search, viewGroup, false);
        this.layout.findViewById(R.id.fs_prev).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                SearchFragment.this.hideKeyboard();
                SearchFragment.this.mainActivity.onBackPressed();
//                AceUtils.nClick(NClicks.SEARCH_PRE);
            }
        });
        this.editText = (FontSettableEditText)this.layout.findViewById(R.id.fs_edittext);
        editText.addTextChangedListener((TextWatcher) new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
            }

            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }

            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
//                if (StringUtils.isNotBlank(charSequence.toString())) {
//                    SearchFragment.this.searchIcon.setVisibility(View.INVISIBLE);
//                    SearchFragment.this.removeButton.setVisibility(View.VISIBLE);
//                    SearchFragment.this.showAutoComplete();
//                    return;
//                }
                SearchFragment.this.removeButton.setVisibility(View.INVISIBLE);
                SearchFragment.this.searchIcon.setVisibility(View.VISIBLE);
                SearchFragment.this.showHistory();
            }
        });
        this.editText.setOnEditorActionListener((TextView.OnEditorActionListener)new TextView.OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                final String string = SearchFragment.this.editText.getText().toString();
//                if (!StringUtils.isBlank(string)) {
//                    SearchFragment.this.hideKeyboard();
//                    if (n == 6 || n == 5 || (keyEvent != null && keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 66)) {
//                        SearchFragment.this.adapter.showProgress(SearchFragment.this.viewPager.getCurrentItem());
//                        SearchFragment.this.checkSearchResultCount(string);
//                        SearchFragment.this.inputHistory(string);
//                        return false;
//                    }
//                }
                return false;
            }
        });
        this.searchIcon = (LinearLayout)this.layout.findViewById(R.id.fs_search_icon);
        (this.removeButton = (LinearLayout)this.layout.findViewById(R.id.fs_remove_button)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.SEARCH_DELETE);
                SearchFragment.this.editText.setText((CharSequence)"");
                SearchFragment.this.removeButton.setVisibility(View.INVISIBLE);
                SearchFragment.this.showHistory();
            }
        });
        this.tagButton = (LinearLayout)this.layout.findViewById(R.id.fs_tag_button);
        this.picButton = (LinearLayout)this.layout.findViewById(R.id.fs_pic_button);
        this.pickerButton = (LinearLayout)this.layout.findViewById(R.id.fs_user_button);
        this.tagButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.SEARCH_LNB_TAG);
                viewPager.setCurrentItem(0, true);
                setIndicator();
            }
        });
        this.picButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.SEARCH_LNB_PIC);
                viewPager.setCurrentItem(1, true);
                setIndicator();
            }
        });
        this.pickerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
//                AceUtils.nClick(NClicks.SEARCH_LNB_USER);
                SearchFragment.this.viewPager.setCurrentItem(2, true);
                SearchFragment.this.setIndicator();
            }
        });


        adapter = new SearchPagerAdapter(this.mainActivity, this);
        viewPager = (ViewPager)this.layout.findViewById(R.id.fs_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageTransformer(false, new MapiaPageTransformer());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
            }

            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
            }

            @Override
            public void onPageSelected(final int n) {
                SearchFragment.this.setIndicator();
            }
        });
//        this.cacheWrapper = new SearchHistoryCacheWrapper((Context)this.mainActivity);

        this.showKeyboard();
        return this.layout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            final Field declaredField = Fragment.class.getDeclaredField("mChildFragmentManager");
            declaredField.setAccessible(true);
            declaredField.set(this, null);
        }
        catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        }
        catch (IllegalAccessException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    public void showKeyboard() {
        new Handler().postDelayed((Runnable) new SoftInputShowHandler(), 300L);
    }

    private class SoftInputShowHandler implements Runnable
    {
        @Override
        public void run() {
            SearchFragment.this.inputMethodManager.showSoftInput((View)SearchFragment.this.editText, 1);
        }
    }
}
package com.mapia.map;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mapia.R;

public class MapActivity extends FragmentActivity{

	int currentFragmentIndex;

	public static LatLng currentLatlng= new LatLng(37.498360, 127.027400);
	public static LatLng cameraLatlng = new LatLng(37.498360, 127.027400);
	public static float cameraZoom = 8;
	public static ImageButton imgBtnNav, imgBtnSearch;
	public static TextView txtMapName;
    private static boolean isMenuExpanded = false;
    private int menuWidth;
	private ListView lvNavList;
	Button btn1, btn2, btn3, btn4;
    LinearLayout llMenuMap = null, llMainMap = null;
	MapPrivateFragment mapPrivateFragment = null;
	MapPublicFragment mapPublicFragment = null;
	MapFollowFragment mapFollowFragment = null;
	MapGroupFragment mapGroupFragment = null;
	Fragment lastFragment = null;

    private String[] navItems = {"PrivateMap","PublicMap","FollowMap","GroupMap"};

	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
        Log.i("Create", "MapActivity");

        /*Custom ActionBar*/
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_activity_map, null);
		imgBtnNav = (ImageButton)mCustomView.findViewById(R.id.actBtnNavi);
        imgBtnNav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Touch", "imgBtnNav");
                menuAnimationToggle();
            }
        });
		imgBtnSearch = (ImageButton)mCustomView.findViewById(R.id.actBtnSearch);
		txtMapName = (TextView)mCustomView.findViewById(R.id.actTxtMapName);
        mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

        initSildeMenu();

		currentFragmentIndex = 1;
		fragmentReplace(currentFragmentIndex);
	}

    private void initSildeMenu() {
        /* init menu layout size */
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        menuWidth = (int) ((metrics.widthPixels) * 0.75);

        llMainMap = (LinearLayout) findViewById(R.id.llMainMap);

        llMenuMap = (LinearLayout) findViewById(R.id.llMenuMap);
        LinearLayout.LayoutParams MenuLayoutPrams = (LinearLayout.LayoutParams) llMenuMap
                .getLayoutParams();
        MenuLayoutPrams.width = menuWidth;
        llMenuMap.setLayoutParams(MenuLayoutPrams);

        /* init menu */
        lvNavList = (ListView)findViewById(R.id.drawerLvItems);
        lvNavList.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position <= 3) fragmentReplace(position + 1);
            }
        });

    }

    private void menuAnimationToggle() {
        if (!isMenuExpanded) {
            isMenuExpanded = true;
            // Expand
            new OpenAnimation(llMainMap, menuWidth,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);
            // enable all of menu view
            LinearLayout viewGroup = (LinearLayout) findViewById(R.id.llMenuMap)
                    .getParent();
            enableDisableViewGroup(viewGroup, true);

            // enable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.VISIBLE);
            findViewById(R.id.ll_empty).setEnabled(true);
            findViewById(R.id.ll_empty).setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            menuAnimationToggle();
                            return true;
                        }
                    });

        } else {
            isMenuExpanded = false;

            // Collapse
            new CloseAnimation(llMainMap, menuWidth,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);


            // enable all of menu view
            LinearLayout viewGroup = (LinearLayout) findViewById(R.id.mapShowFragment)
                    .getParent();
            enableDisableViewGroup(viewGroup, false);

            // disable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.GONE);
            findViewById(R.id.ll_empty).setEnabled(false);

        }
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup,
                                              boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {


            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);

            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }


    /* FragmentReplace */
    private void fragmentReplace(int newFragmentIndex) {
		Fragment newFragment = null;
		newFragment = getFragment(newFragmentIndex);
		if(newFragment == lastFragment) return;
		lastFragment = newFragment;
		
		final android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.mapShowFragment, newFragment);
		transaction.commit();
	}

	private Fragment getFragment(int index) {
		Fragment newFragment = null;
		switch(index){
			case 1:
				if(mapPrivateFragment==null) mapPrivateFragment = new MapPrivateFragment();
				newFragment = mapPrivateFragment;
				break;
			case 2:
				if(mapPublicFragment==null) mapPublicFragment = new MapPublicFragment();
				newFragment = mapPublicFragment;
				break;
			case 3:
				if(mapFollowFragment==null) mapFollowFragment = new MapFollowFragment();
				newFragment = mapFollowFragment;
				break;
			case 4:
				if(mapGroupFragment==null) mapGroupFragment = new MapGroupFragment();
				newFragment = mapGroupFragment;
				break;
			default:
				break;
		}
		return newFragment;
	}
}
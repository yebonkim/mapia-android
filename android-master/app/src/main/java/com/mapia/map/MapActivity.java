package com.mapia.map;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

	private ListView lvNavList;
	Button btn1, btn2, btn3, btn4;
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

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_activity_map, null);

		imgBtnNav = (ImageButton)mCustomView.findViewById(R.id.actBtnNavi);
		imgBtnSearch = (ImageButton)mCustomView.findViewById(R.id.actBtnSearch);
		txtMapName = (TextView)mCustomView.findViewById(R.id.actTxtMapName);

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

        lvNavList.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // if(position>=0 && position<=3) fragmentReplace(position+1);
            }
        });
		currentFragmentIndex = 1;
		fragmentReplace(currentFragmentIndex);
	}

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
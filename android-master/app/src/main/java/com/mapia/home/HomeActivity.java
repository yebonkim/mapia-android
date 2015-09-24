package com.mapia.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.mapia.MainActivity;
import com.mapia.R;
import com.mapia.map.MapFollowFragment;
import com.mapia.map.MapGroupFragment;
import com.mapia.map.MapPrivateFragment;
import com.mapia.map.MapPublicFragment;
import com.mapia.network.NetworkStatusManager;
import com.mapia.post.PostActivity;

/**
 * Created by daehyun on 15. 5. 31..
 */
public class HomeActivity extends MainActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private HomeFragment homeFragment = null;
    private boolean isFirstOnResume = true;
//Map
    int currentFragmentIndex;
    public static LatLng currentLatlng;
    public static LatLng cameraLatlng = new LatLng(0,0);
    public static float cameraZoom = 15;
    public static ImageButton imgBtnNav, imgBtnSearch;
    public static TextView txtMapName;
    Button btn1, btn2, btn3, btn4;
    MapPrivateFragment mapPrivateFragment = null;
    MapPublicFragment mapPublicFragment = null;
    MapFollowFragment mapFollowFragment = null;
    MapGroupFragment mapGroupFragment = null;
    Fragment lastFragment = null;

    public GoogleApiClient mGoogleApiClient;



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

    //
    private void autoRefreshHome(){
        if (homeFragment != null && NetworkStatusManager.getIsAvailableNetwork()){
            long currentTimeMillis = System.currentTimeMillis();
            if(currentTimeMillis-HomeUtils.beforeHomeLoadingTime >= 3600000L){
                HomeUtils.beforeHomeLoadingTime = currentTimeMillis;
            }
        }
    }


//    private void handleMediaFromOtherApp(Intent paramIntent) {
//        if (paramIntent == null) {
//            return;
//        }
//        if (StringUtils.equals(paramIntent.getAction(), "android.intent.action.SEND")) {
//            Intent localIntent = new Intent(this, RatioProxyActivity.class);
//            localIntent.putExtra("galleryShareExtraSteam", paramIntent.getParcelableExtra("galleryShareExtraSteam"));
//            localIntent.setType(paramIntent.getType());
//            startActivityForResult(localIntent, 2);
//            return;
//        }
//        this.mSchemeUrl = paramIntent.getStringExtra("scheme_data");
//    }

    @Override
    protected void onCreate(Bundle paramBundle){
        super.onCreate(null);
        setContentView(R.layout.activity_main);
//        this.mainApplication.setHomeActivity(this);
        this.init(0);
//        handleMediaFromOtherApp(getIntent());
//        new Handler().postDelayed(new MainPostProcessingHandler(null), 3000L);

        currentFragmentIndex = 2;
        fragmentReplace(currentFragmentIndex);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void onDestroy(){
        super.onDestroy();
//        if((SchemeUtils.getmainActivity() != null) && ((SchemeUtils.getMainActivity() instanceof HomeActivity))){
//            SchemeUtils.setMainActivity(null);
//        }
        this.mainApplication.setHomeActivity(null);
    }
    @Override
    protected void onNewIntent(Intent paramIntent){
        super.onNewIntent(paramIntent);
//        handleMediaFromOtherApp(paramIntent);
    }
    @Override
    public void onResume(){
        super.onResume();
//        SchemeUtils.setMainActivity(this);
        if(this.mSchemeUrl !=null) {
//            SchemeUtils.goDestination(this.mSchemeUrl);
//            setSchemeUrl(null);
        }
        autoRefreshHome();
//        if (NotiUtils.getIsNewNoti()){
//            showNotiIcon(false);

//        }
//        for(;;){
//            NpushUtils.setCurrentActivity(0);
//            if(!this.isFirstOnResume){
//                autoRefreshHome();
//            }
//            this.isFirstOnResume = false;
//            AppEventsLogger.activiateApp(this, APPID_FACEBOOK);
//            return;
//            this.notiManager.checkHasNew();
//        }
    }
    public void setHomeFragment(HomeFragment paramHomeFragment){
        this.homeFragment = paramHomeFragment;
    }

//    private class MainPostProcessingHandler implements Runnable{
//        private MainPostProessingHandler(){}
//
//        public void run(){
//            NaverNoticeManager.getInstance().setCompletedNaverNoticeHandler(new NaverNoticeManager.CompleteNaverNotice(){
//                public void onCompletedNaverNotice(){
//                    Object localObject = NaverNoticeManger.getInstance().getSavedUpdateInfo();
//                    if(localObject != null){
//                        PreferenceUtils.putPreference("last_version_name", ((NaverNoticeData)localObject).getUpdateVersionName());
//                        PreferenceUtils.putPreference("update_link_url", ((NaverNoticeData)localObject).getLinkURL());
//                        localObject = ((NaverNoticeData)localObject).getUpdateVersion();
//                        if((localObject != null) && (TextUtils.isDigitsOnly((CharSequence)localObject))){
//                            PreferenceUtils.putIntPreference("last_version_code", Integer.parseInt((String)localObject));
//                        }
//                    }
//                }
//            });
//            NaverNoticeManger.getInstance().requestNaverNotice(HomeActivity.this);
//            MainApplication.getInstance().initNPush();
//            MentionUtil.getInstance().initMentionUtil(HomeActivity.this);
//            FollowUtils.initFollowTagArrayList();
//        }
//    }

    //선택한 이미지 데이터 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_LONG).show();



        if (requestCode == 10) {//PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this.getApplicationContext());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();


                this.mainApplication.setIsMenubarClicked(true);
                final Intent intent = new Intent(this.getApplicationContext(), (Class) PostActivity.class);

                intent.putExtra("latlng",place.getLatLng());
                intent.putExtra("address",place.getAddress());
                this.startActivity(intent);

            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // The good stuff goes here.
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.

    }
}

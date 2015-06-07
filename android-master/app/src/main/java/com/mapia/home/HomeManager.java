package com.mapia.home;

import com.mapia.MainActivity;

/**
 * Created by daehyun on 15. 6. 5..
 */
public class HomeManager {
    private static final int INIT_HOME = 0;
    private static final int SHOW_CASE_CACHE_DATA_WITH_REALTIME_DATA = 3;
    private static final int SHOW_CASE_ONLY_REALTIME_DATA_NO_SPLASH = 1;
    private static final int SHOW_CASE_ONLY_REALTIME_DATA_YES_SPLASH = 2;
    public static int TAG_GROUP_COUNT = 12;
    private static final int UPDATE_HOME = 1;
    private int bannerAddHandlerDelay = 0;
//    private List<HomeBanner> bannerArray = new ArrayList();
    private int bannerIndexForInit = 0;
    private HomeFragment fragment;
//    private HomeBannerAdapter homeBannerAdapter;
//    private HomeHotAdapter homeHotAdapter;
//    private HomeTagListCacheWrapper homeTagListCacheWrapper;
//    private HomeTaggroupAdapter homeTaggroupAdapter;
    private String hotpicImageUrl;
    private String hotuserImageUrl;
    private boolean isFirstShuffle = true;
    private boolean isLoaded = false;
    private MainActivity mainActivity;
//    private RealmManager realmManager;
//    private List<RecomendTag> recommendTag> recommendTagArray = new ArrayList();
    private int recommendTagArrayIndex = 0;
    private int taggroupIndexForInit = 0;
    private int taggroupIndexForShuffle = 0;
    private int taggroupIndexForUpdate = 0;





}

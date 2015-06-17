package com.mapia.myfeed.grid;

/**
 * Created by daehyun on 15. 6. 8..
 */

import com.mapia.MainActivity;
import com.mapia.util.PagingUtils;


public class MyfeedPicGridManager
{
    private MyfeedPicGridAdapter adapter;
    private MyfeedPicGridFragment fragment;
    private long lastFeedId;
    private MainActivity mainActivity;
    private int size;

    public MyfeedPicGridManager(final MainActivity mainActivity, final MyfeedPicGridAdapter adapter, final MyfeedPicGridFragment fragment) {
        super();
        this.lastFeedId = 0L;
        this.size = PagingUtils.getCommonGridPagingSize();
        this.mainActivity = mainActivity;
        this.fragment = fragment;
        (this.adapter = adapter).setSize(this.size);
        this.adapter.setHasNextPage(true);
    }

    public void show(final boolean b) {
        if (b) {
            this.lastFeedId = 0L;
        }
//        this.mainActivity.makeRequest(QueryManager.makeMyfeedListApiUrl(this.lastFeedId, this.size), new Response.Listener<JSONObject>() {
//            public void onResponse(JSONObject jsonObject) {
//                if (jsonObject == null) {
//                    return;
//                }
//                Label_0276: {
//                    JSONArray jsonArray;
//                    try {
//                        if (!"success".equals(jsonObject.getString("resultStatus"))) {
//                            break Label_0276;
//                        }
//                        jsonObject = jsonObject.getJSONObject("result");
//                        jsonArray = jsonObject.getJSONArray("feedPics");
//                        if (MyfeedPicGridManager.this.lastFeedId == 0L && jsonArray.length() == 0) {
//                            MyfeedPicGridManager.this.fragment.showLittleData();
//                            return;
//                        }
//                    }
//                    catch (Exception ex) {
//                        ex.printStackTrace();
//                        MyfeedPicGridManager.this.fragment.afterShow(true, 0);
//                        return;
//                    }
//                    if (MyfeedPicGridManager.this.lastFeedId == 0L && jsonArray.length() < 4) {
//                        MyfeedPicGridManager.this.fragment.showLittleData();
//                    }
//                    else if (MyfeedPicGridManager.this.adapter.getCount() > 3) {
//                        MyfeedPicGridManager.this.fragment.hideLittleData();
//                    }
//                    for (int i = 0; i < jsonArray.length(); ++i) {
//                        final FeedPic feedPic = MyfeedPicGridManager.this.mainActivity.getObjectMapper().readValue(((JSONObject)jsonArray.get(i)).toString(), FeedPic.class);
//                        MyfeedPicGridManager.this.lastFeedId = feedPic.getFeedId();
//                        MyfeedPicGridManager.this.adapter.add((Object)feedPic);
//                    }
//                    final boolean boolean1 = jsonObject.getBoolean("hasNextPage");
//                    MyfeedPicGridManager.this.adapter.setHasNextPage(boolean1);
//                    if (boolean1) {
//                        MyfeedPicGridManager.this.fragment.afterShow(false, 0);
//                        return;
//                    }
//                    MyfeedPicGridManager.this.fragment.afterShow(true, jsonArray.length());
//                    return;
//                }
//                if (MyfeedPicGridManager.this.adapter.getCount() < 4) {
//                    MyfeedPicGridManager.this.fragment.showLittleData();
//                }
//                MyfeedPicGridManager.this.fragment.afterShow(true, 0);
//            }
//        });
    }
}
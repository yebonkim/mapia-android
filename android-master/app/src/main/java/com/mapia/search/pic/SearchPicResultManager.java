package com.mapia.search.pic;

/**
 * Created by daehyun on 15. 6. 7..
 */

import com.mapia.MainActivity;
import com.mapia.util.PagingUtils;


public class SearchPicResultManager
{
    private SearchPicResultAdapter adapter;
    private SearchPicResultFragment fragment;
    private MainActivity mainActivity;
    private int page;
    private int size;

    public SearchPicResultManager(final MainActivity mainActivity, final SearchPicResultAdapter adapter, final SearchPicResultFragment fragment) {
        super();
        this.page = 1;
        this.size = PagingUtils.getCommonGridPagingSize();
        this.mainActivity = mainActivity;
        this.adapter = adapter;
        this.fragment = fragment;
    }

    public void show(final boolean b, final String s) {
        if (b) {
            this.page = 1;
        }
//        this.mainActivity.makeRequest(QueryManager.makeSearchPicApiUrl(s, this.page++, this.size), new Response.Listener<JSONObject>() {
//            public void onResponse(JSONObject jsonObject) {
//                if (jsonObject != null) {
//                    JSONArray jsonArray;
//                    try {
//                        if (!"success".equals(jsonObject.getString("resultStatus"))) {
//                            return;
//                        }
//                        jsonObject = jsonObject.getJSONObject("result");
//                        jsonArray = jsonObject.getJSONArray("searchPics");
//                        if (SearchPicResultManager.this.page == 2 && jsonArray.length() == 0) {
//                            SearchPicResultManager.this.fragment.showNoDataText();
//                            return;
//                        }
//                    }
//                    catch (Exception ex) {
//                        ex.printStackTrace();
//                        SearchPicResultManager.this.fragment.afterShow(true);
//                        return;
//                    }
//                    if (SearchPicResultManager.this.page == 2 && jsonArray.length() < 4) {
//                        SearchPicResultManager.this.fragment.showNoData();
//                    }
//                    for (int i = 0; i < jsonArray.length(); ++i) {
//                        SearchPicResultManager.this.adapter.add((Object)SearchPicResultManager.this.mainActivity.getObjectMapper().readValue(((JSONObject)jsonArray.get(i)).toString(), SearchPic.class));
//                    }
//                    final boolean boolean1 = jsonObject.getBoolean("hasNextPage");
//                    SearchPicResultManager.this.adapter.setQueryParams(s, SearchPicResultManager.this.page, SearchPicResultManager.this.size, boolean1);
//                    if (boolean1) {
//                        SearchPicResultManager.this.fragment.afterShow(false);
//                        return;
//                    }
//                    SearchPicResultManager.this.fragment.afterShow(true);
//                }
//            }
//        });
    }
}
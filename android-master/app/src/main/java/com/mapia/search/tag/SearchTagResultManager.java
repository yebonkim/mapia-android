package com.mapia.search.tag;

/**
 * Created by daehyun on 15. 6. 7..
 */

import com.mapia.MainActivity;
import com.mapia.util.PagingUtils;


public class SearchTagResultManager
{
    private SearchTagResultAdapter adapter;
    private SearchTagResultFragment fragment;
    private MainActivity mainActivity;
    private int page;
    private int size;

    public SearchTagResultManager(final MainActivity mainActivity, final SearchTagResultAdapter adapter, final SearchTagResultFragment fragment) {
        super();
        this.page = 1;
        this.size = PagingUtils.getCommonListPagingSize();
        this.mainActivity = mainActivity;
        this.adapter = adapter;
        this.fragment = fragment;
    }

    public void show(final boolean b, String searchTagApiUrl) {
        if (b) {
            this.page = 1;
        }
//        searchTagApiUrl = QueryManager.makeSearchTagApiUrl(searchTagApiUrl, this.page++, this.size);
//        this.mainActivity.makeRequest(searchTagApiUrl, new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                if (jsonObject != null) {
//                    JSONArray jsonArray;
//                    try {
//                        if (!"success".equals(jsonObject.getString("resultStatus"))) {
//                            return;
//                        }
//                        jsonArray = jsonObject.getJSONObject("result").getJSONArray("searchTags");
//                        if (SearchTagResultManager.this.page == 2 && jsonArray.length() == 0) {
//                            SearchTagResultManager.this.fragment.showNoData();
//                            return;
//                        }
//                    }
//                    catch (Exception ex) {
//                        ex.printStackTrace();
//                        SearchTagResultManager.this.fragment.afterShow(true);
//                        return;
//                    }
//                    for (int i = 0; i < jsonArray.length(); ++i) {
//                        SearchTagResultManager.this.adapter.add((Object)SearchTagResultManager.this.mainActivity.getObjectMapper().readValue(((JSONObject)jsonArray.get(i)).toString(), SearchTag.class));
//                    }
//                    if (jsonArray.length() == 0) {
//                        SearchTagResultManager.this.fragment.afterShow(true);
//                        return;
//                    }
//                    SearchTagResultManager.this.fragment.afterShow(false);
//                }
//            }
//        });
    }
}
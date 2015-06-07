package com.mapia.search.user;

/**
 * Created by daehyun on 15. 6. 6..
 */

import com.mapia.MainActivity;
import com.mapia.util.PagingUtils;


public class SearchUserResultManager
{
    private SearchUserResultAdapter adapter;
    private SearchUserResultFragment fragment;
    private MainActivity mainActivity;
    private int page;
    private int size;

    public SearchUserResultManager(final MainActivity mainActivity, final SearchUserResultAdapter adapter, final SearchUserResultFragment fragment) {
        super();
        this.page = 1;
        this.size = PagingUtils.getCommonListPagingSize();
        this.mainActivity = mainActivity;
        this.adapter = adapter;
        this.fragment = fragment;
    }

    public void show(final boolean b, String searchUserApiUrl) {
        if (b) {
            this.page = 1;
        }
//        searchUserApiUrl = QueryManager.makeSearchUserApiUrl(searchUserApiUrl, this.page++, this.size);
//        this.mainActivity.makeRequest(searchUserApiUrl, new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                if (jsonObject != null) {
//                    JSONArray jsonArray;
//                    try {
//                        if (!"success".equals(jsonObject.getString("resultStatus"))) {
//                            return;
//                        }
//                        jsonArray = jsonObject.getJSONObject("result").getJSONArray("searchUsers");
//                        if (SearchUserResultManager.this.page == 2 && jsonArray.length() == 0) {
//                            SearchUserResultManager.this.fragment.showNoData();
//                            return;
//                        }
//                    }
//                    catch (Exception ex) {
//                        ex.printStackTrace();
//                        SearchUserResultManager.this.fragment.afterShow(true);
//                        return;
//                    }
//                    for (int i = 0; i < jsonArray.length(); ++i) {
//                        SearchUserResultManager.this.adapter.add((Object)SearchUserResultManager.this.mainActivity.getObjectMapper().readValue(((JSONObject)jsonArray.get(i)).toString(), SearchUser.class));
//                    }
//                    if (jsonArray.length() == 0) {
//                        SearchUserResultManager.this.fragment.afterShow(true);
//                        return;
//                    }
//                    SearchUserResultManager.this.fragment.afterShow(false);
//                }
//            }
//        });
    }
}
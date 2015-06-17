package com.mapia.myfeed;

/**
 * Created by daehyun on 15. 6. 8..
 */

import com.mapia.MainActivity;

public class MyfeedManager
{
    private MainActivity mainActivity;
    private MyfeedFragment myfeedFragment;

    public MyfeedManager(final MainActivity mainActivity, final MyfeedFragment myfeedFragment) {
        super();
        this.mainActivity = mainActivity;
        this.myfeedFragment = myfeedFragment;
    }

    private void openAuthFailDialog() {
//        final DialogAuthFail dialogAuthFail = new DialogAuthFail((Context)this.mainActivity, this.mainActivity.getString(2131558427));
//        dialogAuthFail.setCancelable(false);
//        dialogAuthFail.setDialogListener((DialogAuthFail.DialogListener)new DialogAuthFail.DialogListener() {
//            @Override
//            public void selectOption(final int n) {
//                switch (n) {
//                    default: {}
//                    case 0: {
//                        LoginManager.logout(MyfeedManager.this.mainActivity);
//                    }
//                }
//            }
//        });
//        dialogAuthFail.show();
    }

    public void getMyfeedInfo() {
//        this.mainActivity.makeRequest(QueryManager.makeMyfeedInfoApiUrl(), new Response.Listener<JSONObject>() {
//            public void onResponse(final JSONObject jsonObject) {
//                if (jsonObject != null) {
//                    try {
//                        if ("success".equals(jsonObject.getString("resultStatus"))) {
//                            final JSONObject jsonObject2 = jsonObject.getJSONObject("result");
//                            MyfeedManager.this.myfeedFragment.setTagCount(jsonObject2.getInt("feedTagCount"));
//                            MyfeedManager.this.myfeedFragment.setUserCount(jsonObject2.getInt("feedUserCount"));
//                            return;
//                        }
//                    }
//                    catch (JSONException ex) {
//                        NeloLog.warn("0201", "MyfeedManager > " + jsonObject.toString());
//                        final String message = ex.getMessage();
//                        if (StringUtils.isNotBlank(message) && message.startsWith("No value")) {
//                            MyfeedManager.this.openAuthFailDialog();
//                        }
//                        return;
//                    }
//                    switch (jsonObject.getJSONObject("result").getInt("errorCode")) {
//                        case 1000: {
//                            MyfeedManager.this.openAuthFailDialog();
//                        }
//                        default: {}
//                    }
//                }
//            }
//        });
    }
}
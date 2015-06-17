package com.mapia.mention;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.util.ArrayList;


public class MentionUtil
{
    private static MentionUtil instance;
    public static boolean mIsFresh;
    public static ArrayList<MentionModel> mListMention;
    private final String TAG;
//    public MapiaRequest mRequest;

    static {
        MentionUtil.mListMention = new ArrayList<MentionModel>();
        MentionUtil.instance = null;
    }

    public MentionUtil() {
        super();
        this.TAG = "mentionutil";
    }

    public static MentionUtil getInstance() {
        synchronized (MentionUtil.class) {
            if (MentionUtil.instance == null) {
                MentionUtil.instance = new MentionUtil();
            }
            return MentionUtil.instance;
        }
    }
//
    private boolean isRequesting() {
//        return this.mRequest != null && !this.mRequest.isCanceled();
        return false;   //tmp
    }
//
//    private void parseMentionList(final ObjectMapper objectMapper, final JSONObject jsonObject, final MentionListEvent mentionListEvent) {
//        try {
//            if (!"success".equalsIgnoreCase(jsonObject.getString("resultStatus"))) {
//                return;
//            }
//            final JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("followings");
//            MentionUtil.mListMention.clear();
//            for (int i = 0; i < jsonArray.length(); ++i) {
//                final Picker picker = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), Picker.class);
//                final MentionModel mentionModel = new MentionModel();
//                mentionModel.memberNo = picker.getMemberNo();
//                mentionModel.nickname = picker.getNickname();
//                mentionModel.profileUrl = picker.getImageUrl();
//                MentionUtil.mListMention.add(mentionModel);
//            }
//            MentionUtil.mIsFresh = true;
//            if (mentionListEvent != null) {
//                mentionListEvent.onMentionUpdated(MentionUtil.mListMention);
//            }
//        }
//        catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//        catch (IOException ex2) {
//            ex2.printStackTrace();
//        }
//    }
//
//    public void clear() {
//        if (this.mRequest != null && !this.mRequest.isCanceled()) {
//            this.mRequest.cancel();
//            this.mRequest = null;
//        }
//        MentionUtil.mIsFresh = false;
//        MentionUtil.mListMention.clear();
//    }
//
    public ArrayList<MentionModel> getMentionList() {
        if (this.isRequesting() || !MentionUtil.mIsFresh) {
            return null;
        }
        return MentionUtil.mListMention;
    }
//
//    public void initMentionUtil(final MainActivity mainActivity) {
//        final EventBus eventBus = MainApplication.getInstance().getEventBus();
//        if (!eventBus.isRegistered(this)) {
//            eventBus.register(this);
//        }
//        this.requestMentionList(mainActivity, null);
//    }
//
//    public boolean isFresh() {
//        return MentionUtil.mIsFresh;
//    }
//
//    public void onEvent(final Map<String, Object> map) {
//        if ("ecfu".equals(map.get("event"))) {
//            MentionUtil.mIsFresh = false;
//        }
//    }
//
//    public void requestMentionList(final MainActivity mainActivity, final MentionListEvent mentionListEvent) {
//        if (!MentionUtil.mIsFresh && (this.mRequest == null || this.mRequest.isCanceled())) {
//            (this.mRequest = new MapiaRequest(QueryManager.makeEntireFollowingListUrl(MainApplication.getLoginInfo().getMemberNo()), (JSONObject)null, new Response.Listener<JSONObject>() {
//                public void onResponse(final JSONObject jsonObject) {
//                    MentionUtil.this.mRequest = null;
//                    try {
//                        if ("success".equals(jsonObject.getString("resultStatus"))) {
//                            MentionUtil.this.parseMentionList(mainActivity.getObjectMapper(), jsonObject, mentionListEvent);
//                        }
//                    }
//                    catch (JSONException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(final VolleyError volleyError) {
//                    MentionUtil.this.mRequest = null;
//                    volleyError.printStackTrace();
//                }
//            })).setRetryPolicy(RequestUtils.getNoRetryPolicy());
//            MapiaVolley.getRequestQueue().add((Request<Object>)this.mRequest);
//        }
//    }
//
//    public interface MentionListEvent
//    {
//        void onMentionUpdated(ArrayList<MentionModel> p0);
//    }
}
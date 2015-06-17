package com.mapia.scheme;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;

import com.mapia.MainActivity;
import com.mapia.util.BannerUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class SchemeUtils
{
    public static final int ACTIVITY_REQUESTCODE_CAMERA = 0;
    public static final String SCHEME_DATA = "scheme_data";
    private static boolean isReturn;
    private static MainActivity mainActivity;

    static {
        SchemeUtils.isReturn = false;
    }

    public static MainActivity getMainActivity() {
        return SchemeUtils.mainActivity;
    }

    public static void goDestination(String queryParams) {
//        if (!StringUtils.isBlank(queryParams) && SchemeUtils.mainActivity != null) {
//            final String protocol = BannerUtils.getProtocol(queryParams);
//            if (!StringUtils.isBlank(protocol)) {
//                if ("http".equals(protocol)) {
//                    openWebView(queryParams);
//                    return;
//                }
//                final String host = BannerUtils.getHost(queryParams);
//                if (!StringUtils.isBlank(host)) {
//                    final String replace = host.replace("/", "");
//                    queryParams = (String)BannerUtils.getQueryParams(queryParams);
//                    int int1 = 0;
//                    while (true) {
//                        try {
//                            int1 = Integer.parseInt(((Map<K, String>)queryParams).get("version"));
//                            if (int1 > 2) {
//                                queryParams = (String)new DialogUpdate((Context)SchemeUtils.mainActivity, SchemeUtils.mainActivity.getString(2131558547));
//                                ((DialogUpdate)queryParams).setCanceledOnTouchOutside(true);
//                                ((DialogUpdate)queryParams).setDialogListener((DialogUpdate.DialogListener)new DialogUpdate.DialogListener() {
//                                    @Override
//                                    public void selectOption(final int n) {
//                                        switch (n) {
//                                            default: {}
//                                            case 1: {
//                                                NaverNoticeUtil.showLink((Context)SchemeUtils.mainActivity, "market://details?id=com.naver.android.mapia");
//                                            }
//                                        }
//                                    }
//                                });
//                                ((DialogUpdate)queryParams).show();
//                                return;
//                            }
//                            goDestination(replace, (Map<String, String>)queryParams);
//                        }
//                        catch (NumberFormatException ex) {
//                            continue;
//                        }
//                        break;
//                    }
//                }
//            }
//        }
    }
//
//    private static void goDestination(final String s, final Map<String, String> map) {
//        if (!"goHome".equals(s)) {
//            if ("goTagGallery".equals(s)) {
//                openTagGallery(map.get("tag"));
//                return;
//            }
//            if ("goLocationGallery".equals(s)) {
//                openLocationGallery(map.get("locationName"), map.get("locationCode"), Double.parseDouble(map.get("latitude")), Double.parseDouble(map.get("longitude")));
//                return;
//            }
//            if ("goUserTagAlbum".equals(s)) {
//                openUserTagAlbum(Long.parseLong(map.get("memberNo")), map.get("tag"));
//                return;
//            }
//            if ("goRepicGallery".equals(s)) {
//                openRepicGallery(Long.parseLong(map.get("memberNo")), Long.parseLong(map.get("picNo")));
//                return;
//            }
//            if ("goProfile".equals(s)) {
//                openProfile(Long.parseLong(map.get("memberNo")));
//                return;
//            }
//            if ("goHotPic".equals(s)) {
//                openHotPic();
//                return;
//            }
//            if ("goHotUser".equals(s)) {
//                openHotUser();
//                return;
//            }
//            if ("goPic".equals(s)) {
//                openPic(Long.parseLong(map.get("memberNo")), Long.parseLong(map.get("picNo")));
//                return;
//            }
//            if ("goCamera".equals(s)) {
//                openCamera();
//                return;
//            }
//            if ("goNotice".equals(s)) {
//                openNotice();
//            }
//        }
//    }
//
//    private static void openCamera() {
//        if (MainApplication.getInstance().getUploadingStatus() != -1) {
//            MapiaToast.show(getMainActivity(), "\ub3d9\uc601\uc0c1 \uc804\uc1a1\uc644\ub8cc \ud6c4 \uc9c4\ud589\uac00\ub2a5\ud569\ub2c8\ub2e4", 0);
//            return;
//        }
//        MainApplication.getInstance().getPostingInfo().mode = 0;
//        MainApplication.getInstance().getPostingInfo().copyrightYn = "Y";
//        SchemeUtils.mainActivity.startActivityForResult(new Intent((Context)SchemeUtils.mainActivity, (Class)CameraActivity.class), 0);
//        SchemeUtils.mainActivity.overridePendingTransition(0, 0);
//    }
//
//    private static void openHotPic() {
//        SchemeUtils.mainActivity.addFragment(new HotpicFragment());
//    }
//
//    private static void openHotUser() {
//        SchemeUtils.mainActivity.addFragment(new HotuserFragment());
//    }
//
//    private static void openLocationGallery(final String s, final String s2, final double n, final double n2) {
//        final Bundle arguments = new Bundle();
//        arguments.putString("locationName", s);
//        arguments.putString("locationCode", s2);
//        arguments.putDouble("latitude", n);
//        arguments.putDouble("longitude", n2);
//        final LocationGalleryFragment locationGalleryFragment = new LocationGalleryFragment();
//        locationGalleryFragment.setArguments(arguments);
//        SchemeUtils.mainActivity.addFragment(locationGalleryFragment);
//    }
//
//    private static void openNotice() {
//        SchemeUtils.mainActivity.startActivity(new Intent((Context)SchemeUtils.mainActivity, (Class)NaverNoticeArchiveActivity.class));
//    }
//
//    private static void openPic(final long n, final long n2) {
//        final Bundle arguments = new Bundle();
//        arguments.putLong("memberNo", n);
//        arguments.putLong("picNo", n2);
//        final EndFragment endFragment = new EndFragment();
//        endFragment.setArguments(arguments);
//        SchemeUtils.mainActivity.addFragment(endFragment);
//    }
//
//    private static void openProfile(final long n) {
//        final Bundle arguments = new Bundle();
//        arguments.putLong("memberNo", n);
//        final ProfileFragment profileFragment = new ProfileFragment();
//        profileFragment.setArguments(arguments);
//        SchemeUtils.mainActivity.addFragment(profileFragment);
//    }
//
//    private static void openRepicGallery(final long n, final long n2) {
//        final Bundle arguments = new Bundle();
//        arguments.putLong("memberNo", n);
//        arguments.putLong("picNo", n2);
//        final RepicFragment repicFragment = new RepicFragment();
//        repicFragment.setArguments(arguments);
//        SchemeUtils.mainActivity.addFragment(repicFragment);
//    }
//
//    private static void openTagGallery(final String s) {
//        final Bundle arguments = new Bundle();
//        arguments.putString("tag", s);
//        final TagGalleryFragment tagGalleryFragment = new TagGalleryFragment();
//        tagGalleryFragment.setArguments(arguments);
//        SchemeUtils.mainActivity.addFragment(tagGalleryFragment);
//    }
//
//    private static void openUserTagAlbum(final long n, final String s) {
//        final Bundle arguments = new Bundle();
//        arguments.putLong("memberNo", n);
//        arguments.putString("tag", s);
//        final ProfileAlbumFragment profileAlbumFragment = new ProfileAlbumFragment();
//        profileAlbumFragment.setArguments(arguments);
//        SchemeUtils.mainActivity.addFragment(profileAlbumFragment);
//    }
//
//    private static void openWebView(final String s) {
//        NaverNoticeUtil.showLink((Context)SchemeUtils.mainActivity, s);
//    }

    public static void setMainActivity(final MainActivity mainActivity) {
        SchemeUtils.mainActivity = mainActivity;
    }
}
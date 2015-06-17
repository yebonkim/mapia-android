package com.mapia.endpage;

/**
 * Created by daehyun on 15. 6. 8..
 */

import com.mapia.api.model.Activity;
import com.mapia.api.model.End;
import com.mapia.api.model.FeedPic;
import com.mapia.api.model.ListingPic;
import com.mapia.api.model.MyPic;
import com.mapia.api.model.Picker;
import com.mapia.api.model.Profile;
import com.mapia.api.model.Repic;
import com.mapia.api.model.SearchPic;
import com.mapia.api.model.SimplePic;
import com.mapia.api.model.ThumbnailPic;

import java.io.Serializable;
import java.util.ArrayList;


public class EndModelAdapter implements Serializable
{
    public static End makeEnd(final Activity activity) {
        try {
            final End end = new End();
            final SimplePic pic = activity.getPic();
            end.picNo = pic.getPicNo();
            end.picker = pic.getPicker();
            end.picImageUrl = pic.getPicImageUrl();
            end.picImageWidth = pic.getPicImageWidth();
            end.picImageHeight = pic.getPicImageHeight();
            end.picImageColor = pic.getPicImageColor();
            end.fileType = pic.getFileType();
            end.regDate = activity.getRegDate();
            end.penalty = activity.getPenalty();
            return end;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static End makeEnd(final FeedPic feedPic) {
        try {
            final End end = new End();
            end.picNo = feedPic.getPicNo();
            end.picker = feedPic.getPicker();
            end.fileType = feedPic.getFileType();
            end.picImageUrl = feedPic.getPicImageUrl();
            end.picImageWidth = feedPic.getPicImageWidth();
            end.picImageHeight = feedPic.getPicImageHeight();
            end.picImageColor = feedPic.getPicImageColor();
            end.picVid = feedPic.getPicVid();
            end.likeYn = feedPic.getLikeYn();
            end.likeCount = feedPic.getLikeCount();
            end.copyrightYn = feedPic.getCopyrightYn();
            end.replyCount = feedPic.getReplyCount();
            end.repicCount = feedPic.getRepicCount();
            end.regDate = feedPic.getRegDate();
            end.modifyDate = feedPic.getModifyDate();
            return end;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static End makeEnd(final ListingPic listingPic) {
        try {
            final End end = new End();
            end.picNo = listingPic.getPicNo();
            end.picker = listingPic.getPicker();
            end.fileType = listingPic.getFileType();
            end.picImageUrl = listingPic.getPicImageUrl();
            end.picImageWidth = listingPic.getPicImageWidth();
            end.picImageHeight = listingPic.getPicImageHeight();
            end.picImageColor = listingPic.getPicImageColor();
            end.likeCount = listingPic.getLikeCount();
            end.repicCount = listingPic.getRepicCount();
            end.copyrightYn = listingPic.getCopyrightYn();
            end.regDate = listingPic.getRegDate();
            end.modifyDate = listingPic.getModifyDate();
            return end;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static End makeEnd(final Picker picker, final ThumbnailPic thumbnailPic) {
        try {
            final End end = new End();
            end.picNo = thumbnailPic.getPicNo();
            end.picImageUrl = thumbnailPic.getPicImageUrl();
            end.picker = picker;
            return end;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static End makeEnd(final Profile profile, final MyPic myPic) {
        try {
            final End end = new End();
            end.picNo = myPic.getPicNo();
            end.body = myPic.getBody();
            end.picker = makePicker(profile);
            end.fileType = myPic.getFileType();
            end.picImageUrl = myPic.getPicImageUrl();
            end.picImageWidth = myPic.getPicImageWidth();
            end.picImageHeight = myPic.getPicImageHeight();
            end.picImageColor = myPic.getPicImageColor();
            end.picVid = myPic.getPicVid();
            end.likeYn = myPic.getLikeYn();
            end.likeCount = myPic.getLikeCount();
            end.copyrightYn = myPic.getCopyrightYn();
            end.replyCount = myPic.getReplyCount();
            end.repicCount = myPic.getRepicCount();
            end.regDate = myPic.getRegDate();
            end.modifyDate = myPic.getModifyDate();
            return end;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static End makeEnd(final Repic repic) {
        try {
            final End end = new End();
            end.picNo = repic.picNo;
            end.picker = makePicker(repic.picker);
            end.fileType = repic.fileType;
            end.picImageUrl = repic.picImageUrl;
            end.picImageWidth = repic.picImageWidth;
            end.picImageHeight = repic.picImageHeight;
            end.picImageColor = repic.picImageColor;
            end.picVid = repic.picVid;
            end.likeYn = repic.likeYn;
            end.likeCount = repic.likeCount;
            end.copyrightYn = repic.copyrightYn;
            end.body = repic.body;
            end.regDate = repic.regDate;
            end.modifyDate = repic.modifyDate;
            end.mentionedUsers = repic.mentionedUsers;
            return end;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static End makeEnd(final SearchPic searchPic) {
        try {
            final End end = new End();
            end.picNo = searchPic.getPicNo();
            end.picker = searchPic.getPicker();
            end.picImageUrl = searchPic.getPicImageUrl();
            end.picImageWidth = searchPic.getPicImageWidth();
            end.picImageHeight = searchPic.getPicImageHeight();
            return end;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static ArrayList<End> makeEndListForFeedPic(final ArrayList<FeedPic> list) {
        final ArrayList<End> list2 = new ArrayList<End>();
        for (int i = 0; i < list.size(); ++i) {
            final End end = makeEnd(list.get(i));
            if (end != null) {
                list2.add(end);
            }
        }
        return list2;
    }

    public static ArrayList<End> makeEndListForListingPic(final ArrayList<ListingPic> list) {
        final ArrayList<End> list2 = new ArrayList<End>();
        for (int i = 0; i < list.size(); ++i) {
            final End end = makeEnd(list.get(i));
            if (end != null) {
                list2.add(end);
            }
        }
        return list2;
    }

    public static ArrayList<End> makeEndListForMyPic(final Profile profile, final ArrayList<MyPic> list) {
        final ArrayList<End> list2 = new ArrayList<End>();
        for (int i = 0; i < list.size(); ++i) {
            final End end = makeEnd(profile, list.get(i));
            if (end != null) {
                list2.add(end);
            }
        }
        return list2;
    }

    public static ArrayList<End> makeEndListForRepic(final ArrayList<Repic> list) {
        final ArrayList<End> list2 = new ArrayList<End>();
        for (int i = 0; i < list.size(); ++i) {
            final End end = makeEnd(list.get(i));
            if (end != null) {
                list2.add(end);
            }
        }
        return list2;
    }

    public static ArrayList<End> makeEndListForSearchPic(final ArrayList<SearchPic> list) {
        final ArrayList<End> list2 = new ArrayList<End>();
        for (int i = 0; i < list.size(); ++i) {
            final End end = makeEnd(list.get(i));
            if (end != null) {
                list2.add(end);
            }
        }
        return list2;
    }

    public static ArrayList<End> makeEndListForThumbnailPic(final Picker picker, final ArrayList<ThumbnailPic> list) {
        final ArrayList<End> list2 = new ArrayList<End>();
        for (int i = 0; i < list.size(); ++i) {
            final End end = makeEnd(picker, list.get(i));
            if (end != null) {
                list2.add(end);
            }
        }
        return list2;
    }

    private static Picker makePicker(final Profile profile) {
        if (profile == null) {
            return null;
        }
        try {
            final Picker picker = new Picker();
            picker.setMemberNo(profile.getMemberNo());
            picker.setNickname(profile.getNickname());
            picker.setImageUrl(profile.getImageUrl());
            picker.setFollowingYn(profile.getFollowingYn());
            picker.setTags(profile.getTags());
            return picker;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }
}
package com.mapia.myfeed.grid;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mapia.MainActivity;
import com.mapia.R;
import com.mapia.api.model.FeedPic;
import com.mapia.common.Thumb;
import com.mapia.custom.CollageImageView;
import com.mapia.custom.HeightableRelativeLayout;
import com.mapia.custom.SizeableLinearLayout;
import com.mapia.endpage.EndModelAdapter;
import com.mapia.util.GalleryUtils;
import com.mapia.util.ThumbUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyfeedPicGridAdapter extends ArrayAdapter<FeedPic>
{
    private ArrayList<FeedPic> arrayList;
    private boolean hasNextPage;
    private LayoutInflater layoutinflater;
    private MainActivity mainActivity;
    private int size;

    public MyfeedPicGridAdapter(final MainActivity mainActivity, final ArrayList<FeedPic> arrayList) {
        super((Context)mainActivity, Integer.MIN_VALUE, (List)arrayList);
        this.arrayList = arrayList;
        this.mainActivity = mainActivity;
        this.layoutinflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        final View inflate = this.layoutinflater.inflate(R.layout.fragment_common_cell, (ViewGroup)null);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.root = (HeightableRelativeLayout)inflate.findViewById(R.id.fcc_root);
        viewHolder.back = (SizeableLinearLayout)inflate.findViewById(R.id.fcc_background);
        viewHolder.image = (CollageImageView)inflate.findViewById(R.id.fcc_imageview);
        viewHolder.play = (SizeableLinearLayout)inflate.findViewById(R.id.fcc_video);
        viewHolder.gradient = (LinearLayout)inflate.findViewById(R.id.fcc_gradient);
        viewHolder.repic = (ImageView)inflate.findViewById(R.id.fcc_repic);
        viewHolder.noImage = (ImageView)inflate.findViewById(R.id.fcc_noimage);
        final FeedPic feedPic = this.arrayList.get(n);
        final HashMap<String, Integer> threeColumnThumbWidthHeight = GalleryUtils.getThreeColumnThumbWidthHeight(feedPic.getPicImageWidth(), feedPic.getPicImageHeight());
        viewHolder.root.setHeight(threeColumnThumbWidthHeight.get("height"));
        viewHolder.back.setWidthHeight(threeColumnThumbWidthHeight.get("width"), threeColumnThumbWidthHeight.get("height"));
        if (StringUtils.isNotBlank(feedPic.getPicImageColor())) {
            viewHolder.back.setBackgroundColor(Color.parseColor(feedPic.getPicImageColor()));
        }
        String s = "";
        switch (GalleryUtils.getRatioType(feedPic.getPicImageWidth(), feedPic.getPicImageHeight())) {
            case 0: {
                s = ThumbUtils.getSuffix(Thumb.MYFEED_GRID_11);
                break;
            }
            case 1: {
                s = ThumbUtils.getSuffix(Thumb.MYFEED_GRID_43);
                break;
            }
            case 2: {
                s = ThumbUtils.getSuffix(Thumb.MYFEED_GRID_34);
                break;
            }
        }
        if (!StringUtils.isNotBlank(viewHolder.imageUrl) || !viewHolder.imageUrl.equals(feedPic.getPicImageUrl())) {
            viewHolder.imageUrl = feedPic.getPicImageUrl();
            viewHolder.image.setImageDrawable((Drawable)null);
            if (StringUtils.isBlank(feedPic.getPicImageUrl())) {
                viewHolder.noImage.setVisibility(View.VISIBLE);
                viewHolder.image.setImageDrawable((Drawable)null);
                viewHolder.image.setOnClickListener((View.OnClickListener)null);
            }
            else {
                viewHolder.noImage.setVisibility(View.INVISIBLE);
                viewHolder.image.setImageUrl(feedPic.getPicImageUrl() + s, threeColumnThumbWidthHeight.get("width"), threeColumnThumbWidthHeight.get("height"));
                viewHolder.image.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                    public void onClick(final View view) {
//                        AceUtils.nClick(NClicks.MYFEED_THUMB);
                        final Bundle arguments = new Bundle();
                        arguments.putInt("position", n);
                        arguments.putInt("from", 2);
                        arguments.putLong("lastFeedId", (long)MyfeedPicGridAdapter.this.arrayList.get(MyfeedPicGridAdapter.this.arrayList.size() - 1).getFeedId());
                        arguments.putInt("size", MyfeedPicGridAdapter.this.size);
                        arguments.putBoolean("hasNextPage", MyfeedPicGridAdapter.this.hasNextPage);
                        arguments.putSerializable("endArgs", (Serializable) EndModelAdapter.makeEndListForFeedPic(MyfeedPicGridAdapter.this.arrayList));
//                        final EndViewPagerFragment endViewPagerFragment = new EndViewPagerFragment();
//                        endViewPagerFragment.setArguments(arguments);
//                        MyfeedPicGridAdapter.this.mainActivity.hideMenuBar(true);
//                        MyfeedPicGridAdapter.this.mainActivity.addFragment(endViewPagerFragment);
                    }
                });
            }
        }
        if ("CLIP".equals(feedPic.getFileType())) {
            viewHolder.play.setVisibility(View.VISIBLE);
            viewHolder.play.setWidthHeight(threeColumnThumbWidthHeight.get("width"), threeColumnThumbWidthHeight.get("height"));
        }
        else {
            viewHolder.play.setVisibility(View.INVISIBLE);
        }
        if (feedPic.getRepicCount() > 0) {
            viewHolder.gradient.setVisibility(View.INVISIBLE);
        }
        else {
            viewHolder.gradient.setVisibility(View.INVISIBLE);
        }
        if (feedPic.getRepicCount() > 0) {
            viewHolder.repic.setVisibility(View.VISIBLE);
            return inflate;
        }
        viewHolder.repic.setVisibility(View.INVISIBLE);
        return inflate;
    }

    public void setHasNextPage(final boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    private static class ViewHolder
    {
        public SizeableLinearLayout back;
        public LinearLayout gradient;
        public CollageImageView image;
        public String imageUrl;
        public ImageView noImage;
        public SizeableLinearLayout play;
        public ImageView repic;
        public HeightableRelativeLayout root;
    }
}
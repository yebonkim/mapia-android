package com.mapia.search.pic;

/**
 * Created by daehyun on 15. 6. 7..
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mapia.MainActivity;
import com.mapia.R;
import com.mapia.api.model.SearchPic;
import com.mapia.custom.CollageImageView;
import com.mapia.custom.HeightableRelativeLayout;
import com.mapia.custom.SizeableLinearLayout;

import java.util.ArrayList;
import java.util.List;


public class SearchPicResultAdapter extends ArrayAdapter<SearchPic>
{
    private ArrayList<SearchPic> arrayList;
    private boolean hasNextPage;
    private LayoutInflater layoutinflater;
    private MainActivity mainActivity;
    private int page;
    private String query;
    private int size;

    public SearchPicResultAdapter(final MainActivity mainActivity, final ArrayList<SearchPic> arrayList) {
        super((Context)mainActivity, Integer.MIN_VALUE, (List)arrayList);
        this.arrayList = arrayList;
        this.layoutinflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
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
        final SearchPic searchPic = this.arrayList.get(n);
//        final HashMap<String, Integer> threeColumnThumbWidthHeight = GalleryUtils.getThreeColumnThumbWidthHeight(searchPic.getPicImageWidth(), searchPic.getPicImageHeight());
//        viewHolder.root.setHeight(threeColumnThumbWidthHeight.get("height"));
//        viewHolder.back.setWidthHeight(threeColumnThumbWidthHeight.get("width"), threeColumnThumbWidthHeight.get("height"));
//        if (StringUtils.isNotBlank(searchPic.getPicImageColor())) {
//            viewHolder.back.setBackgroundColor(Color.parseColor(searchPic.getPicImageColor()));
//        }
        String s = "";
//        switch (GalleryUtils.getRatioType(searchPic.getPicImageWidth(), searchPic.getPicImageHeight())) {
//            case 0: {
//                s = ThumbUtils.getSuffix(Thumb.TAG_GALLERY_GRID_11);
//                break;
//            }
//            case 1: {
//                s = ThumbUtils.getSuffix(Thumb.TAG_GALLERY_GRID_43);
//                break;
//            }
//            case 2: {
//                s = ThumbUtils.getSuffix(Thumb.TAG_GALLERY_GRID_34);
//                break;
//            }
//        }
//        if (!StringUtils.isNotBlank(viewHolder.imageUrl) || !viewHolder.imageUrl.equals(searchPic.getPicImageUrl())) {
//            viewHolder.imageUrl = searchPic.getPicImageUrl();
//            viewHolder.image.setImageDrawable((Drawable)null);
//            if (StringUtils.isBlank(searchPic.getPicImageUrl())) {
//                viewHolder.noImage.setVisibility(0);
//                viewHolder.image.setImageDrawable((Drawable)null);
//                viewHolder.image.setOnClickListener((View.OnClickListener)null);
//            }
//            else {
//                viewHolder.noImage.setVisibility(8);
//                viewHolder.image.setImageUrl(searchPic.getPicImageUrl() + s, threeColumnThumbWidthHeight.get("width"), threeColumnThumbWidthHeight.get("height"));
//                viewHolder.image.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
//                    public void onClick(final View view) {
//                        final Bundle arguments = new Bundle();
//                        arguments.putInt("position", n);
//                        arguments.putInt("from", 10);
//                        arguments.putString("query", SearchPicResultAdapter.this.query);
//                        arguments.putInt("size", SearchPicResultAdapter.this.size);
//                        arguments.putInt("page", SearchPicResultAdapter.this.page);
//                        arguments.putBoolean("hasNextPage", SearchPicResultAdapter.this.hasNextPage);
//                        arguments.putSerializable("endArgs", (Serializable)EndModelAdapter.makeEndListForSearchPic(SearchPicResultAdapter.this.arrayList));
//                        final EndViewPagerFragment endViewPagerFragment = new EndViewPagerFragment();
//                        endViewPagerFragment.setArguments(arguments);
//                        SearchPicResultAdapter.this.mainActivity.addFragment(endViewPagerFragment);
//                    }
//                });
//            }
//        }
//        if ("CLIP".equals(searchPic.getFileType())) {
//            viewHolder.play.setVisibility(0);
//            viewHolder.play.setWidthHeight(threeColumnThumbWidthHeight.get("width"), threeColumnThumbWidthHeight.get("height"));
//        }
//        else {
//            viewHolder.play.setVisibility(8);
//        }
        if (searchPic.getRepicCount() > 0) {
            viewHolder.gradient.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.gradient.setVisibility(View.INVISIBLE);
        }
        if (searchPic.getRepicCount() > 0) {
            viewHolder.repic.setVisibility(View.VISIBLE);
            return inflate;
        }
        viewHolder.repic.setVisibility(View.INVISIBLE);
        return inflate;
    }

    public void setQueryParams(final String query, final int page, final int size, final boolean hasNextPage) {
        this.query = query;
        this.page = page;
        this.size = size;
        this.hasNextPage = hasNextPage;
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
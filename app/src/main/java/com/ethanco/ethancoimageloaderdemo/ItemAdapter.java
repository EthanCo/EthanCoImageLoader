package com.ethanco.ethancoimageloaderdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethanco.etahncoimageloader.ImageLoader;
import com.ethanco.etahncoimageloader.SquareImageView;

/**
 * Created by Zhk on 2015/12/13.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHodler> {

    private final String[] urlList;
    private final Context mContext;
    private final Drawable mDefaultBitmapDrawable;
    private boolean isScrolling = false;
    private ImageLoader imageLoader;

    public boolean isScrolling() {
        return isScrolling;
    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    public ItemAdapter(String[] urlList, Context context) {
        this.urlList = urlList;
        this.mContext = context;
        imageLoader = ImageLoader.buildImageLoader(context);

        mDefaultBitmapDrawable = DrawableCompat.getDrawable(context, R.mipmap.image_default);
    }

    @Override
    public ItemViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        ItemViewHodler itemViewHodler = new ItemViewHodler(view);
        return itemViewHodler;
    }

    @Override
    public void onBindViewHolder(ItemViewHodler holder, int position) {
        holder.imgView.setImageDrawable(mDefaultBitmapDrawable);
        if (isScrolling) {
            //holder.imgView.setImageDrawable(mDefaultBitmapDrawable);
        } else {
            imageLoader.dispaly(urlList[position], holder.imgView);
        }
    }

    @Override
    public int getItemCount() {
        return urlList.length;
    }

    class ItemViewHodler extends RecyclerView.ViewHolder {
        private SquareImageView imgView;

        public ItemViewHodler(View itemView) {
            super(itemView);
            imgView = (SquareImageView) itemView.findViewById(R.id.imgView);
        }
    }
}

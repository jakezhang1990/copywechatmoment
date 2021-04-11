package com.example.copywechatmoments.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.example.copywechatmoments.R;


public class VideotViewHolder extends ComViewHolder {

    public ImageView videos;
    public VideotViewHolder(View itemView) {
        super(itemView);
        videos=(ImageView) itemView.findViewById(R.id.dongtai_videos);
    }
}

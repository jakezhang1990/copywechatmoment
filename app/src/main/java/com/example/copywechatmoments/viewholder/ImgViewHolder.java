package com.example.copywechatmoments.viewholder;

import android.view.View;

import com.example.copywechatmoments.R;
import com.example.copywechatmoments.widget.NineGridView;


public class ImgViewHolder extends ComViewHolder {
    public NineGridView nineGridView;
    public ImgViewHolder(View itemView) {
        super(itemView);
        nineGridView=(NineGridView) itemView.findViewById(R.id.dongtai_layout_nine);
    }
}

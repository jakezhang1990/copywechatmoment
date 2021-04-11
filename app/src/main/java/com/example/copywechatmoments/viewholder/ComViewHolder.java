package com.example.copywechatmoments.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.copywechatmoments.R;
import com.example.copywechatmoments.widget.CommentsView;
import com.example.copywechatmoments.widget.ExpandTextView;
import com.example.copywechatmoments.widget.PraiseListView;


public class ComViewHolder extends RecyclerView.ViewHolder {
    public ImageView portrait;//发布人头像
    public TextView nickname;//发布人昵称
    public ExpandTextView content;//文字内容
    public TextView time;//发布时间
    public TextView delete;//删除
    public ImageView dianzanPinglun;//点赞评论按钮
    public LinearLayout linearLayoutAll;//点赞评论的背景
    public PraiseListView dianzanList;//点赞列表
    public CommentsView pinglunList;//评论列表
    public View dongtaiDriver;
    public ComViewHolder(View itemView) {
        super(itemView);
        portrait = (ImageView) itemView.findViewById(R.id.dongtai_portrait);
        nickname = (TextView) itemView.findViewById(R.id.dongtai_nickname);
        content = (ExpandTextView) itemView.findViewById(R.id.dongtai_content);
        time = (TextView) itemView.findViewById(R.id.dongtai_tv_time);
        delete = (TextView) itemView.findViewById(R.id.dongtai_tv_delete);
        dianzanPinglun = (ImageView) itemView.findViewById(R.id.dongtai_tv_plugs);
        dianzanList = (PraiseListView) itemView.findViewById(R.id.dongtai_rv_like);
        pinglunList = (CommentsView) itemView.findViewById(R.id.dongtai_rv_comment);
        linearLayoutAll=(LinearLayout)itemView.findViewById(R.id.dongtai_rv_all);
        dongtaiDriver=(View)itemView.findViewById(R.id.dongtai_driver);
    }
}

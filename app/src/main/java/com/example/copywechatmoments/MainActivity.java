package com.example.copywechatmoments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.copywechatmoments.adapter.ExploreDongtaiAdapter;
import com.example.copywechatmoments.adapter.Utils;
import com.example.copywechatmoments.bean.ExploreDongtaiBean;
import com.example.copywechatmoments.bean.ExplorePostDianzanBean;
import com.example.copywechatmoments.bean.ExplorePostPinglunBean;
import com.example.copywechatmoments.listener.Explore_dongtai1_listener;
import com.example.copywechatmoments.utils.CustomDotIndexProvider;
import com.example.copywechatmoments.utils.CustomLoadingUIProvider;
import com.example.copywechatmoments.utils.GlideSimpleLoader;
import com.example.copywechatmoments.utils.KeyboardUtil;
import com.example.copywechatmoments.views.CustomProgressDrawable;
import com.example.copywechatmoments.views.CustomSwipeRefreshLayout;
import com.example.copywechatmoments.widget.LikePopupWindow;
import com.example.copywechatmoments.widget.OnPraiseOrCommentClickListener;

import java.util.ArrayList;
import java.util.List;

import byc.imagewatcher.ImageWatcher;
import byc.imagewatcher.ImageWatcherHelper;

/**
 * @Description: java类作用描述
 * @Author: jakezhang
 * @CreateDate: 2021/4/11
 */
public class MainActivity extends AppCompatActivity implements Explore_dongtai1_listener, ImageWatcher.OnPictureLongPressListener {

    private CustomSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private ExploreDongtaiAdapter adapter;
    private List<ExploreDongtaiBean> mList=new ArrayList<>();

    private LinearLayout llComment;
    private EditText etComment;
    private TextView tvSend;

    public ImageWatcherHelper iwHelper;//方式二
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        llComment = findViewById(R.id.ll_comment);
        etComment = findViewById(R.id.et_comment);
        tvSend = findViewById(R.id.tv_send_comment);
        boolean isTranslucentStatus = false;
        //        新的初始化方式二，不再需要在布局文件中加入<ImageWatcher>标签 减少布局嵌套
        iwHelper = ImageWatcherHelper.with(this, new GlideSimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setTranslucentStatus(!isTranslucentStatus ? Utils.calcStatusBarHeight(this) : 0) // 如果不是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                .setErrorImageRes(R.mipmap.error_picture) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
                .setOnPictureLongPressListener(this)
                .setOnStateChangedListener(new ImageWatcher.OnStateChangedListener() {
                    @Override
                    public void onStateChangeUpdate(ImageWatcher imageWatcher, ImageView clicked, int position, Uri uri, float animatedValue, int actionTag) {
                        Log.e("IW", "onStateChangeUpdate [" + position + "][" + uri + "][" + animatedValue + "][" + actionTag + "]");
                    }

                    @Override
                    public void onStateChanged(ImageWatcher imageWatcher, int position, Uri uri, int actionTag) {
                        if (actionTag == ImageWatcher.STATE_ENTER_DISPLAYING) {
                            //  Toast.makeText(getApplicationContext(), "点击了图片 [" + position + "]" + uri + "", Toast.LENGTH_SHORT).show();
                        } else if (actionTag == ImageWatcher.STATE_EXIT_HIDING) {
                            //  Toast.makeText(getApplicationContext(), "退出了查看大图", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setIndexProvider(new CustomDotIndexProvider())//自定义页码指示器（默认数字）
                .setLoadingUIProvider(new CustomLoadingUIProvider()); // 自定义LoadingUI


        setData();

        adapter=new ExploreDongtaiAdapter(mList,this,this);
        adapter.setIwHelper(iwHelper);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setAdapter(adapter);
        setHeader(mRecyclerView);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideInput();
                return false;
            }
        });

        CustomProgressDrawable drawable = new CustomProgressDrawable(this, mRefreshLayout);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moments_refresh_icon);
        drawable.setBitmap(bitmap);

        mRefreshLayout.setProgressView(drawable);
        mRefreshLayout.setBackgroundColor(Color.BLACK);
        mRefreshLayout.setProgressBackgroundColorSchemeColor(Color.TRANSPARENT);//背景设置透明
        mRefreshLayout.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mRefreshLayout.setRefreshing(false);
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
    }
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.explore_pengyouquan_head_item, view, false);
        adapter.setHeaderView(header);
    }
    public void setData(){
        //文字示例
        ExploreDongtaiBean bean=new ExploreDongtaiBean();
        bean.setType(1);
        bean.setCreattime(System.currentTimeMillis());
        bean.setId(469);
        bean.setUserid(28);
        bean.setNickname("jakezhang1990");
        bean.setHandimg("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F00%2F90%2F47%2F9256efcbf458c23.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620740197&t=01520af4bacf09871e5c92ed72f75a12");
        bean.setWrittenwords("hello world，发布朋友圈消息内容\n发布消息\n朋友圈消息内容\n内容");
        bean.setLikeuself(1);
//        "altnickname":"",
//                "altuserid":"",
//                "content":"八菱科技",
//                "creattime":1608632610000,
//                "friendid":468,
//                "id":60,
//                "userid":28,
//                "usernickname":"污嫚尔"
//    }
        List<ExplorePostPinglunBean> evea=new ArrayList<>();
        ExplorePostPinglunBean pinglunBean=new ExplorePostPinglunBean();
        pinglunBean.setId(60);
        pinglunBean.setUserid(28);
        pinglunBean.setUsernickname("ThoughtWorks");
        pinglunBean.setContent("hello, i am ThoughtWorks");
        pinglunBean.setCreattime(1608632610000l);
        pinglunBean.setFriendid(468);
        evea.add(pinglunBean);

        ExplorePostPinglunBean pinglunBean1=new ExplorePostPinglunBean();
        pinglunBean1.setId(60);
        pinglunBean1.setUserid(29);
        pinglunBean1.setUsernickname("jakezhang1990");
        pinglunBean1.setContent("你好");
        pinglunBean1.setCreattime(1608632610000l);
        pinglunBean1.setFriendid(468);
        pinglunBean1.setAltuserid(28+"");
        pinglunBean1.setAltnickname("ThoughtWorks");
        evea.add(pinglunBean1);

        bean.setEvea(evea);

        List<ExplorePostDianzanBean> fabulous=new ArrayList<>();//点赞列表
        ExplorePostDianzanBean dianzanBean=new ExplorePostDianzanBean();
        dianzanBean.setCreattime(1608958770000l);
        dianzanBean.setFriendid(468);
        dianzanBean.setId(294);
        dianzanBean.setUserid(28);
        dianzanBean.setUsernickname("HW honor");
        fabulous.add(dianzanBean);

        ExplorePostDianzanBean dianzanBean1=new ExplorePostDianzanBean();
        dianzanBean1.setCreattime(1608958770001l);
        dianzanBean1.setFriendid(468);
        dianzanBean1.setId(295);
        dianzanBean1.setUserid(29);
        dianzanBean1.setUsernickname("jakezhang");
        fabulous.add(dianzanBean1);

        bean.setFabulous(fabulous);
        mList.add(bean);
        //图片示例
        ExploreDongtaiBean bean1=new ExploreDongtaiBean();
        bean1.setType(2);
        bean1.setCreattime(System.currentTimeMillis());
        bean1.setId(469);
        bean1.setUserid(28);
        bean1.setNickname("jakezhang1880");
        bean1.setHandimg("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=734658732,254565339&fm=26&gp=0.jpg");
        bean1.setWrittenwords("测试内容\nf\nw\nss");
        bean1.setLikeuself(1);
        //9宫格显示，图片地址
        bean1.setThumbnail("https://pics0.baidu.com/feed/1f178a82b9014a90731da823f99e6d1ab21bee7b.jpeg?token=033c65e7eccf471d086f55a78f6669a4,https://pics4.baidu.com/feed/55e736d12f2eb938a280efcf838bd13de4dd6f0b.jpeg?token=4fda64ff25f7b8d5aab7aaa134bf5f84,https://image.renlaibang.com/Fp5mQrZYnhSRoKayTsPEaBO18Wrt,https://pics1.baidu.com/feed/738b4710b912c8fcf5932871a8eac64dd788211f.jpeg?token=d97e2a47a9bdc3efd26b21f8de50e50c,https://pics2.baidu.com/feed/b2de9c82d158ccbf6e957d8d4f31e836b0354149.jpeg?token=f170ff0238f7092097b12e1861d16ef6,https://pics3.baidu.com/feed/5ab5c9ea15ce36d33ac1702e6a1a6e8feb50b1dd.jpeg?token=1bed46a336c4b0b36031061316e36e81,https://imagepphcloud.thepaper.cn/pph/image/125/452/559.jpg,https://pics2.baidu.com/feed/0823dd54564e92586a1c3c04998e8450cdbf4ec0.png?token=608f7de75bfc95acb8cbdeeca2885d17,https://images.shobserver.com/news/690_390/2021/4/11/f7098bbcc81d48dfaf016ade4496f6f4.jpg");
       //大图预览，图片地址
        bean1.setImgs("https://pics0.baidu.com/feed/1f178a82b9014a90731da823f99e6d1ab21bee7b.jpeg?token=033c65e7eccf471d086f55a78f6669a4,https://pics4.baidu.com/feed/55e736d12f2eb938a280efcf838bd13de4dd6f0b.jpeg?token=4fda64ff25f7b8d5aab7aaa134bf5f84,https://image.renlaibang.com/Fp5mQrZYnhSRoKayTsPEaBO18Wrt,https://pics1.baidu.com/feed/738b4710b912c8fcf5932871a8eac64dd788211f.jpeg?token=d97e2a47a9bdc3efd26b21f8de50e50c,https://pics2.baidu.com/feed/b2de9c82d158ccbf6e957d8d4f31e836b0354149.jpeg?token=f170ff0238f7092097b12e1861d16ef6,https://pics3.baidu.com/feed/5ab5c9ea15ce36d33ac1702e6a1a6e8feb50b1dd.jpeg?token=1bed46a336c4b0b36031061316e36e81,https://imagepphcloud.thepaper.cn/pph/image/125/452/559.jpg,https://pics2.baidu.com/feed/0823dd54564e92586a1c3c04998e8450cdbf4ec0.png?token=608f7de75bfc95acb8cbdeeca2885d17,https://images.shobserver.com/news/690_390/2021/4/11/f7098bbcc81d48dfaf016ade4496f6f4.jpg");
        mList.add(bean1);
        //视频示例
        ExploreDongtaiBean bean2=new ExploreDongtaiBean();
        bean2.setType(3);
        bean2.setCreattime(System.currentTimeMillis());
        bean2.setId(469);
        bean2.setUserid(28);
        bean2.setNickname("jakezhang");
        bean2.setHandimg("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2761721443,1041659468&fm=26&gp=0.jpg");
        bean2.setWrittenwords("测试内容\nf\nw\nss");
        bean2.setLikeuself(1);
        bean2.setThumbnail("https://image.renlaibang.com/FmQdWZrmemYUMGrnNFfewz6t4HAR");
        bean2.setVideos("https://image.renlaibang.com/FkHt1Sft6H0zrhAzwWWfdwrw0h4L");
        mList.add(bean2);
    }
    //评论
    @Override
    public void onPinlunEdit(View view, int friendid, String userid, String userName) {
        showPinglunPopupWindow1(view, friendid, userid, userName);
    }
    //点击昵称，头像
    @Override
    public void onClickUser(String userid) {

    }
    //点击评论点赞按钮
    @Override
    public void onClickEdit(View view,int position) {
        //评论弹框
        showLikePopupWindow(view, position);
    }
    //点击删除朋友圈按钮
    @Override
    public void deletePengyouquan(int id) {

    }
    //删除评论按钮
    @Override
    public void deleteMypinglun(int ids, ExplorePostPinglunBean id) {

    }
    //点击图片
    @Override
    public void imageOnclick() {

    }
    //点击视频
    @Override
    public void videoOnclick(String img, String httpUrl) {
        hideInput();
        Intent dynamicHaoyou = new Intent(this, ExploreVideoPlayer.class);
        dynamicHaoyou.putExtra("typeImg", img);
        dynamicHaoyou.putExtra("typeHttpUrl", httpUrl);
        startActivity(dynamicHaoyou );
    }
    public void hideInput(){
        llComment.setVisibility(View.GONE);
        KeyboardUtil.hideSoftInput(this, etComment);
    }
    private LikePopupWindow likePopupWindow;
    private void showLikePopupWindow(final View view, int position) {
        //item 底部y坐标
        final int mBottomY = getCoordinateY(view) + view.getHeight();
        if (likePopupWindow == null) {
            likePopupWindow = new LikePopupWindow(this, 0);//0.1,分别代表是否点赞
        }
        likePopupWindow.setOnPraiseOrCommentClickListener(new OnPraiseOrCommentClickListener() {
            @Override
            public void onPraiseClick(int position) {
                //调用点赞接口
                likePopupWindow.dismiss();
            }

            @Override
            public void onCommentClick(int position) {
                llComment.setVisibility(View.VISIBLE);
                etComment.requestFocus();
                etComment.setHint("说点什么");

                KeyboardUtil.showSoftInput(MainActivity.this);
                likePopupWindow.dismiss();
                etComment.setText("");
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int y = getCoordinateY(llComment);// - 20;
                        //评论时滑动到对应item底部和输入框顶部对齐
                        mRecyclerView.smoothScrollBy(0, mBottomY - y);
                    }
                },300);
            }

            @Override
            public void onClickFrendCircleTopBg() {

            }

            @Override
            public void onDeleteItem(String id, int position) {

            }

        }).setTextView(0).setCurrentPosition(position);
        if (likePopupWindow.isShowing()) {
            likePopupWindow.dismiss();
        } else {
            likePopupWindow.showPopupWindow(view);
        }
    }

    /**
     * 获取控件左上顶点Y坐标
     *
     * @param view
     * @return
     */
    private int getCoordinateY(View view) {
        int[] coordinate = new int[2];
        view.getLocationOnScreen(coordinate);
        return coordinate[1];
    }

    @Override
    public void onPictureLongPress(ImageView v, Uri uri, int pos) {

    }
    @Override
    public void onBackPressed() {
        //方式二
        if (!iwHelper.handleBackPressed()) {
            super.onBackPressed();
        }
    }

    //当前我给谁发评论，
    private void showPinglunPopupWindow1(View view, int friendid, String userid, String username) {
        //item 底部y坐标
        final int mBottomY = getCoordinateY(view) + view.getHeight();

        llComment.setVisibility(View.VISIBLE);
        etComment.requestFocus();
        if (userid == null || userid.equals("")) {//回复这条评论的发送人，楼主
            etComment.setHint("说点什么");
        } else {
            etComment.setHint("回复:" + username);//回复这个人
        }

        KeyboardUtil.showSoftInput(this, etComment);
        etComment.setText("");
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                int y = getCoordinateY(llComment);// - 20;
                //评论时滑动到对应item底部和输入框顶部对齐
                mRecyclerView.smoothScrollBy(0, mBottomY - y);
            }
        },300);


    }
}
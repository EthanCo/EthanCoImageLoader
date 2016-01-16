package com.ethanco.ethancoimageloaderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private String[] imageUrls = {
            "http://img3.3lian.com/2013/c2/66/95.jpg",
            "http://img1.3lian.com/img2011/w11/1116/1/43.jpg",
            "http://img1.3lian.com/img13/c4/51/14.jpg",
            "http://img1.3lian.com/img13/c3/37/22.jpg",
            "http://img1.3lian.com/2015/w7/29/104.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2817748302,1891413977&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1530732288,347098515&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3146704466,2091127871&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3553126113,3221262004&fm=21&gp=0.jpg",
            "http://picview01.baomihua.com/photos/20120412/m_14_634697891848125000_11915460.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2186344841,3528369720&fm=21&gp=0.jpg",
            "http://img1.3lian.com/img2011/w11/1122/25/53.jpg",
            "http://img1.3lian.com/img2011/w12/1220/20/11.jpg",
            "http://img5q.duitang.com/uploads/item/201402/17/20140217195549_sW8aV.thumb.224_0.jpeg",
            "http://img1.3lian.com/2015/w2/22/103.jpg",
            "http://img1.3lian.com/2015/w2/22/105.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2504265037,3546099285&fm=21&gp=0.jpg",
            "http://pic17.nipic.com/20111119/3101644_163637840932_2.jpg",
            "http://img.taopic.com/uploads/allimg/120713/201679-120G315464492.jpg",
            "http://img5.imgtn.bdimg.com/it/u=736069922,504121467&fm=21&gp=0.jpg",
            "http://pic12.nipic.com/20101228/4750538_104127059129_2.jpg",
            "http://cdn.duitang.com/uploads/item/201207/18/20120718215150_QyFQZ.jpeg",
            "http://img4.imgtn.bdimg.com/it/u=1226338749,1690729399&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1564731299,2795144819&fm=21&gp=0.jpg",
            "http://img5.duitang.com/uploads/item/201207/07/20120707115632_EN3S2.jpeg",
            "http://pic31.nipic.com/20130630/12266309_224901498151_2.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3884698657,600464042&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201302/20/20130220150901_UGPLv.thumb.600_0.jpeg",
            "http://img2.imgtn.bdimg.com/it/u=4276370009,3044981971&fm=21&gp=0.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1305/27/c1/21362913_1369670150303_320x480.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1210980644,3677677792&fm=21&gp=0.jpg",
            "http://pic.33.la/20140525bztp/8079.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1350029324,4209216500&fm=21&gp=0.jpg",
            "http://image.tianjimedia.com/uploadImages/2013/235/EL7SZN7NJ8TS.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3494609009,854713562&fm=21&gp=0.jpg",
            "http://d.3987.com/qxbz_140331/004.jpg"
    };
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        itemAdapter = new ItemAdapter(imageUrls, this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("zhk-MainActivity", "onScrollStateChanged: ");
                if (newState == recyclerView.SCROLL_STATE_IDLE) {
                    Log.i("zhk-MainActivity", "onScrollStateChanged: SCROLL_STATE_IDLE");
                    itemAdapter.setScrolling(true);
                } else {
                    itemAdapter.setScrolling(false);
                }
            }
        });
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}
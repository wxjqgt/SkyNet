package com.weibo.skynet.fragment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.weibo.skynet.R;
import com.weibo.skynet.activity.MainActivity;
import com.weibo.skynet.base.BaseFragment;
import com.weibo.skynet.base.CommonRecyclerViewAdapter;
import com.weibo.skynet.presenter.ImagePresenter;
import com.weibo.skynet.presenter.MainPresener;
import com.weibo.skynet.util.Recyclerview_animation;

import java.util.List;
import rx.Subscription;

public class Fragment_ImageList extends BaseFragment implements ImagePresenter.ShowView, CommonRecyclerViewAdapter.OnItemClickLitener {

    private MainPresener imageListPresener;
    private Subscription subscription;
    private MainActivity mActivity;

    private RecyclerView r_image;

    @Override
    protected void findView() {
        super.findView();
        view = inflater.inflate(R.layout.fragment_image, container, false);
        r_image = (RecyclerView) view.findViewById(R.id.recyclerview_image);
    }

    @Override
    protected void bindData() {
        super.bindData();
        imageListPresener = new ImagePresenter(this);
        imageListPresener.load();
    }

    @Override
    public void onLoadImageDataForViewSuccess(List<String> value) {
        setRecyclerView(value);
    }

    @Override
    public void onLoadImageDataForViewlater(Subscription subscription) {
        this.subscription = subscription;
    }

    private void setRecyclerView(List<String> value) {
        CommonRecyclerViewAdapter<String> adapter = new CommonRecyclerViewAdapter<String>(
                mActivity, R.layout.recyclerview_item, value) {
            @Override
            public void convert(ViewHolder holder, String url, int position) {
                ImageView imageView = holder.getView(R.id.imageview_item);
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = mHeights.get(position);
                imageView.setLayoutParams(lp);
                holder.setImageUrl(R.id.imageview_item, url);
                holder.setText(R.id.textUrl, url);
            }
        };
        adapter.setOnItemClickLitener(this);
        r_image.setLayoutManager(new StaggeredGridLayoutManager(
                4, StaggeredGridLayoutManager.VERTICAL));
        r_image.setAdapter(adapter);
        r_image.setItemAnimator(new DefaultItemAnimator());
        r_image.addItemDecoration(new Recyclerview_animation(mActivity));

    }

    @Override
    public void onItemClick(View view, int position) {
        mActivity.showViewPagerImage(position);
    }

    @Override
    public void onItemLongClick(View view, final int position) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    public Fragment_ImageList() {}

    public static Fragment_ImageList newInstance() {
        Fragment_ImageList fragment = new Fragment_ImageList();
        return fragment;
    }

}
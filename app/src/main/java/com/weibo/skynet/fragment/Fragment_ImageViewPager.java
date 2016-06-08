package com.weibo.skynet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.weibo.skynet.R;
import com.weibo.skynet.activity.MainActivity;
import com.weibo.skynet.adapter.ViewpagerAdapter;
import com.weibo.skynet.base.BaseFragment;
import com.weibo.skynet.presenter.ImageViewPagerPresenter;
import com.weibo.skynet.presenter.MainPresener;
import com.weibo.skynet.util.Custant;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ImageViewPager extends BaseFragment implements ImageViewPagerPresenter.ShowView, ViewPager.OnPageChangeListener {

    private int currentItem,pagercount = 0;
    private MainPresener imageViewPagerPresenter;
    private Subscription subscription;
    private MainActivity mActivity;

    private ViewPager viewPager;
    private TextView pager;

    @Override
    protected void getData() {
        super.getData();
        currentItem = bundle.getInt(Custant.URL_KEY);
    }

    @Override
    protected void findView() {
        super.findView();
        view = inflater.inflate(R.layout.imageviewpager, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.ViewPager_image);
        pager = (TextView) view.findViewById(R.id.text_pager);
    }

    @Override
    protected void bindData() {
        super.bindData();
        imageViewPagerPresenter = new ImageViewPagerPresenter(this);
        imageViewPagerPresenter.load();
    }

    @Override
    public void onLoadImageViewPagerDataForViewSuccess(List<String> value) {
        setViewPager(value);
    }

    @Override
    public void onLoadImageViewPagerDataForViewlater(Subscription subscription) {
        this.subscription = subscription;
    }

    private void setViewPager(List<String> value) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(mActivity.getSupportFragmentManager());
        pagercount = value.size();
        viewPager.setOnPageChangeListener(this);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < pagercount; i++) {
            Fragment f = Fragment_Image.newInstance(value.get(i));
            fragments.add(f);
        }
        adapter.addFragments(fragments);
        viewPager.setAdapter(adapter);
        fragments.clear();
        if (currentItem > -1) {
            setImageCurrentPosition(currentItem);
        }
    }

    public void setImageCurrentPosition(int position) {
        this.currentItem = position;
        viewPager.setCurrentItem(currentItem, false);
        pager.setText(currentItem + 1 + "/" + pagercount);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pager.setText(position + 1 + "/" + pagercount);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
    }


    public Fragment_ImageViewPager() {}

    public static Fragment_ImageViewPager newInstance(int position) {
        Fragment_ImageViewPager fragment = new Fragment_ImageViewPager();
        Bundle bundle = new Bundle();
        bundle.putInt(Custant.URL_KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

}

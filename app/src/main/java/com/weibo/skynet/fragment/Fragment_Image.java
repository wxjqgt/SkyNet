package com.weibo.skynet.fragment;


import android.os.Bundle;

import com.bm.library.PhotoView;
import com.weibo.skynet.R;
import com.weibo.skynet.base.BaseFragment;
import com.weibo.skynet.util.Custant;
import com.weibo.skynet.util.ImageLoader;

public class Fragment_Image extends BaseFragment {

    private String url;
    private PhotoView photoView;

    @Override
    protected void getData() {
        super.getData();
        url = bundle.getString(Custant.KEY);
    }

    @Override
    protected void findView() {
        super.findView();
        view = inflater.inflate(R.layout.viewpageritem,container,false);
        photoView = (PhotoView) view.findViewById(R.id.img);
        photoView.enable();
    }

    @Override
    protected void bindData() {
        super.bindData();
        ImageLoader.load(this,url,R.mipmap.ic_custom,photoView);
    }

    public Fragment_Image() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment_image.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Image newInstance(String url) {
        Fragment_Image fragment = new Fragment_Image();
        Bundle bundle = new Bundle();
        bundle.putString(Custant.KEY, url);
        fragment.setArguments(bundle);
        return fragment;
    }

}

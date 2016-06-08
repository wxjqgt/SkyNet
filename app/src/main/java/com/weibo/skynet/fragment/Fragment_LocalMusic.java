package com.weibo.skynet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weibo.skynet.R;
import com.weibo.skynet.activity.MainActivity;
import com.weibo.skynet.adapter.MusicListAdapter;
import com.weibo.skynet.base.BaseFragment;
import com.weibo.skynet.model.Mp3Info;
import com.weibo.skynet.presenter.MainPresener;
import com.weibo.skynet.presenter.MusicPresenter;
import com.weibo.skynet.util.App_mine;
import com.weibo.skynet.util.Custant;
import com.weibo.skynet.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class Fragment_LocalMusic extends BaseFragment implements MusicPresenter.ShowView,MusicListAdapter.OnItemClickLitener {

    private Subscription s;
    private List<Mp3Info> mp3InfoList = null;
    private RecyclerView lt_localsongs;

    private MainActivity mainActivity;
    private MainPresener musicPresenter;

    @Override
    protected void getData() {
        super.getData();
        mp3InfoList = bundle.getParcelableArrayList(Custant.SONGLIST);
    }

    @Override
    protected void findView() {
        super.findView();
        view = inflater.inflate(R.layout.localsongs, container, false);
        LoadDataLocalsongs(view);
    }

    private void LoadDataLocalsongs(View view) {
        lt_localsongs = (RecyclerView) view.findViewById(R.id.list_localsongs);
        musicPresenter = new MusicPresenter(this);
        musicPresenter.load();
    }

    @Override
    public void onLoadMusicDataForViewSuccess(List<Mp3Info> mp3InfoList) {
        setRecyclerView(mp3InfoList);
    }

    @Override
    public void onLoadMusicDataForViewLater(Subscription s) {
        this.s = s;
    }

    private void setRecyclerView(List<Mp3Info> value) {
        lt_localsongs.setLayoutManager(new LinearLayoutManager(App_mine.context_app));
        MusicListAdapter mla = new MusicListAdapter(App_mine.context_app);
        mla.setData(value);
        lt_localsongs.setItemAnimator(new DefaultItemAnimator());
        lt_localsongs.addItemDecoration(new DividerItemDecoration(App_mine.context_app, DividerItemDecoration.VERTICAL_LIST));
        lt_localsongs.setAdapter(mla);
        mla.setOnItemClickLitener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        mainActivity.ms.playMusic(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (s != null && !s.isUnsubscribed()) {
            s.unsubscribe();
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public Fragment_LocalMusic() {}

    public static Fragment_LocalMusic newInstance(List<Mp3Info> mp3Infos) {
        Fragment_LocalMusic fragment = new Fragment_LocalMusic();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Custant.SONGLIST, (ArrayList<Mp3Info>) mp3Infos);
        fragment.setArguments(args);
        return fragment;
    }

}

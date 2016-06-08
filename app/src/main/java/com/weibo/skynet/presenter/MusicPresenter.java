package com.weibo.skynet.presenter;

import com.weibo.skynet.model.Mp3Info;
import com.weibo.skynet.util.App_mine;
import com.weibo.skynet.util.MainService;
import com.weibo.skynet.util.MediaUtils;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/23.
 */
public class MusicPresenter implements MainPresener{

    private ShowView showView;
    private Subscription s;

    public MusicPresenter(ShowView showView){
        this.showView = showView;
    }

    @Override
    public void load() {
        Single<List<Mp3Info>> single = Single.fromCallable(new Callable<List<Mp3Info>>() {
            @Override
            public List<Mp3Info> call() throws Exception {
                List<Mp3Info> mp3InfoList = MainService.mp3InfoList;
                if (mp3InfoList == null) {
                    mp3InfoList = MediaUtils.getMp3InfoList(App_mine.context_app);
                }
                return mp3InfoList;
            }
        });
        s = single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Mp3Info>>() {
                    @Override
                    public void onSuccess(List<Mp3Info> value) {
                        showView.onLoadMusicDataForViewSuccess(value);
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
        showView.onLoadMusicDataForViewLater(s);
    }

    public interface ShowView{
        void onLoadMusicDataForViewSuccess(List<Mp3Info> mp3InfoList);
        void onLoadMusicDataForViewLater(Subscription s);
    }
}

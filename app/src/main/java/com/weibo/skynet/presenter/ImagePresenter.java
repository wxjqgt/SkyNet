package com.weibo.skynet.presenter;

import android.widget.Toast;

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
public class ImagePresenter implements MainPresener {

    private ShowView showView;
    private Subscription subscription;

    public ImagePresenter(ShowView showView){
        this.showView = showView;
    }

    @Override
    public void load() {
        Single<List<String>> single = Single.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                List<String> images = MainService.imageInfoList;
                if (images == null) {
                    images = MediaUtils.getImagesList(App_mine.context_app);
                }
                return images;
            }
        });
        subscription = single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<String>>() {
                    @Override
                    public void onSuccess(List<String> value) {
                        showView.onLoadImageDataForViewSuccess(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(App_mine.context_app, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        showView.onLoadImageDataForViewlater(subscription);
    }

    public interface ShowView{
        void onLoadImageDataForViewSuccess(List<String> value);
        void onLoadImageDataForViewlater(Subscription subscription);
    }
}

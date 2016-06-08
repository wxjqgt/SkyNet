package com.weibo.skynet.util;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.weibo.skynet.model.Mp3Info;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.vov.vitamio.MediaPlayer;

public class MainService extends Service
        implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener {

    public static MediaPlayer mp;
    private MainBinder mb = null;
    public static List<Mp3Info> mp3InfoList = null;
    public static List<String> imageInfoList = null;
    private ExecutorService service = null;
    private ScheduledExecutorService updateService = null;
    private int musicPosition;
    public static final String KEY = "key";
    public static final String PROGRESS_KEY = "progress_key";
    public static boolean isPause = false, isResume = false;
    private EventType.MusicProgress progress;
    private EventType.MusicPosition position;

    public boolean isPlaying() {
        if (mp != null) {
            return mp.isPlaying();
        }
        return false;
    }

    public long getCurrentPosition() {
        if (mp != null && isPlaying()) {
            return mp.getCurrentPosition();
        }
        return 0;
    }

    public void playMusic(int mposition) {
        if (mp == null) {
            return;
        }
        if (mp3InfoList.size() != 0 && mp3InfoList != null) {
            if (mposition < 0) {
                mposition = mp3InfoList.size() - 1;
            }
            if (mposition >= mp3InfoList.size()) {
                mposition = 0;
            }
        } else {
            return;
        }
        musicPosition = mposition;
        String url = mp3InfoList.get(mposition).getUrl();
        try {
            mp.reset();
            mp.setDataSource(url);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (position == null) {
            position = new EventType.MusicPosition();
        }
        position.setMusicPlayUpdatePosition(musicPosition);
        EventBus.getDefault().post(position);

        isPause = false;
    }

    public void seekTo(int progress) {
        if (mp != null) {
            mp.seekTo(progress);
        }
    }

    public void playNextMusic() {
        musicPosition += 1;
        playMusic(musicPosition);
    }

    public void playLastMusic() {
        musicPosition -= 1;
        playMusic(musicPosition);
    }

    public void resumePlayMusic() {
        if (mp != null) {
            if (isPause) {
                mp.start();
            } else {
                playMusic(SPUtil.getInt(KEY));
                isResume = true;
            }
        }
    }

    public void resumeUpdateMusicUi() {
        int p = SPUtil.getInt(KEY);
        long time = SPUtil.getLong(PROGRESS_KEY);

        if (p == 0 || time == 0) {
            return;
        }
        if (position == null) {
            position = new EventType.MusicPosition();
        }
        position.setMusicPlayUpdatePosition(p);
        EventBus.getDefault().post(position);

        if (progress == null) {
            progress = new EventType.MusicProgress();
        }
        String text = MediaUtils.formattime(time);
        progress.setMusicPlayUpdateProgressText(text);
        progress.setMusicPlayUpdateProgress(time);
        EventBus.getDefault().post(progress);

        EventBus.getDefault().post("resume");

    }

    public void pauseMusic() {
        if (mp != null && mp.isPlaying()) {
            SPUtil.putLong(PROGRESS_KEY, getCurrentPosition());
            mp.pause();
            isPause = true;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        musicPosition += 1;
        playMusic(musicPosition);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        musicPosition += 1;
        playMusic(musicPosition);
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        musicPosition = App_mine.sp.getInt(KEY, 0);
        service = Executors.newFixedThreadPool(3);
        updateService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SPUtil.putInt(KEY, musicPosition);
        if (isPlaying()) {
            SPUtil.putLong(PROGRESS_KEY, getCurrentPosition());
        }
        if (mp != null) {
            mp.release();
            mp = null;
        }
        ClearUtil.clearList(mp3InfoList, imageInfoList);
        ClearUtil.clearObject(position, progress);

        if (service != null) {
            if (!service.isShutdown()) {
                service.shutdown();
                service = null;
            }
        }
        if (updateService != null) {
            if (!updateService.isShutdown()) {
                updateService.shutdown();
                updateService = null;
            }
        }
    }

    public void getInstance() {
        mp = Singleton.getInstance();
        mp.setOnCompletionListener(this);
        mp.setOnErrorListener(this);
    }

    public static class Singleton implements Serializable {

        private static class SingletonHolder {
            static final MediaPlayer INSTANCE = new MediaPlayer(App_mine.context_app);
        }

        public static MediaPlayer getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private Singleton() {
        }

        private Object readResolve() {
            return getInstance();
        }
    }

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        mb = new MainBinder();
        return mb;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (isResume) {
            mp.seekTo((int) SPUtil.getLong(PROGRESS_KEY));
            isResume = false;
        }
    }

    public class MainBinder extends Binder implements IBinder {
        public MainService getService() {
            return MainService.this;
        }
    }

    public List<Mp3Info> loadMusic() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                mp3InfoList = MediaUtils.getMp3InfoList(App_mine.context_app);
            }
        });
        return mp3InfoList;
    }

    public List<String> loadImage() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                imageInfoList = MediaUtils.getImagesList(App_mine.context_app);
            }
        });
        return imageInfoList;
    }

    public void updateMusic() {
        updateService.scheduleAtFixedRate(updateProgress, 1, 1, TimeUnit.SECONDS);
    }

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                if (progress == null) {
                    progress = new EventType.MusicProgress();
                }
                long time = getCurrentPosition();
                String text = MediaUtils.formattime(time);
                progress.setMusicPlayUpdateProgress(time);
                progress.setMusicPlayUpdateProgressText(text);
                EventBus.getDefault().post(progress);
            }
        }
    };


}

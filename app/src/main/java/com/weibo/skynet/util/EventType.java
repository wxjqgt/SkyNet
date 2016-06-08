package com.weibo.skynet.util;

/**
 * Created by Administrator on 2016/6/5.
 */
public class EventType {
    public static class MusicPosition{

        private int musicPlayUpdatePosition;

        public MusicPosition() {
        }

        public MusicPosition(int musicPlayUpdatePosition) {
            this.musicPlayUpdatePosition = musicPlayUpdatePosition;
        }

        public int getMusicPlayUpdatePosition() {
            return musicPlayUpdatePosition;
        }

        public void setMusicPlayUpdatePosition(int musicPlayUpdatePosition) {
            this.musicPlayUpdatePosition = musicPlayUpdatePosition;
        }
    }
    public static class MusicProgress {

        private long musicPlayUpdateProgress;
        private String musicPlayUpdateProgressText;

        public MusicProgress(){}

        public MusicProgress(long musicPlayUpdateProgress, String musicPlayUpdateProgressText, int musicPlayUpdatePosition) {
            this.musicPlayUpdateProgress = musicPlayUpdateProgress;
            this.musicPlayUpdateProgressText = musicPlayUpdateProgressText;
        }

        public String getMusicPlayUpdateProgressText() {
            return musicPlayUpdateProgressText;
        }

        public void setMusicPlayUpdateProgressText(String musicPlayUpdateProgressText) {
            this.musicPlayUpdateProgressText = musicPlayUpdateProgressText;
        }

        public long getMusicPlayUpdateProgress() {
            return musicPlayUpdateProgress;
        }

        public void setMusicPlayUpdateProgress(long musicPlayUpdateProgress) {
            this.musicPlayUpdateProgress = musicPlayUpdateProgress;
            //this.musicPlayUpdateProgressText = MediaUtils.formattime(this.musicPlayUpdateProgress);
        }
    }
}

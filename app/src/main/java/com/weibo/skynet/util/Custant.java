package com.weibo.skynet.util;

/**
 * Created by Administrator on 2016/4/10.
 */
public interface Custant {

    int PROGRESS_MUSIC = 1,POSITION_MUSIC = 2;

    String TYPE = "type",LOCALSONGS = "localsongs",NETWORKSONG = "networksong"
            ,SONGLIST = "imaglist",TYPE_IMAGE = "image",TYPE_MUSIC = "music"
            ,TYPE_VIDEO = "video",TYPE_VIEWPAGER = "viewpager",KEY = "showtype"
            ,URL_KEY = "urlkey",POSITION_TYPE = "position";

    String[] STRINGS = {"image", "music", "video", "exit", "main", "Like", "Record", "Search", "Settings"}
            ,Colors = {"#F44336", "#E91E63", "#9C27B0", "#2196F3", "#03A9F4", "#00BCD4", "#009688"
            , "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#795548",
            "#9E9E9E", "#607D8B"};

    String URL = "http://api.haodou.com/index.php?appid=2" +
            "&appkey=9ef269eec4f7a9d07c73952d06b5413f&format=json&sessionid" +
            "=1461338384160&vc=83&vn=6.1.0&loguid=0&deviceid=haodou86080602" +
            "7038297&uuid=c66638466dd7f848f4d27b83b3b339c5&channel=meizu_" +
            "v610&method=Index.index&virtual=&signmethod=md5&v=2&timestamp=" +
            "1461338387&nonce=0.7953463564160073&appsign=8bf14f32ae2ffc886fd7bc064754cbe0";

}

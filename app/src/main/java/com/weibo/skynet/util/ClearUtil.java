package com.weibo.skynet.util;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class ClearUtil {

    public static void clearList(List...lists){
        int length = lists.length;
        for (int i = 0;i < length;i++){
            List l = lists[i];
            if (l == null){
                continue;
            }
            l.clear();
            l = null;
        }
    }

    public static void clearObject(Object...objects){
        int length = objects.length;
        for (int i = 0;i < length;i++){
            Object object = objects[i];
            if (object == null){
                continue;
            }
            object = null;
        }
    }

}

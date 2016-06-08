package com.weibo.skynet.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.weibo.skynet.R;
import com.weibo.skynet.model.ImageInfo;
import com.weibo.skynet.model.Mp3Info;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class MediaUtils {

    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;

    public static Bitmap getArtwork(Context context, long song_id, long album_id,
                                    boolean allowdefault) {
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork(context);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return null;
    }

    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        byte [] art = null;
        String path = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }

    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(
                context.getResources().openRawResource(R.mipmap.ic_custom), null, opts);
    }

    public static List<String> getImagesList(Context context) {
        List<String> images = new ArrayList<String>();
        ContentResolver c = context.getContentResolver();
        Cursor cursor = c.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED
        );
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        while (cursor.moveToNext()) {
            String path = cursor.getString(index);
            images.add(path);
        }
        cursor.close();
        return images;
    }

    //获取专辑封面的Uri
    public static final Uri albumArtUri =
            Uri.parse("content://media/external/audio/albumart");

    /*
    * 根据全部图片
    */
    public static List<ImageInfo> getImageList() {
        // 指定要查询的uri资源
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 获取ContentResolver
        ContentResolver contentResolver = App_mine.context_app.getContentResolver();
        // 查询的字段
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                //MediaStore.Images.Media.CONTENT_TYPE,
                //MediaStore.Images.Media._COUNT,
                //MediaStore.Images.Media.DESCRIPTION,
                //MediaStore.Images.Media.WIDTH,
                //MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.HEIGHT
        };
        // 条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
        // 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
        String[] selectionArgs = {"image/jpeg"};
        // 排序
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        // 查询sd卡上的图片
        Cursor cursor = contentResolver.query(uri, projection, selection,
                selectionArgs, sortOrder);
        List<ImageInfo> imageIfos = null;
        ImageInfo imageIfo = null;

        String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
        String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
        Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE) / 1024);
        String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

        if (cursor != null) {
            imageIfos = new ArrayList<>();
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                imageIfo = new ImageInfo();
                // 获得图片的id
                imageIfo.setId(id);
                // 获得图片显示的名称
                imageIfo.setDisplay_name(displayName);
                // 获得图片的信息
                imageIfo.setSize(size);
                // 获得图片所在的路径(可以使用路径构建URI)
                imageIfo.setData(data);
                /*imageIfo.setTitle(cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)));
                imageIfo.setContent_type(cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.CONTENT_TYPE)));
                imageIfo.setCount(cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media._COUNT)));
                imageIfo.setDescription(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                MediaStore.Images.Media.DESCRIPTION)));
                imageIfo.setWidth(
                        cursor.getString(cursor.getColumnIndexOrThrow
                                (MediaStore.Images.Media.WIDTH)));
                imageIfo.setHeight(
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                MediaStore.Images.Media.HEIGHT)));
                                */
                imageIfos.add(imageIfo);
            }
            // 关闭cursor
            cursor.close();
        }
        return imageIfos;
    }

    public static ImageInfo getImageById(int id) {
        ImageInfo imageIfo = null;
        // 指定要查询的uri资源
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 获取ContentResolver
        ContentResolver contentResolver = App_mine.context_app.getContentResolver();
        // 查询的字段
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                /*MediaStore.Images.Media.CONTENT_TYPE,
                MediaStore.Images.Media._COUNT,
                MediaStore.Images.Media.DESCRIPTION,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.HEIGHT,*/
        };
        // 条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=" + id;
        // 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
        String[] selectionArgs = {"image/jpeg"};
        // 排序
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        // 查询sd卡上的图片
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor != null) {
            imageIfo = new ImageInfo();
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                // 获得图片的id
                imageIfo.setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
                // 获得图片显示的名称
                imageIfo.setDisplay_name(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                // 获得图片的信息
                imageIfo.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE) / 1024));
                // 获得图片所在的路径(可以使用路径构建URI)
                imageIfo.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                //imageIfo.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)));
                //imageIfo.setContent_type(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.CONTENT_TYPE)));
                //imageIfo.setCount(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._COUNT)));
                //imageIfo.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION)));
                //imageIfo.setWidth(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)));
                //imageIfo.setHeight(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)));
            }
            // 关闭cursor
            cursor.close();
        }
        return imageIfo;
    }

    public static Mp3Info getMp3Info(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media._ID + "=" + id,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
        Mp3Info mp3Info = null;
        int album = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int albumId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
        int artList = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int duration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int size = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
        int title = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int url = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        int display = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        if (cursor.moveToNext()) {
            mp3Info = new Mp3Info();
            int ismusic = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
            if (ismusic != 0) {
                mp3Info.setId(cursor.getLong(id));
                mp3Info.setAlbum(cursor.getString(album));
                mp3Info.setAlbumId(cursor.getInt(albumId));
                mp3Info.setArtist(cursor.getString(artList));
                mp3Info.setDuration(cursor.getInt(duration));
                mp3Info.setSize(cursor.getLong(size));
                mp3Info.setTitle(cursor.getString(title));
                mp3Info.setUrl(cursor.getString(url));
                mp3Info.setDisplay(cursor.getString(display));
            }
        }
        cursor.close();
        return mp3Info;
    }

    //查询所有歌曲信息
    public static ArrayList<Mp3Info> getMp3InfoList(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.DURATION + ">=" + "180000",
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
        ArrayList<Mp3Info> list = new ArrayList<>();
        int id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int album = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int albumId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
        int artList = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int duration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int size = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
        int title = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int url = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        int display = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        while (cursor.moveToNext()) {
            Mp3Info mp3Info = new Mp3Info();
            mp3Info.setId(cursor.getLong(id));
            mp3Info.setAlbum(cursor.getString(album));
            mp3Info.setAlbumId(cursor.getInt(albumId));
            mp3Info.setArtist(cursor.getString(artList));
            mp3Info.setDuration(cursor.getInt(duration));
            mp3Info.setSize(cursor.getLong(size));
            mp3Info.setTitle(cursor.getString(title));
            mp3Info.setUrl(cursor.getString(url));
            mp3Info.setDisplay(cursor.getString(display));
            list.add(mp3Info);
        }
        cursor.close();
        return list;
    }

    //格式化时间
    public static String formattime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + time % (1000 * 60) + "";
        } else if (sec.length() == 3) {
            sec = "00" + time % (1000 * 60) + "";
        } else if (sec.length() == 2) {
            sec = "000" + time % (1000 * 60) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + time % (1000 * 60) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    //获取专辑默认图片
    public static Bitmap getDefautArtWork(Context context, boolean small) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (small) {
            return BitmapFactory.decodeStream(
                    context.getResources().openRawResource(R.mipmap.ic_custom), null, options);
        }
        return BitmapFactory.decodeStream(
                context.getResources().openRawResource(R.mipmap.ic_custom), null, options);
    }

    private static Bitmap getArtWorkFile(Context context, long song_id, long album_id) {
        Bitmap bitmap = null;
        if (album_id < 0 && song_id < 0) {
            throw new IllegalArgumentException("Must specify an album or a1 song_id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if (album_id < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + song_id + "/albumart");
                ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
                if (parcelFileDescriptor != null) {
                    fd = parcelFileDescriptor.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, album_id);
                ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
                if (parcelFileDescriptor != null) {
                    fd = parcelFileDescriptor.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            //只进行大小判断
            options.inJustDecodeBounds = true;
            //得到歌曲option然后得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            options.inSampleSize = 100;//目标是在800pixel上显示，需要调用computeSampleSixe获取图片缩放比例
            options.inJustDecodeBounds = false;//互殴去图片比例后开始正式写入
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //根据option的参数减少所需要的内容
            bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getArtWork(Context context, Long song_id,
                                    long abum_id, boolean allowdefault, boolean small) {
        if (abum_id < 0) {
            if (song_id < 0) {
                Bitmap bitmap = getArtWorkFile(context, song_id, -1);
                if (bitmap != null) {
                    return bitmap;
                }
            }
            if (allowdefault) {
                return getDefautArtWork(context, small);
            }
            return null;
        }
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(albumArtUri, abum_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = cr.openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(in, null, options);
                if (small) {
                    options.inSampleSize = computeSampleSize(options, 40);
                } else {
                    options.inSampleSize = computeSampleSize(options, 600);
                }
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                in = cr.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, options);
            } catch (Exception e) {
                Bitmap bitmap = getArtWorkFile(context, song_id, abum_id);
                if (bitmap != null) {
                    if (bitmap.getConfig() == null) {
                        bitmap = bitmap.copy(Bitmap.Config.RGB_565, false);
                        if (bitmap == null && allowdefault) {
                            return getDefautArtWork(context, small);
                        }
                    }
                } else if (allowdefault) {
                    bitmap = getDefautArtWork(context, small);
                }
                return bitmap;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int target) {
        int w = options.outWidth;
        int h = options.outHeight;
        int candidatew = w / target;
        int candidateh = h / target;
        int candidate = Math.max(candidateh, candidatew);
        if (candidate == 0) {
            return 1;
        }
        if (candidate > 1) {
            if (w > target && (w / target) < target) {
                candidate -= 1;
            }
        }
        if (candidate > 1) {
            if (h > target && (h / target) < target) {
                candidate -= 1;
            }
        }
        return candidate;
    }

    public static Uri getArtWorkUri(Long song_id, long abum_id) {
        if (abum_id < 0) {
            if (song_id < 0) {
                Uri uri = getArtWorkFileUri(song_id, abum_id);
                if (uri != null) {
                    return uri;
                }
            }
            return null;
        }
        Uri uri = ContentUris.withAppendedId(albumArtUri, abum_id);
        if (uri != null) {
            try {
                return uri;
            } catch (Exception e) {
                Uri uri1 = getArtWorkFileUri(song_id, abum_id);
                if (uri1 != null) {
                    return uri1;
                }
                return uri;
            }
        }
        return null;
    }

    private static Uri getArtWorkFileUri(long song_id, long album_id) {
        Uri uri = null;
        if (album_id < 0 && song_id < 0) {
            throw new IllegalArgumentException("Must specify an album or a song_id");
        }
        try {
            if (album_id < 0) {
                uri = Uri.parse("content://media/external/audio/media/" + song_id + "/albumart");
            } else {
                uri = ContentUris.withAppendedId(albumArtUri, album_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

}


























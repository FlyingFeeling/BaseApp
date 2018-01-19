package com.feeling.mybaseapp.utils.photo.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.feeling.mybaseapp.utils.photo.model.PhotoAlbumsInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * 读取图片工具类
 * Created by zcs on 2016/11/26.
 */

public class ReadPhotoUtils {

    private Context mContext;
    private int maxCount;
    private Cursor mCursor;
    private ArrayList<String> mAllImagePaths;
    private int filterSize = 32 * 1024;

    public ReadPhotoUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 按最新修改获取图片路径，过滤小图
     *
     * @return ArrayList<String>
     */
    private void cursorImageByDATA() {
        /*获取Cursor集合*/
        if (mCursor == null) {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
            String key_DATA = MediaStore.Images.Media.DATA;
            ContentResolver resolver = mContext.getContentResolver();
            mCursor = resolver.query(mImageUri, new String[]{key_DATA},
                    key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                    new String[]{"image/jpg", "image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED);
        }
        mAllImagePaths = new ArrayList<>();
        try {
            /*Cursor中根据日期，按最新修改添加图片路径到集合中*/
            if (mCursor != null && mCursor.moveToLast()) {
                while (true) {
                    String path = mCursor.getString(0);
                    File file = new File(path);
                    /*根据图片文件所占内存大小进行过滤*/
                    if (file != null && file.length() > filterSize) {
                        mAllImagePaths.add(path);
                    }
                    if (!mCursor.moveToPrevious()) {
                        break;
                    }
                }
                mCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有图片的路径集合
     *
     * @return
     */
    public ArrayList<String> getAllImagePaths() {
        if (mAllImagePaths == null) {
            cursorImageByDATA();
        }
        return mAllImagePaths;
    }

    /**
     * 获取最近图片路径集合
     *
     * @param maxCount 最大获取数量
     * @return ArrayList<String>
     */
    public ArrayList<String> getLastImagePaths(int maxCount) {
        this.maxCount = maxCount;
        if (mAllImagePaths == null) {
            cursorImageByDATA();
        }
        if (mAllImagePaths.size() > maxCount) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < maxCount; i++) {
                list.add(mAllImagePaths.get(i));
            }
            return list;
        } else {
            return mAllImagePaths;
        }
    }

    /**
     * 获取指定路径下的所有图片文件。
     */
    public ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder == null) return new ArrayList<>();
        File[] allFileNames = folder.listFiles();
        if (allFileNames == null || allFileNames.length < 1) return new ArrayList<>();
        ArrayList<String> imageFilePaths = new ArrayList<>();
        int length = allFileNames.length - 1;
            for (int i = length; i >= 0; i--) {
                File file = allFileNames[i];
                if (file!=null&&file.isFile()) {
                    String filePath = file.getAbsolutePath();
                    if (file.length() > filterSize) {
                        if (filePath.endsWith(".jpg")
                                || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                            imageFilePaths.add(filePath);
                        }
                    }
                }
            }
        return imageFilePaths;
    }

    /**
     * 根据所有图片，保存相册名
     *
     * @return
     */
    public ArrayList<PhotoAlbumsInfo> getPhotoAlbums() {
        if (mAllImagePaths == null) {
            cursorImageByDATA();
        }
        HashSet<String> cachePath = new HashSet<>();
        ArrayList<PhotoAlbumsInfo> photoAlbums = new ArrayList<>();
        //判断是否有图片
        if (mAllImagePaths == null || mAllImagePaths.size() < 1) return new ArrayList<>();
        PhotoAlbumsInfo recentlyAlbumInfo = new PhotoAlbumsInfo("最近图片", "", mAllImagePaths.get(0),
                maxCount > mAllImagePaths.size() ? mAllImagePaths.size() : maxCount);
        photoAlbums.add(recentlyAlbumInfo);
        int ImageSize = mAllImagePaths.size();
        for (int i = 0; i < ImageSize; i++) {
            String fistImagPath = mAllImagePaths.get(i);
            File imgFile = new File(fistImagPath);
            if (imgFile==null) continue;
            File albumFile = imgFile.getParentFile();
            if (albumFile==null) continue;
            String albumsPath = albumFile.getAbsolutePath();
            if (!cachePath.contains(albumsPath)) {
                cachePath.add(albumsPath);
                PhotoAlbumsInfo albumsInfo = new PhotoAlbumsInfo(getPathNameToShow(albumsPath),
                        albumsPath, fistImagPath, getImageCount(albumFile));
                photoAlbums.add(albumsInfo);
            }
        }
        return photoAlbums;
    }

    /**
     * 根据完整路径，获取最后一级路径
     */
    private String getPathNameToShow(String path) {
        int lastSeparator = path.lastIndexOf(File.separator);
        return path.substring(lastSeparator + 1);
    }

    /**
     * 获取目录中图片的个数。
     */
    private int getImageCount(File folder) {
        int count = 0;
        int j = 0;
        File[] files = folder.listFiles();
        if (files == null || files.length < 1) return 0;
        int length = files.length - 1;
        try {
            for (int i = length; i >= 0; i--) {
                File file = files[i];
                if (file.isFile()) {
                    String filePath = file.getAbsolutePath();
                    FileInputStream stream = new FileInputStream(filePath);
                    if (stream.available() > filterSize) {
                        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") ||
                                filePath.endsWith(".png")) {
                            count++;
                        }
                    }
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }
}

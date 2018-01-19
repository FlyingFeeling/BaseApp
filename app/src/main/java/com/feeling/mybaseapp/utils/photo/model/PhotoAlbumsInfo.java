package com.feeling.mybaseapp.utils.photo.model;

/**
 * 存储相册信息
 * Created by zcs on 2016/11/26.
 */

public class PhotoAlbumsInfo {
    /**
     * 相册路径
     */
    private String PhotoAlbumsPath;
    /**
     * 照片数量
     */
    private int PhotoCount;
    /**
     * 相册封面照片路径
     */
    private String FirstImagePath;
    /**
     * 相册名称
     */
    private String PhotoAlbumsName;

    /**
     * 构造函数
     * @param photoAlbumsName   相册名称
     * @param photoAlbumsPath   相册路径
     * @param firstImagePath    相册封面图路径
     * @param photoCount    照片数量
     */
    public PhotoAlbumsInfo(String photoAlbumsName, String photoAlbumsPath,
                           String firstImagePath, int photoCount) {
        this.FirstImagePath = firstImagePath;
        this.PhotoAlbumsName = photoAlbumsName;
        this.PhotoAlbumsPath = photoAlbumsPath;
        this.PhotoCount = photoCount;
    }

    public String getFirstImagePath() {
        return FirstImagePath;
    }

    public String getPhotoAlbumsName() {
        return PhotoAlbumsName;
    }

    public String getPhotoAlbumsPath() {
        return PhotoAlbumsPath;
    }

    public int getPhotoCount() {
        return PhotoCount;
    }
}

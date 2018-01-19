package com.feeling.mybaseapp.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 123 on 2018/1/3.
 */

public class NewBean implements Parcelable {

    /**
     * id : 2
     * create_time : 2017-10-13
     * image : http://test.pallanharter.com/uploads/20171013/d5634675e82fda08052688ea0f9f2894.jpg
     * content : 随着工业排污量急剧增加，大量重...
     * title : [技术资讯] 重金属污染物的来源及处理办法全解析
     * url : http://test.pallanharter.com/api/News/detail/id/2.html
     * click : 80
     * share : 1
     */

    private int id;
    private String create_time;
    private String image;
    private String content;
    private String title;
    private String url;
    private int click;
    private int share;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    @Override
    public String toString() {
        return "NewBean{" +
                "id=" + id +
                ", create_time='" + create_time + '\'' +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", click=" + click +
                ", share=" + share +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.create_time);
        dest.writeString(this.image);
        dest.writeString(this.content);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.click);
        dest.writeInt(this.share);
    }

    public NewBean() {
    }

    protected NewBean(Parcel in) {
        this.id = in.readInt();
        this.create_time = in.readString();
        this.image = in.readString();
        this.content = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.click = in.readInt();
        this.share = in.readInt();
    }

    public static final Parcelable.Creator<NewBean> CREATOR = new Parcelable.Creator<NewBean>() {
        @Override
        public NewBean createFromParcel(Parcel source) {
            return new NewBean(source);
        }

        @Override
        public NewBean[] newArray(int size) {
            return new NewBean[size];
        }
    };
}

package com.ubt.androidlearning.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/8.
 */

public class Messages implements Parcelable {
    private long id;
    private String content;

    public Messages(){}

    protected Messages(Parcel in){

        this.id = in.readLong();
        this.content = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.content);
    }

    public void readFromParcel(Parcel dest)
    {
        id = dest.readLong();
        content = dest.readString();
    }

    public static final Creator<Messages> CREATOR = new Creator<Messages>() {
        @Override
        public Messages createFromParcel(Parcel source) {
            return new Messages(source);
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
        }
    };
}

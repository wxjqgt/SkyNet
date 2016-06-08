package com.weibo.skynet.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wxjqgt on 2016/1/1.
 */
public class FileProperty implements Parcelable {

    private String absolutePath;
    private boolean canRead;
    private boolean canWrite;
    private String name;
    private String parent;
    private String path;
    private String length;
    private String lastModified;
    private boolean isHidden;

    public FileProperty() {
    }

    public FileProperty(String absolutePath, boolean canRead, boolean canWrite,
                        String name, String parent, String path, String length,
                        String lastModified, boolean isHidden) {
        this.absolutePath = absolutePath;
        this.canRead = canRead;
        this.canWrite = canWrite;
        this.name = name;
        this.parent = parent;
        this.path = path;
        this.length = length;
        this.lastModified = lastModified;
        this.isHidden = isHidden;
    }

    @Override
    public String toString() {
        return "FileProperty{" +
                "absolutePath='" + absolutePath + '\'' +
                ", canRead=" + canRead +
                ", canWrite=" + canWrite +
                ", name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                ", path='" + path + '\'' +
                ", length=" + length +
                ", lastModified=" + lastModified +
                ", isHidden=" + isHidden +
                '}';
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.absolutePath);
        dest.writeByte(canRead ? (byte) 1 : (byte) 0);
        dest.writeByte(canWrite ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeString(this.parent);
        dest.writeString(this.path);
        dest.writeString(this.length);
        dest.writeString(this.lastModified);
        dest.writeByte(isHidden ? (byte) 1 : (byte) 0);
    }

    protected FileProperty(Parcel in) {
        this.absolutePath = in.readString();
        this.canRead = in.readByte() != 0;
        this.canWrite = in.readByte() != 0;
        this.name = in.readString();
        this.parent = in.readString();
        this.path = in.readString();
        this.length = in.readString();
        this.lastModified = in.readString();
        this.isHidden = in.readByte() != 0;
    }

    public static final Creator<FileProperty> CREATOR = new Creator<FileProperty>() {
        public FileProperty createFromParcel(Parcel source) {
            return new FileProperty(source);
        }

        public FileProperty[] newArray(int size) {
            return new FileProperty[size];
        }
    };
}


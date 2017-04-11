
package com.valgoodchat.giphy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Original implements Parcelable
{

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("frames")
    @Expose
    private String frames;
    @SerializedName("mp4")
    @Expose
    private String mp4;
    @SerializedName("mp4_size")
    @Expose
    private String mp4Size;
    @SerializedName("webp")
    @Expose
    private String webp;
    @SerializedName("webp_size")
    @Expose
    private String webpSize;
    @SerializedName("hash")
    @Expose
    private String hash;
    public final static Parcelable.Creator<Original> CREATOR = new Creator<Original>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Original createFromParcel(Parcel in) {
            Original instance = new Original();
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.width = ((String) in.readValue((String.class.getClassLoader())));
            instance.height = ((String) in.readValue((String.class.getClassLoader())));
            instance.size = ((String) in.readValue((String.class.getClassLoader())));
            instance.frames = ((String) in.readValue((String.class.getClassLoader())));
            instance.mp4 = ((String) in.readValue((String.class.getClassLoader())));
            instance.mp4Size = ((String) in.readValue((String.class.getClassLoader())));
            instance.webp = ((String) in.readValue((String.class.getClassLoader())));
            instance.webpSize = ((String) in.readValue((String.class.getClassLoader())));
            instance.hash = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Original[] newArray(int size) {
            return (new Original[size]);
        }

    }
    ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Original withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public Original withWidth(String width) {
        this.width = width;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Original withHeight(String height) {
        this.height = height;
        return this;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Original withSize(String size) {
        this.size = size;
        return this;
    }

    public String getFrames() {
        return frames;
    }

    public void setFrames(String frames) {
        this.frames = frames;
    }

    public Original withFrames(String frames) {
        this.frames = frames;
        return this;
    }

    public String getMp4() {
        return mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public Original withMp4(String mp4) {
        this.mp4 = mp4;
        return this;
    }

    public String getMp4Size() {
        return mp4Size;
    }

    public void setMp4Size(String mp4Size) {
        this.mp4Size = mp4Size;
    }

    public Original withMp4Size(String mp4Size) {
        this.mp4Size = mp4Size;
        return this;
    }

    public String getWebp() {
        return webp;
    }

    public void setWebp(String webp) {
        this.webp = webp;
    }

    public Original withWebp(String webp) {
        this.webp = webp;
        return this;
    }

    public String getWebpSize() {
        return webpSize;
    }

    public void setWebpSize(String webpSize) {
        this.webpSize = webpSize;
    }

    public Original withWebpSize(String webpSize) {
        this.webpSize = webpSize;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Original withHash(String hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(url).append(width).append(height).append(size).append(frames).append(mp4).append(mp4Size).append(webp).append(webpSize).append(hash).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Original) == false) {
            return false;
        }
        Original rhs = ((Original) other);
        return new EqualsBuilder().append(url, rhs.url).append(width, rhs.width).append(height, rhs.height).append(size, rhs.size).append(frames, rhs.frames).append(mp4, rhs.mp4).append(mp4Size, rhs.mp4Size).append(webp, rhs.webp).append(webpSize, rhs.webpSize).append(hash, rhs.hash).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeValue(width);
        dest.writeValue(height);
        dest.writeValue(size);
        dest.writeValue(frames);
        dest.writeValue(mp4);
        dest.writeValue(mp4Size);
        dest.writeValue(webp);
        dest.writeValue(webpSize);
        dest.writeValue(hash);
    }

    public int describeContents() {
        return  0;
    }

}

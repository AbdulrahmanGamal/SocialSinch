
package com.valgoodchat.giphy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DownsizedSmall implements Parcelable
{

    @SerializedName("mp4")
    @Expose
    private String mp4;
    @SerializedName("mp4_size")
    @Expose
    private String mp4Size;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    public final static Parcelable.Creator<DownsizedSmall> CREATOR = new Creator<DownsizedSmall>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DownsizedSmall createFromParcel(Parcel in) {
            DownsizedSmall instance = new DownsizedSmall();
            instance.mp4 = ((String) in.readValue((String.class.getClassLoader())));
            instance.mp4Size = ((String) in.readValue((String.class.getClassLoader())));
            instance.width = ((String) in.readValue((String.class.getClassLoader())));
            instance.height = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public DownsizedSmall[] newArray(int size) {
            return (new DownsizedSmall[size]);
        }

    }
    ;

    public String getMp4() {
        return mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public DownsizedSmall withMp4(String mp4) {
        this.mp4 = mp4;
        return this;
    }

    public String getMp4Size() {
        return mp4Size;
    }

    public void setMp4Size(String mp4Size) {
        this.mp4Size = mp4Size;
    }

    public DownsizedSmall withMp4Size(String mp4Size) {
        this.mp4Size = mp4Size;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public DownsizedSmall withWidth(String width) {
        this.width = width;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public DownsizedSmall withHeight(String height) {
        this.height = height;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(mp4).append(mp4Size).append(width).append(height).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DownsizedSmall) == false) {
            return false;
        }
        DownsizedSmall rhs = ((DownsizedSmall) other);
        return new EqualsBuilder().append(mp4, rhs.mp4).append(mp4Size, rhs.mp4Size).append(width, rhs.width).append(height, rhs.height).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mp4);
        dest.writeValue(mp4Size);
        dest.writeValue(width);
        dest.writeValue(height);
    }

    public int describeContents() {
        return  0;
    }

}

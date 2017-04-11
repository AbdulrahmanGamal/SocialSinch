
package com.valgoodchat.giphy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FixedWidthStill implements Parcelable
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
    public final static Parcelable.Creator<FixedWidthStill> CREATOR = new Creator<FixedWidthStill>() {


        @SuppressWarnings({
            "unchecked"
        })
        public FixedWidthStill createFromParcel(Parcel in) {
            FixedWidthStill instance = new FixedWidthStill();
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.width = ((String) in.readValue((String.class.getClassLoader())));
            instance.height = ((String) in.readValue((String.class.getClassLoader())));
            instance.size = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public FixedWidthStill[] newArray(int size) {
            return (new FixedWidthStill[size]);
        }

    }
    ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FixedWidthStill withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public FixedWidthStill withWidth(String width) {
        this.width = width;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public FixedWidthStill withHeight(String height) {
        this.height = height;
        return this;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public FixedWidthStill withSize(String size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(url).append(width).append(height).append(size).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FixedWidthStill) == false) {
            return false;
        }
        FixedWidthStill rhs = ((FixedWidthStill) other);
        return new EqualsBuilder().append(url, rhs.url).append(width, rhs.width).append(height, rhs.height).append(size, rhs.size).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeValue(width);
        dest.writeValue(height);
        dest.writeValue(size);
    }

    public int describeContents() {
        return  0;
    }

}

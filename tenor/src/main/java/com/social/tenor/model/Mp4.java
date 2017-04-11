
package com.social.tenor.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Mp4 implements Parcelable
{

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("dims")
    @Expose
    private List<Long> dims = null;
    @SerializedName("duration")
    @Expose
    private double duration;
    @SerializedName("preview")
    @Expose
    private String preview;
    public final static Parcelable.Creator<Mp4> CREATOR = new Creator<Mp4>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Mp4 createFromParcel(Parcel in) {
            Mp4 instance = new Mp4();
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.dims, (java.lang.Long.class.getClassLoader()));
            instance.duration = ((double) in.readValue((double.class.getClassLoader())));
            instance.preview = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Mp4 [] newArray(int size) {
            return (new Mp4[size]);
        }

    }
    ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Long> getDims() {
        return dims;
    }

    public void setDims(List<Long> dims) {
        this.dims = dims;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(url).append(dims).append(duration).append(preview).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Mp4) == false) {
            return false;
        }
        Mp4 rhs = ((Mp4) other);
        return new EqualsBuilder().append(url, rhs.url).append(dims, rhs.dims).append(duration, rhs.duration).append(preview, rhs.preview).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeList(dims);
        dest.writeValue(duration);
        dest.writeValue(preview);
    }

    public int describeContents() {
        return  0;
    }

}

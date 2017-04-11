
package com.social.tenor.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Webm implements Parcelable
{

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("dims")
    @Expose
    private List<Long> dims = null;
    @SerializedName("preview")
    @Expose
    private String preview;
    public final static Parcelable.Creator<Webm> CREATOR = new Creator<Webm>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Webm createFromParcel(Parcel in) {
            Webm instance = new Webm();
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.dims, (java.lang.Long.class.getClassLoader()));
            instance.preview = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Webm[] newArray(int size) {
            return (new Webm[size]);
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

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(url).append(dims).append(preview).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Webm) == false) {
            return false;
        }
        Webm rhs = ((Webm) other);
        return new EqualsBuilder().append(url, rhs.url).append(dims, rhs.dims).append(preview, rhs.preview).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeList(dims);
        dest.writeValue(preview);
    }

    public int describeContents() {
        return  0;
    }

}


package com.social.tenor.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Tinywebm implements Parcelable
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
    public final static Parcelable.Creator<Tinywebm> CREATOR = new Creator<Tinywebm>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Tinywebm createFromParcel(Parcel in) {
            Tinywebm instance = new Tinywebm();
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.dims, (java.lang.Long.class.getClassLoader()));
            instance.preview = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Tinywebm[] newArray(int size) {
            return (new Tinywebm[size]);
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
        if ((other instanceof Tinywebm) == false) {
            return false;
        }
        Tinywebm rhs = ((Tinywebm) other);
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

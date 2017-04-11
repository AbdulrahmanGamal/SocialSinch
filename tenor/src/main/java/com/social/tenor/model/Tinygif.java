
package com.social.tenor.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Tinygif implements Parcelable
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
    @SerializedName("size")
    @Expose
    private long size;
    public final static Parcelable.Creator<Tinygif> CREATOR = new Creator<Tinygif>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Tinygif createFromParcel(Parcel in) {
            Tinygif instance = new Tinygif();
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.dims, (java.lang.Long.class.getClassLoader()));
            instance.preview = ((String) in.readValue((String.class.getClassLoader())));
            instance.size = ((long) in.readValue((long.class.getClassLoader())));
            return instance;
        }

        public Tinygif[] newArray(int size) {
            return (new Tinygif[size]);
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(url).append(dims).append(preview).append(size).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tinygif) == false) {
            return false;
        }
        Tinygif rhs = ((Tinygif) other);
        return new EqualsBuilder().append(url, rhs.url).append(dims, rhs.dims).append(preview, rhs.preview).append(size, rhs.size).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeList(dims);
        dest.writeValue(preview);
        dest.writeValue(size);
    }

    public int describeContents() {
        return  0;
    }

}

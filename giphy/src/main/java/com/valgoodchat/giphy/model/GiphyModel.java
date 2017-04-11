
package com.valgoodchat.giphy.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GiphyModel implements Parcelable
{

    @SerializedName("data")
    @Expose
    private List<Giphy> data = new ArrayList<Giphy>();
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    public final static Parcelable.Creator<GiphyModel> CREATOR = new Creator<GiphyModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public GiphyModel createFromParcel(Parcel in) {
            GiphyModel instance = new GiphyModel();
            in.readList(instance.data, (Giphy.class.getClassLoader()));
            instance.pagination = ((Pagination) in.readValue((Pagination.class.getClassLoader())));
            instance.meta = ((Meta) in.readValue((Meta.class.getClassLoader())));
            return instance;
        }

        public GiphyModel[] newArray(int size) {
            return (new GiphyModel[size]);
        }

    }
    ;

    public List<Giphy> getData() {
        return data;
    }

    public void setData(List<Giphy> data) {
        this.data = data;
    }

    public GiphyModel withData(List<Giphy> data) {
        this.data = data;
        return this;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public GiphyModel withPagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public GiphyModel withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).append(pagination).append(meta).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GiphyModel)) {
            return false;
        }
        GiphyModel rhs = ((GiphyModel) other);
        return new EqualsBuilder().append(data, rhs.data).append(pagination, rhs.pagination).append(meta, rhs.meta).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(data);
        dest.writeValue(pagination);
        dest.writeValue(meta);
    }

    public int describeContents() {
        return  0;
    }

}

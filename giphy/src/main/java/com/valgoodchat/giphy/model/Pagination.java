
package com.valgoodchat.giphy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Pagination implements Parcelable
{

    @SerializedName("total_count")
    @Expose
    private long totalCount;
    @SerializedName("count")
    @Expose
    private long count;
    @SerializedName("offset")
    @Expose
    private long offset;
    public final static Parcelable.Creator<Pagination> CREATOR = new Creator<Pagination>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Pagination createFromParcel(Parcel in) {
            Pagination instance = new Pagination();
            instance.totalCount = ((long) in.readValue((long.class.getClassLoader())));
            instance.count = ((long) in.readValue((long.class.getClassLoader())));
            instance.offset = ((long) in.readValue((long.class.getClassLoader())));
            return instance;
        }

        public Pagination[] newArray(int size) {
            return (new Pagination[size]);
        }

    }
    ;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public Pagination withTotalCount(long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Pagination withCount(long count) {
        this.count = count;
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public Pagination withOffset(long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalCount).append(count).append(offset).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Pagination) == false) {
            return false;
        }
        Pagination rhs = ((Pagination) other);
        return new EqualsBuilder().append(totalCount, rhs.totalCount).append(count, rhs.count).append(offset, rhs.offset).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(totalCount);
        dest.writeValue(count);
        dest.writeValue(offset);
    }

    public int describeContents() {
        return  0;
    }

}

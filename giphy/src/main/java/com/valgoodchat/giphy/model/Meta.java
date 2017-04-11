
package com.valgoodchat.giphy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Meta implements Parcelable
{

    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("response_id")
    @Expose
    private String responseId;
    public final static Parcelable.Creator<Meta> CREATOR = new Creator<Meta>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Meta createFromParcel(Parcel in) {
            Meta instance = new Meta();
            instance.status = ((long) in.readValue((long.class.getClassLoader())));
            instance.msg = ((String) in.readValue((String.class.getClassLoader())));
            instance.responseId = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Meta[] newArray(int size) {
            return (new Meta[size]);
        }

    }
    ;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Meta withStatus(long status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Meta withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public Meta withResponseId(String responseId) {
        this.responseId = responseId;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(status).append(msg).append(responseId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meta) == false) {
            return false;
        }
        Meta rhs = ((Meta) other);
        return new EqualsBuilder().append(status, rhs.status).append(msg, rhs.msg).append(responseId, rhs.responseId).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(responseId);
    }

    public int describeContents() {
        return  0;
    }

}

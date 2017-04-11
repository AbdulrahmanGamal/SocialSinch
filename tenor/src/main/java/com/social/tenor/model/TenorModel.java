
package com.social.tenor.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TenorModel implements Parcelable
{

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("next")
    @Expose
    private String next;
    public final static Parcelable.Creator<TenorModel> CREATOR = new Creator<TenorModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TenorModel createFromParcel(Parcel in) {
            TenorModel instance = new TenorModel();
            in.readList(instance.results, (Result.class.getClassLoader()));
            instance.next = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public TenorModel[] newArray(int size) {
            return (new TenorModel[size]);
        }

    }
    ;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).append(next).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TenorModel) == false) {
            return false;
        }
        TenorModel rhs = ((TenorModel) other);
        return new EqualsBuilder().append(results, rhs.results).append(next, rhs.next).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(results);
        dest.writeValue(next);
    }

    public int describeContents() {
        return  0;
    }

}

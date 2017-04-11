
package com.social.tenor.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Result implements Parcelable
{

    @SerializedName("created")
    @Expose
    private double created;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("shares")
    @Expose
    private long shares;
    @SerializedName("itemurl")
    @Expose
    private String itemurl;
    @SerializedName("composite")
    @Expose
    private Object composite;
    @SerializedName("hasaudio")
    @Expose
    private boolean hasaudio;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;
    public final static Parcelable.Creator<Result> CREATOR = new Creator<Result>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Result createFromParcel(Parcel in) {
            Result instance = new Result();
            instance.created = ((double) in.readValue((double.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.media, (Medium.class.getClassLoader()));
            in.readList(instance.tags, (java.lang.Object.class.getClassLoader()));
            instance.shares = ((long) in.readValue((long.class.getClassLoader())));
            instance.itemurl = ((String) in.readValue((String.class.getClassLoader())));
            instance.composite = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.hasaudio = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Result[] newArray(int size) {
            return (new Result[size]);
        }

    }
    ;

    public double getCreated() {
        return created;
    }

    public void setCreated(double created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public String getItemurl() {
        return itemurl;
    }

    public void setItemurl(String itemurl) {
        this.itemurl = itemurl;
    }

    public Object getComposite() {
        return composite;
    }

    public void setComposite(Object composite) {
        this.composite = composite;
    }

    public boolean isHasaudio() {
        return hasaudio;
    }

    public void setHasaudio(boolean hasaudio) {
        this.hasaudio = hasaudio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(created).append(url).append(media).append(tags).append(shares).append(itemurl).append(composite).append(hasaudio).append(title).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Result) == false) {
            return false;
        }
        Result rhs = ((Result) other);
        return new EqualsBuilder().append(created, rhs.created).append(url, rhs.url).append(media, rhs.media).append(tags, rhs.tags).append(shares, rhs.shares).append(itemurl, rhs.itemurl).append(composite, rhs.composite).append(hasaudio, rhs.hasaudio).append(title, rhs.title).append(id, rhs.id).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(created);
        dest.writeValue(url);
        dest.writeList(media);
        dest.writeList(tags);
        dest.writeValue(shares);
        dest.writeValue(itemurl);
        dest.writeValue(composite);
        dest.writeValue(hasaudio);
        dest.writeValue(title);
        dest.writeValue(id);
    }

    public int describeContents() {
        return  0;
    }

}


package com.valgoodchat.giphy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Giphy implements Parcelable
{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("bitly_gif_url")
    @Expose
    private String bitlyGifUrl;
    @SerializedName("bitly_url")
    @Expose
    private String bitlyUrl;
    @SerializedName("embed_url")
    @Expose
    private String embedUrl;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("content_url")
    @Expose
    private String contentUrl;
    @SerializedName("source_tld")
    @Expose
    private String sourceTld;
    @SerializedName("source_post_url")
    @Expose
    private String sourcePostUrl;
    @SerializedName("is_indexable")
    @Expose
    private long isIndexable;
    @SerializedName("import_datetime")
    @Expose
    private String importDatetime;
    @SerializedName("trending_datetime")
    @Expose
    private String trendingDatetime;
    @SerializedName("images")
    @Expose
    private Images images;
    public final static Parcelable.Creator<Giphy> CREATOR = new Creator<Giphy>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Giphy createFromParcel(Parcel in) {
            Giphy instance = new Giphy();
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.slug = ((String) in.readValue((String.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.bitlyGifUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.bitlyUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.embedUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.username = ((String) in.readValue((String.class.getClassLoader())));
            instance.source = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((String) in.readValue((String.class.getClassLoader())));
            instance.contentUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.sourceTld = ((String) in.readValue((String.class.getClassLoader())));
            instance.sourcePostUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.isIndexable = ((long) in.readValue((long.class.getClassLoader())));
            instance.importDatetime = ((String) in.readValue((String.class.getClassLoader())));
            instance.trendingDatetime = ((String) in.readValue((String.class.getClassLoader())));
            instance.images = ((Images) in.readValue((Images.class.getClassLoader())));
            return instance;
        }

        public Giphy[] newArray(int size) {
            return (new Giphy[size]);
        }

    }
    ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Giphy withType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Giphy withId(String id) {
        this.id = id;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Giphy withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Giphy withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getBitlyGifUrl() {
        return bitlyGifUrl;
    }

    public void setBitlyGifUrl(String bitlyGifUrl) {
        this.bitlyGifUrl = bitlyGifUrl;
    }

    public Giphy withBitlyGifUrl(String bitlyGifUrl) {
        this.bitlyGifUrl = bitlyGifUrl;
        return this;
    }

    public String getBitlyUrl() {
        return bitlyUrl;
    }

    public void setBitlyUrl(String bitlyUrl) {
        this.bitlyUrl = bitlyUrl;
    }

    public Giphy withBitlyUrl(String bitlyUrl) {
        this.bitlyUrl = bitlyUrl;
        return this;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public Giphy withEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Giphy withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Giphy withSource(String source) {
        this.source = source;
        return this;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Giphy withRating(String rating) {
        this.rating = rating;
        return this;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Giphy withContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public String getSourceTld() {
        return sourceTld;
    }

    public void setSourceTld(String sourceTld) {
        this.sourceTld = sourceTld;
    }

    public Giphy withSourceTld(String sourceTld) {
        this.sourceTld = sourceTld;
        return this;
    }

    public String getSourcePostUrl() {
        return sourcePostUrl;
    }

    public void setSourcePostUrl(String sourcePostUrl) {
        this.sourcePostUrl = sourcePostUrl;
    }

    public Giphy withSourcePostUrl(String sourcePostUrl) {
        this.sourcePostUrl = sourcePostUrl;
        return this;
    }

    public long getIsIndexable() {
        return isIndexable;
    }

    public void setIsIndexable(long isIndexable) {
        this.isIndexable = isIndexable;
    }

    public Giphy withIsIndexable(long isIndexable) {
        this.isIndexable = isIndexable;
        return this;
    }

    public String getImportDatetime() {
        return importDatetime;
    }

    public void setImportDatetime(String importDatetime) {
        this.importDatetime = importDatetime;
    }

    public Giphy withImportDatetime(String importDatetime) {
        this.importDatetime = importDatetime;
        return this;
    }

    public String getTrendingDatetime() {
        return trendingDatetime;
    }

    public void setTrendingDatetime(String trendingDatetime) {
        this.trendingDatetime = trendingDatetime;
    }

    public Giphy withTrendingDatetime(String trendingDatetime) {
        this.trendingDatetime = trendingDatetime;
        return this;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Giphy withImages(Images images) {
        this.images = images;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).append(id).append(slug).append(url).append(bitlyGifUrl).append(bitlyUrl).append(embedUrl).append(username).append(source).append(rating).append(contentUrl).append(sourceTld).append(sourcePostUrl).append(isIndexable).append(importDatetime).append(trendingDatetime).append(images).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Giphy)) {
            return false;
        }
        Giphy rhs = ((Giphy) other);
        return new EqualsBuilder().append(type, rhs.type).append(id, rhs.id).append(slug, rhs.slug).append(url, rhs.url).append(bitlyGifUrl, rhs.bitlyGifUrl).append(bitlyUrl, rhs.bitlyUrl).append(embedUrl, rhs.embedUrl).append(username, rhs.username).append(source, rhs.source).append(rating, rhs.rating).append(contentUrl, rhs.contentUrl).append(sourceTld, rhs.sourceTld).append(sourcePostUrl, rhs.sourcePostUrl).append(isIndexable, rhs.isIndexable).append(importDatetime, rhs.importDatetime).append(trendingDatetime, rhs.trendingDatetime).append(images, rhs.images).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeValue(id);
        dest.writeValue(slug);
        dest.writeValue(url);
        dest.writeValue(bitlyGifUrl);
        dest.writeValue(bitlyUrl);
        dest.writeValue(embedUrl);
        dest.writeValue(username);
        dest.writeValue(source);
        dest.writeValue(rating);
        dest.writeValue(contentUrl);
        dest.writeValue(sourceTld);
        dest.writeValue(sourcePostUrl);
        dest.writeValue(isIndexable);
        dest.writeValue(importDatetime);
        dest.writeValue(trendingDatetime);
        dest.writeValue(images);
    }

    public int describeContents() {
        return  0;
    }

}

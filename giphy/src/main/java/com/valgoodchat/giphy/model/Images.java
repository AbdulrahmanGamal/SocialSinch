
package com.valgoodchat.giphy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Images implements Parcelable
{

    @SerializedName("fixed_height")
    @Expose
    private FixedHeight fixedHeight;
    @SerializedName("fixed_height_still")
    @Expose
    private FixedHeightStill fixedHeightStill;
    @SerializedName("fixed_height_downsampled")
    @Expose
    private FixedHeightDownsampled fixedHeightDownsampled;
    @SerializedName("fixed_width")
    @Expose
    private FixedWidth fixedWidth;
    @SerializedName("fixed_width_still")
    @Expose
    private FixedWidthStill fixedWidthStill;
    @SerializedName("fixed_width_downsampled")
    @Expose
    private FixedWidthDownsampled fixedWidthDownsampled;
    @SerializedName("fixed_height_small")
    @Expose
    private FixedHeightSmall fixedHeightSmall;
    @SerializedName("fixed_height_small_still")
    @Expose
    private FixedHeightSmallStill fixedHeightSmallStill;
    @SerializedName("fixed_width_small")
    @Expose
    private FixedWidthSmall fixedWidthSmall;
    @SerializedName("fixed_width_small_still")
    @Expose
    private FixedWidthSmallStill fixedWidthSmallStill;
    @SerializedName("downsized")
    @Expose
    private Downsized downsized;
    @SerializedName("downsized_still")
    @Expose
    private DownsizedStill downsizedStill;
    @SerializedName("downsized_large")
    @Expose
    private DownsizedLarge downsizedLarge;
    @SerializedName("downsized_medium")
    @Expose
    private DownsizedMedium downsizedMedium;
    @SerializedName("original")
    @Expose
    private Original original;
    @SerializedName("original_still")
    @Expose
    private OriginalStill originalStill;
    @SerializedName("looping")
    @Expose
    private Looping looping;
    @SerializedName("original_mp4")
    @Expose
    private OriginalMp4 originalMp4;
    @SerializedName("preview")
    @Expose
    private Preview preview;
    @SerializedName("downsized_small")
    @Expose
    private DownsizedSmall downsizedSmall;
    @SerializedName("preview_gif")
    @Expose
    private PreviewGif previewGif;
    @SerializedName("preview_webp")
    @Expose
    private PreviewWebp previewWebp;
    public final static Parcelable.Creator<Images> CREATOR = new Creator<Images>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Images createFromParcel(Parcel in) {
            Images instance = new Images();
            instance.fixedHeight = ((FixedHeight) in.readValue((FixedHeight.class.getClassLoader())));
            instance.fixedHeightStill = ((FixedHeightStill) in.readValue((FixedHeightStill.class.getClassLoader())));
            instance.fixedHeightDownsampled = ((FixedHeightDownsampled) in.readValue((FixedHeightDownsampled.class.getClassLoader())));
            instance.fixedWidth = ((FixedWidth) in.readValue((FixedWidth.class.getClassLoader())));
            instance.fixedWidthStill = ((FixedWidthStill) in.readValue((FixedWidthStill.class.getClassLoader())));
            instance.fixedWidthDownsampled = ((FixedWidthDownsampled) in.readValue((FixedWidthDownsampled.class.getClassLoader())));
            instance.fixedHeightSmall = ((FixedHeightSmall) in.readValue((FixedHeightSmall.class.getClassLoader())));
            instance.fixedHeightSmallStill = ((FixedHeightSmallStill) in.readValue((FixedHeightSmallStill.class.getClassLoader())));
            instance.fixedWidthSmall = ((FixedWidthSmall) in.readValue((FixedWidthSmall.class.getClassLoader())));
            instance.fixedWidthSmallStill = ((FixedWidthSmallStill) in.readValue((FixedWidthSmallStill.class.getClassLoader())));
            instance.downsized = ((Downsized) in.readValue((Downsized.class.getClassLoader())));
            instance.downsizedStill = ((DownsizedStill) in.readValue((DownsizedStill.class.getClassLoader())));
            instance.downsizedLarge = ((DownsizedLarge) in.readValue((DownsizedLarge.class.getClassLoader())));
            instance.downsizedMedium = ((DownsizedMedium) in.readValue((DownsizedMedium.class.getClassLoader())));
            instance.original = ((Original) in.readValue((Original.class.getClassLoader())));
            instance.originalStill = ((OriginalStill) in.readValue((OriginalStill.class.getClassLoader())));
            instance.looping = ((Looping) in.readValue((Looping.class.getClassLoader())));
            instance.originalMp4 = ((OriginalMp4) in.readValue((OriginalMp4.class.getClassLoader())));
            instance.preview = ((Preview) in.readValue((Preview.class.getClassLoader())));
            instance.downsizedSmall = ((DownsizedSmall) in.readValue((DownsizedSmall.class.getClassLoader())));
            instance.previewGif = ((PreviewGif) in.readValue((PreviewGif.class.getClassLoader())));
            instance.previewWebp = ((PreviewWebp) in.readValue((PreviewWebp.class.getClassLoader())));
            return instance;
        }

        public Images[] newArray(int size) {
            return (new Images[size]);
        }

    }
    ;

    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public Images withFixedHeight(FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
        return this;
    }

    public FixedHeightStill getFixedHeightStill() {
        return fixedHeightStill;
    }

    public void setFixedHeightStill(FixedHeightStill fixedHeightStill) {
        this.fixedHeightStill = fixedHeightStill;
    }

    public Images withFixedHeightStill(FixedHeightStill fixedHeightStill) {
        this.fixedHeightStill = fixedHeightStill;
        return this;
    }

    public FixedHeightDownsampled getFixedHeightDownsampled() {
        return fixedHeightDownsampled;
    }

    public void setFixedHeightDownsampled(FixedHeightDownsampled fixedHeightDownsampled) {
        this.fixedHeightDownsampled = fixedHeightDownsampled;
    }

    public Images withFixedHeightDownsampled(FixedHeightDownsampled fixedHeightDownsampled) {
        this.fixedHeightDownsampled = fixedHeightDownsampled;
        return this;
    }

    public FixedWidth getFixedWidth() {
        return fixedWidth;
    }

    public void setFixedWidth(FixedWidth fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public Images withFixedWidth(FixedWidth fixedWidth) {
        this.fixedWidth = fixedWidth;
        return this;
    }

    public FixedWidthStill getFixedWidthStill() {
        return fixedWidthStill;
    }

    public void setFixedWidthStill(FixedWidthStill fixedWidthStill) {
        this.fixedWidthStill = fixedWidthStill;
    }

    public Images withFixedWidthStill(FixedWidthStill fixedWidthStill) {
        this.fixedWidthStill = fixedWidthStill;
        return this;
    }

    public FixedWidthDownsampled getFixedWidthDownsampled() {
        return fixedWidthDownsampled;
    }

    public void setFixedWidthDownsampled(FixedWidthDownsampled fixedWidthDownsampled) {
        this.fixedWidthDownsampled = fixedWidthDownsampled;
    }

    public Images withFixedWidthDownsampled(FixedWidthDownsampled fixedWidthDownsampled) {
        this.fixedWidthDownsampled = fixedWidthDownsampled;
        return this;
    }

    public FixedHeightSmall getFixedHeightSmall() {
        return fixedHeightSmall;
    }

    public void setFixedHeightSmall(FixedHeightSmall fixedHeightSmall) {
        this.fixedHeightSmall = fixedHeightSmall;
    }

    public Images withFixedHeightSmall(FixedHeightSmall fixedHeightSmall) {
        this.fixedHeightSmall = fixedHeightSmall;
        return this;
    }

    public FixedHeightSmallStill getFixedHeightSmallStill() {
        return fixedHeightSmallStill;
    }

    public void setFixedHeightSmallStill(FixedHeightSmallStill fixedHeightSmallStill) {
        this.fixedHeightSmallStill = fixedHeightSmallStill;
    }

    public Images withFixedHeightSmallStill(FixedHeightSmallStill fixedHeightSmallStill) {
        this.fixedHeightSmallStill = fixedHeightSmallStill;
        return this;
    }

    public FixedWidthSmall getFixedWidthSmall() {
        return fixedWidthSmall;
    }

    public void setFixedWidthSmall(FixedWidthSmall fixedWidthSmall) {
        this.fixedWidthSmall = fixedWidthSmall;
    }

    public Images withFixedWidthSmall(FixedWidthSmall fixedWidthSmall) {
        this.fixedWidthSmall = fixedWidthSmall;
        return this;
    }

    public FixedWidthSmallStill getFixedWidthSmallStill() {
        return fixedWidthSmallStill;
    }

    public void setFixedWidthSmallStill(FixedWidthSmallStill fixedWidthSmallStill) {
        this.fixedWidthSmallStill = fixedWidthSmallStill;
    }

    public Images withFixedWidthSmallStill(FixedWidthSmallStill fixedWidthSmallStill) {
        this.fixedWidthSmallStill = fixedWidthSmallStill;
        return this;
    }

    public Downsized getDownsized() {
        return downsized;
    }

    public void setDownsized(Downsized downsized) {
        this.downsized = downsized;
    }

    public Images withDownsized(Downsized downsized) {
        this.downsized = downsized;
        return this;
    }

    public DownsizedStill getDownsizedStill() {
        return downsizedStill;
    }

    public void setDownsizedStill(DownsizedStill downsizedStill) {
        this.downsizedStill = downsizedStill;
    }

    public Images withDownsizedStill(DownsizedStill downsizedStill) {
        this.downsizedStill = downsizedStill;
        return this;
    }

    public DownsizedLarge getDownsizedLarge() {
        return downsizedLarge;
    }

    public void setDownsizedLarge(DownsizedLarge downsizedLarge) {
        this.downsizedLarge = downsizedLarge;
    }

    public Images withDownsizedLarge(DownsizedLarge downsizedLarge) {
        this.downsizedLarge = downsizedLarge;
        return this;
    }

    public DownsizedMedium getDownsizedMedium() {
        return downsizedMedium;
    }

    public void setDownsizedMedium(DownsizedMedium downsizedMedium) {
        this.downsizedMedium = downsizedMedium;
    }

    public Images withDownsizedMedium(DownsizedMedium downsizedMedium) {
        this.downsizedMedium = downsizedMedium;
        return this;
    }

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public Images withOriginal(Original original) {
        this.original = original;
        return this;
    }

    public OriginalStill getOriginalStill() {
        return originalStill;
    }

    public void setOriginalStill(OriginalStill originalStill) {
        this.originalStill = originalStill;
    }

    public Images withOriginalStill(OriginalStill originalStill) {
        this.originalStill = originalStill;
        return this;
    }

    public Looping getLooping() {
        return looping;
    }

    public void setLooping(Looping looping) {
        this.looping = looping;
    }

    public Images withLooping(Looping looping) {
        this.looping = looping;
        return this;
    }

    public OriginalMp4 getOriginalMp4() {
        return originalMp4;
    }

    public void setOriginalMp4(OriginalMp4 originalMp4) {
        this.originalMp4 = originalMp4;
    }

    public Images withOriginalMp4(OriginalMp4 originalMp4) {
        this.originalMp4 = originalMp4;
        return this;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public Images withPreview(Preview preview) {
        this.preview = preview;
        return this;
    }

    public DownsizedSmall getDownsizedSmall() {
        return downsizedSmall;
    }

    public void setDownsizedSmall(DownsizedSmall downsizedSmall) {
        this.downsizedSmall = downsizedSmall;
    }

    public Images withDownsizedSmall(DownsizedSmall downsizedSmall) {
        this.downsizedSmall = downsizedSmall;
        return this;
    }

    public PreviewGif getPreviewGif() {
        return previewGif;
    }

    public void setPreviewGif(PreviewGif previewGif) {
        this.previewGif = previewGif;
    }

    public Images withPreviewGif(PreviewGif previewGif) {
        this.previewGif = previewGif;
        return this;
    }

    public PreviewWebp getPreviewWebp() {
        return previewWebp;
    }

    public void setPreviewWebp(PreviewWebp previewWebp) {
        this.previewWebp = previewWebp;
    }

    public Images withPreviewWebp(PreviewWebp previewWebp) {
        this.previewWebp = previewWebp;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(fixedHeight).append(fixedHeightStill).append(fixedHeightDownsampled).append(fixedWidth).append(fixedWidthStill).append(fixedWidthDownsampled).append(fixedHeightSmall).append(fixedHeightSmallStill).append(fixedWidthSmall).append(fixedWidthSmallStill).append(downsized).append(downsizedStill).append(downsizedLarge).append(downsizedMedium).append(original).append(originalStill).append(looping).append(originalMp4).append(preview).append(downsizedSmall).append(previewGif).append(previewWebp).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Images) == false) {
            return false;
        }
        Images rhs = ((Images) other);
        return new EqualsBuilder().append(fixedHeight, rhs.fixedHeight).append(fixedHeightStill, rhs.fixedHeightStill).append(fixedHeightDownsampled, rhs.fixedHeightDownsampled).append(fixedWidth, rhs.fixedWidth).append(fixedWidthStill, rhs.fixedWidthStill).append(fixedWidthDownsampled, rhs.fixedWidthDownsampled).append(fixedHeightSmall, rhs.fixedHeightSmall).append(fixedHeightSmallStill, rhs.fixedHeightSmallStill).append(fixedWidthSmall, rhs.fixedWidthSmall).append(fixedWidthSmallStill, rhs.fixedWidthSmallStill).append(downsized, rhs.downsized).append(downsizedStill, rhs.downsizedStill).append(downsizedLarge, rhs.downsizedLarge).append(downsizedMedium, rhs.downsizedMedium).append(original, rhs.original).append(originalStill, rhs.originalStill).append(looping, rhs.looping).append(originalMp4, rhs.originalMp4).append(preview, rhs.preview).append(downsizedSmall, rhs.downsizedSmall).append(previewGif, rhs.previewGif).append(previewWebp, rhs.previewWebp).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fixedHeight);
        dest.writeValue(fixedHeightStill);
        dest.writeValue(fixedHeightDownsampled);
        dest.writeValue(fixedWidth);
        dest.writeValue(fixedWidthStill);
        dest.writeValue(fixedWidthDownsampled);
        dest.writeValue(fixedHeightSmall);
        dest.writeValue(fixedHeightSmallStill);
        dest.writeValue(fixedWidthSmall);
        dest.writeValue(fixedWidthSmallStill);
        dest.writeValue(downsized);
        dest.writeValue(downsizedStill);
        dest.writeValue(downsizedLarge);
        dest.writeValue(downsizedMedium);
        dest.writeValue(original);
        dest.writeValue(originalStill);
        dest.writeValue(looping);
        dest.writeValue(originalMp4);
        dest.writeValue(preview);
        dest.writeValue(downsizedSmall);
        dest.writeValue(previewGif);
        dest.writeValue(previewWebp);
    }

    public int describeContents() {
        return  0;
    }

}

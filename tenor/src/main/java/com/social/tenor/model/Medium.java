
package com.social.tenor.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Medium implements Parcelable
{

    @SerializedName("nanomp4")
    @Expose
    private Nanomp4 nanomp4;
    @SerializedName("nanowebm")
    @Expose
    private Nanowebm nanowebm;
    @SerializedName("tinygif")
    @Expose
    private Tinygif tinygif;
    @SerializedName("tinymp4")
    @Expose
    private Tinymp4 tinymp4;
    @SerializedName("tinywebm")
    @Expose
    private Tinywebm tinywebm;
    @SerializedName("webm")
    @Expose
    private Webm webm;
    @SerializedName("gif")
    @Expose
    private Gif gif;
    @SerializedName("mp4")
    @Expose
    private Mp4 mp4;
    @SerializedName("nanogif")
    @Expose
    private Nanogif nanogif;
    @SerializedName("loopedmp4")
    @Expose
    private Loopedmp4 loopedmp4;
    public final static Parcelable.Creator<Medium> CREATOR = new Creator<Medium>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Medium createFromParcel(Parcel in) {
            Medium instance = new Medium();
            instance.nanomp4 = ((Nanomp4) in.readValue((Nanomp4.class.getClassLoader())));
            instance.nanowebm = ((Nanowebm) in.readValue((Nanowebm.class.getClassLoader())));
            instance.tinygif = ((Tinygif) in.readValue((Tinygif.class.getClassLoader())));
            instance.tinymp4 = ((Tinymp4) in.readValue((Tinymp4.class.getClassLoader())));
            instance.tinywebm = ((Tinywebm) in.readValue((Tinywebm.class.getClassLoader())));
            instance.webm = ((Webm) in.readValue((Webm.class.getClassLoader())));
            instance.gif = ((Gif) in.readValue((Gif.class.getClassLoader())));
            instance.mp4 = ((Mp4) in.readValue((Mp4.class.getClassLoader())));
            instance.nanogif = ((Nanogif) in.readValue((Nanogif.class.getClassLoader())));
            instance.loopedmp4 = ((Loopedmp4) in.readValue((Loopedmp4.class.getClassLoader())));
            return instance;
        }

        public Medium[] newArray(int size) {
            return (new Medium[size]);
        }

    }
    ;

    public Nanomp4 getNanomp4() {
        return nanomp4;
    }

    public void setNanomp4(Nanomp4 nanomp4) {
        this.nanomp4 = nanomp4;
    }

    public Nanowebm getNanowebm() {
        return nanowebm;
    }

    public void setNanowebm(Nanowebm nanowebm) {
        this.nanowebm = nanowebm;
    }

    public Tinygif getTinygif() {
        return tinygif;
    }

    public void setTinygif(Tinygif tinygif) {
        this.tinygif = tinygif;
    }

    public Tinymp4 getTinymp4() {
        return tinymp4;
    }

    public void setTinymp4(Tinymp4 tinymp4) {
        this.tinymp4 = tinymp4;
    }

    public Tinywebm getTinywebm() {
        return tinywebm;
    }

    public void setTinywebm(Tinywebm tinywebm) {
        this.tinywebm = tinywebm;
    }

    public Webm getWebm() {
        return webm;
    }

    public void setWebm(Webm webm) {
        this.webm = webm;
    }

    public Gif getGif() {
        return gif;
    }

    public void setGif(Gif gif) {
        this.gif = gif;
    }

    public Mp4 getMp4() {
        return mp4;
    }

    public void setMp4(Mp4 mp4) {
        this.mp4 = mp4;
    }

    public Nanogif getNanogif() {
        return nanogif;
    }

    public void setNanogif(Nanogif nanogif) {
        this.nanogif = nanogif;
    }

    public Loopedmp4 getLoopedmp4() {
        return loopedmp4;
    }

    public void setLoopedmp4(Loopedmp4 loopedmp4) {
        this.loopedmp4 = loopedmp4;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nanomp4).append(nanowebm).append(tinygif).append(tinymp4).append(tinywebm).append(webm).append(gif).append(mp4).append(nanogif).append(loopedmp4).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Medium) == false) {
            return false;
        }
        Medium rhs = ((Medium) other);
        return new EqualsBuilder().append(nanomp4, rhs.nanomp4).append(nanowebm, rhs.nanowebm).append(tinygif, rhs.tinygif).append(tinymp4, rhs.tinymp4).append(tinywebm, rhs.tinywebm).append(webm, rhs.webm).append(gif, rhs.gif).append(mp4, rhs.mp4).append(nanogif, rhs.nanogif).append(loopedmp4, rhs.loopedmp4).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(nanomp4);
        dest.writeValue(nanowebm);
        dest.writeValue(tinygif);
        dest.writeValue(tinymp4);
        dest.writeValue(tinywebm);
        dest.writeValue(webm);
        dest.writeValue(gif);
        dest.writeValue(mp4);
        dest.writeValue(nanogif);
        dest.writeValue(loopedmp4);
    }

    public int describeContents() {
        return  0;
    }

}

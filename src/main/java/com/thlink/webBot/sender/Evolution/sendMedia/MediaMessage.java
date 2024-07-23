package com.thlink.webBot.sender.Evolution.sendMedia;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MediaMessage {

    @SerializedName("mediatype")
    @Expose
    private String mediatype;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("media")
    @Expose
    private String media;

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

}

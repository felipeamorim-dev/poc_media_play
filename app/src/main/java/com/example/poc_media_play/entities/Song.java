package com.example.poc_media_play.entities;

import java.util.Objects;

public class Song {

    private Integer mediaId;
    private String title;
    private String url;
    private boolean isDoanload = false;

    public Song() {
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDoanload() {
        return isDoanload;
    }

    public void setDoanload(boolean doanload) {
        isDoanload = doanload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return mediaId.equals(song.mediaId) && title.equals(song.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediaId, title);
    }

    @Override
    public String toString() {
        return "Song{" +
                "mediaId=" + mediaId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", isDoanload=" + isDoanload +
                '}';
    }
}

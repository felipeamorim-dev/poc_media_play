package com.example.poc_media_play.listeners;

import android.content.Context;

import com.example.poc_media_play.entities.Song;

public interface DownloadAudioFile {

    void processDownload(Context context, Song song);
}

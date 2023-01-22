package com.example.poc_media_play.repositories;

import static com.example.poc_media_play.utils.HandleFileUtils.VerifyExistDirectoryAssets;
import static com.example.poc_media_play.utils.HandleFileUtils.isFileDirectory;
import static com.example.poc_media_play.utils.HandleFileUtils.transferMusicDirectory;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.example.poc_media_play.entities.Song;
import com.example.poc_media_play.listeners.DownloadAudioFile;
import com.example.poc_media_play.ui.PlayMusicActivity;
import java.io.File;



public class AudioRepository implements DownloadAudioFile {

    private final Context context;
    private File mMusicInDownloads;
    private File mMusicInAssets;
    private Song music;

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(downloadId));

            if(cursor.moveToFirst()) {
                @SuppressLint("Range")
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                if(status == DownloadManager.STATUS_SUCCESSFUL) {
                    try {

                        transferMusicDirectory(mMusicInDownloads, mMusicInAssets);
                        Log.d("File", "Transferencia de arquivo finalizada");

                        Intent musicIntent = new Intent(context, PlayMusicActivity.class);
                        musicIntent.putExtra("title", music.getTitle());
                        context.startActivity(musicIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };

    public AudioRepository(Context context) {
        this.context = context;
    }

    private void downloadMusicStorageFromUrl(String url, String title) {
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(title);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, title);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public void processDownload(Context context, Song song) {
        this.music = song;
        mMusicInDownloads = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), music.getTitle());
        mMusicInAssets = new File(context.getFilesDir(), "assets/" + music.getTitle());
        File assets = new File(context.getFilesDir(), "assets");

        VerifyExistDirectoryAssets(assets);

        if(!isFileDirectory(music.getTitle(), context)) {
            downloadMusicStorageFromUrl(music.getUrl(), music.getTitle());
        }

        context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}

package com.example.poc_media_play.repositories;

import static com.example.poc_media_play.utils.HandleFileUtils.*;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.poc_media_play.entities.Song;
import com.example.poc_media_play.listeners.DownloadAudioFile;
import com.example.poc_media_play.ui.PlayMusicActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FireStorageRepository implements DownloadAudioFile {

    private static FireStorageRepository INSTANCE;
    private static FirebaseStorage firebaseStorage;

    private FireStorageRepository() {
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance();
        }
    }

    public static FireStorageRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FireStorageRepository();
        }

        return INSTANCE;
    }

    private void initActivityPlayMusic(String title, Context context) {
        Intent musicIntent = new Intent(context, PlayMusicActivity.class);
        musicIntent.putExtra("title", title);
        context.startActivity(musicIntent);
    }

    @Override
    public void processDownload(Context context, Song song) {
        StorageReference httpsDownloadReference = firebaseStorage.getReferenceFromUrl(song.getUrl());
        File assetsDir = new File(context.getFilesDir(), "assets");
        File audioFileOut = new File(context.getFilesDir() + "/assets", song.getTitle());

        VerifyExistDirectoryAssets(assetsDir);

        if(!isFileDirectory(song.getTitle(), context)) {
            httpsDownloadReference.getFile(audioFileOut).addOnSuccessListener(taskSnapshot -> {
                initActivityPlayMusic(song.getTitle(), context);
            }).addOnFailureListener(e -> {
                Log.d("ErrorDownload", e.getMessage());
                Toast.makeText(context, "Falha na realização do download do arquivo de audio!", Toast.LENGTH_LONG).show();
            });
        } else initActivityPlayMusic(song.getTitle(), context);
    }
}

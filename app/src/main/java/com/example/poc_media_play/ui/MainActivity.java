package com.example.poc_media_play.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.poc_media_play.R;
import com.example.poc_media_play.adapter.AudioAdapter;
import com.example.poc_media_play.auth.AuthAnonymous;
import com.example.poc_media_play.entities.Song;
import com.example.poc_media_play.interfaces.OnListClick;
import com.example.poc_media_play.listeners.DownloadAudioFile;
import com.example.poc_media_play.repositories.FireStorageRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final ViewHolder mViewHolder = new ViewHolder();
    private final Map<String, Song> songMap = new HashMap<>();
    private List<Song> mSongsList = new ArrayList<>();
    private FirebaseFirestore mDb;
    private CollectionReference dbRef;
    private AudioAdapter mAudioAdapter;
    private AuthAnonymous auth;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = new AuthAnonymous();

        mDb = FirebaseFirestore.getInstance();
        dbRef = mDb.collection("songs");

        OnListClick audioListener = id -> {
            Log.d("Item", "Item selecionado. Id: " + id);
            DownloadAudioFile downloadAudioFile = FireStorageRepository.getInstance();
            Song audio = mSongsList.get(id - 1);

            if(!audio.isDoanload()) {
                downloadAudioFile.processDownload(getApplicationContext(), audio);
            }
        };

        this.mViewHolder.mRecycleAudio = findViewById(R.id.mAudioList);

        mAudioAdapter = new AudioAdapter(mSongsList, audioListener);
        this.mViewHolder.mRecycleAudio.setAdapter(mAudioAdapter);

        this.mViewHolder.mRecycleAudio.setLayoutManager(new LinearLayoutManager(this));

        dbRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> list = task.getResult().getDocuments();
                for (DocumentSnapshot doc : list) {
                    Song s = doc.toObject(Song.class);
                    mSongsList.add(s);
                    songMap.put(doc.getId(), s);
                    Log.d("Data", "Documento de audio: " + doc.getData());
                }
                mAudioAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getmAuth().getCurrentUser();
        if (Objects.nonNull(user)){
            auth.signInAnonymous(this);
        }
    }

    private static class ViewHolder {
        RecyclerView mRecycleAudio;
    }

    private String getKeyValueSongs(Song song) {
        for (Map.Entry<String, Song> songMap : songMap.entrySet()) {
            if(songMap.getValue().equals(song)) {
                return songMap.getKey();
            }
        }
        return "";
    }
}

package com.example.poc_media_play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poc_media_play.R;
import com.example.poc_media_play.entities.Song;
import com.example.poc_media_play.interfaces.OnListClick;
import com.example.poc_media_play.viewholder.AudioViewHolder;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioViewHolder> {

    private List<Song> mAudioList;
    private OnListClick mAudioListener;

    public AudioAdapter(List<Song> mAudioList, OnListClick audioListener) {
        this.mAudioList = mAudioList;
        this.mAudioListener = audioListener;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_audio, parent, false);

        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        Song music = this.mAudioList.get(position);
        holder.bind(music, this.mAudioListener);
    }

    @Override
    public int getItemCount() {
        return this.mAudioList.size();
    }
}

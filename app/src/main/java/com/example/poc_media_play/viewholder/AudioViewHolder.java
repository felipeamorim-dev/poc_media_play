package com.example.poc_media_play.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poc_media_play.R;
import com.example.poc_media_play.entities.Song;
import com.example.poc_media_play.interfaces.OnListClick;

public class AudioViewHolder extends RecyclerView.ViewHolder{

    private TextView mAudioName;

    public AudioViewHolder(@NonNull View itemView) {
        super(itemView);

        mAudioName = itemView.findViewById(R.id.audio_name);
    }

    public void bind(Song music, OnListClick audioListener) {
        mAudioName.setText(music.getTitle());

        mAudioName.setOnClickListener(view -> {
            try {
                audioListener.onClick(music.getMediaId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

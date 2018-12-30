package jmapps.supplicationsfromquran.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jmapps.supplicationsfromquran.R;

public class PlayListContentViewHolder extends RecyclerView.ViewHolder {

    public final ImageView tbPlayPlayList;
    public final ImageView tbPlayPlayListAccent;
    public final TextView tvAyahNumber;

    public PlayListContentViewHolder(View itemView) {
        super(itemView);

        tbPlayPlayList = itemView.findViewById(R.id.iv_play_playlist);
        tbPlayPlayListAccent = itemView.findViewById(R.id.iv_play_playlist_accent);
        tvAyahNumber = itemView.findViewById(R.id.tv_ayah_number);
    }
}

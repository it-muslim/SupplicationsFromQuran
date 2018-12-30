package jmapps.supplicationsfromquran.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jmapps.supplicationsfromquran.MainActivity;
import jmapps.supplicationsfromquran.Model.PlayListContentModel;
import jmapps.supplicationsfromquran.R;
import jmapps.supplicationsfromquran.ViewHolder.PlayListContentViewHolder;

public class PlayListContentAdapter extends RecyclerView.Adapter<PlayListContentViewHolder> {

    private final MainActivity mMainActivity;
    private List<PlayListContentModel> mPlayListContentModel;
    private int currentIndex = -1;

    public PlayListContentAdapter(MainActivity mainActivity,
                                  List<PlayListContentModel> playListContentModels) {
        this.mMainActivity = mainActivity;
        this.mPlayListContentModel = playListContentModels;
    }

    @NonNull
    @Override
    public PlayListContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayListContentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PlayListContentViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        final String strDuaName = mPlayListContentModel.get(position).getStrPlayListItem();
        holder.tvAyahNumber.setText(strDuaName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.playOnly(position + 1);
            }
        });

        holder.tbPlayPlayList.setVisibility((position != currentIndex) ? View.VISIBLE : View.GONE);
        holder.tbPlayPlayListAccent.setVisibility((position == currentIndex) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mPlayListContentModel.size();
    }

    public void setColorPlaying(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }
}
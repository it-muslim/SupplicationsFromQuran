package jmapps.supplicationsfromquran.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jmapps.supplicationsfromquran.Model.PlayListContentModel;
import jmapps.supplicationsfromquran.R;
import jmapps.supplicationsfromquran.ViewHolder.PlayListContentViewHolder;

public class PlayListContentAdapter extends RecyclerView.Adapter<PlayListContentViewHolder> {

    private List<PlayListContentModel> mPlayListContentModel;
    private OnItemAdapterClickListener onItemAdapterClickListener;
    private int currentIndex = -1;

    public interface OnItemAdapterClickListener {
        void onItemAdapterClick(int position);
    }

    public PlayListContentAdapter(List<PlayListContentModel> playListContentModels,
                                  OnItemAdapterClickListener onItemAdapterClickListener) {
        this.mPlayListContentModel = playListContentModels;
        this.onItemAdapterClickListener = onItemAdapterClickListener;
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
        holder.tbPlayPlayList.setVisibility((position != currentIndex) ? View.VISIBLE : View.GONE);
        holder.tbPlayPlayListAccent.setVisibility((position == currentIndex) ? View.VISIBLE : View.GONE);
        holder.bindClick(onItemAdapterClickListener, position);
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
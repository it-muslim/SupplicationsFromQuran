package jmapps.supplicationsfromquran.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jmapps.supplicationsfromquran.Model.MainContentModel;
import jmapps.supplicationsfromquran.R;
import jmapps.supplicationsfromquran.ViewHolder.MainContentViewHolder;

public class MainContentAdapter extends RecyclerView.Adapter<MainContentViewHolder> {

    private final List<MainContentModel> mMainContentModel;
    private LayoutInflater inflater;
    private OnItemAdapterClickListener mOnItemAdapterClickListener;
    private int currentIndex = -1;

    public interface OnItemAdapterClickListener {
        void itemAdapterClickListener(MainContentViewHolder holder, List<MainContentModel> model, int position);
    }

    public MainContentAdapter(List<MainContentModel> mainContentModel,
                              Context context,
                              OnItemAdapterClickListener onItemAdapterClickListener) {
        this.mMainContentModel = mainContentModel;
        this.mOnItemAdapterClickListener = onItemAdapterClickListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MainContentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int parent) {
        View mainContentView = inflater.inflate(R.layout.item_main_content, viewGroup, false);
        return new MainContentViewHolder(mainContentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainContentViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        final String strListLength = position + 1 + "/" + String.valueOf(mMainContentModel.size());
        final String strAyahArabic = mMainContentModel.get(position).getStrAyahArabic();
        final String strAyahTranslation = mMainContentModel.get(position).getStrAyahTranslation();

        holder.tvAyahArabic.setText(strAyahArabic);

        // В случае арабской локализации strAyahTranslation будет равен NULL
        if (strAyahTranslation != null) {
            // Там, где он не равен NULL показываем его
            holder.tvAyahTranslation.setVisibility(View.VISIBLE);
            holder.tvAyahTranslation.setText(Html.fromHtml(strAyahTranslation));
        } else {
            // Если же равен (в арабской локализации), то скрываем
            holder.tvAyahTranslation.setVisibility(View.GONE);
        }

        holder.tvListLength.setText(strListLength);
        holder.viewColorLine.setVisibility((position == currentIndex) ? View.VISIBLE : View.INVISIBLE);
        holder.viewMainLine.setVisibility((position == currentIndex) ? View.INVISIBLE : View.VISIBLE);
        // Привязка интерфейса
        holder.findItemClick(mOnItemAdapterClickListener, holder, mMainContentModel, position);
    }

    @Override
    public int getItemCount() {
        return mMainContentModel.size();
    }

    public void setLinePlaying(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }
}
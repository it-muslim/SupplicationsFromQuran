package jmapps.supplicationsfromquran.Adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import jmapps.supplicationsfromquran.MainActivity;
import jmapps.supplicationsfromquran.Model.MainContentModel;
import jmapps.supplicationsfromquran.R;
import jmapps.supplicationsfromquran.ViewHolder.MainContentViewHolder;

public class MainContentAdapter extends RecyclerView.Adapter<MainContentViewHolder> {

    private final List<MainContentModel> mMainContentModel;
    private MainActivity mMainActivity;
    private LayoutInflater inflater;
    private int currentIndex = -1;

    public MainContentAdapter(List<MainContentModel> mainContentModel,
                              MainActivity mainActivity) {
        this.mMainContentModel = mainContentModel;
        this.mMainActivity = mainActivity;
        inflater = LayoutInflater.from(mMainActivity);
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
        final String strIdPosition = mMainContentModel.get(position).getStrIdPosition();
        final String strListLength = strIdPosition + "/" + String.valueOf(mMainContentModel.size());
        final String strAyahArabic = mMainContentModel.get(position).getStrAyahArabic();
        final String strAyahTranslation = mMainContentModel.get(position).getStrAyahTranslation();

        holder.tvAyahArabic.setText(strAyahArabic);
        if (strAyahTranslation != null) {
            holder.tvAyahTranslation.setVisibility(View.VISIBLE);
            holder.tvAyahTranslation.setText(Html.fromHtml(strAyahTranslation));
        } else {
            holder.tvAyahTranslation.setVisibility(View.GONE);
        }
        holder.tvListLength.setText(strListLength);
        final int positionId = Integer.parseInt(strIdPosition);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.playOnly(position + 1);
            }
        });

        holder.btnCopyContent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                eventContentCopy(strListLength, strAyahArabic, strAyahTranslation);
            }
        });

        holder.btnShareContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventContentShare(strListLength, strAyahArabic, strAyahTranslation);
            }
        });

        holder.viewColorLine.setVisibility((positionId == currentIndex) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mMainContentModel.size();
    }

    public void setLinePlaying(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void eventContentCopy(String strContentLength, String strAyahContent, String strTranslateContent) {
        if (strTranslateContent == null) {
            strAyahContent = "";
        }

        ClipboardManager clipboard = (ClipboardManager)
                Objects.requireNonNull(mMainActivity.getSystemService(Context.CLIPBOARD_SERVICE));

        ClipData clip = ClipData.newPlainText("",
                Html.fromHtml(strContentLength + "<p/>" + strAyahContent + "<p/>" + strTranslateContent));

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(mMainActivity, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    private void eventContentShare(String strContentLength, String strAyahContent, String strTranslateContent) {
        if (strTranslateContent == null) {
            strAyahContent = "";
        }

        Intent shareText = new Intent(Intent.ACTION_SEND);
        shareText.setType("text/plain");
        shareText.putExtra(Intent.EXTRA_TEXT,
                Html.fromHtml(strContentLength + "<p/>" + strAyahContent + "<p/>" + strTranslateContent +
                        "<p/>" + "____________" + "<p/>" +
                        "https://play.google.com/store/apps/details?id=jmapps.supplicationsfromquran"));
        shareText.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareText = Intent.createChooser(shareText, mMainActivity.getString(R.string.share_to));
        mMainActivity.startActivity(shareText);
    }
}
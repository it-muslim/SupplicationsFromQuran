package jmapps.supplicationsfromquran.ViewHolder;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jmapps.supplicationsfromquran.R;

public class MainContentViewHolder extends RecyclerView.ViewHolder implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final SharedPreferences mPreferences;
    public final TextView tvAyahArabic;
    public final TextView tvAyahTranslation;
    public final View viewColorLine;
    public final TextView tvListLength;
    public final Button btnCopyContent;
    public final Button btnShareContent;

    public MainContentViewHolder(@NonNull View mainContent) {
        super(mainContent);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(mainContent.getContext());

        PreferenceManager.getDefaultSharedPreferences(mainContent.getContext())
                .registerOnSharedPreferenceChangeListener(this);

        viewColorLine = mainContent.findViewById(R.id.view_color_line);
        tvAyahArabic = mainContent.findViewById(R.id.tv_ayah_arabic);
        tvAyahTranslation = mainContent.findViewById(R.id.tv_ayah_translation);
        tvListLength = mainContent.findViewById(R.id.tv_list_length);
        btnCopyContent = mainContent.findViewById(R.id.btn_copy_content);
        btnShareContent = mainContent.findViewById(R.id.btn_share_content);

        indentText();
        sizeText();
        textColorArabic();
        textColorTranslation();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        indentText();
        sizeText();
        textColorArabic();
        textColorTranslation();
    }

    private void indentText() {
        int indentSize = mPreferences.getInt("indent_size", 16);
        float scale = itemView.getContext().getResources().getDisplayMetrics().density;
        int dpSize = (int) (indentSize * scale + 0.5f);
        tvAyahArabic.setPadding(dpSize, 25, dpSize, 25);
        tvAyahTranslation.setPadding(dpSize, 0, dpSize, 25);
    }

    private void sizeText() {
        int textSize = mPreferences.getInt("text_size", 18);

        tvAyahArabic.setTextSize(textSize);
        tvAyahTranslation.setTextSize(textSize);
    }

    private void textColorArabic() {
        int ARState = mPreferences.getInt("key_r_arabic", 188);
        int AGState = mPreferences.getInt("key_g_arabic", 68);
        int ABState = mPreferences.getInt("key_b_arabic", 68);

        tvAyahArabic.setTextColor(Color.argb(255, ARState, AGState, ABState));
    }

    private void textColorTranslation() {
        int TRState = mPreferences.getInt("key_r_translation", 0);
        int TGState = mPreferences.getInt("key_g_translation", 0);
        int TBState = mPreferences.getInt("key_b_translation", 0);

        tvAyahTranslation.setTextColor(Color.argb(255, TRState, TGState, TBState));
    }
}
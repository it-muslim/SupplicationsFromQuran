package jmapps.supplicationsfromquran.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Objects;

import jmapps.supplicationsfromquran.R;

public class SettingsBottomSheet extends BottomSheetDialogFragment implements
        View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String keyProgressArabic = "key_progress_arabic";
    private static final String keyRStateArabic = "key_r_arabic";
    private static final String keyGStateArabic = "key_g_arabic";
    private static final String keyBStateArabic = "key_b_arabic";

    private static final String keyProgressTranslation = "key_progress_translation";
    private static final String keyRStateTranslation = "key_r_translation";
    private static final String keyGStateTranslation = "key_g_translation";
    private static final String keyBStateTranslation = "key_b_translation";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settingRoot = inflater.inflate(R.layout.bottom_sheet_settings, container, false);

        setRetainInstance(true);
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();

        Button btnIndentMinus = settingRoot.findViewById(R.id.btn_indent_minus);
        Button btnIndentPlus = settingRoot.findViewById(R.id.btn_indent_plus);
        Button btnTextSizeMinus = settingRoot.findViewById(R.id.btn_text_size_minus);
        Button btnTextSizePlus = settingRoot.findViewById(R.id.btn_text_size_plus);
        SeekBar sbArabicTextColor = settingRoot.findViewById(R.id.sb_arabic_text_color);
        SeekBar sbTranslationTextColor = settingRoot.findViewById(R.id.sb_translation_text_color);

        btnIndentMinus.setOnClickListener(this);
        btnIndentPlus.setOnClickListener(this);
        btnTextSizeMinus.setOnClickListener(this);
        btnTextSizePlus.setOnClickListener(this);
        sbArabicTextColor.setOnSeekBarChangeListener(this);
        sbTranslationTextColor.setOnSeekBarChangeListener(this);

        int valueArabicProgress = mPreferences.getInt(keyProgressArabic, 956);
        int valueTranslationProgress = mPreferences.getInt(keyProgressTranslation, 0);

        sbArabicTextColor.setProgress(valueArabicProgress);
        sbTranslationTextColor.setProgress(valueTranslationProgress);

        return settingRoot;
    }

    @Override
    public void onClick(View v) {

        int indentSize = mPreferences.getInt("indent_size", 16);
        int textSize = mPreferences.getInt("text_size", 18);

        switch (v.getId()) {
            case R.id.btn_indent_minus:
                if (indentSize > 12) {
                    indentSize--;
                }
                break;
            case R.id.btn_indent_plus:
                if (indentSize < 50) {
                    indentSize++;
                }
                break;
            case R.id.btn_text_size_minus:
                if (textSize > 12) {
                    textSize--;
                }
                break;
            case R.id.btn_text_size_plus:
                if (textSize < 50) {
                    textSize++;
                }
                break;
        }
        mEditor.putInt("indent_size", indentSize);
        mEditor.putInt("text_size", textSize);
        mEditor.apply();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_arabic_text_color:

                int ARState = 0;
                int AGState = 0;
                int ABState = 0;

                textColor(progress,
                        ARState,
                        AGState,
                        ABState,
                        keyRStateArabic,
                        keyGStateArabic,
                        keyBStateArabic,
                        keyProgressArabic);
                break;
            case R.id.sb_translation_text_color:

                int TRState = 0;
                int TGState = 0;
                int TBState = 0;

                textColor(progress,
                        TRState,
                        TGState,
                        TBState,
                        keyRStateTranslation,
                        keyGStateTranslation,
                        keyBStateTranslation,
                        keyProgressTranslation);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void textColor(int progress,
                           int RState,
                           int GState,
                           int BState,
                           String keyR,
                           String keyG,
                           String keyB,
                           String keyProgress) {

        if (progress < 256) {
            BState = progress;
        } else if (progress < 256 * 2) {
            GState = progress % 256;
            BState = 256 - progress % 256;
        } else if (progress < 256 * 3) {
            GState = 255;
            BState = progress % 256;
        } else if (progress < 256 * 4) {
            RState = progress % 256;
            GState = 256 - progress % 256;
            BState = 256 - progress % 256;
        } else if (progress < 256 * 5) {
            RState = 255;
            GState = 0;
            BState = progress % 256;
        } else if (progress < 256 * 6) {
            RState = 255;
            GState = progress % 256;
            BState = 256 - progress % 256;
        } else if (progress < 256 * 7) {
            RState = 255;
            GState = 255;
            BState = progress % 256;
        }

        mEditor.putInt(keyProgress, progress).apply();
        mEditor.putInt(keyR, RState);
        mEditor.putInt(keyG, GState);
        mEditor.putInt(keyB, BState);
        mEditor.apply();
    }
}
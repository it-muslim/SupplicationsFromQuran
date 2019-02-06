package jmapps.supplicationsfromquran;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import jmapps.supplicationsfromquran.Adapter.MainContentAdapter;
import jmapps.supplicationsfromquran.Adapter.PlayListContentAdapter;
import jmapps.supplicationsfromquran.DBSetup.DBAssetHelper;
import jmapps.supplicationsfromquran.DBSetup.SQLiteOpenHelperMainList;
import jmapps.supplicationsfromquran.Fragment.ListAppsBottomSheet;
import jmapps.supplicationsfromquran.Fragment.SettingsBottomSheet;
import jmapps.supplicationsfromquran.Model.MainContentModel;
import jmapps.supplicationsfromquran.Model.PlayListContentModel;
import jmapps.supplicationsfromquran.Player.GetTrackIteratorImpl;
import jmapps.supplicationsfromquran.Player.MainContract;
import jmapps.supplicationsfromquran.Player.MainPresenterImpl;
import jmapps.supplicationsfromquran.ViewHolder.MainContentViewHolder;

import static jmapps.supplicationsfromquran.MainApplication.keyNightMode;

public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        MainContentAdapter.OnItemAdapterClickListener, MainContract.MainView,
        PlayListContentAdapter.OnItemAdapterClickListener {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private SQLiteDatabase sqLiteDatabase;

    private MenuItem itemNightMode;

    private MainPresenterImpl mainPresenter;

    private RecyclerView rvMainList;
    private List<MainContentModel> mainContentModelList;
    private MainContentAdapter mainContentAdapter;
    private RecyclerView rvPlayList;
    private PlayListContentAdapter playListContentAdapter;

    private ToggleButton tbPlayPause;
    private SeekBar sbAudioProgress;
    private ToggleButton tbIsLoop;

    private int currentIndex;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBAssetHelper dbAssetHelper = new DBAssetHelper(this);
        sqLiteDatabase = dbAssetHelper.getReadableDatabase();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        changeLanguage(mPreferences.getString("key_current_language", getString(R.string.default_lang)));
        setTitle(getString(R.string.app_name));
        checkedNightModeState();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvMainList = findViewById(R.id.rv_main_list);
        Button btnPreviousTrack = findViewById(R.id.btn_previous_track);
        tbPlayPause = findViewById(R.id.tb_play_pause);
        Button btnNextTrack = findViewById(R.id.btn_next_track);
        sbAudioProgress = findViewById(R.id.sb_audio_progress);
        tbIsLoop = findViewById(R.id.tb_loop);
        Button btnPlayList = findViewById(R.id.btn_play_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMainList.setLayoutManager(linearLayoutManager);

        SQLiteOpenHelperMainList sqLiteOpenHelperMainList = new SQLiteOpenHelperMainList(this);
        mainContentModelList = sqLiteOpenHelperMainList.getMainListContent();

        mainContentAdapter = new MainContentAdapter(mainContentModelList, this, this);
        rvMainList.setAdapter(mainContentAdapter);

        mainPresenter = new MainPresenterImpl(this, this,
                new GetTrackIteratorImpl(mainContentModelList, mainContentAdapter, rvMainList), sbAudioProgress);

        btnPreviousTrack.setOnClickListener(this);
        tbPlayPause.setOnCheckedChangeListener(this);
        btnNextTrack.setOnClickListener(this);
        tbIsLoop.setOnCheckedChangeListener(this);
        btnPlayList.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        itemNightMode = menu.findItem(R.id.action_night_mode);
        boolean nightModeState = mPreferences.getBoolean(keyNightMode, false);
        itemNightMode.setChecked(nightModeState);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean nightModeState = !itemNightMode.isChecked();

        switch (item.getItemId()) {
            case R.id.action_night_mode:
                if (nightModeState) {
                    nightModeState(true);
                    recreateActivity();
                } else {
                    nightModeState(false);
                    recreateActivity();
                }
                mEditor.putBoolean(keyNightMode, nightModeState).apply();
                break;
            case R.id.action_settings:
                SettingsBottomSheet settingsBottomSheet = new SettingsBottomSheet();
                settingsBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
                settingsBottomSheet.show(getSupportFragmentManager(), "settings");
                break;
            case R.id.action_languages:
                dialogWithSelectLang();
                break;
            case R.id.action_list_apps:
                ListAppsBottomSheet listAppsBottomSheet = new ListAppsBottomSheet();
                listAppsBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
                listAppsBottomSheet.show(getSupportFragmentManager(), "list_apps");
                break;
            case R.id.action_about_us:
                aboutUsDialog();
                break;
            case R.id.action_share_link:
                shareAppLink();
                break;
            case R.id.action_exit:
                mainPresenter.destroy();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_play_pause:
                mainPresenter.playTrackChecked(isChecked);
                break;
            case R.id.tb_loop:
                mainPresenter.loopTrack(isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous_track:
                if (currentIndex > 0) {
                    currentIndex--;
                    mainPresenter.playTrack(currentIndex);
                }
                break;
            case R.id.btn_next_track:
                if (currentIndex < mainContentModelList.size() - 1) {
                    currentIndex++;
                    mainPresenter.playTrack(currentIndex);
                }
                break;
            case R.id.btn_play_list:
                getPlayListBottomSheet();
                break;
        }
    }

    @Override
    public void setPlayState(boolean isChecked) {
        tbPlayPause.setChecked(isChecked);
    }

    @Override
    public void setLoopState(boolean isChecked) {
        tbIsLoop.setChecked(isChecked);
    }

    @Override
    public void setOnCompletion(boolean isChecked) {
        if (currentIndex < mainContentModelList.size() - 1) {
            currentIndex++;
            mainPresenter.playTrack(currentIndex);
            if (playListContentAdapter != null) {
                playListContentAdapter.setColorPlaying(currentIndex);
                rvPlayList.smoothScrollToPosition(currentIndex);
            }
        } else {
            sbAudioProgress.setProgress(0);
            tbPlayPause.setChecked(false);
            mainContentAdapter.setLinePlaying(-1);
            rvMainList.smoothScrollToPosition(0);
            if (playListContentAdapter != null) {
                playListContentAdapter.setColorPlaying(-1);
                rvPlayList.smoothScrollToPosition(0);
            }
        }
    }

    @Override
    public void itemAdapterClickListener(MainContentViewHolder holder, List<MainContentModel> model, final int position) {

        final String strListLength = position + 1 + "/" + String.valueOf(model.size());
        final String strAyahArabic = model.get(position).getStrAyahArabic();
        final String strAyahTranslation = model.get(position).getStrAyahTranslation();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.playTrack(currentIndex = position);
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
    }


    @Override
    public void onItemAdapterClick(int position) {
        mainPresenter.playTrack(currentIndex = position);
        playListContentAdapter.setColorPlaying(currentIndex = position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.destroy();
        sqLiteDatabase.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mainPresenter.destroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mainPresenter.destroy();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mainPresenter.clearPlayer();
            sbAudioProgress.setProgress(0);
            mainContentAdapter.setLinePlaying(-1);
            tbPlayPause.setChecked(false);
        }
    }

    private void getPlayListBottomSheet() {
        View view = View.inflate(this, R.layout.bottom_sheet_play_list, null);
        rvPlayList = view.findViewById(R.id.rv_play_list);

        SQLiteOpenHelperMainList liteOpenHelperMainList = new SQLiteOpenHelperMainList(this);
        List<PlayListContentModel> playListContentModel = liteOpenHelperMainList.getPlayListContent();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPlayList.setLayoutManager(linearLayoutManager);

        playListContentAdapter = new PlayListContentAdapter(playListContentModel, this);
        playListContentAdapter.setColorPlaying(currentIndex);
        rvPlayList.smoothScrollToPosition(currentIndex);
        rvPlayList.setAdapter(playListContentAdapter);
        rvPlayList.setHasFixedSize(true);

        BottomSheetDialog dialogPlayList = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        dialogPlayList.setContentView(view);
        dialogPlayList.show();
    }

    private void checkedNightModeState() {
        if (MainApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void dialogWithSelectLang() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_language)
                .setIcon(R.drawable.ic_language_accent)
                .setSingleChoiceItems(R.array.language_arrays, -1, null)
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        switch (selectedPosition) {
                            case 0:
                                changeLanguage("ar");
                                recreateActivity();
                                break;
                            case 1:
                                changeLanguage("en");
                                recreateActivity();
                                break;
                            case 2:
                                changeLanguage("ru");
                                recreateActivity();
                                break;
                        }
                    }
                })
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void eventContentCopy(String strContentLength, String strAyahContent, String strTranslateContent) {

        if (strTranslateContent == null) {
            strAyahContent = "";
        }

        ClipboardManager clipboard = (ClipboardManager)
                Objects.requireNonNull(getSystemService(Context.CLIPBOARD_SERVICE));

        ClipData clip = ClipData.newPlainText("",
                Html.fromHtml(strContentLength + "<p/>" + strAyahContent + "<p/>" + strTranslateContent));

        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
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
        shareText = Intent.createChooser(shareText, getString(R.string.share_to));
        startActivity(shareText);
    }

    private void changeLanguage(String strLanguage) {
        Locale locale = new Locale(strLanguage);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        mEditor.putString("key_current_language", strLanguage).apply();
    }

    private void recreateActivity() {
        Intent recreate = new Intent(MainActivity.this, MainActivity.class);
        recreate.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(recreate);
        finish();
        overridePendingTransition(0, 0);
        mainPresenter.destroy();
    }

    private void nightModeState(boolean state) {
        itemNightMode.setChecked(state);
        MainApplication.getInstance().setIsNightModeEnabled(state);
    }

    private void aboutUsDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams")
        View dialogAboutUs = inflater.inflate(R.layout.dialog_about_us, null);
        AlertDialog.Builder instructionDialog = new AlertDialog.Builder(this);

        instructionDialog.setView(dialogAboutUs);
        TextView tvAboutUsContent = dialogAboutUs.findViewById(R.id.tv_about_us_content);
        tvAboutUsContent.setMovementMethod(LinkMovementMethod.getInstance());

        instructionDialog.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        instructionDialog.create().show();
    }

    private void shareAppLink() {
        Intent shareText = new Intent(Intent.ACTION_SEND);
        shareText.setType("text/plain");
        shareText.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + "\n" +
                "https://play.google.com/store/apps/details?id=jmapps.supplicationsfromquran");
        shareText.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareText = Intent.createChooser(shareText, getString(R.string.share_to));
        startActivity(shareText);
    }
}
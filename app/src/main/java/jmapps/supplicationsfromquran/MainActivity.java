package jmapps.supplicationsfromquran;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Locale;

import jmapps.supplicationsfromquran.Adapter.MainContentAdapter;
import jmapps.supplicationsfromquran.Adapter.PlayListContentAdapter;
import jmapps.supplicationsfromquran.DBSetup.DBAssetHelper;
import jmapps.supplicationsfromquran.DBSetup.SQLiteOpenHelperMainList;
import jmapps.supplicationsfromquran.Fragment.ListAppsBottomSheet;
import jmapps.supplicationsfromquran.Fragment.SettingsBottomSheet;
import jmapps.supplicationsfromquran.Model.MainContentModel;
import jmapps.supplicationsfromquran.Model.PlayListContentModel;
import jmapps.supplicationsfromquran.Player.MainContract;
import jmapps.supplicationsfromquran.Player.MainPresenterImpl;
import jmapps.supplicationsfromquran.Player.MyPlayer;

import static jmapps.supplicationsfromquran.MainApplication.keyNightMode;

public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        MainContract.MainView {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private SQLiteDatabase sqLiteDatabase;

    private PlayListContentAdapter playListContentAdapter;
    private MainPresenterImpl mainPresenter;
    private MyPlayer myPlayer;
    private MainContentAdapter mainContentAdapter;
    private MenuItem itemNightMode;
    private ToggleButton tbPlayPause;
    private SeekBar sbAudioProgress;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        changeLanguage(mPreferences.getString("key_current_language", getString(R.string.default_lang)));
        setTitle(getString(R.string.app_name));
        checkedNightModeState();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBAssetHelper dbAssetHelper = new DBAssetHelper(this);
        sqLiteDatabase = dbAssetHelper.getReadableDatabase();

        RecyclerView rvMainList = findViewById(R.id.rv_main_list);
        Button btnPreviousTrack = findViewById(R.id.btn_previous_track);
        tbPlayPause = findViewById(R.id.tb_play_pause);
        Button btnNextTrack = findViewById(R.id.btn_next_track);
        sbAudioProgress = findViewById(R.id.sb_audio_progress);
        ToggleButton tbIsLoop = findViewById(R.id.tb_loop);
        Button btnPlayList = findViewById(R.id.btn_play_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMainList.setLayoutManager(linearLayoutManager);

        SQLiteOpenHelperMainList sqLiteOpenHelperMainList = new SQLiteOpenHelperMainList(this);
        List<MainContentModel> mainContentModelList = sqLiteOpenHelperMainList.getMainListContent();

        mainContentAdapter = new MainContentAdapter(mainContentModelList, this);
        rvMainList.setAdapter(mainContentAdapter);

        mainPresenter = new MainPresenterImpl(this);
        int currentIndex = 1;
        myPlayer = new MyPlayer(
                this, tbPlayPause, sbAudioProgress, rvMainList, mainContentAdapter, currentIndex);

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
                myPlayer.clearMediaPlayer();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_play_pause:
                mainPresenter.play(isChecked);
                break;
            case R.id.tb_loop:
                mainPresenter.loop(isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous_track:
                myPlayer.previousTrack();
                break;
            case R.id.btn_next_track:
                myPlayer.nextTrack();
                break;
            case R.id.btn_play_list:
                getPlayListBottomSheet();
                break;
        }
    }

    @Override
    public void playState(boolean isChecked) {
        myPlayer.setPlay(isChecked);
    }

    @Override
    public void loopState(boolean isChecked) {
        myPlayer.setLoop(isChecked);
    }

    private void getPlayListBottomSheet() {
        View view = View.inflate(this, R.layout.bottom_sheet_play_list, null);
        RecyclerView rvPlayList = view.findViewById(R.id.rv_play_list);

        SQLiteOpenHelperMainList liteOpenHelperMainList = new SQLiteOpenHelperMainList(this);
        List<PlayListContentModel> playListContentModel = liteOpenHelperMainList.getPlayListContent();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPlayList.setLayoutManager(linearLayoutManager);

        playListContentAdapter = new PlayListContentAdapter(this, playListContentModel);
        rvPlayList.setAdapter(playListContentAdapter);
        rvPlayList.setHasFixedSize(true);

        BottomSheetDialog dialogPlayList = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        dialogPlayList.setContentView(view);
        dialogPlayList.show();
    }

    public void playOnly(int position) {
        myPlayer.playOnly(position);
        tbPlayPause.setChecked(true);
        if (playListContentAdapter != null) {
            playListContentAdapter.setColorPlaying(position - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myPlayer.clearMediaPlayer();
        sqLiteDatabase.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myPlayer.clearMediaPlayer();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myPlayer.clearMediaPlayer();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            myPlayer.clearMediaPlayer();
            sbAudioProgress.setProgress(0);
            mainContentAdapter.setLinePlaying(-1);
            tbPlayPause.setChecked(false);
        }
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
        myPlayer.clearMediaPlayer();
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
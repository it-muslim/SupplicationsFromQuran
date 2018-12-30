package jmapps.supplicationsfromquran.Player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import jmapps.supplicationsfromquran.Adapter.MainContentAdapter;

public class MyPlayer implements SeekBar.OnSeekBarChangeListener {

    private final Context mContext;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private final ToggleButton mTbPlayPause;
    private final SeekBar mAudioProgress;
    private final RecyclerView mRvListMain;
    private final MainContentAdapter mMainContentAdapter;
    private int mCurrentIndex;

    public MyPlayer(Context context,
                    ToggleButton tbPlayPause,
                    SeekBar audioProgress,
                    RecyclerView rvMainList,
                    MainContentAdapter mainContentAdapter,
                    int currentIndex) {
        this.mContext = context;
        this.mTbPlayPause = tbPlayPause;
        this.mCurrentIndex = currentIndex;
        this.mAudioProgress = audioProgress;
        this.mRvListMain = rvMainList;
        this.mMainContentAdapter = mainContentAdapter;
    }

    public void previousTrack() {
        if (mCurrentIndex > 1) {
            mCurrentIndex--;
            mTbPlayPause.setChecked(true);
            play();
        }
    }

    public void setPlay(boolean isChecked) {
        if (isChecked) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            } else {
                play();
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
    }

    public void nextTrack() {
        if (mCurrentIndex < 55) {
            mCurrentIndex++;
            mTbPlayPause.setChecked(true);
            play();
        }
    }

    public void setLoop(boolean isChecked) {
        if (isChecked) {
            mediaPlayer.setLooping(true);
        } else {
            mediaPlayer.setLooping(false);
        }
    }

    private void play() {
        clearMediaPlayer();
        int resID = mContext.getResources().getIdentifier("ayah_" + mCurrentIndex,
                "raw", "jmapps.supplicationsfromquran");

        handler = new Handler();
        mediaPlayer = MediaPlayer.create(mContext, resID);
        handler.postDelayed(updateTimeTask, 100);
        int durationTrackTime = mediaPlayer.getDuration();
        mAudioProgress.setMax(durationTrackTime);
        mAudioProgress.setOnSeekBarChangeListener(this);
        mRvListMain.smoothScrollToPosition(mCurrentIndex - 1);
        mMainContentAdapter.setLinePlaying(mCurrentIndex);
        lastState();
        mediaPlayer.start();
    }

    private void lastState() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mCurrentIndex < 55) {
                    mCurrentIndex++;
                    play();
                    mediaPlayer.setOnCompletionListener(this);
                } else {
                    mAudioProgress.setProgress(0);
                    mTbPlayPause.setChecked(false);
                    mMainContentAdapter.setLinePlaying(56);
                }
            }
        });
    }

    private final Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                mAudioProgress.setProgress(currentPosition);
            }
            try {
                handler.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mediaPlayer != null && fromUser) {
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void clearMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playOnly(int position) {
        clearMediaPlayer();
        mCurrentIndex = position;
        int resID = mContext.getResources().getIdentifier("ayah_" + mCurrentIndex,
                "raw", "jmapps.supplicationsfromquran");

        handler = new Handler();
        mediaPlayer = MediaPlayer.create(mContext, resID);
        handler.postDelayed(updateTimeTask, 100);
        int durationTrackTime = mediaPlayer.getDuration();
        mAudioProgress.setMax(durationTrackTime);
        mAudioProgress.setOnSeekBarChangeListener(this);
        mRvListMain.smoothScrollToPosition(mCurrentIndex);
        mMainContentAdapter.setLinePlaying(mCurrentIndex);
        lastState();
        mediaPlayer.start();
    }
}
package jmapps.supplicationsfromquran.Player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;

public class MainPresenterImpl implements MainContract.Presenter,
        MainContract.GetTrackIterator.CurrentTrack, MediaPlayer.OnCompletionListener,
        SeekBar.OnSeekBarChangeListener {

    private Context context;

    private MainContract.MainView mainView;
    private MainContract.GetTrackIterator getTrackIterator;

    private Handler handler;
    private MediaPlayer mediaPlayer;
    private SeekBar audioProgress;

    public MainPresenterImpl(Context context,
                             MainContract.MainView mainView,
                             MainContract.GetTrackIterator getTrackIterator,
                             SeekBar audioProgress) {
        this.context = context;
        this.mainView = mainView;
        this.getTrackIterator = getTrackIterator;
        this.audioProgress = audioProgress;
    }

    @Override
    public void playTrack(int index) {
        if (getTrackIterator != null) {
            getTrackIterator.getCurrentTrack(this, index);
            mediaPlayer.start();

            if (mainView != null) {
                mainView.setPlayState(true);
            }
        }
    }

    @Override
    public void playTrackChecked(boolean isChecked) {
        if (getTrackIterator != null) {
            if (isChecked) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                } else {
                    getTrackIterator.getCurrentTrack(this, 0);
                    mediaPlayer.start();
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
            }
        }

        if (mainView != null) {
            mainView.setPlayState(isChecked);
        }
    }

    @Override
    public void loopTrack(boolean isChecked) {
        if (isChecked) {
            mediaPlayer.setLooping(true);
        } else {
            mediaPlayer.setLooping(false);
        }

        if (mainView != null) {
            mainView.setLoopState(isChecked);
        }
    }

    @Override
    public void onCurrentTrack(String currentTrackName) {
        initPlayer(currentTrackName);
    }

    private void initPlayer(String trackName) {
        clearPlayer();
        int resID = context.getResources().getIdentifier(trackName,
                "raw", "jmapps.supplicationsfromquran");
        handler = new Handler();
        mediaPlayer = MediaPlayer.create(context, resID);
        handler.postDelayed(updateTimeTask, 100);
        int durationTrackTime = mediaPlayer.getDuration();
        audioProgress.setMax(durationTrackTime);
        audioProgress.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }


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

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mainView != null) {
            mainView.setOnCompletion(true);
        }
    }

    @Override
    public void clearPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void destroy() {
        clearPlayer();
        mainView = null;
    }

    private final Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                audioProgress.setProgress(currentPosition);
            }
            try {
                handler.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
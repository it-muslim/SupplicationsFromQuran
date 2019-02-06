package jmapps.supplicationsfromquran.Player;

public interface MainContract {
    interface MainView {
        void setPlayState(boolean isChecked);

        void setLoopState(boolean isChecked);

        void setOnCompletion(boolean isChecked);
    }

    interface GetTrackIterator {

        interface CurrentTrack {
            void onCurrentTrack(String currentTrackName);
        }

        void getCurrentTrack(CurrentTrack currentTrack, int index);
    }

    interface Presenter {

        void playTrack(int index);

        void playTrackChecked(boolean isChecked);

        void loopTrack(boolean isChecked);

        void clearPlayer();

        void destroy();
    }
}
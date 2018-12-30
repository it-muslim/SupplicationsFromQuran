package jmapps.supplicationsfromquran.Player;

public interface MainContract {
    interface MainView {
        void playState(boolean isChecked);

        void loopState(boolean isChecked);
    }

    interface Presenter {
        void play(boolean isChecked);

        void loop(boolean isChecked);
    }
}

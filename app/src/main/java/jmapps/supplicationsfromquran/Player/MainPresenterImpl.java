package jmapps.supplicationsfromquran.Player;

public class MainPresenterImpl implements MainContract.Presenter {

    private final MainContract.MainView mainView;

    public MainPresenterImpl(MainContract.MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void play(boolean isChecked) {
        mainView.playState(isChecked);
    }

    @Override
    public void loop(boolean isChecked) {
        mainView.loopState(isChecked);
    }
}

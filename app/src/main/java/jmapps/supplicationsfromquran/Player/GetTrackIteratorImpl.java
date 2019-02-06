package jmapps.supplicationsfromquran.Player;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import jmapps.supplicationsfromquran.Adapter.MainContentAdapter;
import jmapps.supplicationsfromquran.Model.MainContentModel;

public class GetTrackIteratorImpl implements MainContract.GetTrackIterator {

    private List<MainContentModel> model;
    private MainContentAdapter adapter;
    private RecyclerView mainList;

    public GetTrackIteratorImpl(List<MainContentModel> model,
                                MainContentAdapter adapter,
                                RecyclerView mainList) {
        this.model = model;
        this.adapter = adapter;
        this.mainList = mainList;
    }

    @Override
    public void getCurrentTrack(CurrentTrack currentTrack, int index) {
        currentTrack.onCurrentTrack(getCurrentTrackName(index));
    }

    // Текущий трек
    private String getCurrentTrackName(int index) {
        adapter.setLinePlaying(index);
        mainList.smoothScrollToPosition(index);
        return model.get(index).getStrNameAudio();
    }
}
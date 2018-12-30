package jmapps.supplicationsfromquran.Model;

public class PlayListContentModel {

    private String strIdPosition;
    private final String strPlayListItem;

    public PlayListContentModel(String strIdPosition, String strPlayListItem) {
        this.strIdPosition = strIdPosition;
        this.strPlayListItem = strPlayListItem;
    }

    public String getStrIdPosition() {
        return strIdPosition;
    }

    public String getStrPlayListItem() {
        return strPlayListItem;
    }
}

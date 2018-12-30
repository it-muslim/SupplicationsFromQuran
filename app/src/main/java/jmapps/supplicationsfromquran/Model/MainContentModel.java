package jmapps.supplicationsfromquran.Model;

public class MainContentModel {

    private final String strIdPosition;
    private String strAyahArabic;
    private String strAyahTranslation;
    private String strNameAudio;
    private String strNameDua;

    public MainContentModel(String strIdPosition,
                            String strAyahArabic,
                            String strAyahTranslation,
                            String strNameAudio,
                            String strNameDua) {
        this.strIdPosition = strIdPosition;
        this.strAyahArabic = strAyahArabic;
        this.strAyahTranslation = strAyahTranslation;
        this.strNameAudio = strNameAudio;
        this.strNameDua = strNameDua;
    }

    public String getStrIdPosition() {
        return strIdPosition;
    }

    public String getStrAyahArabic() {
        return strAyahArabic;
    }

    public String getStrAyahTranslation() {
        return strAyahTranslation;
    }

    public String getStrNameAudio() {
        return strNameAudio;
    }

    public String getStrNameDua() {
        return strNameDua;
    }
}
package jmapps.supplicationsfromquran.Model;

import jmapps.supplicationsfromquran.R;

public class ListAppModel {

    private final int iconApp;
    private final String strNameApp;
    private final String strLinkApp;

    public static final ListAppModel[] listAppModel = {
            new ListAppModel(R.drawable.notes_small, "Заметки",
                    "https://play.google.com/store/apps/details?id=jmapps.simplenotes"),
            new ListAppModel(R.drawable.supplications_small, "Мольбы из Корана",
                    "https://play.google.com/store/apps/details?id=jmapps.supplicationsfromquran"),
            new ListAppModel(R.drawable.arabicinyourhands_small, "Арабский в твоих руках. Том 1",
                    "https://play.google.com/store/apps/details?id=jmapps.arabicinyourhands"),
            new ListAppModel(R.drawable.lessons_small, "Уроки Рамадана",
                    "https://play.google.com/store/apps/details?id=jmapps.lessonsoframadan"),
            new ListAppModel(R.drawable.fortress_small, "Крепость мусульманина",
                    "https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslim"),
            new ListAppModel(R.drawable.thenames_small, "Толкование прекрасных имён Аллаха",
                    "https://play.google.com/store/apps/details?id=jmapps.thenamesof"),
            new ListAppModel(R.drawable.strength_small, "Сила воли",
                    "https://play.google.com/store/apps/details?id=jmapps.strengthofwill"),
            new ListAppModel(R.drawable.questions_small, "200 вопросов по вероучению Ислама",
                    "https://play.google.com/store/apps/details?id=jmapps.questions200"),
            new ListAppModel(R.drawable.hadith_small, "40 хадисов имама ан-Навави",
                    "https://play.google.com/store/apps/details?id=jmapps.hadith40"),

    };

    private ListAppModel(int iconApp, String strNameApp, String strLinkApp) {
        this.iconApp = iconApp;
        this.strNameApp = strNameApp;
        this.strLinkApp = strLinkApp;
    }

    public int getIconApp() {
        return iconApp;
    }

    public String getStrNameApp() {
        return strNameApp;
    }

    public String getStrLinkApp() {
        return strLinkApp;
    }
}
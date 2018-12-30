package jmapps.supplicationsfromquran.DBSetup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import jmapps.supplicationsfromquran.Model.MainContentModel;
import jmapps.supplicationsfromquran.Model.PlayListContentModel;
import jmapps.supplicationsfromquran.R;

public class SQLiteOpenHelperMainList extends SQLiteOpenHelper {

    private static final int DBVersion = 4;

    private static final String TableName = "Table_of_contents";

    private static final String idPosition = "_id";
    private static final String ContentAyah = "ContentAyah";
    private static final String ContentTranslate = "ContentTranslation";
    private static final String NameAudio = "NameAudio";
    private static final String NameDua = "NameDua";

    private static final String TableOfNames = "CREATE TABLE " + TableName +
            "(" + idPosition + " " + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ContentAyah + " TEXT, " + ContentAyah + " TEXT, "
            + NameAudio + " TEXT, " + NameDua + " TEXT)";

    private static final String[] mColumnsContent = {idPosition, ContentAyah, ContentTranslate, NameAudio, NameDua};

    public SQLiteOpenHelperMainList(Context context) {
        super(context, context.getString(R.string.data_base_name), null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TableOfNames);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableName);
        this.onCreate(sqLiteDatabase);
    }

    public List<MainContentModel> getMainListContent() {

        SQLiteDatabase dbOnlyContent = this.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = dbOnlyContent.query(TableName,
                mColumnsContent,
                null, null,
                null, null,
                null, null);

        List<MainContentModel> allMainListContent = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allMainListContent.add(new MainContentModel
                        (cursor.getString(cursor.getColumnIndex(idPosition)),
                                cursor.getString(cursor.getColumnIndex(ContentAyah)),
                                cursor.getString(cursor.getColumnIndex(ContentTranslate)),
                                cursor.getString(cursor.getColumnIndex(NameAudio)),
                                cursor.getString(cursor.getColumnIndex(NameDua))));
                cursor.moveToNext();
            }
        }
        return allMainListContent;
    }

    public List<PlayListContentModel> getPlayListContent() {

        SQLiteDatabase dbOnlyContent = this.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = dbOnlyContent.query(TableName,
                mColumnsContent,
                null, null,
                null, null,
                null, null);

        List<PlayListContentModel> allPlayListContent = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allPlayListContent.add(new PlayListContentModel(
                        cursor.getString(cursor.getColumnIndex(idPosition)),
                        cursor.getString(cursor.getColumnIndex(NameDua))));
                cursor.moveToNext();
            }
        }
        return allPlayListContent;
    }
}
package jmapps.supplicationsfromquran.DBSetup;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import jmapps.supplicationsfromquran.R;

public class DBAssetHelper extends SQLiteAssetHelper {

    private static final int DBVersion = 4;

    public DBAssetHelper(Context context) {
        super(context, context.getString(R.string.data_base_name), null, DBVersion);

        setForcedUpgrade(DBVersion);
    }
}
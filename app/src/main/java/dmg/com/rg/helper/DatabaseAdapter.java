package dmg.com.rg.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

import dmg.com.rg.model.MyMenu;

/**
 * Created by Star on 10/26/16.
 */

public class DatabaseAdapter {

    protected final String TAG = this.getClass().getSimpleName();
    public static final String MENU_TABLE = "menu";
    public static final String HOME_TABLE = "home";
    public static final String GALLERY_TABLE = "gallery";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public DatabaseAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public DatabaseAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DatabaseAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void executeQuery(String sql) {
        open();
        try {
            mDb.execSQL(sql);

        } catch (SQLException mSQLException) {
            Log.e(TAG, "executeQuery >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getQueryData(String sql) {
        open();
        try {
            return mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getQueryData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public long writeMenu(ContentValues values) {

        String whereClause = String.format("title = '%s'", values.get(MyMenu.TITLE));
        String query = String.format("SELECT * FROM %s WHERE %s", MENU_TABLE, whereClause);

        open();
        try {
            long returnValue;

            Cursor cur = mDb.rawQuery(query, null);
            if (cur.moveToFirst()) {
                returnValue = mDb.update(MENU_TABLE, values, whereClause, null);
                if (returnValue < 0) {
                    Log.d("Error", "Writing Menu on DB!");
                    return -1;
                }
            }
            else {
                returnValue = mDb.insert(MENU_TABLE, null, values);
                if (returnValue < 0) {
                    Log.d("Error", "Writing Menu on DB!");
                    return -1;
                }
            }
            close();

            return returnValue;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getQueryData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getMenus() {
        String query = String.format("SELECT * FROM %s", MENU_TABLE);
        return getQueryData(query);
    }

    public boolean removeMenu() {
        open();
        try {
            if (mDb.delete(MENU_TABLE, "1", null) < 0) {
                Log.d("Error", "Delete contact on DB!");
            }
            close();

            return true;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getQueryData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public long writeHome(ContentValues values) {

        String whereClause = String.format("title = '%s'", values.get(MyMenu.TITLE));
        String query = String.format("SELECT * FROM %s WHERE %s", HOME_TABLE, whereClause);

        open();
        try {
            long returnValue;

            Cursor cur = mDb.rawQuery(query, null);
            if (cur.moveToFirst()) {
                returnValue = mDb.update(HOME_TABLE, values, whereClause, null);
                if (returnValue < 0) {
                    Log.d("Error", "Writing Menu on DB!");
                    return -1;
                }
            }
            else {
                returnValue = mDb.insert(HOME_TABLE, null, values);
                if (returnValue < 0) {
                    Log.d("Error", "Writing Menu on DB!");
                    return -1;
                }
            }
            close();

            return returnValue;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getQueryData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getHomes() {
        String query = String.format("SELECT * FROM %s", HOME_TABLE);
        return getQueryData(query);
    }

    public boolean removeHome() {
        open();
        try {
            if (mDb.delete(HOME_TABLE, "1", null) < 0) {
                Log.d("Error", "Delete contact on DB!");
            }
            close();

            return true;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getQueryData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public long writeGallery(ContentValues values) {

        open();
        try {
            long returnValue;

            returnValue = mDb.insert(GALLERY_TABLE, null, values);
            if (returnValue < 0) {
                Log.d("Error", "Writing Menu on DB!");
                return -1;
            }

            close();

            return returnValue;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getQueryData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getGallerys() {
        String query = String.format("SELECT * FROM %s", GALLERY_TABLE);
        return getQueryData(query);
    }

    public boolean removeGallery() {
        open();
        try {
            if (mDb.delete(GALLERY_TABLE, "1", null) < 0) {
                Log.d("Error", "Delete contact on DB!");
            }
            close();

            return true;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getQueryData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public void close() {
        mDbHelper.close();
    }

}

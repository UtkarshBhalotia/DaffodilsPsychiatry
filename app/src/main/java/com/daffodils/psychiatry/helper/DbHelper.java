package com.daffodils.psychiatry.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.io.File;

public class DbHelper {

    public static final String DATABASE_NAME = "Daffodils.db";
    public static final int DATABASE_VERSION = 1;
    public static final String UNIQUE_ID = "_uniqueid";
    public static final String TABLE_LOGIN = "_login_table";

    // Constants for Login Table
    public static final String USERNAME = "_username";
    public static final String PASSWORD = "_password";
    public static final String NAME = "_name";
    public static final String MOBILE = "_mobile";
    public static final String USER_ID = "_userid";
    public static final String MODULE_ID = "_moduleid";


    public static final String LOGINTABLE = ""
            + "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN
            + " (" + UNIQUE_ID + " integer primary key autoincrement,"
            + USERNAME + " string,"
            + PASSWORD + " string,"
            + NAME + " string,"
            + MOBILE + " string,"
            + USER_ID + " string,"
            + MODULE_ID + " String ) ;";


    public void insert_LoginTime_into_database(String Username, String Password, String Name, String Mobile, String User_id, String Module_id) {
        sqLiteDatabase.execSQL(" INSERT INTO " + TABLE_LOGIN + " ( " + USERNAME + "," + PASSWORD + "," + NAME + "," + MOBILE + "," + USER_ID + "," + MODULE_ID +") VALUES ( '" + Username + "','" + Password + "','" + Name + "','" + Mobile + "','" + User_id + "','" + Module_id + "')");

    }

    public void update_into_LOGIN_Table(String Username, String Password, String Name, String Mobile, String User_id, String Module_id) {
        sqLiteDatabase.execSQL("UPDATE " + TABLE_LOGIN + " SET " + USERNAME + " = '" + Username + "' , " + PASSWORD + " = '" + Password + "' , " + NAME + " = '" + Name + "' , " + MOBILE + " = '" + Mobile + "' , " + USER_ID + " = '" + User_id + "' , " + MODULE_ID + " = '" + Module_id + "' WHERE " + USERNAME + "='" + Username + "'");
    }


    public Cursor fetch_Login_Data() {

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from " + TABLE_LOGIN + "" , null);
        return cursor;
    }

    public Cursor fetch_Login_Data_From_TABLE(String Username, String Password) {

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from " + TABLE_LOGIN + " WHERE " + USERNAME + "='" + Username + "'" + " AND " + PASSWORD + "='" + Password + "'"  , null);
        return cursor;
    }

    public void deleteAllLoginData() {
        sqLiteDatabase.execSQL("DELETE  FROM " + TABLE_LOGIN);

    }


    //******************************************************************************************************


    private Context context;
    private SQLiteHelper sqLiteHelper;
    public SQLiteDatabase sqLiteDatabase;

    public DbHelper(Context c) {
        context = c;
    }
    public boolean checkDataBase() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }
    public DbHelper openToRead() throws android.database.SQLException {

        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        try {

            sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        } catch (Throwable e) {

        }


        return this;
    }

    public DbHelper openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {

            sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        } catch (Exception e) {

        }
        return this;
    }


    public void close() {

        sqLiteHelper.close();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
            sqLiteDatabase.close();
    }

    //************************************* Notifications ****************************************************


    // **********************************Common *************************************************************


    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(LOGINTABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {

            if (oldversion < newversion){

               /* try{
                    String sq1 = "ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COMP_NAME + " string";
                    sqLiteDatabase.execSQL(sq1);
                    String sq2 = "ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + ROLE_ID + " string";
                    sqLiteDatabase.execSQL(sq2);
                    sqLiteDatabase.execSQL("DELETE  FROM " + TABLE_LOGIN);

                    //    Toast.makeText(context, "Added:", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context, "Error :" + e , Toast.LENGTH_LONG).show();

                }*/

            }

        }
    }
}
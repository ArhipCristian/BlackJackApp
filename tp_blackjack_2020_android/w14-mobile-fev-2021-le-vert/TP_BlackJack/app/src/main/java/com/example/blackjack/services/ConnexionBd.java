package com.example.blackjack.services;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
public class ConnexionBd {
    public static int version = 1;
    public  static String nomBD = "blackjackusers.db";
    private  static SQLiteDatabase bd;
    public static  SQLiteDatabase getBd(Context context){
        DbUsersHelper dbUsersHelper = new DbUsersHelper(context, nomBD, null, version);
        bd = dbUsersHelper.getWritableDatabase();
        return bd;
    }
    public static void close(){bd.close();}
}

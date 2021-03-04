package com.example.blackjack.managers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.blackjack.entities.User;
import com.example.blackjack.services.ConnexionBd;

import java.util.ArrayList;
public class UserManager {
    public static boolean add(Context context, User userToAdd) {
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        //inserer
        ContentValues cv = new ContentValues();
        cv.put("username", userToAdd.getUsername());
        cv.put("email", userToAdd.getEmail());
        cv.put("password", userToAdd.getPassword());
        long retour = bd.insert("users", null, cv);
        ConnexionBd.close();
        return retour != -1;
    }
    public static User getById(Context context, int id) {
        User user = null;
        String query = "select * from users where id = ?";
        Cursor cursor = ConnexionBd.getBd(context).rawQuery(query, new String[]{"" + id});
        if (cursor.isBeforeFirst()) {
            user = new User();
            while (cursor.moveToNext()) {
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setBalance(cursor.getDouble(cursor.getColumnIndex("balance")));
            }
        }
        ConnexionBd.close();
        return user;
    }
    public static User getByEmailPassword(Context context, String email, String password) {
        User user = null;
        String query = "select * from users where email = ? AND password = ?";
        Cursor cursor = ConnexionBd.getBd(context).rawQuery(query, new String[]{"" + email, "" + password});
        if (cursor.isBeforeFirst()) {
            user = new User();
            while (cursor.moveToNext()) {
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setBalance(cursor.getDouble(cursor.getColumnIndex("balance")));
            }
        }
        ConnexionBd.close();
        return user;
    }
    public static ArrayList<User> getAll(Context context) {
        ArrayList<User> retour = null;
        String query = "select * from users";
        Cursor cursor = ConnexionBd.getBd(context).rawQuery(query, null);
        if (cursor.isBeforeFirst()) {
            retour = new ArrayList<>();
            while (cursor.moveToNext()) {
                retour.add(new User(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getDouble(cursor.getColumnIndex("balance"))
                ));
            }
        }
        ConnexionBd.close();
        return retour;
    }
    public static boolean addFunds(Context context, int id, double funds) {
        User u = UserManager.getById(context, id);
        Double newFunds = funds + u.getBalance();
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        ContentValues cv = new ContentValues();
        cv.put("balance", newFunds);
        long retour = bd.update("users", cv, "id= ?", new String[]{"" + id});
        ConnexionBd.close();
        return retour != -1;
    }

    public static boolean updateBalance(Context context, int id, double newBalance){
        User u = UserManager.getById(context, id);
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        ContentValues cv = new ContentValues();
        cv.put("balance", newBalance);
        long retour = bd.update("users", cv, "id= ?", new String[]{"" + id});
        ConnexionBd.close();
        return retour != -1;
    }
}

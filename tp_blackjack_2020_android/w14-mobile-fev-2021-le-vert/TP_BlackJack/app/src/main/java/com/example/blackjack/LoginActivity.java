package com.example.blackjack;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackjack.adapters.UserAdapter;
import com.example.blackjack.entities.User;
import com.example.blackjack.managers.UserManager;

import java.util.ArrayList;
public class LoginActivity extends AppCompatActivity {
    TextView tv_testing;
    UserAdapter userAdapter;
    Button btn_login_connect;
    EditText ed_login_email, ed_login_pw;
    String email, pw, test;
    Context ctx;
    ArrayList<User> users;

    TextView tv_sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_login);
        tv_sign_up = findViewById(R.id.tv_sign_up);
        btn_login_connect = findViewById(R.id.btn_connect_login);
        ed_login_email = findViewById(R.id.ed_login_email);
        ed_login_pw = findViewById(R.id.ed_login_pw);
        tv_testing = findViewById(R.id.tv_testing_login);
        users = UserManager.getAll(ctx);
        for(User u : users){
            test+= u.toString();
        }
        tv_testing.setText(test);
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InscritptionActivity.class));
            }
        });

        btn_login_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = ed_login_email.getText().toString();
                pw = ed_login_pw.getText().toString();
                User user = UserManager.getByEmailPassword(ctx, email, pw);

                if(!isNullOrEmpty(user.getUsername())){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", user.getId());
                    editor.commit();
                    tv_testing.setText(user.toString());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "The information provided was not correct, try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void mainActivity(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }

}
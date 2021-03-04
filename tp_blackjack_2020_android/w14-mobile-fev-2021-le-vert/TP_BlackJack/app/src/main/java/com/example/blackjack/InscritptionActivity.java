package com.example.blackjack;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackjack.entities.User;
import com.example.blackjack.managers.UserManager;

import java.util.ArrayList;
public class InscritptionActivity extends AppCompatActivity {

    EditText ed_signup_user, ed_signup_email, ed_signup_pw, ed_signup_pw_confirm;
    CheckBox checkBox_signup;
    Button btn_signup;
    String username, email, password, password_confirm, test;
    User usertoAdd, usertoSend;
    Context ctx;
    TextView tv_testing;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_inscritption);
        ed_signup_user = findViewById(R.id.ed_signup_user);
        ed_signup_email = findViewById(R.id.ed_signup_email);
        ed_signup_pw = findViewById(R.id.ed_signup_pw);
        ed_signup_pw_confirm = findViewById(R.id.ed_signup_pw_confirm);
        btn_signup = findViewById(R.id.btn_signup);
        tv_testing = findViewById(R.id.tv_testing_signup);
        checkBox_signup = findViewById(R.id.checkBox_signup);
        users = UserManager.getAll(ctx);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed_signup_user.getText().toString();
                email = ed_signup_email.getText().toString();
                password = ed_signup_pw.getText().toString();
                password_confirm = ed_signup_pw_confirm.getText().toString();

                if(password.equals(password_confirm) && !password.equals("") && !email.equals("") && !username.equals("") && checkBox_signup.isChecked()){
                    usertoAdd = new User();
                    usertoSend = new User();
                    usertoAdd.setUsername(username);
                    usertoAdd.setEmail(email);
                    usertoAdd.setPassword(password);
                    UserManager.add(ctx,usertoAdd);
                    usertoSend = UserManager.getByEmailPassword(ctx, email,password);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", usertoSend.getId());
                    editor.commit();
                    /*users = UserManager.getAll(ctx);
                    Toast.makeText(getApplicationContext(), "Bonjour", Toast.LENGTH_LONG).show();
                    test ="";
                    for(User u: users){
                        test += u.toString();
                    }
                    tv_testing.setText(test);*/
                    startActivity(new Intent(ctx, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "The passwords don't match or some information is missing,try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        for(User u: users){
            test += u.toString();
        }
        tv_testing.setText(test);


    }
}
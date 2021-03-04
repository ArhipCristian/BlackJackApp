package com.example.blackjack.ui;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.blackjack.LoginActivity;
import com.example.blackjack.MainActivity;
import com.example.blackjack.R;
import com.example.blackjack.entities.User;
import com.example.blackjack.managers.UserManager;
public class ProfileFragment extends Fragment {
    User user_Profile;
    View view;
    TextView tv_username, tv_email, tv_balance;
    Button btn_15_dollars, btn_25_dollars, btn_50_dollars;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_username = view.findViewById(R.id.tv_username_profile);
        tv_email = view.findViewById(R.id.tv_email_profile);
        tv_balance = view.findViewById(R.id.tv_balance);
        btn_15_dollars = view.findViewById(R.id.btn_15_dollars);
        btn_25_dollars = view.findViewById(R.id.btn_25_dollars);
        btn_50_dollars = view.findViewById(R.id.btn_50_dollars);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int id = sharedPreferences.getInt("id", -1);
        user_Profile = UserManager.getById(getContext(), id);
        tv_username.setText(user_Profile.getUsername());
        tv_email.setText(user_Profile.getEmail());
        tv_balance.setText(user_Profile.getBalance() + "$");
        btn_15_dollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.addFunds(getContext(), id, 15);
                user_Profile = UserManager.getById(getContext(),id);
                tv_balance.setText(user_Profile.getBalance() + "$");
            }
        });
        btn_25_dollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.addFunds(getContext(), id, 25);
                user_Profile = UserManager.getById(getContext(),id);
                tv_balance.setText(user_Profile.getBalance() + "$");
            }
        });
        btn_50_dollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.addFunds(getContext(), id, 50);
                user_Profile = UserManager.getById(getContext(),id);
                tv_balance.setText(user_Profile.getBalance() + "$");
            }
        });
        return view;
    }

    public void logoutClick(View view) {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}

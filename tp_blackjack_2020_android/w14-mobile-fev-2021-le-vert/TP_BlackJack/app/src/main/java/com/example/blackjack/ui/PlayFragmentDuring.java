package com.example.blackjack.ui;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.blackjack.R;
import com.example.blackjack.entities.User;
import com.example.blackjack.managers.UserManager;
public class PlayFragmentDuring extends Fragment {
    User user;
    View view;
    Button btn_bet_5, btn_bet_20, btn_bet_50, btn_play;
    TextView tv_bet, tv_balance;
    ImageView img_cancel;
    Double bet, balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_during, container, false);

        return view;


    }

}

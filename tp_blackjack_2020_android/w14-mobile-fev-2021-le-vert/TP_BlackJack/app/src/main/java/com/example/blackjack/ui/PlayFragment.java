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
public class PlayFragment extends Fragment {
    User user_play;
    View view;
    Button btn_bet_5, btn_bet_20, btn_bet_50, btn_play;
    TextView tv_bet, tv_balance_play;
    ImageView img_cancel;
    Double bet, balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play, container, false);
        bet = Double.valueOf(5);
        btn_bet_5 = view.findViewById(R.id.btn_bet_5);
        btn_bet_20 = view.findViewById(R.id.btn_bet_20);
        btn_bet_50 = view.findViewById(R.id.btn_bet_50);
        btn_play = view.findViewById(R.id.btn_start_game);
        tv_bet = view.findViewById(R.id.tv_bet);
        img_cancel = view.findViewById(R.id.img_cancel);
        tv_balance_play = view.findViewById(R.id.tv_balance_play);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int id = sharedPreferences.getInt("id", -1);
        user_play = UserManager.getById(getContext(), id);
        balance = user_play.getBalance();
        tv_balance_play.setText(user_play.getBalance()+ "$");

        btn_bet_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet += 5;
                tv_bet.setText(""+bet + "$");
            }
        });
        btn_bet_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet += 20;
                tv_bet.setText(""+bet + "$");
            }
        });
        btn_bet_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet += 50;
                tv_bet.setText(""+bet + "$");
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                balance -= bet;
                if(balance < 0){
                    balance = Double.valueOf(0);
                }
                tv_balance_play.setText(""+ balance + "$");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("bet", bet.floatValue());
                editor.commit();

                //AJOUT NEW FRAGMENT
                view = inflater.inflate(R.layout.fragment_play_during, container, false);
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet = Double.valueOf(5);
                tv_bet.setText(""+bet +"$");
            }
        });

        return view;


    }

}

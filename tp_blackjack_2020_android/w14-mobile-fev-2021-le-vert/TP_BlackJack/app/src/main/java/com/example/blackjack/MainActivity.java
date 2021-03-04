package com.example.blackjack;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.blackjack.entities.User;
import com.example.blackjack.managers.UserManager;
import com.example.blackjack.ui.PlayFragment;
import com.example.blackjack.ui.ProfileFragment;
import com.example.blackjack.ui.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
public class MainActivity extends AppCompatActivity {
    User user;
    TextView tv_testing_profile;
    RelativeLayout rl;
    LinearLayout ll_main;
    Context context;
    //variables profile
    User user_Profile;
    View view_profile;
    TextView tv_username, tv_email, tv_balance, tv_logout_profile;
    Button btn_15_dollars, btn_25_dollars, btn_50_dollars;
    //Variables play(bet phase)
    User user_play;
    View view;
    Button btn_bet_5, btn_bet_20, btn_bet_50, btn_play;
    TextView tv_bet, tv_balance_play;
    ImageView img_cancel;
    Double bet, balance, initialBalance;
    //Variables play during phase
    TextView tv_dealer_hand, tv_player_hand, tv_balance_during, tv_bet_during, tv_end_message;
    ImageView card_dealer_1, card_dealer_2, card_dealer_3, card_dealer_4, card_dealer_5, card_dealer_6, card_dealer_7, card_dealer_8;
    ImageView card_player_1, card_player_2, card_player_3, card_player_4, card_player_5, card_player_6, card_player_7, card_player_8;
    Button btn_play_hit, btn_play_stand, btn_play_again;
    LinearLayout ll_end_game, ll_money_bet;
    ArrayList<String> cardNames;
    Random randomGenerator;
    int dealer_hand_value, player_hand_value;
    boolean isRunning;
    int cardCounter, dealerCardCounter;
    boolean stillPlayerTurn;
    ArrayList<ImageView> invisible_player_cards, invisible_dealer_cards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        randomGenerator = new Random();
        setContentView(R.layout.activity_main);
        ll_main = findViewById(R.id.linearlayout_container);
        ll_main.addView(createProfile());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int id = sharedPreferences.getInt("id", -1);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ll_main.removeAllViews();
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    ll_main.addView(createProfile());
//                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.nav_play:
//                    selectedFragment = new PlayFragment();
                    ll_main.addView(createPlay());
                    break;
                case R.id.nav_settings:
//                    selectedFragment = new SettingsFragment();
                    ll_main.addView(createSettings());
                    break;
            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
    public View createProfile() {
        ll_main.removeAllViews();
        view_profile = getLayoutInflater().inflate(R.layout.activity_profile, null, false);
        tv_username = view_profile.findViewById(R.id.tv_username_profile);
        tv_email = view_profile.findViewById(R.id.tv_email_profile);
        tv_balance = view_profile.findViewById(R.id.tv_balance);
        btn_15_dollars = view_profile.findViewById(R.id.btn_15_dollars);
        btn_25_dollars = view_profile.findViewById(R.id.btn_25_dollars);
        btn_50_dollars = view_profile.findViewById(R.id.btn_50_dollars);
        tv_logout_profile = view_profile.findViewById(R.id.tv_logout_profile);
        tv_logout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int id = sharedPreferences.getInt("id", -1);
        user_Profile = UserManager.getById(context, id);
        tv_username.setText(user_Profile.getUsername());
        tv_email.setText(user_Profile.getEmail());
        tv_balance.setText(user_Profile.getBalance() + "$");
        btn_15_dollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.addFunds(context, id, 15);
                user_Profile = UserManager.getById(context, id);
                tv_balance.setText(user_Profile.getBalance() + "$");
            }
        });
        btn_25_dollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.addFunds(context, id, 25);
                user_Profile = UserManager.getById(context, id);
                tv_balance.setText(user_Profile.getBalance() + "$");
            }
        });
        btn_50_dollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.addFunds(context, id, 50);
                user_Profile = UserManager.getById(context, id);
                tv_balance.setText(user_Profile.getBalance() + "$");
            }
        });
        return view_profile;
    }
    public View createPlay() {
        view = getLayoutInflater().inflate(R.layout.fragment_play, null, false);
        bet = Double.valueOf(0);
        btn_bet_5 = view.findViewById(R.id.btn_bet_5);
        btn_bet_20 = view.findViewById(R.id.btn_bet_20);
        btn_bet_50 = view.findViewById(R.id.btn_bet_50);
        btn_play = view.findViewById(R.id.btn_start_game);
        tv_bet = view.findViewById(R.id.tv_bet);
        img_cancel = view.findViewById(R.id.img_cancel);
        tv_balance_play = view.findViewById(R.id.tv_balance_play);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int id = sharedPreferences.getInt("id", -1);
        user_play = UserManager.getById(context, id);
        balance = user_play.getBalance();
        initialBalance = balance;
        tv_bet.setText(bet + "$");
        balance -= bet;
        if(balance < 0){
            balance = Double.valueOf(0);
        }
        tv_balance_play.setText(balance + "$");
        btn_bet_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet += 5;
                balance -= 5;
                if (balance < 0) {
                    balance = Double.valueOf(0);
                    bet = initialBalance;
                }
                tv_bet.setText("" + bet + "$");
                tv_balance_play.setText("" + balance + "$");
            }
        });
        btn_bet_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet += 20;
                balance -= 20;
                if (balance < 0) {
                    balance = Double.valueOf(0);
                    bet = initialBalance;
                }
                tv_bet.setText("" + bet + "$");
                tv_balance_play.setText("" + balance + "$");
            }
        });
        btn_bet_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet += 50;
                balance -= 50;
                if (balance < 0) {
                    balance = Double.valueOf(0);
                    bet = initialBalance;
                }
                tv_bet.setText("" + bet + "$");
                tv_balance_play.setText("" + balance + "$");
            }
        });
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                balance -= bet;
                if (balance < 0) {
                    balance = Double.valueOf(0);
                }
                tv_balance_play.setText("" + balance + "$");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("bet", bet.floatValue());
                editor.commit();
                createPlayDuring();
            }
        });
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet = Double.valueOf(5);
                tv_bet.setText("" + bet + "$");
                balance = initialBalance -5;
                tv_balance_play.setText("" + balance + "$");
            }
        });
        return view;
    }
    public void createPlayDuring() {
        ll_main.removeAllViews();
        dealer_hand_value = 0;
        player_hand_value = 0;
        isRunning = true;
        view = getLayoutInflater().inflate(R.layout.activity_play_during, null, false);
        tv_dealer_hand = view.findViewById(R.id.tv_dealer_hand);
        tv_player_hand = view.findViewById(R.id.tv_player_hand);
        tv_balance_during = view.findViewById(R.id.tv_balance_play_during);
        tv_bet_during = view.findViewById(R.id.tv_bet_during);
        btn_play_hit = view.findViewById(R.id.btn_play_hit);
        btn_play_stand = view.findViewById(R.id.btn_play_stand);
        btn_play_again = view.findViewById(R.id.btn_play_again);
        tv_end_message = view.findViewById(R.id.tv_end_message);
        ll_end_game = view.findViewById(R.id.linear_layout_end);
        ll_money_bet = view.findViewById(R.id.ll_money_bet);
        tv_balance_during.setText("" + balance + "$");
        tv_bet_during.setText("" + bet + "$");
        //reset game
        initializeCardsVariables();
        initalizeCardNames();
        stillPlayerTurn = true;
        cardCounter = 1;
        dealerCardCounter = 0;
        //dealer turn
        String card = randomCard();
        try {
            card_dealer_1.setImageDrawable(Drawable.createFromStream(context.getAssets().open("cards/" + card), null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (card.charAt(0) == 'A') {
            dealer_hand_value += 11;
        } else if ((card.charAt(0) == '1') || (card.charAt(0) == 'J') || (card.charAt(0) == 'K') || (card.charAt(0) == 'Q')) {
            dealer_hand_value += 10;
        } else {
            char c = card.charAt(0);
            int numeric = Integer.parseInt(String.valueOf(c));
            dealer_hand_value += numeric;
        }
        tv_dealer_hand.setText("" + dealer_hand_value);
        //player turn
        String card_player = randomCard();
        try {
            card_player_1.setImageDrawable(Drawable.createFromStream(context.getAssets().open("cards/" + card_player), null));
            card_player_1.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (card_player.charAt(0) == 'A') {
            player_hand_value += 11;
        } else if ((card_player.charAt(0) == '1') || (card_player.charAt(0) == 'J') || (card_player.charAt(0) == 'K') || (card_player.charAt(0) == 'Q')) {
            player_hand_value += 10;
        } else {
            char c = card_player.charAt(0);
            int numeric = Integer.parseInt(String.valueOf(c));
            player_hand_value += numeric;
        }
        tv_player_hand.setText("" + player_hand_value);
        card_player = randomCard();
        try {
            card_player_2.setImageDrawable(Drawable.createFromStream(context.getAssets().open("cards/" + card_player), null));
            card_player_2.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (card_player.charAt(0) == 'A') {
            player_hand_value += 11;
        } else if ((card_player.charAt(0) == '1') || (card_player.charAt(0) == 'J') || (card_player.charAt(0) == 'K') || (card_player.charAt(0) == 'Q')) {
            player_hand_value += 10;
        } else {
            char c = card_player.charAt(0);
            int numeric = Integer.parseInt(String.valueOf(c));
            player_hand_value += numeric;
        }
        tv_player_hand.setText("" + player_hand_value);
        //button on click listeners
        btn_play_hit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardCounter++;
                if (cardCounter <= 7) {
                    String card_player = randomCard();
                    try {
                        invisible_player_cards.get(cardCounter).setImageDrawable(Drawable.createFromStream(context.getAssets().open("cards/" + card_player), null));
                        invisible_player_cards.get(cardCounter).setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (card_player.charAt(0) == 'A') {
                        player_hand_value += 11;
                    } else if ((card_player.charAt(0) == '1') || (card_player.charAt(0) == 'J') || (card_player.charAt(0) == 'K') || (card_player.charAt(0) == 'Q')) {
                        player_hand_value += 10;
                    } else {
                        char c = card_player.charAt(0);
                        int numeric = Integer.parseInt(String.valueOf(c));
                        player_hand_value += numeric;
                    }
                    tv_player_hand.setText("" + player_hand_value);
                    if (player_hand_value > 21) {
                        endOfGame();
                    }
                }
            }
        });
        btn_play_stand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stillPlayerTurn = false;
                dealerTurn();
            }
        });
//        while (player_hand_value <= 21 && dealer_hand_value <= 17 && stillPlayerTurn) {
        ll_main.addView(view);
        if (player_hand_value == 21) {
            endOfGame();
        }
//        }
    }
    public View createSettings() {
        view = getLayoutInflater().inflate(R.layout.activity_settings, null, false);
        return view;
    }
    public void initializeCardsVariables() {
        //dealer cards
        card_dealer_1 = view.findViewById(R.id.card_dealer_1);
        card_dealer_2 = view.findViewById(R.id.card_dealer_2);
        card_dealer_3 = view.findViewById(R.id.card_dealer_3);
        card_dealer_4 = view.findViewById(R.id.card_dealer_4);
        card_dealer_5 = view.findViewById(R.id.card_dealer_5);
        card_dealer_6 = view.findViewById(R.id.card_dealer_6);
        card_dealer_7 = view.findViewById(R.id.card_dealer_7);
        card_dealer_8 = view.findViewById(R.id.card_dealer_8);
        invisible_dealer_cards = new ArrayList<ImageView>();
        invisible_dealer_cards.add(card_dealer_1);
        invisible_dealer_cards.add(card_dealer_2);
        invisible_dealer_cards.add(card_dealer_3);
        invisible_dealer_cards.add(card_dealer_4);
        invisible_dealer_cards.add(card_dealer_5);
        invisible_dealer_cards.add(card_dealer_6);
        invisible_dealer_cards.add(card_dealer_7);
        invisible_dealer_cards.add(card_dealer_8);
        //player cards
        card_player_1 = view.findViewById(R.id.card_player_1);
        card_player_2 = view.findViewById(R.id.card_player_2);
        card_player_3 = view.findViewById(R.id.card_player_3);
        card_player_4 = view.findViewById(R.id.card_player_4);
        card_player_5 = view.findViewById(R.id.card_player_5);
        card_player_6 = view.findViewById(R.id.card_player_6);
        card_player_7 = view.findViewById(R.id.card_player_7);
        card_player_8 = view.findViewById(R.id.card_player_8);
        invisible_player_cards = new ArrayList<ImageView>();
        invisible_player_cards.add(card_player_1);
        invisible_player_cards.add(card_player_2);
        invisible_player_cards.add(card_player_3);
        invisible_player_cards.add(card_player_4);
        invisible_player_cards.add(card_player_5);
        invisible_player_cards.add(card_player_6);
        invisible_player_cards.add(card_player_7);
        invisible_player_cards.add(card_player_8);
    }
    public void initalizeCardNames() {
        cardNames = new ArrayList<String>();
        cardNames.add("2C.png");
        cardNames.add("2D.png");
        cardNames.add("2H.png");
        cardNames.add("2S.png");
        cardNames.add("3C.png");
        cardNames.add("3D.png");
        cardNames.add("3H.png");
        cardNames.add("3S.png");
        cardNames.add("4C.png");
        cardNames.add("4D.png");
        cardNames.add("4H.png");
        cardNames.add("4S.png");
        cardNames.add("5C.png");
        cardNames.add("5D.png");
        cardNames.add("5H.png");
        cardNames.add("5S.png");
        cardNames.add("6C.png");
        cardNames.add("6D.png");
        cardNames.add("6H.png");
        cardNames.add("6S.png");
        cardNames.add("7C.png");
        cardNames.add("7D.png");
        cardNames.add("7H.png");
        cardNames.add("7S.png");
        cardNames.add("8C.png");
        cardNames.add("8D.png");
        cardNames.add("8H.png");
        cardNames.add("8S.png");
        cardNames.add("9C.png");
        cardNames.add("9D.png");
        cardNames.add("9H.png");
        cardNames.add("9S.png");
        cardNames.add("10C.png");
        cardNames.add("10D.png");
        cardNames.add("10H.png");
        cardNames.add("10S.png");
        cardNames.add("AC.png");
        cardNames.add("AD.png");
        cardNames.add("AH.png");
        cardNames.add("AS.png");
        cardNames.add("JC.png");
        cardNames.add("JD.png");
        cardNames.add("JH.png");
        cardNames.add("JS.png");
        cardNames.add("KC.png");
        cardNames.add("KD.png");
        cardNames.add("KH.png");
        cardNames.add("KS.png");
        cardNames.add("QC.png");
        cardNames.add("QD.png");
        cardNames.add("QH.png");
        cardNames.add("QS.png");
    }
    public String randomCard() {
        int index = randomGenerator.nextInt(cardNames.size());
        String cardPicked = cardNames.get(index);
        cardNames.remove(index);
        return cardPicked;
    }
    public void dealerTurn() {
        while (dealer_hand_value < 17 && dealerCardCounter < 7) {
            dealerCardCounter++;
            if (dealerCardCounter <= 7) {
                String card = randomCard();
                try {
                    invisible_dealer_cards.get(dealerCardCounter).setImageDrawable(Drawable.createFromStream(context.getAssets().open("cards/" + card), null));
                    invisible_dealer_cards.get(dealerCardCounter).setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (card.charAt(0) == 'A') {
                    dealer_hand_value += 11;
                } else if ((card.charAt(0) == '1') || (card.charAt(0) == 'J') || (card.charAt(0) == 'K') || (card.charAt(0) == 'Q')) {
                    dealer_hand_value += 10;
                } else {
                    char c = card.charAt(0);
                    int numeric = Integer.parseInt(String.valueOf(c));
                    dealer_hand_value += numeric;
                }
                tv_dealer_hand.setText("" + dealer_hand_value);
                if (dealer_hand_value > 16) {
                    endOfGame();
                }
            }
        }
    }
    public void endOfGame() {
        btn_play_stand.setVisibility(View.INVISIBLE);
        btn_play_hit.setVisibility(View.INVISIBLE);
        tv_end_message.setVisibility(View.VISIBLE);
        btn_play_again.setVisibility(View.VISIBLE);
        ll_end_game.setVisibility(View.VISIBLE);
        ll_money_bet.setVisibility(View.GONE);
        if (player_hand_value == 21) {
            tv_end_message.setText("You have blackjack!" + bet * 2.5 + "$");
            UserManager.updateBalance(context, user_play.getId(), balance + (bet * 2.5));
        } else if ((dealer_hand_value > 21) || (player_hand_value > dealer_hand_value && player_hand_value <= 21)) {
            tv_end_message.setText("You won!" + bet * 2 + "$");
            UserManager.updateBalance(context, user_play.getId(), balance + (bet * 2));
        } else if (player_hand_value == dealer_hand_value) {
            tv_end_message.setText("It's a push!" + bet + "$");
            UserManager.updateBalance(context, user_play.getId(), balance + bet);
        } else if ((dealer_hand_value > player_hand_value) || (player_hand_value > 21)) {
            tv_end_message.setText("You lost!");
            UserManager.updateBalance(context, user_play.getId(), balance);
        }
        btn_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_main.removeAllViews();
                ll_main.addView(createPlay());
            }
        });
    }
}
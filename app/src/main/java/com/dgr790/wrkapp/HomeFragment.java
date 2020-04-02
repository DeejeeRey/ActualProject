package com.dgr790.wrkapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ProgressBar pb;
    private Button btnStart, btnStop, btnShowInfo, btnHideInfo;
    private EditText etTime;
    private TextView tvTimer, tvMessage;
    private CountDownTimer timer;
    private ImageView ivBackground;
    private int mins;
    private int score;

    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private int dbScore;
    private int dbTimes = 0;
    private boolean timerOn;
    private HashMap<String, String> userHashMap;
    private DatabaseReference currentDB;

    private boolean dataAdded;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        pb = (ProgressBar) v.findViewById(R.id.progressBar);
        btnStart = (Button) v.findViewById(R.id.btnStart);
        btnStop = (Button) v.findViewById(R.id.btnStop);
        etTime = (EditText) v.findViewById(R.id.etTime);
        tvTimer = (TextView) v.findViewById(R.id.tvTimer);
        btnShowInfo = (Button) v.findViewById(R.id.btnShowInfo);
        btnHideInfo = (Button) v.findViewById(R.id.btnHideInfo);
        tvMessage = (TextView) v.findViewById(R.id.tvMessage);
        ivBackground = (ImageView) v.findViewById(R.id.ivBackground);

        rotatePB();

        btnStop.setVisibility(v.INVISIBLE);
        btnHideInfo.setVisibility(v.INVISIBLE);
        ivBackground.setVisibility(v.INVISIBLE);
        tvMessage.setVisibility(getView().INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        timerOn = false;


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeString = etTime.getText().toString();
                if (timeString.isEmpty()) {
                    showMessage("Enter a time to set");
                } else {
                    mins = Integer.parseInt(timeString);
                    startTimer();

                }

            }
        });

        btnShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo();
            }
        });


        return v;
    }

    private void showInfo() {
        btnShowInfo.setVisibility(getView().INVISIBLE);
        btnHideInfo.setVisibility(getView().VISIBLE);
        ivBackground.setVisibility(getView().VISIBLE);
        tvMessage.setVisibility(getView().VISIBLE);

        tvMessage.setText("Welcome to WRK - the app to help you study!\n\n" +
                "It's simple, set the amount of time you would like to study and get to work!\n\n" +
                "Be careful not to leave the app, or your hard work won't be rewarded.\n\n" +
                "Compete against your friends to get the highest score!\n\n" +
                "Happy Studying! Now get to 'WRK'...");

        btnHideInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShowInfo.setVisibility(getView().VISIBLE);
                btnHideInfo.setVisibility(getView().INVISIBLE);
                ivBackground.setVisibility(getView().INVISIBLE);
                tvMessage.setVisibility(getView().INVISIBLE);
            }
        });


    }


    private void rotatePB() {
        Animation ani = new RotateAnimation(0.0f, 90.0f, 500f, 550f);
        ani.setFillAfter(true);
        pb.startAnimation(ani);
    }

    private void startTimer() {

        timerOn = true;

        btnStart.setVisibility(getView().GONE);
        btnStop.setVisibility(getView().VISIBLE);

        timer = new CountDownTimer(60*mins*1000, 500) {


            @Override
            public void onTick(long millisUntilFinished) {
                pb.setMax(60*mins);
                long seconds = millisUntilFinished/1000;
                pb.setProgress((int)seconds);
                tvTimer.setText(String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60));
                pb.setMax(60*mins);
            }

            @Override
            public void onFinish() {
               if(tvTimer.getText().equals("00:00")) {
                   timerOn = false;
                   score = mins;
                   updateDB();


                   timer.cancel();
                   btnStop.setVisibility(getView().GONE);
                   btnStart.setVisibility(getView().VISIBLE);
               }
            }
        };
        timer.start();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerOn = false;
                timer.cancel();
                pb.setProgress(60*mins);
                tvTimer.setText("00:00");
                btnStop.setVisibility(getView().GONE);
                btnStart.setVisibility(getView().VISIBLE);
            }
        });

    }

    private void updateDB() {
        dataAdded = false;

        currentDB = FirebaseDatabase.getInstance().getReference().child("Users");


        currentDB.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataAdded) {
                    userHashMap = (HashMap<String, String>) dataSnapshot.getValue();

                    dbScore = Integer.valueOf(String.valueOf(userHashMap.get("Score")));
                    dbTimes = Integer.valueOf(String.valueOf(userHashMap.get("Times")));

                    score = score + dbScore;
                    Map newScore = new HashMap();
                    newScore.put("Score", score);
                    currentDB.child(userID).updateChildren(newScore);

                    dbTimes++;
                    Map newTimes = new HashMap();
                    newTimes.put("Times", dbTimes);
                    currentDB.child(userID).updateChildren(newTimes);
                    dataAdded = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (timerOn) {
            timerOn = false;
            timer.cancel();
            pb.setProgress(60*mins);
            tvTimer.setText("00:00");
            btnStop.setVisibility(getView().GONE);
            btnStart.setVisibility(getView().VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void showMessage(String m) {
        Toast.makeText(this.getContext(), m, Toast.LENGTH_SHORT).show();
    }

}

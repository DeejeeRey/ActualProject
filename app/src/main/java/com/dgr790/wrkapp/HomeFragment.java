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
    private Button btnStart, btnStop, btnSignOut;
    private EditText etTime;
    private TextView tvTimer;
    private CountDownTimer timer;
    private int mins;
    private int score;

    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private int dbScore;
    private int dbTimes = 0;
    private boolean finished;
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
        rotatePB();
        btnStop.setVisibility(v.GONE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();


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




        return v;
    }


    private void rotatePB() {
        Animation ani = new RotateAnimation(0.0f, 90.0f, 250.0f, 273f);
        ani.setFillAfter(true);
        pb.startAnimation(ani);
    }

    private void startTimer() {

        finished = false;

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
                   score = mins;
                   finished = true;
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
                timer.cancel();
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


//
//        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
//
//        // Get current users score
//        getCurrentScore(currentDB);
//        getCurrentTimes(currentDB);
//
//        System.out.println(dbScore);
//        System.out.println(dbTimes);
//
//        score = score + dbScore;
//        Map newScore = new HashMap();
//        newScore.put("Score", score);
//        currentDB.updateChildren(newScore);
//
//        dbTimes ++;
//        Map newTimes = new HashMap();
//        newTimes.put("Times", dbTimes);
//        currentDB.updateChildren(newTimes);
    }


    private void getCurrentScore(DatabaseReference currentDB) {
        currentDB.child("Score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               dbScore = dataSnapshot.getValue(int.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getCurrentTimes(DatabaseReference currentDB) {
        currentDB.child("Times").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbTimes = dataSnapshot.getValue(int.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showMessage(String m) {
        Toast.makeText(this.getContext(), m, Toast.LENGTH_SHORT).show();
    }

}

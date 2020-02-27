package com.dgr790.wrkapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView ivProfileImage;
    private Button btnEdit, btnSignOut;
    private TextView tvName, tvUser, tvEmail, tvPoints, tvTotal, tvAverage;
    private TextView tvPointsDisp, tvTotalDisp, tvAverageDisp;
    private ImageView ivUserInfo, ivUserStats;

    private String firstname, lastname, username, email, score, times;
    private String returnValueString;
    private int returnValueInt;

    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private HashMap<String, String> userHashMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ivProfileImage = (CircleImageView) v.findViewById(R.id.ivProfileImage);
        btnEdit = (Button) v.findViewById(R.id.btnEdit);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvUser = (TextView) v.findViewById(R.id.tvUser);
        tvEmail = (TextView) v.findViewById(R.id.tvEmail);
        tvPoints = (TextView) v.findViewById(R.id.tvPoints);
        tvTotal = (TextView) v.findViewById(R.id.tvTotal);
        tvAverage = (TextView) v.findViewById(R.id.tvAverage);
        tvPointsDisp = (TextView) v.findViewById(R.id.tvPointsDisp);
        tvTotalDisp = (TextView) v.findViewById(R.id.tvTotalDisp);
        tvAverageDisp = (TextView) v.findViewById(R.id.tvAverageDisp);
        ivUserInfo = (ImageView) v.findViewById(R.id.ivUserInfo);
        ivUserStats = (ImageView) v.findViewById(R.id.ivUserStats);
        btnSignOut = (Button) v.findViewById(R.id.btnSignOut);

        String[] dataNames = {"First Name", "Second Name", "Username", "Email", "Score", "Times"};





        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();


        setProfileImage();
        setProfileInfo(dataNames);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(v);
            }
        });


        return v;
    }

    private void setProfileInfo(String[] dataNames) {
        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Users");


        currentDB.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userHashMap = (HashMap<String, String>) dataSnapshot.getValue();

                tvName.setText(userHashMap.get("First Name") + " " + userHashMap.get("Second Name"));
                tvUser.setText(userHashMap.get("Username"));
                tvEmail.setText(userHashMap.get("Email"));
                tvPoints.setText(String.valueOf(userHashMap.get("Score")));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void changeLayout() {
        // GET RID OF STATS BOX & REPLACE WITH WHAT IS ON SLIDES
        ivUserStats.setVisibility(getView().GONE);
        tvPoints.setVisibility(getView().GONE);
        tvTotal.setVisibility(getView().GONE);
        tvAverage.setVisibility(getView().GONE);
        tvPointsDisp.setVisibility(getView().GONE);
        tvTotalDisp.setVisibility(getView().GONE);
        tvAverageDisp.setVisibility(getView().GONE);
        btnEdit.setVisibility(getView().GONE);

    }

    private void setProfileImage() {
        Glide.with(this).load(currentUser.getPhotoUrl()).into(ivProfileImage);
    }

    private void signOut(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent loginActivity = new Intent(v.getContext(), LoginActivity.class);
        startActivity(loginActivity);

    }
}

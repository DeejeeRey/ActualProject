package com.dgr790.wrkapp;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView ivProfileImage;
    private Button btnEdit;
    private TextView tvName, tvUser, tvEmail, tvPoints, tvTotal, tvAverage;
    private TextView tvPointsDisp, tvTotalDisp, tvAverageDisp;
    private ImageView ivUserInfo, ivUserStats;

    private String firstname, lastname, username, email, score, times;
    private String returnValueString;
    private int returnValueInt;

    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;


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



        return v;
    }

    private void setProfileInfo(String[] dataNames) {
        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        String[] dataValues = new String[6];

        for (int i=0; i<4; i++) {
            dataValues[i] = getStringData(dataNames[i], currentDB);
        }

        for (int i=4; i<6; i++) {
            dataValues[i] = getIntData(dataNames[i], currentDB);
        }

        tvName.setText(dataValues[0] + dataValues[1]);
        tvUser.setText(dataValues[2]);
        tvEmail.setText(dataValues[3]);
        tvPoints.setText(dataValues[4]);

    }

    private String getStringData(String type, DatabaseReference currentDB) {

        currentDB.child(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                returnValueString = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return returnValueString;
    }

    private String getIntData(String type, DatabaseReference currentDB) {

        currentDB.child(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                returnValueInt = ((Long) dataSnapshot.getValue()).intValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return Integer.toString(returnValueInt);
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
}

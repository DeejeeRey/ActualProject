package com.dgr790.wrkapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView ivProfileImage;
    private Button btnEdit, btnSignOut, btnChangeUsername, btnChangeEmail, btnChangePass, btnDone;
    private EditText changeUsernameET, changeEmailET, changePassET;
    private TextView tvName, tvUser, tvEmail, tvPoints, tvTotal, tvAverage;
    private TextView tvNameDisp, tvUserDisp, tvEmailDisp, tvPointsDisp, tvTotalDisp, tvAverageDisp;
    private ImageView ivUserInfo, ivUserStats;

    private String firstname, lastname, username, email, score, times;
    private String returnValueString;
    private int returnValueInt;

    private String userID;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference currentDB;

    private HashMap<String, String> userHashMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        setViews(v);

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

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertLayout();
            }
        });

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
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

    // Used to display all user info gathered from online database
    private void setProfileInfo(String[] dataNames) {
        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Users");


        currentDB.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userHashMap = (HashMap<String, String>) dataSnapshot.getValue();

                int points = Integer.parseInt(String.valueOf(userHashMap.get("Score")));
                int times = Integer.parseInt(String.valueOf(userHashMap.get("Times")));

                tvName.setText(userHashMap.get("First Name") + " " + userHashMap.get("Second Name"));
                tvUser.setText(userHashMap.get("Username"));
                tvEmail.setText(userHashMap.get("Email"));
                tvPoints.setText(String.valueOf(userHashMap.get("Score")));
                tvTotal.setText(points/60 + " hrs " + points%60 + " mins");
                if (times != 0) {
                    tvAverage.setText(points / times + " mins");
                } else {
                    tvAverage.setText(0 + " mins");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Change password method
    private void changePassword() {
        String newPass = changePassET.getText().toString();
        if (newPass.isEmpty()) {
            showMessage("Please enter a new password");
        } else {
            currentUser.updatePassword(newPass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showMessage("Password successfully updated");
                            } else {
                                showMessage("Password could not be updated");
                            }
                        }
                    });
        }
    }

    // Change username method
    private void changeUsername() {
        final String newUsername = changeUsernameET.getText().toString();
        currentDB = FirebaseDatabase.getInstance().getReference().child("Users");

        if (newUsername.isEmpty()) {
            showMessage("Please enter a new username");
        } else {
            currentDB.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map usernameMap = new HashMap();
                    usernameMap.put("Username", newUsername);
                    currentDB.child(userID).updateChildren(usernameMap);
                    showMessage("Username successfully updated");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showMessage("Username could not be updated");
                }
            });
        }
    }

    // Change email method
    private void changeEmail() {
        String newEmail = changeEmailET.getText().toString();
        if (newEmail.isEmpty()) {
            showMessage("Please enter a new email");
        } else {
            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showMessage("Email successfully updated");
                            } else {
                                showMessage("Email could not be updated");
                            }
                        }
                    });
        }
    }


    // Change layout when edit button is pressed
    private void changeLayout() {
        tvPoints.setVisibility(getView().INVISIBLE);
        tvTotal.setVisibility(getView().INVISIBLE);
        tvAverage.setVisibility(getView().INVISIBLE);
        tvPointsDisp.setVisibility(getView().INVISIBLE);
        tvTotalDisp.setVisibility(getView().INVISIBLE);
        tvAverageDisp.setVisibility(getView().INVISIBLE);
        btnEdit.setVisibility(getView().INVISIBLE);

        changeUsernameET.setVisibility(getView().VISIBLE);
        changeEmailET.setVisibility(getView().VISIBLE);
        changePassET.setVisibility(getView().VISIBLE);
        btnChangeUsername.setVisibility(getView().VISIBLE);
        btnChangeEmail.setVisibility(getView().VISIBLE);
        btnChangePass.setVisibility(getView().VISIBLE);
        btnDone.setVisibility(getView().VISIBLE);
    }

    private void revertLayout() {
        tvPoints.setVisibility(getView().VISIBLE);
        tvTotal.setVisibility(getView().VISIBLE);
        tvAverage.setVisibility(getView().VISIBLE);
        tvPointsDisp.setVisibility(getView().VISIBLE);
        tvTotalDisp.setVisibility(getView().VISIBLE);
        tvAverageDisp.setVisibility(getView().VISIBLE);
        btnEdit.setVisibility(getView().VISIBLE);

        changeUsernameET.setVisibility(getView().INVISIBLE);
        changeEmailET.setVisibility(getView().INVISIBLE);
        changePassET.setVisibility(getView().INVISIBLE);
        btnChangeUsername.setVisibility(getView().INVISIBLE);
        btnChangeEmail.setVisibility(getView().INVISIBLE);
        btnChangePass.setVisibility(getView().INVISIBLE);
    }

    // Sets all views - didn't want cluttered onCreate
    private void setViews(View v) {
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
        changeUsernameET = (EditText) v.findViewById(R.id.changeUsernameET);
        changeEmailET = (EditText) v.findViewById(R.id.changeEmailET);
        changePassET = (EditText) v.findViewById(R.id.changePassET);
        btnChangeUsername = (Button) v.findViewById(R.id.btnChangeUsername);
        btnChangeEmail = (Button) v.findViewById(R.id.btnChangeEmail);
        btnChangePass = (Button) v.findViewById(R.id.btnChangePass);
        btnDone = (Button) v.findViewById(R.id.btnDone);
    }

    // Sets profile image
    private void setProfileImage() {
        Glide.with(this).load(currentUser.getPhotoUrl()).into(ivProfileImage);
    }

    // Signs user out of firebase account
    private void signOut(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent loginActivity = new Intent(v.getContext(), LoginActivity.class);
        startActivity(loginActivity);

    }

    private void showMessage(String m) {
        if (this.getContext() != null) {
            Toast.makeText(this.getContext(), m, Toast.LENGTH_SHORT).show();
        }
    }
}

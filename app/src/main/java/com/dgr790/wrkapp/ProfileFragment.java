package com.dgr790.wrkapp;

import android.os.Bundle;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView ivProfileImage;
    private Button btnEdit;
    private TextView tvName, tvUser, tvEmail, tvPoints, tvTotal, tvAverage;
    private TextView tvPointsDisp, tvTotalDisp, tvAverageDisp;
    private ImageView ivUserInfo, ivUserStats;


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


        setProfileImage();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout();
            }
        });



        return v;
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

    }
}

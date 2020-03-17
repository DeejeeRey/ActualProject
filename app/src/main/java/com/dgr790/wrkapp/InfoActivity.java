package com.dgr790.wrkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {

    private String[] userInformation;
    private String message;

    private ImageView ivBackground;
    private TextView tvMessage;
    private Button btnGo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle extras = getIntent().getExtras();
        userInformation = extras.getStringArray("userInformation");

        setMessage();

        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        btnGo = (Button) findViewById(R.id.btnGo);

        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        ivBackground.startAnimation(myFadeInAnimation);

        tvMessage.setText(message);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                homeActivity.putExtra("userInformation", userInformation);
                startActivity(homeActivity);
                finish();
            }
        });

    }

    private void setMessage() {
        message = "Welcome to WRK - the app to help you study!\n\n" +
                "It's simple, set the amount of time you would like to study and get to work!\n\n" +
                "Be careful not to leave the app, or your hard work won't be rewarded.\n\n" +
                "Compete against your friends to get the highest score!\n\n" +
                "Happy Studying! Now get to 'WRK'...";
    }
}

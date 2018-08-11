package com.jim.multipos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jim.mpviews.MpCircleWithText;
import com.jim.multipos.R;

public class Custom extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa);
        MpCircleWithText circleText = findViewById(R.id.mpCircle);
        circleText.setPercentandText(90, "Hello");
    }
}

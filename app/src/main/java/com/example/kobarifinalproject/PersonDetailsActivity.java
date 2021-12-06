package com.example.kobarifinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PersonDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "personId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
    }
}
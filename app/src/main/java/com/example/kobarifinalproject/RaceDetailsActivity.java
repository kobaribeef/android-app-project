package com.example.kobarifinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kobarifinalproject.DataAccess.FirebaseListener;
import com.example.kobarifinalproject.DataAccess.RaceDataAccess;
import com.example.kobarifinalproject.models.Person;
import com.example.kobarifinalproject.models.Race;

public class RaceDetailsActivity extends AppCompatActivity {

    public static final String TAG = "RaceDetailsActivity";
    public static final String EXTRA_RACE_ID = "raceId";

    private RaceDataAccess da;
    private Race race;
    private EditText txtRaceName;
    private Button btnSaveRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_details);

        txtRaceName = findViewById(R.id.txtRace);
        btnSaveRace = findViewById(R.id.btnSaveRace);

        //linking to Firebase for race collection
        da = new RaceDataAccess();

        //retrieving a race Id from RaceListActivity
        Intent i = getIntent();
        String raceId = i.getStringExtra(EXTRA_RACE_ID);
        if(raceId != null){
            da.getRaceById(raceId, new FirebaseListener() {
                @Override
                public void done(Object obj) {
                    race = (Race) obj;
                    Log.d(TAG, race.toString());
                    putDataIntoUI();
                }
            });
        }

        btnSaveRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    getDataFromUI();
                    save();
                }
            }
        });
    }

    //method for validation
    private boolean validate() {
        boolean isValid = true;

        if (txtRaceName.getText().toString().isEmpty()) {
            isValid = false;
            txtRaceName.setError("Please enter a race");
        }

        return isValid;
    }

    //method to put data into app from database
    private void putDataIntoUI(){
        if(race != null){
            txtRaceName.setText(race.getRace());
        }
    }

    //method to retrieve data from app
    private void getDataFromUI(){
        String raceName = txtRaceName.getText().toString();

        race = new Race(raceName);
        Log.d(TAG, "ADDING NEW RACE");
    }

    //method add a race and go to RaceListActivity
    private void save(){
        da.addRace(race, new FirebaseListener() {
            @Override
            public void done(Object obj) {
                Race newRace = (Race)obj;
                Log.d(TAG, "New Race: " + newRace.toString());
            }
        });

        Intent i = new Intent(this, RaceListActivity.class);
        startActivity(i);
    }
}
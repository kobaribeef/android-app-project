package com.example.kobarifinalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kobarifinalproject.DataAccess.FirebaseListener;
import com.example.kobarifinalproject.DataAccess.PersonDataAccess;
import com.example.kobarifinalproject.DataAccess.RaceDataAccess;
import com.example.kobarifinalproject.models.Person;
import com.example.kobarifinalproject.models.Race;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RaceListActivity extends AppCompatActivity {

    public static final String TAG = "RaceListActivity";

    private RaceDataAccess da;
    private Button btnAddRace;
    private Button btnLogout;
    private ArrayList<Race> allRaces;
    private ListView lsRaces;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_list);

        setTitle("Race List");

        auth = FirebaseAuth.getInstance();
        lsRaces = findViewById(R.id.lsRaces);
        da = new RaceDataAccess();
        addRace();
        logOut();
        da.getAllRace(new FirebaseListener() {
            @Override
            public void done(Object obj) {

                allRaces = (ArrayList<Race>) obj;
                setupList();
            }
        });
    }

    private void addRace(){
        btnAddRace = findViewById(R.id.btnAddRace);
        btnAddRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RaceListActivity.this, RaceDetailsActivity.class);
                startActivity(i);
            }
        });
    }

    private void logOut(){
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(RaceListActivity.this, LoginActivity.class);
                startActivity(i);
                Log.d(TAG, "Logged out");
            }
        });
    }

    private void setupList(){
        ArrayAdapter<Race> adapter = new ArrayAdapter<Race>(RaceListActivity.this, R.layout.custom_item_list_2, R.id.lblRace, allRaces){
            @Override
            public View getView(int position, View convertView, ViewGroup parentListView){
                View listItemView = super.getView(position, convertView, parentListView);

                Race currentRace = allRaces.get(position);
                TextView lblRace = listItemView.findViewById(R.id.lblRace);
                lblRace.setText(currentRace.getRace());
                Button delete = listItemView.findViewById(R.id.btnDeleteRace);

                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Race selectedRace = allRaces.get(position);
                        Intent i = new Intent(RaceListActivity.this, GetPeopleByRaceActivity.class);
                        i.putExtra(GetPeopleByRaceActivity.EXTRA_RACE_ID, selectedRace.getId());
                        startActivity(i);
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(RaceListActivity.this);
                        alert.setTitle("Delete Race");
                        alert.setMessage("Are you sure you want to delete this race");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Race selectedRace = allRaces.get(position);
                                da.deleteRace(selectedRace, new FirebaseListener() {
                                    @Override
                                    public void done(Object obj) {

                                    }
                                });
                                dialog.dismiss();
                                allRaces.remove(selectedRace);
                                notifyDataSetChanged();
                            }
                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });

                        alert.show();
                    }
                });

                return listItemView;
            }
        };

        lsRaces.setAdapter(adapter);
    }
}
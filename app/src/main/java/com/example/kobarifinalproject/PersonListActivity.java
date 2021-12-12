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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kobarifinalproject.DataAccess.FirebaseListener;
import com.example.kobarifinalproject.DataAccess.PersonDataAccess;
import com.example.kobarifinalproject.DataAccess.RaceDataAccess;
import com.example.kobarifinalproject.models.Person;
import com.example.kobarifinalproject.models.Race;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class PersonListActivity extends AppCompatActivity {

    public static final String TAG = "PersonListActivity";
    public static final String EXTRA_PEOPLE_BY_RACE_ID = "raceId";

    private RaceDataAccess daR;
    private PersonDataAccess daP;
    private Race race;
    private Button btnAddPerson;
    private Button btnBackToRaceMenu;
    private ArrayList<Person> allPeople;
    private ListView lsPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        daR = new RaceDataAccess();

        Intent i = getIntent();
        String raceId = i.getStringExtra(EXTRA_PEOPLE_BY_RACE_ID);
        if(raceId != null){
            daR.getRaceById(raceId, new FirebaseListener() {
                @Override
                public void done(Object obj) {
                    race = (Race) obj;
                    Log.d(TAG, race.toString());

                }
            });
        }

        lsPeople = findViewById(R.id.lsPeople);
        daP = new PersonDataAccess();
        addPerson();
        raceMenu();
        daP.getAllPeople(new FirebaseListener() {
            @Override
            public void done(Object obj) {

                allPeople = (ArrayList<Person>) obj;

                ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(PersonListActivity.this, R.layout.custom_item_list, R.id.lblPerson, allPeople){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parentListView){
                        View listItemView = super.getView(position, convertView, parentListView);

                        Person currentPerson = allPeople.get(position);
                        TextView lblPerson = listItemView.findViewById(R.id.lblPerson);
                        lblPerson.setText(currentPerson.getName());
                        CheckBox chkMet = listItemView.findViewById(R.id.chkMetList);
                        chkMet.setChecked(currentPerson.isMet());
                        Button update = listItemView.findViewById(R.id.btnUpdatePerson);
                        Button delete = listItemView.findViewById(R.id.btnDeletePerson);

                        chkMet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                currentPerson.setMet(chkMet.isChecked());
                                daP.updatePerson(currentPerson, new FirebaseListener() {
                                    @Override
                                    public void done(Object obj) {
                                        Log.d(TAG, currentPerson.toString());
                                    }
                                });

                            }
                        });

                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Person selectedPerson = allPeople.get(position);
                                Intent i = new Intent(PersonListActivity.this, PersonDetailsActivity.class);
                                i.putExtra(PersonDetailsActivity.EXTRA_PERSON_ID, selectedPerson.getId());
                                startActivity(i);
                            }
                        });

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(PersonListActivity.this);
                                alert.setTitle("Delete Person");
                                alert.setMessage("Are you sure you want to delete this person");
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        Person selectedPerson = allPeople.get(position);
                                        daP.deletePerson(selectedPerson, new FirebaseListener() {
                                            @Override
                                            public void done(Object obj) {

                                            }
                                        });
                                        dialog.dismiss();
                                        allPeople.remove(selectedPerson);
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

                lsPeople.setAdapter(adapter);
            }
        });
    }

    private void addPerson(){
        btnAddPerson = findViewById(R.id.btnAddPerson);
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PersonListActivity.this, PersonDetailsActivity.class);
                startActivity(i);
            }
        });
    }

    private void raceMenu(){
        btnBackToRaceMenu = findViewById(R.id.btnBackToRaceMenu);
        btnBackToRaceMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PersonListActivity.this, RaceListActivity.class);
                startActivity(i);
            }
        });
    }

}
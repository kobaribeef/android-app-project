package com.example.kobarifinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kobarifinalproject.DataAccess.FirebaseListener;
import com.example.kobarifinalproject.DataAccess.PersonDataAccess;
import com.example.kobarifinalproject.models.Person;

import java.util.ArrayList;
import java.util.Date;

public class PersonListActivity extends AppCompatActivity {

    public static final String TAG = "PersonListActivity";
    PersonDataAccess da;
    Button btnGetAllPeople;
    EditText txtPersonId;
    Button btnGetPersonById;
    Button btnAddPerson;
    Button btnUpdatePerson;
    Button btnDeletePerson;
    private ArrayList<Person> allPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        da = new PersonDataAccess();
    }

    private void getAllPeople(){
        btnGetAllPeople = findViewById(R.id.btnGetAllPeople);
        btnGetAllPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                da.getAllPeople(new FirebaseListener() {
                    @Override
                    public void done(Object obj) {
                        ArrayList<Person> people = (ArrayList<Person>) obj;
                        for( Person p : people){
                            Log.d(TAG, p.toString());
                        }
                    }
                });
            }
        });
    }

    private void getPersonById(){
        txtPersonId = findViewById(R.id.txtPersonId);
        btnGetPersonById = findViewById(R.id.btnGetPersonById);
        btnGetPersonById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personId = txtPersonId.getText().toString();
                da.getPersonById(personId, new FirebaseListener() {
                    @Override
                    public void done(Object obj) {
                        Person p = (Person) obj;
                        Log.d(TAG, p.toString());
                    }
                });
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

    private void updatePerson(){
        btnUpdatePerson = findViewById(R.id.btnUpdatePerson);
        btnUpdatePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personId = txtPersonId.getText().toString();
                if(personId.isEmpty()){
                    Log.d(TAG, "ENTER THE ID OF THE PERSON TO UPDATE");
                }else{
                    Person p = new Person(personId, "Updated name", new Date(), false);
                    da.updateDog(d, new FirebaseListener() {
                        @Override
                        public void done(Object obj) {

                        }
                    });
                }
            }
        });
    }

    private void setUpList(){

    }

}
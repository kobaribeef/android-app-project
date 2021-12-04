package com.example.kobarifinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.kobarifinalproject.DataAccess.PersonDataAccess;

public class PersonListActivity extends AppCompatActivity {

    PersonDataAccess da;
    Button btnGetAllPeople;
    EditText txtPersonId;
    Button btnGetPersonById;
    Button btnAddPerson;
    Button btnUpdatePerson;
    Button btnDeletePerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);
    }

}
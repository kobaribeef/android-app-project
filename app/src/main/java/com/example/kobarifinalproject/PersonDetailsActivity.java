package com.example.kobarifinalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kobarifinalproject.DataAccess.FirebaseListener;
import com.example.kobarifinalproject.DataAccess.PersonDataAccess;
import com.example.kobarifinalproject.models.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PersonDetailsActivity extends AppCompatActivity {

    public static final String TAG = "PersonDetailsActivity";
    public static final String EXTRA_PERSON_ID = "personId";

    private PersonDataAccess da;
    private Person person;
    private EditText txtName;
    private EditText txtDescription;
    private EditText txtRaceDescription;
    private CheckBox chkMet;
    private EditText txtFirstMetDate;
    private Button btnSavePerson;
    private Button btnDeletePersonPDA;
    private Button btnPickDate;
    DatePickerDialog.OnDateSetListener datePickerListener;
    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtRaceDescription = findViewById(R.id.txtRaceDescription);
        chkMet = findViewById(R.id.chkMet);
        txtFirstMetDate = findViewById(R.id.txtFirstMetDate);
        btnSavePerson = findViewById(R.id.btnSavePerson);
        btnDeletePersonPDA = findViewById(R.id.btnDeletePersonPDA);
        btnPickDate = findViewById(R.id.btnPickDate);

        da = new PersonDataAccess();

        Intent i = getIntent();
        String personId = i.getStringExtra(EXTRA_PERSON_ID);
        if(personId != null){
            da.getPersonById(personId, new FirebaseListener() {
                @Override
                public void done(Object obj) {
                    person = (Person) obj;
                    Log.d(TAG, person.toString());
                    putDataIntoUI();
                }
            });
        }else{
            btnDeletePersonPDA.setVisibility(View.GONE);
        }

        btnSavePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    getDataFromUI();
                    save();
                }
            }
        });

        btnDeletePersonPDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeletedDialog();
            }
        });

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                txtFirstMetDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
    }

    private boolean validate(){
        boolean isValid = true;

        if(txtName.getText().toString().isEmpty()){
            isValid = false;
            txtName.setError("Please enter a name");
        }

        if(txtDescription.getText().toString().isEmpty()){
            isValid = false;
            txtDescription.setError("Please Enter their description");
        }

        if(txtRaceDescription.getText().toString().isEmpty()){
            isValid = false;
            txtRaceDescription.setError("Please enter their race description");
        }
        if(chkMet.isChecked()){
            if(txtFirstMetDate.getText().toString().isEmpty()){
                isValid = false;
                txtFirstMetDate.setError("Please enter the date you both met");
            }
        }else{
            txtFirstMetDate.setText("Have not met yet", TextView.BufferType.EDITABLE);
        }
        return isValid;
    }

    private void putDataIntoUI(){
        if(person != null){
            txtName.setText(person.getName());
            txtDescription.setText(person.getDescription());
            txtRaceDescription.setText(person.getRaceDescription());
            chkMet.setChecked(person.isMet());
            txtFirstMetDate.setText(person.getFirstMet().toString());
        }
    }

    private void getDataFromUI(){
        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();
        String raceDescription = txtRaceDescription.getText().toString();
        boolean met = chkMet.isChecked();
        String firstMetString = txtFirstMetDate.getText().toString();
        Date firstMet = null;

        try {
            firstMet = sdf.parse(firstMetString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(person != null){
            person.setName(name);
            person.setDescription(description);
            person.setRaceDescription(raceDescription);
            person.setMet(met);
            person.setFirstMet(firstMet);
            Log.d(TAG, "UPDATING PERSON");
        }else{
            person = new Person(name, description, raceDescription, met, firstMet);
            Log.d(TAG, "ADDING NEW PERSON");
        }
    }

    private void save(){
        if(person.getId() != null){
            da.updatePerson(person, new FirebaseListener() {
                @Override
                public void done(Object obj) {

                }
            });
        }else{
            da.addPerson(person, new FirebaseListener() {
                @Override
                public void done(Object obj) {
                    Person newPerson = (Person)obj;
                    Log.d(TAG, "New Person: " + newPerson.toString());
                }
            });
        }

        Intent i = new Intent(this, PersonListActivity.class);
        startActivity(i);
    }

    private void showDeletedDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Person");
        alert.setMessage("Are you sure you want to delete this person");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                da.deletePerson(person, new FirebaseListener() {
                    @Override
                    public void done(Object obj) {
                        Log.d(TAG, "Person deleted");
                    }
                });
                dialog.dismiss();
                Intent intent = new Intent(PersonDetailsActivity.this, PersonListActivity.class);
                startActivity(intent);
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

    private void showDatePicker(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDate = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, mYear, mMonth, mDate);
        datePickerDialog.show();
    }
}
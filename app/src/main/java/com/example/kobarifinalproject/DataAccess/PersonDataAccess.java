package com.example.kobarifinalproject.DataAccess;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kobarifinalproject.models.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class PersonDataAccess {

    public static final String TAG = "PersonDataAccess";
    private static FirebaseFirestore db;
    CollectionReference peopleCollection;

    public PersonDataAccess(){
        db = FirebaseFirestore.getInstance();
        peopleCollection = db.collection("people");
    }

    public void getAllPeople(FirebaseListener listener){
        peopleCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                List<Person> personObjects = new ArrayList();

                for(DocumentSnapshot p : docs){
                    Person person = convertDocumentToPerson(p);
                    personObjects.add(person);
                }

                listener.done(personObjects);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Unable to get all people" + e.toString());

            }
        });
    }

    private Person convertDocumentToPerson(DocumentSnapshot doc){
        String personId = doc.getId();
        try{
            String personName = doc.getString("name");
            String personDesc = doc.getString("description");
            Timestamp ts = doc.getTimestamp("firstMet");
            Date firstMet = ts.toDate();
            boolean met = doc.getBoolean("met");
            Person person = new Person(personId, personName, personDesc, met, firstMet);
            return person;
        }catch(Exception e){
            Log.d(TAG, "UNABLE TO CONVERT DOCUMENT TO DOG" + e.toString());
            return null;
        }
    }
}

package com.example.kobarifinalproject.DataAccess;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kobarifinalproject.models.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    public void getPersonById(String personId, FirebaseListener listener){
        peopleCollection.document(personId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot p) {
                Person person = convertDocumentToPerson(p);
                listener.done(person);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO GET PERSON BY ID" + e.toString());
            }
        });
    }

    public void addPerson(Person newPerson, FirebaseListener listener){
        peopleCollection.add(convertPersonToMap(newPerson)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                newPerson.setId(documentReference.getId());
                listener.done(newPerson);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO ADD PERSON" + e.toString());
            }
        });
    }

    public void updatePerson(Person updatedPerson, FirebaseListener listener){
        peopleCollection.document(updatedPerson.getId()).set(convertPersonToMap(updatedPerson)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.done(updatedPerson);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO UPDATE PERSON" + e.toString());
            }
        });
    }

    public void deletePerson(Person deletedPerson, FirebaseListener listener){
        peopleCollection.document(deletedPerson.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.done(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO DELETE DOG" + e.toString());
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

    private HashMap<String, Object> convertPersonToMap(Person person){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", person.getName());
        map.put("description", person.getDescription());
        map.put("firstMet", person.getFirstMet());
        map.put("met", person.isMet());
        return map;
    }
}

package com.example.kobarifinalproject.DataAccess;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kobarifinalproject.models.Person;
import com.example.kobarifinalproject.models.Race;
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

public class RaceDataAccess {

    public static final String TAG = "RaceDataAccess";
    private static FirebaseFirestore db;
    CollectionReference racesCollection;

    public RaceDataAccess(){
        db = FirebaseFirestore.getInstance();
        racesCollection = db.collection("races");
    }

    public void getAllRace(FirebaseListener listener){
        racesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                List<Race> raceObjects = new ArrayList();

                for(DocumentSnapshot r : docs){
                    Race race = convertDocumentToRace(r);
                    raceObjects.add(race);
                }

                listener.done(raceObjects);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Unable to get all race" + e.toString());

            }
        });
    }

    public void getRaceById(String raceId, FirebaseListener listener){
        racesCollection.document(raceId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot r) {
                Race race = convertDocumentToRace(r);
                listener.done(race);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO GET RACE BY ID" + e.toString());
            }
        });
    }

    public void addRace(Race newRace, FirebaseListener listener){
        racesCollection.add(convertRaceToMap(newRace)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                newRace.setId(documentReference.getId());
                listener.done(newRace);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO ADD RACE" + e.toString());
            }
        });
    }

    public void updateRace(Race updatedRace, FirebaseListener listener){
        racesCollection.document(updatedRace.getId()).set(convertRaceToMap(updatedRace)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.done(updatedRace);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO UPDATE Race" + e.toString());
            }
        });
    }

    public void deleteRace(Race deletedRace, FirebaseListener listener){
        racesCollection.document(deletedRace.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.done(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "UNABLE TO DELETE RACE" + e.toString());
            }
        });
    }

    private Race convertDocumentToRace(DocumentSnapshot doc){
        String raceId = doc.getId();
        try{
            String theRace = doc.getString("race");
            Race race = new Race(raceId, theRace);
            return race;
        }catch(Exception e){
            Log.d(TAG, "UNABLE TO CONVERT DOCUMENT TO RACE" + e.toString());
            return null;
        }
    }

    private HashMap<String, Object> convertRaceToMap(Race race){
        HashMap<String, Object> map = new HashMap<>();
        map.put("race", race.getRace());
        return map;
    }
}

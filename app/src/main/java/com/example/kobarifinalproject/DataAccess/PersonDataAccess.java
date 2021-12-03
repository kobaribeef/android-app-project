package com.example.kobarifinalproject.DataAccess;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class PersonDataAccess {

    public static final String TAG = "PersonDataAccess";
    private static FirebaseFirestore db;
    CollectionReference peopleCollection;

    public PersonDataAccess(){
        db = FirebaseFirestore.getInstance();
        peopleCollection = db.collection("people");
    }
}

package com.example.kobarifinalproject.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Race {

    String id;
    String race;
    ArrayList<Person> people = new ArrayList<>();

    public ArrayList<Person> getPeople(){
        return people;
    }

    public void setPeople(ArrayList<Person> people){
        this.people = people;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Race(String id, String race){
        this.id = id;
        this.race = race;
    }
}

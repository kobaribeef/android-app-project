package com.example.kobarifinalproject.models;

import java.util.Date;

public class Person {

    String id;
    String name;
    String description;
    String raceDescription;
    boolean met;
    Date firstMet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRaceDescription(){ return raceDescription; }

    public void setRaceDescription(String raceDescription){ this.raceDescription = raceDescription; }

    public boolean isMet() {
        return met;
    }

    public void setMet(boolean met) {
        this.met = met;
    }

    public Date getFirstMet() {
        return firstMet;
    }

    public void setFirstMet(Date firstMet) {
        this.firstMet = firstMet;
    }

    public Person(String name, String description, String raceDescription, boolean met, Date firstMet){
        this.name = name;
        this.description = description;
        this.raceDescription = raceDescription;
        this.met = met;
        this.firstMet = firstMet;
    }

    public Person(String id, String name, String description, String raceDescription, boolean met, Date firstMet){
        this(name, description, raceDescription, met, firstMet);
        this.id = id;
    }

    @Override
    public String toString(){

        return String.format(
                "ID: %s NAME: %s FIRST MET: %s MET: %b",
                this.id != null ? this.id : "NO ID YET",
                this.name != null ? this.name : "NO NAME YET",
                this.firstMet != null ? this.firstMet.toString() : "HAVE NOT MET IRL YET",
                this.met);
    }
}

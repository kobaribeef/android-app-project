package com.example.kobarifinalproject.models;

public class Race {

    String id;
    String race;

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

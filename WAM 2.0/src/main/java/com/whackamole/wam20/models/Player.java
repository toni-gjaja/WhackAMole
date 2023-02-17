package com.whackamole.wam20.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    public String name;

    public int points;

    public int highestStreak;

    List<String> moves;

    public Player(String name){

        this.name = name;
        this.moves = new ArrayList<>();

    }

    public void setPoints(int points){

        this.points = points;

    }

    public void setHighestStreak(int streak){

        this.highestStreak = streak;

    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public List<String> getMoves(){

        return moves;
    }

    public String getName() {
        return name;
    }

    public int getPoints(){
        return points;
    }

    public int getHighestStreak(){
        return highestStreak;
    }
}

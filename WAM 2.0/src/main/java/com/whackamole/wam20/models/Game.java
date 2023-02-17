package com.whackamole.wam20.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Game implements Serializable {

    public LocalDateTime startTime;

    public Player player;

    public Game(Player player){

        this.startTime = LocalDateTime.now();
        this.player = player;

    }

}

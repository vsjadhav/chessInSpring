package com.springTut.chess;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int playerId;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy="enrolledPlayers",cascade = CascadeType.ALL)
    private Set<Game> gamesEnrolledIn = new HashSet<>();

    public Player(String name){
        this.name = name;
    }

    public int getPlayerId() {
        return playerId;
    }


    public String getName() {
        return name;
    }

    public Set<Game> getGamesEnrolledIn() {
        return gamesEnrolledIn;
    }
}

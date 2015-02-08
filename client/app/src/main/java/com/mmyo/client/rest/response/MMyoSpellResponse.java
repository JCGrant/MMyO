package com.mmyo.client.rest.response;

import android.util.Log;

import com.mmyo.client.Direction;
import com.thalmic.myo.Pose;

public class MMyoSpellResponse implements Response {
//    int id;
//    Pose pose;
//    Direction direction;

    String spell;
    String enemySpell;
    int dmgTaken;
    int dmgDealt;
    int health;
    int enemyHealth;

    public MMyoSpellResponse(String spell, String enemySpell, int dmgTaken, int dmgDealt, int health, int enemyHealth) {
        this.spell = spell;
        this.enemySpell = enemySpell;
        this.dmgTaken = dmgTaken;
        this.dmgDealt = dmgDealt;
        this.health = health;
        this.enemyHealth = enemyHealth;
    }
    public void displayInformation() {
//        Log.v("Response:", id + " " + pose + " " + direction);
        Log.v("Response:", spell + " " + enemySpell + " " + dmgTaken + " " + dmgDealt + " " + health + " " + enemyHealth);
    }

    public String getSpell() {
        return spell;
    }
    public String getEnemySpell() {
        return enemySpell;
    }
    public int getDmgTaken() {
        return dmgTaken;
    }
    public int getDmgDealt() {
        return dmgDealt;
    }
    public int getHealth() {
        return health;
    }
    public int getEnemyHealth() {
        return enemyHealth;
    }

}

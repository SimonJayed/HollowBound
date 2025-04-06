package data;

import java.io.Serializable;

public class DataStorage implements Serializable {

    public int level;
    public String name;
    public int worldX, worldY;
    public int spawnPointX, spawnPointY;
    public double speed, tempSpeed;
    public double attack, defense;
    public double exp, nextLevelExp;
    public int sizeIncrement;
    public double initialHP, initialEnergy;
    public double maxHP, hp;
    public double maxEnergy, energy, energyRegen;
    public double vit, pow, mag, agi, luck;


    public String playing;

    public int currentMap;
    public boolean event0Flag;
    public boolean event1Flag;
    public boolean event2Flag;
    public boolean event3Flag;
    public boolean event4Flag;
    public boolean event5Flag;
    public boolean event6Flag;


    public DataStorage() {
    }
}


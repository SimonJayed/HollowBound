package data;

import entity.NPC_Amaryllis;
import entity.NPC_Fort;
import entity.NPC_Sylvie;
import entity.components.Skill;
import main.GamePanel;

import java.io.*;

public class SaveLoad {
    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));

            DataStorage ds = new DataStorage();

            ds.level = gp.player.level;
            ds.name = gp.player.getName();
            ds.worldX = gp.player.worldX;
            ds.worldY = gp.player.worldY;
            ds.spawnPointX = gp.player.spawnPointX;
            ds.spawnPointY = gp.player.spawnPointY;
            ds.speed = gp.player.speed;
            ds.tempSpeed = gp.player.tempSpeed;
            ds.attack = gp.player.attack;
            ds.defense = gp.player.defense;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.sizeIncrement = gp.player.sizeIncrement;
            ds.initialHP = gp.player.initialHP;
            ds.initialEnergy = gp.player.initialEnergy;
            ds.maxHP = gp.player.maxHP;
            ds.hp = gp.player.hp;
            ds.maxEnergy = gp.player.maxEnergy;
            ds.energy = gp.player.energy;
            ds.energyRegen = gp.player.energyRegen;
            ds.vit = gp.player.vit;
            ds.pow = gp.player.pow;
            ds.mag = gp.player.mag;
            ds.agi = gp.player.agi;
            ds.luck = gp.player.luck;

            ds.playing = gp.player.playing;

            ds.currentMap = gp.currentMap;

            ds.event0Flag = gp.player.event0Flag;
            ds.event1Flag = gp.player.event1Flag;
            ds.event2Flag = gp.player.event2Flag;
            ds.event3Flag = gp.player.event3Flag;
            ds.event4Flag = gp.player.event4Flag;
            ds.event5Flag = gp.player.event5Flag;
            ds.event6Flag = gp.player.event6Flag;


            oos.writeObject(ds);
            oos.close();
            System.out.println("Game saved successfully!");

        } catch (Exception e) {
            System.out.println("Save exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            DataStorage ds = (DataStorage)ois.readObject();
            ois.close();

            gp.player.playing = ds.playing;

            switch(ds.playing) {
                case "fort":
                    gp.companion1 = new NPC_Sylvie(gp);
                    gp.companion2 = new NPC_Amaryllis(gp);
                    break;
                case "amaryllis":
                    gp.companion1 = new NPC_Sylvie(gp);
                    gp.companion2 = new NPC_Fort(gp);
                    break;
                case "sylvie":
                    gp.companion1 = new NPC_Fort(gp);
                    gp.companion2 = new NPC_Amaryllis(gp);
                    break;
            }


            gp.player.getImage(ds.playing);
            gp.player.getCombatImages(ds.playing);
            gp.player.skills.clear();


            if (ds.playing.equals("fort")) {
                gp.player.skills.add(new Skill("Rage Bait", "Taunts the enemy and increases defense for 2 turns.", ds.vit*0.4, 25+ds.maxEnergy*0.2, 3, "BUFF_SELF"));
                gp.player.skills.add(new Skill("Meat Shield", "Grants additional defense to an ally for 3 turns.", ds.vit*0.5, 25+ds.maxEnergy*0.4, 3, "BUFF_ALLY"));
                gp.player.skills.add(new Skill("SMAAAAASHHHHH", "Hits the enemy equivalent to lost health and defense stat.", (ds.maxHP-ds.hp*0.8)+(ds.defense*1.2), 25+ds.maxEnergy*0.8, 5, "DAMAGE"));
            } else if (ds.playing.equals("amaryllis")) {
                gp.player.skills.add(new Skill("Disabling Reagent", "Silences the enemy, rendering them unable to use skills, along with a random debuff that lasts 1 turn.", ds.agi*2, 50+ds.maxEnergy*0.3, 4, "DAMAGE"));
                gp.player.skills.add(new Skill("Experimental Cure", "Replaces an ally's status effects (including positive ones) with a random buff that lasts 3 turns.", ds.agi*3, 50+ds.maxEnergy*0.3, 3, "BUFF_ALLY"));
                gp.player.skills.add(new Skill("Unleash", "Unleashes a flurry of 3-5 attacks at the enemy.", ds.agi*2, 50+ds.maxEnergy*0.8, 5, "BUFF_SELF"));
            } else if (ds.playing.equals("sylvie")) {
                gp.player.skills.add(new Skill("Nature's Embrace", "Channels natural energy to heal a single ally", ds.mag*2, ds.maxEnergy*0.2, 2, "BUFF_ALLY"));
                gp.player.skills.add(new Skill("Thorned Whip", "Summons thorny vines to strike at an enemy. Lowers enemy AGI.", 25, ds.maxEnergy*0.4, 1, "DAMAGE"));
                gp.player.skills.add(new Skill("Bloom of Life", "Creates an explosion of natural energy that buffs and heals allies, and slightly damages the enemy.", ds.mag*6, ds.maxEnergy*0.8, 2, "BUFF_ALLY"));
            }

            gp.player.level = ds.level;
            gp.player.setName(ds.name);
            gp.player.worldX = ds.worldX;
            gp.player.worldY = ds.worldY;
            gp.player.spawnPointX = ds.spawnPointX;
            gp.player.spawnPointY = ds.spawnPointY;
            gp.player.speed = ds.speed;
            gp.player.tempSpeed = ds.tempSpeed;
            gp.player.attack = ds.attack;
            gp.player.defense = ds.defense;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.sizeIncrement = ds.sizeIncrement;
            gp.player.initialHP = ds.initialHP;
            gp.player.initialEnergy = ds.initialEnergy;
            gp.player.maxHP = ds.maxHP;
            gp.player.hp = ds.hp;
            gp.player.maxEnergy = ds.maxEnergy;
            gp.player.energy = ds.energy;
            gp.player.energyRegen = ds.energyRegen;
            gp.player.vit = ds.vit;
            gp.player.pow = ds.pow;
            gp.player.mag = ds.mag;
            gp.player.agi = ds.agi;
            gp.player.luck = ds.luck;

            gp.currentMap = ds.currentMap;


            System.out.println("Game loaded successfully!");

        } catch (Exception e) {
            System.out.println("Load exception: " + e.getMessage());
            e.printStackTrace();
            gp.gameState = gp.titleState;
        }
    }
}


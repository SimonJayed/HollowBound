package misc;

import entity.*;
import main.GamePanel;
import object.*;

public class AssetSetter {
    GamePanel gp;

    int buffer = 0;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }


    public void setPlayer(int choice){
        switch(choice){
            case 0:{
                gp.player.playing = "amaryllis";
                gp.companion1 = new NPC_Sylvie(gp);
                gp.companion2 = new NPC_Fort(gp);
                break;
            }
            case 1:{
                gp.player.playing = "fort";
                gp.companion1 = new NPC_Sylvie(gp);
                gp.companion2 = new NPC_Amaryllis(gp);
                break;
            }
            case 2: {
                gp.player.playing = "sylvie";
                gp.companion1 = new NPC_Fort(gp);
                gp.companion2 = new NPC_Amaryllis(gp);
                break;
            }
            default: {
                gp.player.playing = "fort";
                gp.companion1 = new NPC_Sylvie(gp);
                gp.companion2 = new NPC_Amaryllis(gp);
                break;
            }
        }
        gp.player.setDefaultValues();
    }

    public void setObjectEntity(){
        int mapNum = 0;
        int i= 0;

        //MAP1
        i = 0;



        //MAP2
        gp.foreground[2][i] = new OBJ_Door(gp);
        gp.foreground[2][i].worldX = 29 * gp.tileSize;
        gp.foreground[2][i].worldY = 22 * gp.tileSize;

        //MAP 5
        i = 0;


        //MAP 8
        i = 0;


    }

    public void setLivingEntity(){
        int mapNum = 0;
        int i = 0;

        //MAP 0
        gp.livingEntity[0][i] = new NPC_Miming(gp);
        gp.livingEntity[0][i].worldX = gp.livingEntity[0][i].spawnPointX = 46 * gp.tileSize;
        gp.livingEntity[0][i].worldY = gp.livingEntity[0][i].spawnPointY = 39 * gp.tileSize;
        gp.livingEntity[0][i].hasEvent = true;
        i++;


        //MAP 1


        //MAP 2


        //MAP 3
        i = 0;
        gp.livingEntity[3][i] = new NPC_MamaPausy(gp);
        gp.livingEntity[3][i].worldX = gp.livingEntity[3][i].spawnPointX = 46 * gp.tileSize;
        gp.livingEntity[3][i].worldY = gp.livingEntity[3][i].spawnPointY = 39 * gp.tileSize;
        gp.livingEntity[3][i].hasEvent = true;

        //MAP 4
        i = 0;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 32 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 18 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 33 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 18 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 34 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 19 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 34 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 21 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 40 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 18 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 41 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 19 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 20 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 15 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 21 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 15 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 22 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 15 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 33 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 15 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 34 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 15 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 34 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 32 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 35 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 32 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 36 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 32 * gp.tileSize;
        i++;
        gp.livingEntity[4][i] = new NPC_Miming(gp);
        gp.livingEntity[4][i].worldX = gp.livingEntity[4][i].spawnPointX = 36 * gp.tileSize;
        gp.livingEntity[4][i].worldY = gp.livingEntity[4][i].spawnPointY = 31 * gp.tileSize;


        //MAP 5

        //MAP 8
        i = 0;
        gp.livingEntity[8][i] = new NPC_VillagerSynthia(gp);
        gp.livingEntity[8][i].worldX = gp.livingEntity[8][i].spawnPointX = 7 * gp.tileSize;
        gp.livingEntity[8][i].worldY = gp.livingEntity[8][i].spawnPointY = 36 * gp.tileSize;
        i++;
        gp.livingEntity[8][i] = new NPC_VillagerOldMan(gp);
        gp.livingEntity[8][i].worldX = gp.livingEntity[8][i].spawnPointX = 38 * gp.tileSize;
        gp.livingEntity[8][i].worldY = gp.livingEntity[8][i].spawnPointY = 13 * gp.tileSize;
        i++;
    }

}

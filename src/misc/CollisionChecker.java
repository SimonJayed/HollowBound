package misc;

import entity.Entity;
import main.GamePanel;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction){
            case "up": {
                entityTopRow = (entityTopWorldY - (int) entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];

                if (gp.tileM.tile[tileNum1]. collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            }
            case "down": {
                entityBottomRow = (entityBottomWorldY + (int) entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1]. collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            }
            case "left": {
                entityLeftCol = (entityLeftWorldX - (int) entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1]. collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            }
            case "right": {
                entityRightCol = (entityRightWorldX + (int) entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1]. collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            }
        }
    }

    public int checkObject(Entity entity, boolean player){
        int index = 999;

        for (int i = 0; i < gp.objectEntity[1].length; i++){
            if (gp.objectEntity[gp.currentMap][i] != null && gp.objectEntity[gp.currentMap][i] != gp.foreground[gp.currentMap][i]){
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                gp.objectEntity[gp.currentMap][i].solidArea.x = gp.objectEntity[gp.currentMap][i].worldX + gp.objectEntity[gp.currentMap][i].solidArea.x;
                gp.objectEntity[gp.currentMap][i].solidArea.y = gp.objectEntity[gp.currentMap][i].worldY + gp.objectEntity[gp.currentMap][i].solidArea.y;

                switch(entity.direction){
                    case "up": {
                        entity.solidArea.y -= entity.speed;
                        break;
                    }
                    case "down": {
                        entity.solidArea.y += entity.speed;
                        break;
                    }
                    case "left": {
                        entity.solidArea.x -= entity.speed;
                        break;
                    }
                    case "right": {
                        entity.solidArea.x += entity.speed;
                        break;
                    }
                }
                if (entity.solidArea.intersects(gp.objectEntity[gp.currentMap][i].solidArea)){
                    if (gp.objectEntity[gp.currentMap][i].collision){
                        entity.collisionOn = true;
                    }
                    if (player){
                        index = i;
                    }
                }


                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.objectEntity[gp.currentMap][i].solidArea.x = gp.objectEntity[gp.currentMap][i].solidAreaDefaultX;
                gp.objectEntity[gp.currentMap][i].solidArea.y = gp.objectEntity[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkEntity(Entity entity, Entity[][] target){
        int index = 999;

        for (int i = 0; i < target[1].length; i++){
            if (target[gp.currentMap][i] != null && target[gp.currentMap][i] != gp.foreground[gp.currentMap][i]){
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                switch(entity.direction){
                    case "up": {
                        entity.solidArea.y -= entity.speed;
                        break;
                    }
                    case "down": {
                        entity.solidArea.y += entity.speed;
                        break;
                    }
                    case "left": {
                        entity.solidArea.x -= entity.speed;
                        break;
                    }
                    case "right": {
                        entity.solidArea.x += entity.speed;
                        break;
                    }
                }
                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if (target[gp.currentMap][i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity){
        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch(entity.direction){
            case "up": {
                entity.solidArea.y -= entity.speed;
                break;
            }
            case "down": {
                entity.solidArea.y += entity.speed;
                break;
            }
            case "left": {
                entity.solidArea.x -= entity.speed;
                break;
            }
            case "right": {
                entity.solidArea.x += entity.speed;
                break;
            }
        }
        if (entity.solidArea.intersects(gp.player.solidArea)){
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }


}

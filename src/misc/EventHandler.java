package misc;


import entity.Entity;
import main.GamePanel;

import java.util.Objects;

public class EventHandler{
    GamePanel gp;
    EventRect eventRect[][][];

    int previousEventX, previousEventY;

    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;

        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {

            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
                if(row == gp.maxWorldRow){
                    row = 0;
                    map++;
                }
            }
        }
    }

    public void checkEvent() {
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize){
            canTouchEvent = true;
        }
        //MAP 0 (Intro Forest) EVENTS
        if (canTouchEvent && gp.currentMap == 0){
            //GOING TO MAP 1 (Forest Path)
            if (hit(0,48, 42, "right") || hit(0,48, 43, "right") || hit(0,48, 43, "right") || hit(0,48, 44, "right") || hit(0,48, 45, "right") || hit(0,48, 46, "right")){
                teleport(gp.playState, 2, 45, "right");
            }
            //ENCOUNTERS
        }
        //MAP 1 (Forest Path) EVENTS
        if (canTouchEvent && gp.currentMap == 1){
            //GOING BACK TO MAP 0 (Intro Forest)
            if (hit(1,1, 44, "left") || hit(1,1, 45, "left") || hit(1,1, 46, "left") || hit(1,1, 47, "left")){
                teleport(gp.playState, 47, 44, "left");
            }

            //GOING TO MAP 2 (Cat Cave Entrance)
            if (hit(1,23, 44, "right")  || hit(1,23, 45, "right") || hit(1,23, 46, "right") || hit(1,23, 47, "right")){
                teleport(gp.playState, 2, 35, "right");
            }

            //ENCOUNTERS
        }
        //MAP 2 (Cat Cave Entrance) EVENTS
        if (canTouchEvent && gp.currentMap == 2){
            //GOING BACK TO MAP 1 (Forest Path)
            if (hit(2,1, 34, "left") || hit(2,1, 35, "left") || hit(2,1, 36, "left") || hit(2,1, 37, "left") || hit(2,1, 38, "left")){
                teleport(gp.playState, 22, 45, "left");
            }

            //GOING BACK TO MAP 4 (Cat Cave)
            if (hit(2,21, 21, "up") || hit(2,22, 21, "up") || hit(2,22, 22, "up") || hit(2,23, 20, "up") || hit(2,24, 20, "up") || hit(2,25, 20, "up") || hit(2,26, 20, "up") || hit(2,27, 20, "up") || hit(2,28, 20, "up") || hit(2,29, 21, "up") || hit(2,30, 22, "up")){
                teleport(gp.playState, 24, 37, "up");
                gp.ui.addMessage("Went into Cat Cave");
                gp.player.isSafe = true;
                if(!gp.player.event1Flag && !gp.player.event2Flag){
                    gp.event.eventNum = 1;
                    gp.gameState = gp.eventState;
                    gp.ui.startFadeIn();
                    gp.event.playEvent1();
                }
            }

            //GOING TO MAP 3 (Forest Path Near Village)
            if (hit(2,48, 35, "right") || hit(2,48, 36, "right") || hit(2,48, 37, "right") || hit(2,48, 38, "right")){
                teleport(gp.playState, 2, 42, "right");
            }

            //ENCOUNTERS
        }
        //MAP 3 (Forest Path Near Village) EVENTS
        if (canTouchEvent && gp.currentMap == 3){
            //GOING BACK TO MAP 2
            if (hit(3,1, 41, "left") || hit(3,1, 42, "left")  || hit(3,1, 43, "left") || hit(3,1, 44, "left") ){
                teleport(gp.playState, 48, 36, "left");
            }

            //GOING TO MAP 5 (Graveyard)
            if (hit(3,48, 38, "right") || hit(3,48, 39, "right") || hit(3,48, 40, "right") || hit(3,48, 41, "right")){
                teleport(gp.playState, 2, 19, "right");
                gp.ui.addMessage("Went to Graveyard");
            }

            //ENCOUNTERS
            if (hit(3,28, 39, "right") || hit(2,28, 40, "right") || hit(2,28, 41, "right") || hit(2,28, 42, "right")){
                gp.ui.addMessage("Ambushed by Mama Pussicles");
                if(!gp.player.event2Flag){
                    gp.event.eventNum = 2;
                    gp.gameState = gp.eventState;
                    gp.ui.startFadeIn();
                    System.out.println("Event 2 played");
                    gp.event.playEvent2();
                }
            }
        }
        //MAP 4 (Cat Cave) EVENTS
        if (canTouchEvent && gp.currentMap == 4){
            //GOING BACK TO MAP 3
            if (hit(4,20, 37, "down") || hit(3,21, 37, "down")  || hit(3,22, 38, "down") || hit(3,23, 38, "down") || hit(3,24, 38, "down")  || hit(3,25, 38, "down")  || hit(3,26, 38, "down")  || hit(3,27, 38, "down") || hit(3,28, 39, "down")){
                teleport(gp.playState, 25, 20, "down");
            }

            //GOING TO MAP 6
            if (hit(4,11, 13, "up")){
                teleport(gp.playState, 32, 47, "up");
                gp.player.isSafe = true;
            }

            //ENCOUNTERS
        }
        //MAP 5 (Graveyard) EVENTS
        if (canTouchEvent && gp.currentMap == 5){
            //GOING BACK TO MAP 3
            if (hit(5,1, 17, "left") || hit(5,1, 18, "left")  || hit(5,1, 19, "left") || hit(5,1, 20, "left")){
                teleport(gp.playState, 47, 39, "left");
            }

            //GOING TO MAP 8
            if (hit(5,31, 48, "down") || hit(5,32, 48, "down") || hit(5,33, 48, "down")){
                teleport(gp.playState, 28, 3, "down");
                gp.player.isSafe = true;
            }

            //ENCOUNTERS
        }
        //MAP 6 (Old Man's Forest) EVENTS
        if (canTouchEvent && gp.currentMap == 6){
            //GOING BACK TO MAP 4
            if (hit(6,32, 48, "down")){
                teleport(gp.playState, 11, 14, "down");
                gp.player.isSafe = true;
            }

            //GOING TO MAP 7
            if (hit(6,31, 48, "up") || hit(5,32, 48, "up") || hit(5,33, 48, "up")){
//                teleport(gp.playState, 28, 3, "up");
            }

            //ENCOUNTERS
        }
        //MAP 7 (Old Man's Hut) EVENTS
        if (canTouchEvent && gp.currentMap == 7){
            //GOING BACK TO MAP 3
            if (hit(5,1, 17, "left") || hit(5,1, 18, "left")  || hit(5,1, 19, "left") || hit(5,1, 20, "left")){
                teleport(gp.playState, 47, 39, "left");
            }

            //ENCOUNTERS
        }
        //MAP 8 (Village) EVENTS
        if (canTouchEvent && gp.currentMap == 8){
            //GOING BACK TO MAP 7
            if (hit(8,27, 1, "up") || hit(8,28, 1, "up")  || hit(8,29, 1, "up")){
                teleport(gp.playState, 32, 48, "up");
            }

            //GOING TO MAP 9
            if (hit(8,48, 28, "right") || hit(8,48, 29, "right") || hit(8,48, 30, "right")){
                teleport(gp.playState, 2, 43, "right");
            }

            //ENCOUNTERS
        }
        //MAP 9 (Veyra Hill) EVENTS
        if (canTouchEvent && gp.currentMap == 9){
            //GOING BACK TO MAP 8
            if (hit(9,1, 42, "left") || hit(9,1, 43, "left") || hit(9,1, 44, "left")){
                teleport(gp.playState, 47, 29, "left");
                gp.player.isSafe = true;
            }

            //GOING TO MAP 10
            if (hit(5,31, 48, "down") || hit(5,32, 48, "down") || hit(5,33, 48, "down")){
                teleport(gp.playState, 28, 3, "down");
                gp.ui.addMessage("Went to Village");
            }

            //ENCOUNTERS
        }
    }

    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y + 5;
        eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
        eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

        if (gp.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
        eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;

        return hit;
    }

    public void teleport(int gameState, int x, int y, String reqDirection) {
        gp.gameState = gameState;
        switch(gp.currentMap){
            case 0:{
                if(reqDirection.equals("right")){
                    gp.currentMap = 1;
                }
                gp.playMusic(5);
                break;
            }
            case 1:{
                if(reqDirection.equals("left")){
                    gp.currentMap = 0;
                }if(reqDirection.equals("right")){
                    gp.currentMap = 2;
                    System.out.println("Next Map");
                }
                gp.playMusic(6);
                break;
            }
            case 2:{
                if(reqDirection.equals("left")){
                    gp.currentMap = 1;
                }if(reqDirection.equals("up")){
                    gp.currentMap = 4;
                    System.out.println("Next Map");
                }if(reqDirection.equals("right")){
                    gp.currentMap = 3;
                    System.out.println("Next Map");
                }
                gp.playMusic(5);
                break;
            }
            case 3:{
                if(reqDirection.equals("left")){
                    gp.currentMap = 2;
                }if(reqDirection.equals("right")){
                    gp.currentMap = 5;
                    System.out.println("Next Map");
                }
                gp.playMusic(5);
                break;
            }
            case 4:{
                if(reqDirection.equals("down")){
                    gp.currentMap = 2;
                }if(reqDirection.equals("up")){
                    gp.currentMap = 6;
                    System.out.println("Next Map");
                }
                gp.playMusic(9);
                break;
            }
            case 5:{
                if(reqDirection.equals("left")){
                    gp.currentMap = 3;
                }if(reqDirection.equals("down")){
                    gp.currentMap = 8;
                    System.out.println("Next Map");
                }
                gp.playMusic(7);
                break;
            }
            case 6:{
                if(reqDirection.equals("down")){
                    gp.currentMap = 4;
                }
                break;
            }
//            case 7:{
//                if(reqDirection.equals("left")){
//                    gp.currentMap = 3;
//                }if(reqDirection.equals("down")){
//                    gp.currentMap = 8;
//                    System.out.println("Next Map");
//                }
//                break;
//            }
            case 8:{
                if(reqDirection.equals("up")){
                    gp.currentMap = 5;
                }if(reqDirection.equals("right")){
                    gp.currentMap = 9;
                    System.out.println("Next Map");
                }
                gp.playMusic(0);
                break;
            }
            case 9:{
                if(reqDirection.equals("left")){
                    gp.currentMap = 8;
                }if(reqDirection.equals("right")){
                    gp.currentMap = 10;
                    System.out.println("Next Map");
                }
                gp.playMusic(11);
                break;
            }
        }
        gp.player.worldX = gp.player.spawnPointX = gp.tileSize * x ;
        gp.player.worldY = gp.player.spawnPointY =  gp.tileSize * y;
        System.out.println("Map Changed");
        gp.player.isSafe = false;
        gp.player.encounterReset = true;
        gp.player.stepLimit = 0;
        gp.player.steps = 0;
        gp.ui.startFadeIn();
    }

    public void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.player.hp -= 100;

        if (Objects.equals(gp.player.direction, "up")) {
            gp.player.worldY += (gp.player.speed + 2);
        }
        if (Objects.equals(gp.player.direction, "down")) {
            gp.player.worldY -= (gp.player.speed + 2);
        }
        if (Objects.equals(gp.player.direction, "left")) {
            gp.player.worldX += (gp.player.speed + 2);
        }
        if (Objects.equals(gp.player.direction, "right")) {
            gp.player.worldX -= (gp.player.speed + 2);
        }
        canTouchEvent = false;
    }

    public void opponentEncounter(Entity[] entity,int index, int x, int y) {
        entity[gp.currentMap] = gp.livingEntity[gp.currentMap][index];
        entity[gp.currentMap].speed = 1;
        if(!entity[gp.currentMap].isDefeated){
            if(entity[gp.currentMap] != null && entity[gp.currentMap].isAlive){
                gp.gameState = gp.eventState;
                entity[gp.currentMap].isIdling = false;
            }
            else{
                entity[gp.currentMap] = null;
                System.out.println(entity[gp.currentMap] + " is no more.");
            }
            entity[gp.currentMap].solidArea.x = 0;
            entity[gp.currentMap].solidArea.y = 0;
            gp.battleScreen.canEscape = false;
            canTouchEvent = false;
            eventRect[gp.currentMap][x][y].eventDone = true;
        }
        else{
            canTouchEvent = true;
        }

    }
}


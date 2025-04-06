package misc;

import entity.Entity;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed,
            enterPressed, shiftPressed, tabPressed, ePressed,
            qPressed, mPressed, spacePressed, zeroPressed;


    public boolean showDebugTest = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TITLESTATE
        if(!gp.ui.fading){
            if (gp.gameState == gp.titleState) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.titleScreen.commandNum--;
                    if (gp.titleScreen.commandNum < 0) {
                        gp.titleScreen.commandNum = 2;
                    }
                    gp.playSoundEffect(3);
                    gp.sound.setVolume(-20.0f);
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.titleScreen.commandNum++;
                    if (gp.titleScreen.commandNum > 2) {
                        gp.titleScreen.commandNum = 0;
                    }
                    gp.playSoundEffect(3);
                    gp.sound.setVolume(-20.0f);
                }
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    if (gp.titleScreen.commandNum == 0) {
                        System.out.println("NEW GAME");
                        gp.ui.startFadeIn();
                        gp.gameState = gp.characterPickState;
                        gp.titleScreen.emptyImages();
                        gp.sound.setVolume(-25.0f);
                    }
                    if (gp.titleScreen.commandNum == 1) {
                        if(gp.player.hasSave) {
                            gp.saveLoad.load();
                            gp.gameState = gp.playState;
                        }
                        else{
                            gp.ui.addMessage("No Save File.");
                            gp.ui.drawMessage(gp.tileSize, gp.tileSize, 35);
                        }
                    }
                    if (gp.titleScreen.commandNum == 2) {

                    }
                    if (gp.titleScreen.commandNum == 3) {
                        System.exit(0);
                    }
                }
            }
            //CHARACTER PICK
            else if(gp.gameState == gp.characterPickState) {
                if(!gp.pickScreen.isPicking){
                    if(code == KeyEvent.VK_W){
                        gp.pickScreen.commandNum = 3;
                    }
                    else if(code == KeyEvent.VK_S){
                        gp.pickScreen.commandNum = 0;
                    }
                    else if (code == KeyEvent.VK_A) {
                        gp.pickScreen.commandNum--;
                        System.out.println(gp.pickScreen.commandNum);
                        if (gp.pickScreen.commandNum < 0) {
                            gp.pickScreen.commandNum = 0;
                        }
                        gp.playSoundEffect(3);
                        gp.sound.setVolume(-20.0f);
                    }
                    else if (code == KeyEvent.VK_D) {
                        gp.pickScreen.commandNum++;
                        if (gp.pickScreen.commandNum > 2) {
                            gp.pickScreen.commandNum = 2;
                        }
                        gp.playSoundEffect(3);
                        gp.sound.setVolume(-20.0f);
                    }
                    if (code == KeyEvent.VK_ESCAPE) {
                        gp.ui.startFadeIn();
                        gp.gameState = gp.titleState;
                    }
                }
                else{
                    if(code == KeyEvent.VK_W){
                        gp.pickScreen.commandNum = 3;
                    }
                    if (code == KeyEvent.VK_ESCAPE) {
                        gp.ui.startFadeIn();
                        gp.pickScreen.isPicking = false;
                    }
                }
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    if(gp.pickScreen.isPicking){
                        if (gp.pickScreen.commandNum == 0) {
                            gp.aSetter.setPlayer(0);
                        }
                        if (gp.pickScreen.commandNum == 1) {
                            gp.aSetter.setPlayer(1);
                            gp.ui.startFadeIn();

                            System.out.println("Event 0 played");
                            gp.gameState = gp.eventState;
                            gp.ui.startFadeIn();
                            gp.event.playEvent0();
                        }
                        if (gp.pickScreen.commandNum == 2) {
                            gp.aSetter.setPlayer(2);
                            gp.ui.startFadeIn();
                        }
                        gp.pickScreen.emptyImages();
                    }
                    else{
                        gp.pickScreen.isPicking = true;
                        if (gp.pickScreen.commandNum == 3) {
                            gp.ui.startFadeOut();
                            gp.gameState = gp.titleState;
                        }
                    }
                }
            }
            //BATTLESTATE
            else if (gp.gameState == gp.battleState) {
                if(gp.battleScreen.battleFinished){
                    if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER){
                        gp.gameState = gp.playState;
                        gp.ui.startFadeIn();
                    }
                }

                if(gp.battleScreen.buffer>10  && !gp.battleScreen.isEnemyTurn){
                    if(!gp.battleScreen.isAttacking && !gp.battleScreen.isChoosingSkill){
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            gp.battleScreen.battleCommandNum--;
                            if (gp.battleScreen.battleCommandNum < 0) {
                                gp.battleScreen.battleCommandNum = 3;
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            gp.battleScreen.battleCommandNum++;
                            if (gp.battleScreen.battleCommandNum > 3) {
                                gp.battleScreen.battleCommandNum = 0;
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                            if (gp.battleScreen.battleCommandNum == 0) {
                                gp.battleScreen.attack();
                            }
                            if (gp.battleScreen.battleCommandNum == 1) {
                                gp.battleScreen.skill();
                            }
                            if (gp.battleScreen.battleCommandNum == 3) {
                                if(gp.battleScreen.canEscape){
                                    int num = gp.randomize(1, 8);
                                    if(num == 8){
                                        gp.player.invincible = true;
                                        gp.gameState = gp.playState;
                                    }
                                    else {
                                        gp.battleScreen.battleCommandNum = 0;
                                        gp.battleScreen.currentTurnFinished = true;
                                        gp.ui.addMessage("Escape Failed");
                                    }
                                }
                                else{
                                    gp.ui.addMessage("Can't Escape");
                                }
                                gp.battleScreen.isAttacking = false;
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                    }
                    else if (gp.battleScreen.isChoosingSkill && !gp.battleScreen.isAttacking) {
                        System.out.println("Choosing Skill");
                        if (gp.battleScreen.isPickingAlly) {
                            System.out.println("Picking ALly");
                            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                                gp.battleScreen.battleCommandNum--;
                                if (gp.battleScreen.battleCommandNum < 0) {
                                    gp.battleScreen.battleCommandNum = gp.battleScreen.selectableAllies.size() - 1;
                                }
                                gp.playSoundEffect(3);
                                gp.sound.setVolume(-20.0f);
                            }

                            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                                gp.battleScreen.battleCommandNum++;
                                if (gp.battleScreen.battleCommandNum > gp.battleScreen.selectableAllies.size() - 1) {
                                    gp.battleScreen.battleCommandNum = 0;
                                }
                                gp.playSoundEffect(3);
                                gp.sound.setVolume(-20.0f);
                            }

                            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                                if (gp.battleScreen.battleCommandNum == 0) {
                                    gp.battleScreen.battleQueue.get(gp.battleScreen.currentTurn).useSkill(gp.battleScreen.selectedSkill, gp.battleScreen.selectableAllies.get(0));
                                }
                                if (gp.battleScreen.battleCommandNum == 1) {
                                    gp.battleScreen.battleQueue.get(gp.battleScreen.currentTurn).useSkill(gp.battleScreen.selectedSkill, gp.battleScreen.selectableAllies.get(1));
                                }
                                if (gp.battleScreen.battleCommandNum == 2) {
                                    gp.battleScreen.battleQueue.get(gp.battleScreen.currentTurn).useSkill(gp.battleScreen.selectedSkill, gp.battleScreen.selectableAllies.get(2));
                                }
                                gp.playSoundEffect(3);
                                gp.sound.setVolume(-20.0f);
                                gp.battleScreen.isChoosingSkill = false;
                                gp.battleScreen.currentTurnFinished = true;
                                gp.battleScreen.battleCommandNum = 0;
                                gp.battleScreen.selectedSkill = 0;
                            }

                            if (code == KeyEvent.VK_ESCAPE) {
                                gp.battleScreen.isPickingAlly = false;
                                gp.battleScreen.selectedSkill = 0;
                            }
                        }
                        else{
                            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                                gp.battleScreen.battleCommandNum--;
                                if (gp.battleScreen.battleCommandNum < 0) {
                                    gp.battleScreen.battleCommandNum = 2;
                                }
                                gp.playSoundEffect(3);
                                gp.sound.setVolume(-20.0f);
                            }
                            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                                gp.battleScreen.battleCommandNum++;
                                if (gp.battleScreen.battleCommandNum > 2) {
                                    gp.battleScreen.battleCommandNum = 0;
                                }
                                gp.playSoundEffect(3);
                                gp.sound.setVolume(-20.0f);
                            }
                            if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                                if(gp.battleScreen.battleCommandNum == 0){
                                    gp.battleScreen.useSkill(0);
                                }
                                if(gp.battleScreen.battleCommandNum == 1){
                                    gp.battleScreen.useSkill(1);
                                }
                                if(gp.battleScreen.battleCommandNum == 2){
                                    gp.battleScreen.useSkill(2);
                                }
                                gp.playSoundEffect(3);
                                gp.sound.setVolume(-20.0f);
                            }
                        }
                        if(code == KeyEvent.VK_ESCAPE){
                            gp.battleScreen.isChoosingSkill = false;
                            gp.battleScreen.battleCommandNum = 1;
                        }

                    }
                    else if(gp.battleScreen.isAttacking && !gp.battleScreen.isChoosingSkill){
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            gp.battleScreen.battleCommandNum--;
                            if (gp.battleScreen.battleCommandNum < 0) {
                                gp.battleScreen.battleCommandNum = 2;
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            gp.battleScreen.battleCommandNum++;
                            if (gp.battleScreen.battleCommandNum > 2) {
                                gp.battleScreen.battleCommandNum = 0;
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                            if(gp.battleScreen.battleCommandNum == 0){
                                gp.battleScreen.playerAttack("HEAD");
                            }
                            if(gp.battleScreen.battleCommandNum == 1){
                                gp.battleScreen.playerAttack("TORSO");
                            }
                            if(gp.battleScreen.battleCommandNum == 2){
                                gp.battleScreen.playerAttack("LEGS");
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                        if(code == KeyEvent.VK_ESCAPE){
                            gp.battleScreen.isAttacking = false;
                        }
                    }
                }

            }
            //PLAYSTATE
            else if (gp.gameState == gp.playState && !gp.player.isDefeated) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    upPressed = true;
                }
                if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                    leftPressed = true;
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    downPressed = true;
                }
                if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                    rightPressed = true;
                }

                if (code == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                }
                if (code == KeyEvent.VK_TAB) {
                    tabPressed = !tabPressed;
                }
                if (code == KeyEvent.VK_ENTER) {
                    enterPressed = !enterPressed;
                }
                if (code == KeyEvent.VK_SPACE) {
                    spacePressed = true;
                }
                if (code == KeyEvent.VK_0) {
                    zeroPressed = !zeroPressed;
                }
                if (code == KeyEvent.VK_I || code == KeyEvent.VK_ESCAPE) {
                    gp.gameState = gp.inventoryState;
                    System.out.println("Inventory opened");
                }
                if (code == KeyEvent.VK_M ) {
                    gp.gameState = gp.mapState;
                    gp.map.miniMapOn = false;
                    System.out.println("Map opened");
                }
            }
            else if (gp.gameState == gp.dialogueState) {
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    gp.gameState = gp.playState;
                }
            }
            else if(gp.gameState == gp.eventState){
                if(gp.event.dialogueOn && gp.event.canClick){
                    if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                        gp.event.nextDialogue();
                        gp.ui.startFadeIn();
                    }
                }
            }
            else if (gp.gameState == gp.inventoryState) {
                if(gp.inventoryScreen.isStatPanel){
                    if(gp.player.statPoints > 0){
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            gp.inventoryScreen.commandNum--;
                            if (gp.inventoryScreen.commandNum < 0) {
                                gp.inventoryScreen.commandNum = 4;
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            gp.inventoryScreen.commandNum++;
                            if (gp.inventoryScreen.commandNum > 4) {
                                gp.inventoryScreen.commandNum = 0;
                            }
                            gp.playSoundEffect(3);
                            gp.sound.setVolume(-20.0f);
                        }
                        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                            if(gp.inventoryScreen.commandNum == 0){
                                gp.player.vit++;
                            }
                            if(gp.inventoryScreen.commandNum == 1){
                                gp.player.pow++;
                            }
                            if(gp.inventoryScreen.commandNum == 2){
                                gp.player.mag++;
                            }
                            if(gp.inventoryScreen.commandNum == 3){
                                gp.player.agi++;
                            }
                            if(gp.inventoryScreen.commandNum == 4){
                                gp.player.luck++;
                            }
                            gp.player.statPoints--;
                        }
                    }

                    if(code == KeyEvent.VK_Q){
                        gp.inventoryScreen.charNum--;
                        if(gp.inventoryScreen.charNum < 0){
                            gp.inventoryScreen.charNum = 2;
                        }
                    }
                    if(code == KeyEvent.VK_R){
                        gp.inventoryScreen.charNum++;
                        if(gp.inventoryScreen.charNum > 2){
                            gp.inventoryScreen.charNum = 0;
                        }
                    }
                }else if(gp.inventoryScreen.isSettings){
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                        gp.inventoryScreen.commandNum--;
                        if (gp.inventoryScreen.commandNum < 0) {
                            gp.inventoryScreen.commandNum = 4;
                        }
                        gp.playSoundEffect(3);
                        gp.sound.setVolume(-20.0f);
                    }
                    if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                        gp.inventoryScreen.commandNum++;
                        if (gp.inventoryScreen.commandNum > 4) {
                            gp.inventoryScreen.commandNum = 0;
                        }
                        gp.playSoundEffect(3);
                        gp.sound.setVolume(-20.0f);
                    }
                    if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                        if(gp.inventoryScreen.commandNum == 0){

                        }
                        if(gp.inventoryScreen.commandNum == 1){

                        }
                        if(gp.inventoryScreen.commandNum == 2){

                        }
                        if(gp.inventoryScreen.commandNum == 3){
                            try {
                                gp.saveLoad.save();
                                gp.player.hasSave = true;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if(gp.inventoryScreen.commandNum == 4){
                            gp.gameState = gp.titleState;
                        }
                    }

                }
                if(code == KeyEvent.VK_1){
                    gp.inventoryScreen.isInventory = true;
                    gp.inventoryScreen.isStatPanel = false;
                    gp.inventoryScreen.isSettings = false;
                }
                if(code == KeyEvent.VK_2){
                    gp.inventoryScreen.isStatPanel = true;
                    gp.inventoryScreen.isSettings = false;
                    gp.inventoryScreen.isInventory = false;
                }
                if(code == KeyEvent.VK_3){
                    gp.inventoryScreen.isSettings = true;
                    gp.inventoryScreen.isInventory = false;
                    gp.inventoryScreen.isStatPanel = false;
                }
                if (code == KeyEvent.VK_I || code == KeyEvent.VK_ESCAPE) {
                    gp.gameState = gp.playState;
                    System.out.println("Inventory closed");
                }
            }

            else if (gp.gameState == gp.mapState) {
                if (code == KeyEvent.VK_M || code == KeyEvent.VK_ESCAPE) {
                    gp.gameState = gp.playState;
                    System.out.println("Map closed");
                }
            }
        }




        if (code == KeyEvent.VK_T) {
            if (!showDebugTest) {
                showDebugTest = true;
            } else {
                showDebugTest = false;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            upPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            downPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_Q){
            qPressed = false;
        }
        if (code == KeyEvent.VK_E){
            ePressed = false;
        }
        if (code == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
}

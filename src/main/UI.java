package main;

import entity.Entity;
import object.OBJ_Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    BufferedImage titleBackground;

    public boolean messageOn = false;
    ArrayList <String> message = new ArrayList<>();
    ArrayList <Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;

    public String currentDialogue = "";
    public int commandNum = 0;

    public boolean toggleTime = true;

    public int fadeAlpha = 0;
    public boolean fading = false;
    public boolean fadeIn = false;
    public boolean fadeOut = false;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp){
        this.gp = gp;
//        Entity heart = new OBJ_Heart(gp);
//
//        heart_full = heart.image1;
//        heart_half = heart.image2;
//        heart_blank = heart.image3;
    }

    public void addMessage(String text){
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(gp.customFont);
        g2.setColor(Color.white);

        if (gameFinished && !messageOn) {
            gp.gameThread = null;
        }
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            gp.map.miniMapOn = true;
            playTime += (double) 1 / 60;
            drawMessage();
        }
        else if (gp.gameState == gp.titleState) {
            gp.titleScreen.draw(g2);
        }
        else if(gp.gameState == gp.inventoryState){
            gp.map.miniMapOn = true;
            drawPlayerLife();
            gp.inventoryScreen.draw(g2);
        }
        else if (gp.gameState == gp.newGameState) {
            drawNewGameScreen();
        }
        else if (gp.gameState == gp.loadGameState) {
            drawLoadGameScreen();
        }
        else if (gp.gameState == gp.settingsState) {
            drawSettingsScreen();
        }
        else if (gp.gameState == gp.characterPickState) {
            gp.pickScreen.draw(g2);
        }
        else if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
        else if (gp.gameState == gp.battleState) {
            gp.battleScreen.draw(g2);
        }
        else if(gp.gameState == gp.mapState){
            gp.map.drawFullMapScreen(g2);
        }
        else if(gp.gameState == gp.eventState){
            gp.map.miniMapOn = false;
        }
        fadeToBlack(g2);
    }


    public void drawPlayerLife(){

        int x = 0;
        int y = 0;

        String text = gp.player.getName() + "    Lvl. " + gp.player.level;
        g2.setColor(new Color(255, 255, 255));
        g2.fillRoundRect(x-3, y, text.length()*(gp.tileSize/3)-35, 5, 5, 5);
        g2.setColor(new Color(255, 255, 255));
        g2.fillRoundRect(x-2, y+5, text.length()*(gp.tileSize/3)-30, 5, 5, 5);
        g2.setColor(new Color(255, 255, 255));
        g2.fillRoundRect(x-1, y+10, text.length()*(gp.tileSize/3)-25, 5,5, 5);
        g2.setColor(new Color(255, 255, 255));
        g2.fillRoundRect(x, y+15, text.length()*(gp.tileSize/3)-20, 5,5, 5);

        g2.setFont(g2.getFont().deriveFont( 14f));
        g2.setColor(new Color(0, 0, 0));
        g2.drawString(text, x+10, y+15);

        g2.setFont(g2.getFont().deriveFont( 15f));
        g2.setColor(new Color(0, 0, 0));
        g2.drawString(Integer.toString(gp.player.hollowCounter), x+gp.tileSize*3+1, y+16);
        g2.setColor(new Color(255, 0, 0));
        g2.drawString(Integer.toString(gp.player.hollowCounter), x+gp.tileSize*3, y+15);


        y += gp.tileSize/3+2;

        double oneScale = (gp.tileSize*5)/gp.player.maxHP;
        double hpBarValue = oneScale * gp.player.hp;

        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(x, y, gp.tileSize*5+10, 20);

        g2.setColor(new Color(255,0,30));
        g2.fillRect(x+3, y+2, (int) hpBarValue+4, 16);

        text = gp.df.format(gp.player.hp) + "/" + gp.df.format(gp.player.maxHP);
        g2.setFont(g2.getFont().deriveFont( 14f));
        g2.setColor(new Color(0, 0, 0));
        g2.drawString(text, x+5, y+15);

        y += gp.tileSize/3+2;

        double oneScale1 = (gp.tileSize*5)/gp.player.maxEnergy;
        double energyBarValue = oneScale1 * gp.player.energy;

        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(x, y, gp.tileSize*5+3, 18);

        g2.setColor(new Color(255, 227, 24));
        g2.fillRect(x+3, y, (int) energyBarValue-3, 16);

        text = gp.df.format(gp.player.energy) + "/" + gp.df.format(gp.player.maxEnergy);
        g2.setFont(g2.getFont().deriveFont( 14f));
        g2.setColor(new Color(0, 0, 0));
        g2.drawString(text, x+5, y+13);

        y = gp.screenHeight-18;

        double oneScale2 = gp.screenWidth/gp.player.nextLevelExp;
        double expBarValue = oneScale2 * gp.player.exp;

        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(x, y, gp.screenWidth, 24);

        g2.setColor(new Color(70, 255, 36));
        g2.fillRect(x, y+3, (int) expBarValue, 14);

        text = gp.df.format(gp.player.exp) + "/" + gp.df.format(gp.player.nextLevelExp);
        String text2 = " (" + gp.df.format(gp.player.exp/gp.player.nextLevelExp*100) + "%)";
        x = getXforCenteredText(g2, text);
        g2.setFont(g2.getFont().deriveFont( 14f));
        g2.setColor(new Color(0, 0, 0));
        g2.drawString(text + text2, x, y+15);
    }

    public void drawMessage(){
        int messageX = gp.tileSize/2;
        int messageY = gp.tileSize*2;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));

        for (int i = 0; i < message.size(); i++){

            if (message.get(i) != null){

                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;

                if (messageCounter.get(i) > 250){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    public void drawNewGameScreen(){

    }
    public void drawLoadGameScreen(){

    }
    public void drawSettingsScreen(){

    }

    public void drawDialogueScreen() {
        int width = gp.screenWidth - (gp.tileSize * 2);
        int height = gp.tileSize * 4;
        int x = gp.tileSize;
        int y = gp.tileSize * 10;

        drawSubWindow(x, y, width, height);

        x += gp.tileSize;
        y += gp.tileSize;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
        g2.setColor(Color.white);
        if(currentDialogue != null){
            for (String line : currentDialogue.split("\n")) {
                g2.drawString(line, x, y);
                y += 40;
            }
        }
        else{
            currentDialogue = "...";
            for (String line : currentDialogue.split("\n")) {
                g2.drawString(line, x, y);
                y += 40;
            }
        }
    }

    public void startFadeOut() {
        fading = true;
        fadeOut = true;
        fadeIn = false;
        fadeAlpha = 0;
    }

    public void startFadeIn() {
        fading = true;
        fadeIn = true;
        fadeOut = false;
        fadeAlpha = 255;
    }

    public void fadeToBlack(Graphics2D g2) {
        if (!fading) return;

        if (fadeOut) {
            if(gp.gameState == gp.titleState){
                fadeAlpha += 85;
            }else{
                fadeAlpha += 50;
            }
            if (fadeAlpha >= 255) {
                fadeAlpha = 255;
                fading = false;
            }
        } else if (fadeIn) {
            if(gp.gameState == gp.titleState){
                fadeAlpha -= 85;
            }else {
                fadeAlpha -= 10;
            }
            if (fadeAlpha <= 0) {
                fadeAlpha = 0;
                fading = false;
            }
        }

        g2.setColor(new Color(0, 0, 0, fadeAlpha));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }

    public int getXforCenteredText(Graphics2D g2, String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0, 0, 0, 150);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height,20, 20);

        g2.setColor(new Color(255,255,255));
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 20, 20);
    }
}

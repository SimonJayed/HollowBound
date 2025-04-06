package main;

import data.SaveLoad;
import entity.Entity;
import entity.Player;
import misc.AssetSetter;
import misc.CollisionChecker;
import misc.EventHandler;
import misc.KeyHandler;
import screen.*;
import tile.Map;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxscreenCol = 18;
    public final int maxscreenRow = 14;

    public final int screenWidth = tileSize * maxscreenCol;
    public final int screenHeight = tileSize * maxscreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;

    public double FPS = 60;
    public SaveLoad saveLoad;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Sound sound = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public Map map = new Map(this);
    public EventScenes event = new EventScenes(this);
    public Thread gameThread;

    public Player player = new Player(this, keyH);
    public Entity companion1, companion2;
    public Entity objectEntity[][] = new Entity[maxMap][30];
    public Entity foreground[][] = new Entity[maxMap][30];
    public Entity livingEntity[][] = new Entity[maxMap][20];
    public ArrayList <Entity> entityList = new ArrayList<>();

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int dialogueState = 2;
    public final int inventoryState = 3;
    public final int loadGameState = 4;
    public final int characterPickState = 5;
    public final int battleState = 6;
    public final int mapState = 7;
    public final int eventState = 8;

    public TitleScreen titleScreen = new TitleScreen(this);
    public CharacterPickScreen pickScreen = new CharacterPickScreen(this);
    public BattleScreen battleScreen = new BattleScreen(this);
    public InventoryScreen inventoryScreen = new InventoryScreen(this);

    public Font customFont;

    public DecimalFormat df = new DecimalFormat("#,##0.#");

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        saveLoad = new SaveLoad(this);
    }

    public void setupGame(){
        ui.startFadeIn();
        ui.loadImages();
        aSetter.setObjectEntity();
        aSetter.setLivingEntity();
        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){
        double drawInterval;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null){
            drawInterval = 1_000_000_000.0 / FPS;
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1){
                try{
                    update();
                    repaint();
                    delta-=1;
                    drawCount++;
                } catch (ArrayIndexOutOfBoundsException e){
                }

            }

            if (timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        if (gameState == playState) {
            player.update();
        }
        if(gameState == playState || gameState == eventState){
            for(Entity entity: livingEntity[currentMap]){
                if (entity != null){
                    entity.update();
                    if (!entity.isAlive){
                        entity = null;
                        entityList.remove(entity);
                    }
                    if(gameState == eventState){
                        System.out.println("Updating Event");
                        event.updateEvent();
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/font1.ttf");
            assert fontStream != null;
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 40);
        }

        g2.setFont(customFont);

        long drawStart = 0;
        if (keyH.showDebugTest){
            drawStart = System.nanoTime();
        }
        if(gameState != playState && gameState != inventoryState && gameState != eventState && gameState != dialogueState){
            ui.draw(g2);
        }
        else{
            //PLAY STATE
            tileM.draw(g2);

            entityList.clear();
            entityList.add(player);

            for(int i = 0; i < livingEntity[1].length; i++) {
                if (livingEntity[currentMap][i] != null && livingEntity[currentMap][i].isAlive) {
                    entityList.add(livingEntity[currentMap][i]);
                }
            }
            for(int i = 0; i < objectEntity[1].length; i++) {
                if (objectEntity[currentMap][i] != null) {
                    entityList.add(objectEntity[currentMap][i]);
                }
            }



            entityList.sort(new Comparator<>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            for(int i = 0; i < foreground[1].length; i++) {
                if (foreground[currentMap][i] != null) {
                    entityList.add(foreground[currentMap][i]);
                }
            }

            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            entityList.clear();



            if(gameState == eventState){
                event.draw(g2);
            }
            ui.draw(g2);

            map.drawMiniMap(g2);

            if (keyH.showDebugTest){
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;

                g2.setFont(g2.getFont().deriveFont(23f));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;

                g2.drawString("WorldX" + player.worldX, x, y);y += lineHeight;
                g2.drawString("WorldY" + player.worldY, x, y);y += lineHeight;
                g2.drawString("Col" + (player.worldX+ player.solidArea.x)/tileSize, x, y);y += lineHeight;
                g2.drawString("Row" + (player.worldY+ player.solidArea.y)/tileSize, x, y);y += lineHeight;
                g2.drawString("Event" + event.eventNum, x, y);y += lineHeight;
                g2.drawString("Draw Time: " + passed , x, y );
            }
        }

        g2.dispose();
    }

    public int randomize(int min, int max) {
        Random num = new Random();
        if (min > max) {
            throw new IllegalArgumentException("min should be less than or equal to max");
        }
        return num.nextInt((max - min) + 1) + min;
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void stopMusic(){
        sound.stop();
    }
    public void playSoundEffect(int i){
        sound.setFile(i);
        sound.play();
    }
}

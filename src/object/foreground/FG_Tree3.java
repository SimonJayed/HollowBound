package object.foreground;

import entity.Entity;
import main.GamePanel;

public class FG_Tree3 extends Entity {

    public FG_Tree3(GamePanel gp){
        super(gp);

        setName("Tree3");
        down1 = setup("/objects/foreground/treeForeground3", gp.tileSize*6, gp.tileSize*6);
        down2 = setup("/objects/foreground/treeForeground3", gp.tileSize*6, gp.tileSize*6);
        collision = false;
        collisionOn = false;

        this.solidArea.x = 0;
        this.solidArea.y = 0;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.solidArea.width = 0;
        this.solidArea.height = 0;
    }
}
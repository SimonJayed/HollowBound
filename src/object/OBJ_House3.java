package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_House3 extends Entity {

    public OBJ_House3(GamePanel gp){
        super(gp);

        setName("House3");
        down1 = setup("/objects/balay3", gp.tileSize*6, gp.tileSize*5);
        down2 = setup("/objects/balay3", gp.tileSize*6, gp.tileSize*5);
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
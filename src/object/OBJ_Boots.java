package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

    public OBJ_Boots(GamePanel gp){
        super(gp);

        setName("Boots");
        down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
        down2 = setup("/objects/boots", gp.tileSize, gp.tileSize);
    }
}

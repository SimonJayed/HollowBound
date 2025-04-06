package screen;

import entity.Entity;
import entity.components.Skill;
import main.GamePanel;
import misc.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class BattleScreen implements Screen{
    GamePanel gp;
    Graphics2D g2;


    BufferedImage background = null;
    BufferedImage actionWindow = null;
    BufferedImage actionWindow1 = null;
    BufferedImage heart = null;
    BufferedImage energy = null;
    BufferedImage transition = null;

    public ArrayList<Entity> battleQueue = new ArrayList<>();
    public ArrayList<Entity> selectableAllies = new ArrayList<>();
    public Entity currentEnemy;
    public int currentTurn = 0;
    public int battleCommandNum = 0;
    public int selectedSkill = 0;
    public int roundNum = 0;
    public int prevRoundNum = -1;

    public double output = 0;
    public int buffer = 0;
    public int damageBuffer = 0;

    public boolean isAttacking = false;
    public boolean isChoosingSkill = false;
    public boolean isPickingAlly = false;
    public boolean canEscape = true;
    private boolean battleActive = false;
    public boolean currentTurnFinished = false;
    public boolean isEnemyTurn = false;
    public boolean battleFinished = false;

    public BattleScreen(GamePanel gp){
        this.gp = gp;
    }

    public void loadImages(){
        UtilityTool uTool = new UtilityTool();
        try {
            if(gp.currentMap == 0){
                background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/background/battle/forestIntro.png")));
            }
            else{
                background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/background/battle/corruptedForest.png")));
            }
            transition = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/background/battle/FIGHT.png")));
            transition = uTool.scaleImage(transition, gp.screenWidth, gp.screenHeight);
            actionWindow = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/actionWindow2.png")));
            actionWindow = uTool.scaleImage(actionWindow, gp.screenWidth, gp.screenHeight);
            actionWindow1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/actionWindow1.png")));
            actionWindow1 = uTool.scaleImage(actionWindow1, gp.screenWidth, gp.screenHeight);
            heart = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/heart.png")));
            heart = uTool.scaleImage(heart, gp.tileSize/2, gp.tileSize/2);
            energy = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/energy.png")));
            energy = uTool.scaleImage(energy, gp.tileSize/2, gp.tileSize/2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emptyImages(){
        background = null;
        actionWindow = null;
        actionWindow1 = null;
        heart = null;
        energy = null;
        transition = null;
    }

    public void startBattle(Entity enemy) {
        gp.ui.startFadeIn();
        if(!gp.ui.fading){
            gp.stopMusic();
        }
        gp.playMusic(8);
        gp.sound.setVolume(-20f);
        battleFinished = false;
        gp.gameState = gp.battleState;
        currentTurn = 0;
        buffer = 0;
        battleCommandNum = 0;
        loadImages();
        currentEnemy = enemy;
        battleQueue.clear();
        if(!gp.player.event0Flag){
            battleQueue.add(gp.player);
        }
        else{
            battleQueue.add(gp.player);
            battleQueue.add(gp.companion1);
            battleQueue.add(gp.companion2);
        }
        battleQueue.add(currentEnemy);


        battleQueue.sort(Comparator.comparingInt(e -> (int) -e.agi));

        battleActive = true;
    }

    @Override
    public void draw(Graphics2D g2){
        this.g2 = g2;
        if(gp.ui.fading){
            g2.drawImage(transition, 0, 0, null);
            return;
        }
        g2.setColor(Color.white);
        g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

        drawEnemy(g2);

        drawPartyMembers(g2);


        if(!battleFinished){
            gp.ui.drawSubWindow(gp.tileSize*6, 0, gp.tileSize*12, gp.tileSize*3);
            gp.ui.drawSubWindow(gp.tileSize*6, gp.screenHeight-gp.tileSize*4, gp.tileSize*12, gp.tileSize*4);
            gp.ui.drawSubWindow(gp.tileSize-3, gp.tileSize/2-10, gp.tileSize*battleQueue.size()+3, gp.tileSize*2+15);

            update();

            gp.ui.drawMessage(gp.tileSize*6 + 10, gp.tileSize/2 + 10, 35);
            gp.ui.drawDescription(gp.tileSize*6 + 10, gp.screenHeight-gp.tileSize*3-gp.tileSize/2, 35);
            drawTurnQueue(g2);
        }
        else{
            gp.ui.drawSubWindow(gp.tileSize*6, gp.screenHeight-gp.tileSize*4, gp.tileSize*12, gp.tileSize*4);
            gp.ui.drawMessage(gp.ui.getXforCenteredText(gp.ui.message.getFirst()), gp.screenHeight+gp.tileSize, 50);
        }






    }

    public void update() {
        if (!battleActive) {
            return;
        }
        battleQueue.sort(Comparator.comparingInt(e -> (int) -e.agi));

        buffer++;
        if(currentTurnFinished){
            currentTurn = (currentTurn + 1) % battleQueue.size();
            currentTurnFinished = false;
            buffer = 0;

            if (currentTurn == 0) {
                roundNum++;

                for (Entity member : battleQueue) {
                    member.energy += member.energyRegen/2;
                    if (member.energy > member.maxEnergy) {
                        member.energy = member.maxEnergy;
                    }
                    for (Skill skill : member.skills) {
                        skill.updateCooldown();
                    }
                    if(member.healing > 0){
                        member.healing--;
                    }
                    if(member.strengthened > 0){
                        member.strengthened--;
                    }else{
                        if(member.tempAtk > 10){
                            member.attack = member.tempAtk;
                        }
                    }
                    if(member.hardened > 0){
                        System.out.println(member.defense + " and " + member.tempDef);
                        member.hardened--;
                    }
                    else{
                        if(member.tempDef > 10){
                            member.defense = member.tempDef;
                            System.out.println("Defense: " + member.defense + " and Enemy defens: " + currentEnemy.defense);
                        }

                    }
                    if(member.aggro > 0){
                        member.aggro--;
                    }
                }
            }
        }

        Entity currentEntity = battleQueue.get(currentTurn);

        if(currentEntity.hp <= 0){
            currentTurnFinished = true;
            return;
        }

        if (currentEntity == currentEnemy) {
            isEnemyTurn = true;
            if (buffer > 25) {
                enemyTurn();
                isEnemyTurn = false;
                currentTurnFinished = true;
                System.out.println("Enemy Turn Finished. ");
            }
        }
    }

    public void attack() {
        isAttacking = true;
        battleCommandNum = 0;
    }

    public void skill(){
        isChoosingSkill = true;
        battleCommandNum = 0;
    }

    public void drawPartyMembers(Graphics2D g2) {
        int baseX = gp.tileSize * 2 + gp.tileSize;
        int baseY = gp.screenHeight / 2 - gp.tileSize;
        int spacing = gp.tileSize * 4;

        int x = baseX;
        int y = baseY;
        for (int i = 0; i < battleQueue.size(); i++) {
            Entity entity = battleQueue.get(i);
            if (entity != null && entity != currentEnemy) {
                if (entity == gp.player) {
                    x = baseX + (spacing);
                    y = baseY + (gp.tileSize);
                }
                else if (entity == gp.companion2){
                    x = baseX;
                    y = baseY + (spacing-gp.tileSize);
                }
                else{
                    x = baseX;
                    y = baseY;
                }

                BufferedImage sprite = entity.right1;
                if(entity.hp <= 0){
                    sprite = entity.defeated1;
                }

                g2.drawImage(sprite, x, y, gp.tileSize+gp.tileSize/2, gp.tileSize+gp.tileSize/2, null);

                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
                g2.drawString(entity.getName(), x, y-50);
                String text = "Level: " + entity.level;
                g2.drawString(text, x, y-35);

                double oneScale = gp.tileSize/entity.maxHP;
                double hpBarValue = oneScale * entity.hp;

                g2.setColor(new Color(0, 0, 0));
                g2.fillRect(x, y-29, gp.tileSize, 10);

                g2.setColor(new Color(255,0,30));
                g2.fillRect(x, y-28, (int) hpBarValue, 8);
                g2.drawImage(heart, x-10, y-gp.tileSize/2-7, gp.tileSize/3-3, gp.tileSize/3-3, null);

                text = gp.df.format(entity.hp) + "/" + gp.df.format(entity.maxHP);
                g2.setFont(g2.getFont().deriveFont( 10f));
                g2.setColor(Color.white);
                g2.drawString(text, x + gp.tileSize+2, y-21);

                double oneScale1 = gp.tileSize/entity.maxEnergy;
                double energyBarValue = oneScale1 * entity.energy;

                g2.setColor(new Color(0, 0, 0));
                g2.fillRect(x, y-14, gp.tileSize, 10);

                g2.setColor(new Color(255, 227, 24));
                g2.fillRect(x, y-13, (int) energyBarValue, 8);
                g2.drawImage(energy, x-10, y-gp.tileSize/2+8, gp.tileSize/3-3, gp.tileSize/3-3, null);

                text = gp.df.format(entity.energy) + "/" + gp.df.format(entity.maxEnergy);
                g2.setFont(g2.getFont().deriveFont( 10f));
                g2.setColor(Color.white);
                g2.drawString(text, x + gp.tileSize+2, y-5);

                if (i == currentTurn) {
                    if(!isAttacking && !isChoosingSkill) {
                        int tempX = x - (gp.tileSize*2+gp.tileSize/2)+5;
                        int tempY = y + 15;
                        drawOption("ATTACK", tempX, tempY, 0);
                        if(battleCommandNum == 0){
                            gp.ui.addDescription("Use Basic Attack\n \n Attack enemy with a basic attack.");
                        }
                        tempX += 3;
                        tempY += gp.tileSize/2;

                        drawOption("SKILL", tempX, tempY, 1);
                        if(battleCommandNum == 1){
                            gp.ui.addDescription("Use Skills\n \n Use skills. Will consume energy.");
                        }
                        tempX += 3;
                        tempY += gp.tileSize/2;

                        drawOption("ITEM", tempX, tempY, 2);
                        if(battleCommandNum == 2){
                            gp.ui.addDescription("Use Items\n \n Use items available in your inventory.");
                        }
                        tempX += 3;
                        tempY += gp.tileSize/2;

                        drawOption("FLEE", tempX, tempY, 3);
                        if(battleCommandNum == 3){
                            gp.ui.addDescription("Run Away\n \n Attempt to escape from the battle.");
                        }
                    }
                    else if(isChoosingSkill && !isAttacking){
                        int tempX = x - (gp.tileSize*2+gp.tileSize/2)+5;
                        int tempY = y + 10;
                        if(isPickingAlly){
                            selectableAllies.clear();
                            for (Entity member : battleQueue) {
                                if (member != currentEnemy && member.hp > 0) {
                                    selectableAllies.add(member);
                                }
                            }

                            switch(selectableAllies.size()-1){
                                case 0:{
                                    drawOption(selectableAllies.get(0).getName(), tempX, tempY, 0);
                                    if(battleCommandNum == 0){
                                        gp.ui.addDescription("Use skill on " + selectableAllies.get(0).getName());
                                    }

                                    break;
                                }
                                case 1:{
                                    drawOption(selectableAllies.get(0).getName(), tempX, tempY, 0);
                                    if(battleCommandNum == 0){
                                        gp.ui.addDescription("Use skill on " + selectableAllies.get(0).getName());
                                    }
                                    tempX += 3;
                                    tempY += gp.tileSize/2;

                                    drawOption(selectableAllies.get(1).getName(), tempX, tempY, 1);
                                    if(battleCommandNum == 1){
                                        gp.ui.addDescription("Use skill on " + selectableAllies.get(1).getName());
                                    }

                                    break;
                                }
                                case 2:{
                                    drawOption(selectableAllies.get(0).getName(), tempX, tempY, 0);
                                    if(battleCommandNum == 0){
                                        gp.ui.addDescription("Use skill on " + selectableAllies.get(0).getName());
                                    }
                                    tempX += 3;
                                    tempY += gp.tileSize/2;

                                    drawOption(selectableAllies.get(1).getName(), tempX, tempY, 1);
                                    if(battleCommandNum == 1){
                                        gp.ui.addDescription("Use skill on " + selectableAllies.get(1).getName());
                                    }
                                    tempX += 3;
                                    tempY += gp.tileSize/2;
                                    drawOption(selectableAllies.get(2).getName(), tempX, tempY, 2);
                                    if(battleCommandNum == 2){
                                        gp.ui.addDescription("Use skill on " + selectableAllies.get(2).getName());
                                    }
                                    break;
                                }
                            }
                        }
                        else{
                            drawOption("Skill 1", tempX, tempY, 0);
                            if(battleCommandNum == 0){
                                gp.ui.addDescription(entity.skills.get(0).toString() );
                            }
                            tempX += 3;
                            tempY += gp.tileSize/2;

                            drawOption("Skill 2", tempX, tempY, 1);
                            if(battleCommandNum == 1){
                                gp.ui.addDescription(entity.skills.get(1).toString() );
                            }
                            tempX += 3;
                            tempY += gp.tileSize/2;

                            drawOption("Ultimate", tempX, tempY, 2);
                            if(battleCommandNum == 2){
                                gp.ui.addDescription(entity.skills.get(2).toString() );
                            }
                        }

                    }
                    else if(isAttacking){
                        int tempX = x - (gp.tileSize*2+gp.tileSize/2)+5;
                        int tempY = y + 10;
                        drawOption("HEAD", tempX, tempY, 0);
                        if(battleCommandNum == 0){
                            gp.ui.addDescription("Attack Head\n \n Low hit chance (40% + Luck Factor)\n Deals 2-3 times damage if successful.");
                        }
                        tempX += 3;
                        tempY += gp.tileSize/2;

                        drawOption("TORSO", tempX, tempY, 1);
                        if(battleCommandNum == 1) {
                            gp.ui.addDescription("Attack Torso\n \n High hit chance\n Deals normal damage.");
                        }
                    }
                }
                if(entity.isAttacked || entity.isHealed){
                    damaged(g2, entity, x, y);
                }
            }
        }
    }
    public void drawTurnQueue(Graphics2D g2) {
        int baseX = gp.tileSize;
        int baseY = gp.tileSize;
        int spacing = gp.tileSize;

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
        g2.drawString("TURN QUEUE", gp.tileSize, gp.tileSize-10);

        int x = baseX;
        int y = baseY;

        for (int i = 0; i < battleQueue.size(); i++) {
            Entity entity = battleQueue.get(i);

            BufferedImage sprite = entity.down1;

            g2.drawImage(sprite, x, y, gp.tileSize, gp.tileSize, null);

            if (i == currentTurn) {
                if(entity == currentEnemy){
                    g2.setColor(Color.RED);
                }
                else{
                    g2.setColor(Color.YELLOW);
                }
                g2.drawRect(x - 2, y - 2, gp.tileSize + 4, gp.tileSize + 4);
            }

            x += spacing;
        }
    }
    public void drawEnemy(Graphics2D g2){
        for (int i = 0; i < battleQueue.size(); i++) {
            Entity entity = battleQueue.get(i);
            if (entity != null && entity == currentEnemy) {
                int x = gp.screenWidth / 2 + gp.tileSize * 2;
                int y = gp.screenHeight / 2;
                BufferedImage sprite = currentEnemy.left1;

                g2.drawImage(sprite, x, y-currentEnemy.sizeIncrement, gp.tileSize+currentEnemy.sizeIncrement+gp.tileSize/2, gp.tileSize+currentEnemy.sizeIncrement+gp.tileSize/2, null);

                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
                g2.drawString(entity.getName(), x, y-50-currentEnemy.sizeIncrement);
                String text = "Level: " + entity.level;
                g2.drawString(text, x, y-35-currentEnemy.sizeIncrement);

                double oneScale = gp.tileSize/entity.maxHP;
                double hpBarValue = oneScale * entity.hp;

                g2.setColor(new Color(0, 0, 0));
                g2.fillRect(x, y-29-currentEnemy.sizeIncrement, gp.tileSize, 10);

                g2.setColor(new Color(255,0,30));
                g2.fillRect(x, y-28-currentEnemy.sizeIncrement, (int) hpBarValue, 8);
                g2.drawImage(heart, x-10, y-gp.tileSize/2-7-currentEnemy.sizeIncrement, gp.tileSize/3-3, gp.tileSize/3-3, null);

                text = gp.df.format(entity.hp) + "/" + gp.df.format(entity.maxHP);
                g2.setFont(g2.getFont().deriveFont( 10f));
                g2.setColor(Color.white);
                g2.drawString(text, x + gp.tileSize+2, y-21-currentEnemy.sizeIncrement);

                double oneScale1 = gp.tileSize/entity.maxEnergy;
                double energyBarValue = oneScale1 * entity.energy;

                g2.setColor(new Color(0, 0, 0));
                g2.fillRect(x, y-14-currentEnemy.sizeIncrement, gp.tileSize, 10);

                g2.setColor(new Color(255, 227, 24));
                g2.fillRect(x, y-13-currentEnemy.sizeIncrement, (int) energyBarValue, 8);
                g2.drawImage(energy, x-10, y-gp.tileSize/2+8-currentEnemy.sizeIncrement, gp.tileSize/3-3, gp.tileSize/3-3, null);

                text = gp.df.format(entity.energy) + "/" + gp.df.format(entity.maxEnergy);
                g2.setFont(g2.getFont().deriveFont( 10f));
                g2.setColor(Color.white);
                g2.drawString(text, x + gp.tileSize+2, y-5-currentEnemy.sizeIncrement);

                if(currentEnemy.isAttacked || currentEnemy.isHealed){
                    damaged(g2, entity, x, y);
                }
            }
        }
    }
    public void damaged(Graphics2D g2, Entity entity, int x, int y) {
        showDamage(g2, entity, x, y, output, entity.isHealed);
    }
    public void showDamage(Graphics2D g2, Entity entity, int x, int y, double amount, boolean isHealing) {
        if (entity.isAttacked || entity.isHealed) {
            y++;
            damageBuffer++;

            String text = "";
            Color textColor;

            if (entity.dodged) {
                text = "Dodged!";
                textColor = Color.WHITE;
            } else if (isHealing) {
                text = "+" + (int)amount;
                textColor = new Color(0, 255, 0);
            } else {
                text = "-" + (int)amount;
                textColor = new Color(255, 0, 0);
            }

            g2.setColor(new Color(0, 0, 0));
            g2.setFont(g2.getFont().deriveFont(20f));
            g2.drawString(text, x + gp.tileSize*2 + 10, y - gp.tileSize);

            // Draw main text with appropriate color
            g2.setColor(textColor); // Use the color we determined above
            g2.setFont(g2.getFont().deriveFont(18f));
            g2.drawString(text, x + gp.tileSize*2 + 10, y - gp.tileSize);

            if (damageBuffer > 20 && !currentTurnFinished) {
                entity.isAttacked = false;
                entity.isHealed = false;
                damageBuffer = 0;
            }
        }
    }

    public double calculateDamage(Entity attacker, Entity defender, String targetArea) {
        int hitRoll = gp.randomize(1, 100);
        double luckFactor = attacker.luck * 0.01 - (defender.luck * 0.01);

        double damage = 0;

        int hitChance = 0;
        double damageMultiplier = 1.0;

        switch (targetArea) {
            case "HEAD":
                hitChance = (int)(40 + (luckFactor * 100));
                damageMultiplier = gp.randomize(2, 3);
                break;
            case "TORSO":
                hitChance = (int)(85 + (luckFactor * 100));
                damageMultiplier = 1.0;
                break;
        }

        hitChance = Math.max(hitChance, 5);

        if (hitRoll <= hitChance) {
            damage = (attacker.attack - defender.defense) * damageMultiplier;
            damage = Math.max(damage, 1);
            return damage;
        } else {
            return 0;
        }
    }


    public void useSkill(int skillIndex) {
        Entity currentEntity = battleQueue.get(currentTurn);
        Skill skill = currentEntity.skills.get(skillIndex);

        if (currentEntity.energy < currentEntity.skills.get(skillIndex).energyCost) {
            gp.ui.addMessage(currentEntity.getName() + " doesn't have enough energy.");
            isChoosingSkill = false;
            return;
        }
        if (skill.currentCooldown > 0) {
            gp.ui.addMessage(skill.name + " is on cooldown for " + skill.currentCooldown + " more turns.");
            isChoosingSkill = false;
            isPickingAlly = false;
            return;
        }

        switch(skill.type){
            case "BUFF_SELF":{
                currentEntity.useSkill(skillIndex, currentEntity);
                currentTurnFinished = true;
                isPickingAlly = false;
                isChoosingSkill = false;
                isAttacking = false;
                battleCommandNum = 0;
                break;
            }
            case "BUFF_ALLY":{
                isPickingAlly = true;
                selectedSkill = skillIndex;
                break;
            }
            case "DAMAGE":{
                if(currentEntity != currentEnemy){
                    currentEntity.useSkill(skillIndex, currentEnemy);
                }
                else{
                    currentEntity.useSkill(skillIndex, currentEnemy);
                }
                currentTurnFinished = true;
                isPickingAlly = false;
                isChoosingSkill = false;
                isAttacking = false;
                battleCommandNum = 0;
                break;
            }
        }

        if (currentEnemy.hp <= 0) {
            if(!gp.player.event0Flag){
                eventEndBattle();
            }
            endBattle();
        }
    }

    public void playerAttack(String targetArea) {
        Entity attacker = battleQueue.get(currentTurn);

        output = calculateDamage(attacker, currentEnemy, targetArea);

        if (output > 0) {
            currentEnemy.hp -= output;
            currentEnemy.isAttacked = true;

            if (currentEnemy.hp < 0) {
                currentEnemy.hp = 0;
            }
            currentEnemy.dodged = false;
            gp.ui.addMessage("Hit! " + attacker.getName() + " deals " + (int)output + " damage to " + currentEnemy.getName());
        } else {
            currentEnemy.dodged = true;
            gp.ui.addMessage(attacker.getName() + " missed!");
        }

        if (currentEnemy.hp <= 0) {
            if(!gp.player.event0Flag){
                eventEndBattle();
            }
            endBattle();
        }

        isAttacking = false;
        isChoosingSkill = false;
        currentTurnFinished = true;
        battleCommandNum = 0;
    }

    public void enemyAttack(Entity target) {
        if (currentEnemy == null) return;

        String targetArea = (gp.randomize(0, 10) >= 9) ? "HEAD" : "TORSO";

        output = calculateDamage(currentEnemy, target, targetArea);

        if (output > 0) {
            target.hp -= output;
            target.isAttacked = true;

            if (target.hp < 0) {
                target.hp = 0;
            }

            if (target.hp <= 50 && currentEnemy == gp.livingEntity[3][0] || target.hp <= 100 && !gp.player.event0Flag) {
                if(!gp.player.event0Flag && gp.event.sequenceCheck < 3){
                    gp.gameState = gp.eventState;
                    target.hp = target.maxHP;
                    return;
                }
                target.hp = target.maxHP;
                eventEndBattle();
                return;
            }
            target.dodged = false;
            gp.ui.addMessage("Hit! " + currentEnemy.getName() + " deals " + (int)output + " damage to " + target.getName());
        } else {
            target.dodged = true;
            gp.ui.addMessage(currentEnemy.getName() + " missed!");
        }

        boolean allDefeated = true;
        for (Entity member : battleQueue) {
            if (member != currentEnemy && member != null && member.hp > 0) {
                allDefeated = false;
                break;
            }
        }

        if (allDefeated) {
            enemyEndBattle();
        }
    }

    public void endBattle(){
        double expGain = currentEnemy.nextLevelExp;


        battleFinished = true;

        for(Entity member : battleQueue){
            if(member != currentEnemy){
                member.exp += expGain;
                if(member.hp <= member.maxHP){
                    member.hp += 5;
                }
                member.regen();
                member.update();
                for(Skill skill: member.skills){
                    skill.currentCooldown = 0;
                }
                System.out.println(member.getName() + " updated and got " + expGain + " exp.");
            }
        }

        gp.ui.addMessage(gp.player.getName() + " gained " + expGain +".\n \n" +
                gp.companion1.getName() + " gained " + expGain +".\n \n" +
                gp.companion2.getName() + " gained " + expGain +".\n \n");
        currentEnemy.isDefeated = true;
        System.out.println(currentEnemy.getName() + " has died " + currentEnemy.hollowCounter + " times");
        System.out.println("Battle finished.");
    }

    public void enemyEndBattle(){
        gp.player.isDefeated = true;
        gp.player.hollowCounter++;
        System.out.println(gp.player.getName() + " has died " + gp.player.hollowCounter + " times");
        gp.gameState = gp.playState;
        System.out.println("Battle finished.");
        emptyImages();
    }
    public void eventEndBattle(){
        double expGain = gp.player.nextLevelExp;

        gp.player.exp += expGain;
        gp.companion1.exp += expGain;
        gp.companion2.exp += expGain;

        System.out.println(gp.player.getName() + " has died " + gp.player.hollowCounter + " times but its an event");
        gp.gameState = gp.eventState;
        gp.event.sequenceCheck++;
        System.out.println("Battle finished.");
        emptyImages();
    }

    public void enemyTurn(){
        if(currentEnemy != null){
            ArrayList<Entity> targets = new ArrayList<>();
            for (Entity entity : battleQueue) {
                if (entity != currentEnemy && entity != null && entity.hp > 0) {
                    targets.add(entity);
                }
            }

            targets.sort(Comparator.comparingInt(e -> -e.aggro));

            ArrayList<Skill> availableSkills = new ArrayList<>();

            for(Skill skill: currentEnemy.skills){
                if(currentEnemy.energy >= skill.energyCost){
                    availableSkills.add(skill);
                }
            }
            if(!availableSkills.isEmpty()){
                int skillChoice = gp.randomize(0, availableSkills.size()-1);
                currentEnemy.useSkill(skillChoice, targets.get(0));
            }
            else{
                enemyAttack(targets.get(0));
            }

        }
    }

    public void drawOption(String option, int x, int y, int commandNum){
        g2.setColor(new Color(20, 61, 143, 200));
        drawActionWindow(x-8, y-gp.tileSize/2+5, gp.tileSize*2+gp.tileSize/2, gp.tileSize/2);
        g2.setFont(g2.getFont().deriveFont(16f));
        g2.setColor(new Color(0, 0, 0));
        g2.drawString(option, x, y);

        if(this.battleCommandNum == commandNum){
            g2.setColor(new Color(255, 0, 0));
            g2.drawString(option, x, y);
        }
    }

    public void drawActionWindow(int x, int y, int width, int height){
        g2.drawImage(actionWindow, x, y, width, height, null);
    }
}

package src.game;

import src.game.card.CardObject;
import src.game.card.Monster;
import src.game.card.Spell;
import src.game.card.Trap;
import src.game.player.Player;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import java.util.ArrayList;

public class MouseMatch extends MouseAdapter {

    private Table table;
    private Player player;
    private Game game;
    private HUD hud;

    public MouseMatch(Match match, Game game) {
        this.table = match.getTable();
        this.player = match.getPlayer();
        this.hud = match.getHUD();
        this.game = game;
    }

    public boolean inRectangle(MouseEvent e, int x, int y, int width, int height) {
        if (e.getX() > x && e.getX() < x + width) {
            if (e.getY() > y && e.getY() < y + height) {
                return true;
            }
        }
        return false;
    }

    public boolean inRectangle(MouseEvent e, Rectangle rectangle) {
        if (e.getX() > rectangle.x && e.getX() < rectangle.x + rectangle.width) {
            if (e.getY() > rectangle.y && e.getY() < rectangle.y + rectangle.height) {
                return true;
            }
        }
        return false;
    }

    public int inHand(int x, int y, ArrayList<int[]> positions) {
        for (int i = 0; i < positions.size(); i++) {
            if (x > positions.get(i)[0] && x < positions.get(i)[0] + CardObject.cardWidth) {
                if (y > positions.get(i)[1] && y < positions.get(i)[1] + CardObject.cardHeight) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void mouseClicked(MouseEvent e) {
        if (Game.gameState == STATE.Match) {
            // Flag per capire se ho settato una carta all hud dal tavolo
            boolean flag = false;
            // Table
            if (inRectangle(e, table.centerTableX(), table.centerPlayerTableY(), table.getTableWidth(), table.getTableHeight())) {
                System.out.println("AYO! you are clicking the table :D");

                // CHECKING GRAVEYARD
                if (inRectangle(e, player.getGraveyard().getX(), player.getGraveyard().getY(), CardObject.cardWidth, CardObject.cardHeight)) {
                    player.getGraveyard().setIsSelected(true);
                } else if (player.getGraveyard().getIsSelected()) {
                    player.getGraveyard().setIsSelected(false);
                }

                // CHECKING EXTRADECK
                if (inRectangle(e, player.getExtraDeck().getListRectangle())) {
                    player.getExtraDeck().setIsSelected(true);
                } else if (player.getExtraDeck().getIsSelected() && !inRectangle(e, player.getExtraDeck().getX() + CardObject.cardWidth/2 - 15, player.getExtraDeck().getY() - 50, 30, 30)) {
                    player.getExtraDeck().setIsSelected(false);
                }

                // CHECKING MONSTERS
                if (table.getPlayerMonsterOnField().size() > 0) {
                    ArrayList<CardObject> monsterOnPlayerField = table.getPlayerMonsterOnField();
                    System.out.println(table.getPlayerMonsterOnField().size() + " mostri sul terreno");
                    for (int i = 0; i < monsterOnPlayerField.size(); i++) {
                        CardObject card = monsterOnPlayerField.get(i);
                        if (inRectangle(e, card.getX(), card.getY(), CardObject.cardWidth, CardObject.cardHeight)) {
                            if (hud.getCard() != card || hud.getCard() == null)  {
                                if (hud.getCard() != null) {
                                    hud.getCard().setIsSelected(false);
                                }
                                hud.setCard(card);
                                card.setIsSelected(true);
                                flag = true;
                            }
                        } else if (hud.getCard() != null && !flag){
                            hud.getCard().setIsSelected(false);
                            hud.setCard(null);
                        }
                    }
                }

                // CHECKING SPELL TRAPS
                if (table.getPlayerSpellTrapOnField().size() > 0) {
                    ArrayList<CardObject> spellTrapOnPlayerField = table.getPlayerSpellTrapOnField();
                    System.out.println(spellTrapOnPlayerField.size() + " carte magie/trappola sul terreno");
                    for (int i = 0; i < spellTrapOnPlayerField.size(); i++) {
                        CardObject card = spellTrapOnPlayerField.get(i);
                        if (inRectangle(e, card.getX(), card.getY(), CardObject.cardWidth, CardObject.cardHeight)) {
                            if (hud.getCard() != card || hud.getCard() == null)  {
                                if (hud.getCard() != null) {
                                    hud.getCard().setIsSelected(false);
                                }
                                hud.setCard(card);
                                card.setIsSelected(true);
                                flag = true;
                            }
                        } else if (hud.getCard() != null && !flag){
                            hud.getCard().setIsSelected(false);
                            hud.setCard(null);
                        }
                    }
                }

                // draw button
                if (e.getX() > table.getPlayerFieldCardPositions()[0][4][0]
                        && e.getX() < table.getPlayerFieldCardPositions()[0][4][0] + CardObject.cardWidth) {
                    if (e.getY() > table.getPlayerFieldCardPositions()[0][4][1]
                            && e.getY() < table.getPlayerFieldCardPositions()[0][4][1] + CardObject.cardHeight) {
                        player.draw();
                    }
                }
            }

            // Graveyard Interaction 

            if (player.getGraveyard().getIsSelected()) {
                if (inRectangle(e, player.getGraveyard().getX() + CardObject.cardWidth/2 - 15, player.getGraveyard().getY() - 50, 30, 30)) {
                    player.getGraveyard().setCanRenderList(true);
                } else if (!inRectangle(e, player.getGraveyard().getX(), player.getGraveyard().getY(), CardObject.cardWidth, CardObject.cardHeight)) {
                    player.getGraveyard().setIsSelected(false);
                } 
            } else if (player.getGraveyard().getCanRenderList()) {
                if (!inRectangle(e, player.getGraveyard().getListRectangle())) {
                    player.getGraveyard().setCanRenderList(false);
                }
            }

            if (player.getExtraDeck().getIsSelected()) { 
                if (inRectangle(e, player.getExtraDeck().getListRectangle().x + CardObject.cardWidth/2 - 15, player.getExtraDeck().getListRectangle().y - 50, 30, 30)) {
                    System.out.println("on god");
                    player.getExtraDeck().setCanRenderList(true);
                } else if (!inRectangle(e, player.getExtraDeck().getX(), player.getExtraDeck().getY(), CardObject.cardWidth, CardObject.cardHeight)) {
                    player.getExtraDeck().setIsSelected(false);
                }
            } else if (player.getExtraDeck().getCanRenderList()) {
                if (!inRectangle(e, player.getExtraDeck().getListRectangle())) {
                    player.getExtraDeck().setCanRenderList(false);
                }
            }
            
            //Check if selected HUD card has actions that can be executed like summon, set etc...
            if (hud.getCard() != null) {
                if (hud.getCard() instanceof Spell) {
                    Spell card = (Spell) hud.getCard();
                    if (card.getIsSettable()) {
                        if (e.getX() > card.getX() + CardObject.cardWidth/2 - 15 && e.getX() < card.getX() + CardObject.cardWidth/2 + 15){
                            if (e.getY() > card.getY() - 50 && e.getY() <  card.getY() - 20) {
                                card.set(player.getHand(), table);
                            }
                        } 
                    }
                }
                else if (hud.getCard() instanceof Trap) {
                    Trap card = (Trap) hud.getCard();
                    if (card.getIsSettable()) {
                        if (e.getX() > card.getX() + CardObject.cardWidth/2 - 15 && e.getX() < card.getX() + CardObject.cardWidth/2 + 15){
                            if (e.getY() > card.getY() - 50 && e.getY() <  card.getY() - 20) {
                                card.set(player.getHand(), table);
                            }
                        } 
                    }
                }
                else if (hud.getCard() instanceof Monster) {
                    Monster card = (Monster) hud.getCard();
                    if (card.getIsNormalSummonable()){
                        if (e.getX() > card.getX() + CardObject.cardWidth/2 - 15 && e.getX() < card.getX() + CardObject.cardWidth/2 + 15){
                            if (e.getY() > card.getY() - 50 && e.getY() <  card.getY() - 20) {
                                if (player.getCanNormalSummon()) {
                                    card.normalSummon(player.getHand(), table);
                                    player.setCanNormalSummon(false);
                                }
                            }
                        } 
                    }
                }
            }

            // testing in rectangle
            int i = inHand(e.getX(), e.getY(), player.getHand().getPositions());
            if (i != -1) {
                CardObject selectedCard = player.getHand().getHand().get(i);
                if (hud.getCard() != selectedCard || hud.getCard() == null) {
                    if (hud.getCard() != null)
                        hud.getCard().setIsSelected(false);
                    hud.setCard(selectedCard);
                    selectedCard.setIsSelected(true);
                } 
            } else if (hud.getCard() != null && !flag) {
                hud.getCard().setIsSelected(false);
                hud.setCard(null);
            }

        }
    }
}
package Core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import Entities.Player;
import Entities.Turret;
import Panels.PlayPanel;
import Panels.SinglePlayerGame;

public class GameKeyListener implements KeyListener, GameConstants, EntityConstants {

    private Player playerOne, playerTwo;
    private JPanel playPanel;  // SinglePlayerGame 대신 JPanel을 사용
    private boolean isSinglePlayer;

    public GameKeyListener(Player p1, Player p2, JPanel pp, boolean isSinglePlayer) {
        this.playerOne = p1;
        this.playerTwo = p2;
        this.playPanel = pp;
        this.isSinglePlayer = isSinglePlayer;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int cost;
        Turret turret;

        // playerOne이 null이 아닌지 확인
        if (this.playerOne != null) {
            // playerOne이 null이 아니면 getCurrentEvolution() 호출
            this.playerOne.getCurrentEvolution();
        } else {
            System.out.println("Error: playerOne is not initialized.");
        }
        switch (keyCode) {
            // Pause Game
	        case KeyEvent.VK_SPACE:
	            if (playPanel instanceof PlayPanel) {
	                PlayPanel panel = (PlayPanel) playPanel;
	                panel.setGamePaused(!panel.getGamePaused());
	                if (panel.getGamePaused()) {
	                    panel.showSettingsMenu(); // 설정 메뉴 표시
	                } else {
	                    panel.hideSettingsMenu(); // 설정 메뉴 숨김
	                }
	            } else if (playPanel instanceof SinglePlayerGame) {
	                SinglePlayerGame panel = (SinglePlayerGame) playPanel;
	                panel.setGamePaused(!panel.getGamePaused());
	                if (panel.getGamePaused()) {
	                    panel.showSettingsMenu(); // 설정 메뉴 표시
	                } else {
	                    panel.hideSettingsMenu(); // 설정 메뉴 숨김
	                }
	            }

            // Volume on/off
            case KeyEvent.VK_G:
                if (playPanel instanceof PlayPanel) {
                    PlayPanel panel = (PlayPanel) playPanel;
                    if (panel.getPlayingMusic()) {
                        panel.setPlayingMusic(false);
                        panel.stopPlayingMusic();
                    } else {
                        panel.setPlayingMusic(true);
                        panel.playMusic();
                    }
                } else if (playPanel instanceof SinglePlayerGame) {
                    SinglePlayerGame panel = (SinglePlayerGame) playPanel;
                    if (panel.getPlayingMusic()) {
                        panel.setPlayingMusic(false);
                        panel.stopPlayingMusic();
                    } else {
                        panel.setPlayingMusic(true);
                        panel.playMusic();
                    }
                }
                break;

            // Player One: creatures
            case KeyEvent.VK_1:
                cost = FIRST_COST + (playerOne.getCurrentEvolution() * FIRST_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, FIRST_TYPE);
                }
                break;

            case KeyEvent.VK_2:
                cost = SECOND_COST + (playerOne.getCurrentEvolution() * SECOND_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, SECOND_TYPE);
                }
                break;

            case KeyEvent.VK_3:
                cost = THIRD_COST + (playerOne.getCurrentEvolution() * THIRD_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, THIRD_TYPE);
                }
                break;

            case KeyEvent.VK_4:
                cost = FOURTH_COST + (playerOne.getCurrentEvolution() * FOURTH_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, FOURTH_TYPE);
                }
                break;

            // Player One: turrets
            case KeyEvent.VK_Z:
            case KeyEvent.VK_X:
            case KeyEvent.VK_C:
                int turretIndex = (keyCode == KeyEvent.VK_Z) ? FIRST_TURRET
                                  : (keyCode == KeyEvent.VK_X) ? SECOND_TURRET : THIRD_TURRET;

                turret = playerOne.getTower().getTurrets().get(turretIndex);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerOne.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerOne.getGold() >= cost) {
                        playerOne.buyTurret(cost, turretIndex);
                    }
                } else {
                    playerOne.sellTurret(turret.getGoldFromSell(), turretIndex);
                }
                break;

            // Player One: evolve
            case KeyEvent.VK_A:
                if (playerOne.getGold() >= playerOne.getEvolutionCost()) {
                    playerOne.evolve();
                }
                break;

            // Player Two: creatures (only if not single-player)
            default:
                if (!isSinglePlayer && playerTwo != null) {
                    handlePlayerTwoInput(keyCode);
                }
                break;
        }
    }


    private void handlePlayerTwoInput(int keyCode) {
        int cost;
        Turret turret;

        switch (keyCode) {
            case KeyEvent.VK_7:
                cost = FIRST_COST + (playerTwo.getCurrentEvolution() * FIRST_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, FIRST_TYPE);
                }
                break;

            case KeyEvent.VK_8:
                cost = SECOND_COST + (playerTwo.getCurrentEvolution() * SECOND_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, SECOND_TYPE);
                }
                break;

            case KeyEvent.VK_9:
                cost = THIRD_COST + (playerTwo.getCurrentEvolution() * THIRD_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, THIRD_TYPE);
                }
                break;

            case KeyEvent.VK_0:
                cost = FOURTH_COST + (playerTwo.getCurrentEvolution() * FOURTH_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, FOURTH_TYPE);
                }
                break;

            // Player Two: turrets
            case KeyEvent.VK_B:
                turret = playerTwo.getTower().getTurrets().get(THIRD_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerTwo.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerTwo.getGold() >= cost) {
                        playerTwo.buyTurret(cost, THIRD_TURRET);
                    }
                } else {
                    playerTwo.sellTurret(turret.getGoldFromSell(), THIRD_TURRET);
                }
                break;

            case KeyEvent.VK_N:
                turret = playerTwo.getTower().getTurrets().get(SECOND_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerTwo.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerTwo.getGold() >= cost) {
                        playerTwo.buyTurret(cost, SECOND_TURRET);
                    }
                } else {
                    playerTwo.sellTurret(turret.getGoldFromSell(), SECOND_TURRET);
                }
                break;

            case KeyEvent.VK_M:
                turret = playerTwo.getTower().getTurrets().get(FIRST_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerTwo.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerTwo.getGold() >= cost) {
                        playerTwo.buyTurret(cost, FIRST_TURRET);
                    }
                } else {
                    playerTwo.sellTurret(turret.getGoldFromSell(), FIRST_TURRET);
                }
                break;

            // Player Two: evolve
            case KeyEvent.VK_L:
                if (playerTwo.getGold() >= playerTwo.getEvolutionCost()) {
                    playerTwo.evolve();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

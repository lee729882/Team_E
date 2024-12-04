package Core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Entities.Player;
import Entities.Turret;
import Panels.PlayPanel;

// 게임 내 키보드 입력을 처리하는 클래스
// - 플레이어의 행동(생성, 진화 등)을 키보드 입력으로 제어
public class GameKeyListener implements KeyListener, GameConstants, EntityConstants {

    private Player playerOne, playerTwo; // 두 플레이어 객체
    private PlayPanel playPanel; // 게임 패널

    // 생성자: 플레이어와 게임 패널을 초기화
    public GameKeyListener(Player p1, Player p2, PlayPanel pp) {
        playerOne = p1;
        playerTwo = p2;
        playPanel = pp;
    }

    // 키를 눌렀을 때의 동작을 정의
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); // 누른 키의 코드
        int cost; // 비용 변수
        Turret turret; // 터렛 객체

        switch (keyCode) {
            // 게임 일시정지/재개
            case KeyEvent.VK_SPACE:
                if (playPanel.getGamePaused()) {
                    playPanel.setGamePaused(false); // 게임 재개
                } else {
                    playPanel.setGamePaused(true); // 게임 일시정지
                }
                break;

            // 음악 재생/정지
            case KeyEvent.VK_G:
                if (playPanel.getPlayingMusic()) {
                    playPanel.setPlayingMusic(false); // 음악 정지
                    playPanel.stopPlayingMusic();
                } else {
                    playPanel.setPlayingMusic(true); // 음악 재생
                    playPanel.playMusic();
                }
                break;

            // -----------------
            // 플레이어 1
            // 생명체 생성
            case KeyEvent.VK_1:
                cost = FIRST_COST + (playerOne.getCurrentEvolution() * FIRST_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, FIRST_TYPE); // 첫 번째 타입 생명체 생성
                }
                break;

            case KeyEvent.VK_2:
                cost = SECOND_COST + (playerOne.getCurrentEvolution() * SECOND_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, SECOND_TYPE); // 두 번째 타입 생명체 생성
                }
                break;

            case KeyEvent.VK_3:
                cost = THIRD_COST + (playerOne.getCurrentEvolution() * THIRD_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, THIRD_TYPE); // 세 번째 타입 생명체 생성
                }
                break;

            case KeyEvent.VK_4:
                cost = FOURTH_COST + (playerOne.getCurrentEvolution() * FOURTH_MULTIPLIER);
                if (playerOne.getGold() >= cost) {
                    playerOne.queueCreature(cost, FOURTH_TYPE); // 네 번째 타입 생명체 생성
                }
                break;

            // 터렛 구매/판매
            case KeyEvent.VK_Z:
                turret = playerOne.getTower().getTurrets().get(FIRST_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerOne.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerOne.getGold() >= cost) {
                        playerOne.buyTurret(cost, FIRST_TURRET); // 첫 번째 터렛 구매
                    }
                } else {
                    playerOne.sellTurret(turret.getGoldFromSell(), FIRST_TURRET); // 첫 번째 터렛 판매
                }
                break;

            case KeyEvent.VK_X:
                turret = playerOne.getTower().getTurrets().get(SECOND_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerOne.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerOne.getGold() >= cost) {
                        playerOne.buyTurret(cost, SECOND_TURRET); // 두 번째 터렛 구매
                    }
                } else {
                    playerOne.sellTurret(turret.getGoldFromSell(), SECOND_TURRET); // 두 번째 터렛 판매
                }
                break;

            case KeyEvent.VK_C:
                turret = playerOne.getTower().getTurrets().get(THIRD_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerOne.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerOne.getGold() >= cost) {
                        playerOne.buyTurret(cost, THIRD_TURRET); // 세 번째 터렛 구매
                    }
                } else {
                    playerOne.sellTurret(turret.getGoldFromSell(), THIRD_TURRET); // 세 번째 터렛 판매
                }
                break;

            // 플레이어 1 진화
            case KeyEvent.VK_A:
                if (playerOne.getGold() >= playerOne.getEvolutionCost()) {
                    playerOne.evolve(); // 진화 실행
                }
                break;

            // -----------------
            // 플레이어 2
            // 생명체 생성
            case KeyEvent.VK_7:
                cost = FIRST_COST + (playerTwo.getCurrentEvolution() * FIRST_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, FIRST_TYPE); // 첫 번째 타입 생명체 생성
                }
                break;

            case KeyEvent.VK_8:
                cost = SECOND_COST + (playerTwo.getCurrentEvolution() * SECOND_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, SECOND_TYPE); // 두 번째 타입 생명체 생성
                }
                break;

            case KeyEvent.VK_9:
                cost = THIRD_COST + (playerTwo.getCurrentEvolution() * THIRD_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, THIRD_TYPE); // 세 번째 타입 생명체 생성
                }
                break;

            case KeyEvent.VK_0:
                cost = FOURTH_COST + (playerTwo.getCurrentEvolution() * FOURTH_MULTIPLIER);
                if (playerTwo.getGold() >= cost) {
                    playerTwo.queueCreature(cost, FOURTH_TYPE); // 네 번째 타입 생명체 생성
                }
                break;

            // 터렛 구매/판매
            case KeyEvent.VK_B:
                turret = playerTwo.getTower().getTurrets().get(THIRD_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerTwo.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerTwo.getGold() >= cost) {
                        playerTwo.buyTurret(cost, THIRD_TURRET); // 세 번째 터렛 구매
                    }
                } else {
                    playerTwo.sellTurret(turret.getGoldFromSell(), THIRD_TURRET); // 세 번째 터렛 판매
                }
                break;

            case KeyEvent.VK_N:
                turret = playerTwo.getTower().getTurrets().get(SECOND_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerTwo.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerTwo.getGold() >= cost) {
                        playerTwo.buyTurret(cost, SECOND_TURRET); // 두 번째 터렛 구매
                    }
                } else {
                    playerTwo.sellTurret(turret.getGoldFromSell(), SECOND_TURRET); // 두 번째 터렛 판매
                }
                break;

            case KeyEvent.VK_M:
                turret = playerTwo.getTower().getTurrets().get(FIRST_TURRET);
                if (turret == null) {
                    cost = BASE_GOLD_BUY + (playerTwo.getCurrentEvolution() * TURRET_MULTIPLIER);
                    if (playerTwo.getGold() >= cost) {
                        playerTwo.buyTurret(cost, FIRST_TURRET); // 첫 번째 터렛 구매
                    }
                } else {
                    playerTwo.sellTurret(turret.getGoldFromSell(), FIRST_TURRET); // 첫 번째 터렛 판매
                }
                break;

            // 플레이어 2 진화
            case KeyEvent.VK_L:
                if (playerTwo.getGold() >= playerTwo.getEvolutionCost()) {
                    playerTwo.evolve(); // 진화 실행
                }
                break;
        }
    }

    // 키 입력 해제 시 동작
    @Override
    public void keyReleased(KeyEvent e) {
    }

    // 키 입력 시 동작 (미사용)
    @Override
    public void keyTyped(KeyEvent e) {
    }
}

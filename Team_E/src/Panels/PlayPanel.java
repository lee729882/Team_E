package Panels;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.sound.sampled.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Core.EntityConstants;
import Core.GameConstants;
import Core.GameFrame;
import Core.GameKeyListener;
import Entities.Player;

//플레이 패널(PlayPanel) 클래스
//- 게임의 메인 플레이 화면을 구성하며, 게임 로직과 그래픽을 처리
public class PlayPanel extends JPanel implements GameConstants, EntityConstants {

	private Player playerOne, playerTwo; // 두 플레이어 객체
    private BufferedImage background; // 배경 이미지
    private boolean playingMusic = true; // 음악 재생 여부
    private boolean gameOverFirst = false; // 첫 번째 플레이어 게임 오버 여부
    private boolean gameOverSecond = false; // 두 번째 플레이어 게임 오버 여부
    private GameFrame gameFrame; // 게임 프레임 참조
    private JPanel cardsPanel; // 카드 레이아웃의 패널
    private CardLayout cardLayout; // 카드 레이아웃
    private String firstName, secondName; // 두 플레이어의 이름
    private boolean gamePaused = false; // 게임 일시정지 여부

    // 이미지 배열 (생성 및 공격 스프라이트, 투사체, 타워 및 터렛 이미지)
    private BufferedImage[][][] leftMove = new BufferedImage[NUM_DIFFERENT_CREATURES][NUM_EVOLUTIONS][NUM_MOVE_SPRITES];
    private BufferedImage[][][] leftAttack = new BufferedImage[NUM_DIFFERENT_CREATURES][NUM_EVOLUTIONS][NUM_ATTACK_SPRITES];
    private BufferedImage[][] leftProjectiles = new BufferedImage[NUM_EVOLUTIONS][NUM_DIFFERENT_PROJECTILES];
    private BufferedImage[] leftTower = new BufferedImage[NUM_TOWER_TURRET_SPRITES];
    private BufferedImage[] leftTurret = new BufferedImage[NUM_TOWER_TURRET_SPRITES];

    private BufferedImage[][][] rightMove = new BufferedImage[NUM_DIFFERENT_CREATURES][NUM_EVOLUTIONS][NUM_MOVE_SPRITES];
    private BufferedImage[][][] rightAttack = new BufferedImage[NUM_DIFFERENT_CREATURES][NUM_EVOLUTIONS][NUM_ATTACK_SPRITES];
    private BufferedImage[][] rightProjectiles = new BufferedImage[NUM_EVOLUTIONS][NUM_DIFFERENT_PROJECTILES];
    private BufferedImage[] rightTower = new BufferedImage[NUM_TOWER_TURRET_SPRITES];
    private BufferedImage[] rightTurret = new BufferedImage[NUM_TOWER_TURRET_SPRITES];

    private BufferedImage[] menuItems = new BufferedImage[NUM_MENU_ICONS]; // 메뉴 아이템 이미지
    private BufferedImage[][] creatureCreationIcons = new BufferedImage[NUM_DIFFERENT_CREATURES][NUM_EVOLUTIONS]; // 유닛 생성 아이콘
    static AudioInputStream audioStream; // 배경 음악 스트림
    static Clip music; // 배경 음악 클립

    // 생성자: 패널 초기화
    public PlayPanel() {
    	setLayout(null); // 레이아웃 매니저 사용하지 않음
        this.setFocusable(true); // 포커스를 받을 수 있도록 설정
        this.requestFocusInWindow(); // 초기 포커스 요청
        importImages(); // 이미지 로드
        try {
            background = ImageIO.read(this.getClass().getResource(BACKGROUND_PATH + "Game.jpg")); // 배경 이미지 로드
        } catch (Exception e) {
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource("../Assets/backgroundMusic.wav"));
            music = AudioSystem.getClip();
            music.open(audioStream);
        } catch (Exception e) {
        }

        // 플레이어 이름 가져오기
        firstName = LoginPanel.firstLoginField.getText();
        secondName = LoginPanel.secondLoginField.getText();

        // 두 플레이어 객체 생성
        this.playerOne = new Player(LEFT_TEAM, firstName, leftMove, leftAttack, leftProjectiles, leftTower, leftTurret);
        this.playerTwo = new Player(RIGHT_TEAM, secondName, rightMove, rightAttack, rightProjectiles, rightTower, rightTurret);

        // 키 리스너 추가
        GameKeyListener gameKeyListener = new GameKeyListener(playerOne, playerTwo, this);
        addKeyListener(gameKeyListener);
    }

    // 게임 실행 메서드
    public void runGame(PlayPanel pp) {
    	playMusic(); // 음악 재생
        gameFrame = (GameFrame) SwingUtilities.getWindowAncestor(this); // 게임 프레임 가져오기
        cardsPanel = gameFrame.getCardsPanel(); // 카드 패널 가져오기
        cardLayout = (CardLayout) cardsPanel.getLayout(); // 카드 레이아웃 가져오기

        // 페인트 타이머 (화면 갱신)
        final Timer paintTimer = new Timer(PAINT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                repaint(); // 패널 다시 그리기
            }
        });
        paintTimer.start();

        // 소환 타이머 (생성 큐에서 유닛 소환)
        final Timer summonTimer = new Timer(SUMMON_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!playerOne.getSummonQueue().isEmpty() && !pp.getGamePaused()) {
                    playerOne.summonCreature();
                }
                if (!playerTwo.getSummonQueue().isEmpty() && !pp.gamePaused) {
                    playerTwo.summonCreature();
                }
            }
        });
        summonTimer.start();

        // 골드 증가 타이머
        final Timer goldTimer = new Timer(GOLD_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!pp.gamePaused) {
                    playerOne.gainGold();
                    playerTwo.gainGold();
                }
            }
        });
        goldTimer.start();
    }

    // 이미지 로드 메서드
    public void importImages() {
        try {
        	// 유닛 스프라이트 및 아이콘 로드
            for (int row = 0; row < NUM_DIFFERENT_CREATURES; row++) {
                for (int evolution = 0; evolution < NUM_EVOLUTIONS; evolution++) {
                    this.creatureCreationIcons[row][evolution] = ImageIO.read(this.getClass()
                            .getResource(CREATURE_CREATION_ICONS_PATH + row + "-" + evolution + PNG_EXT));
                    for (int column = 0; column < NUM_MOVE_SPRITES; column++) {
                        this.leftMove[row][evolution][column] = ImageIO.read(this.getClass()
                                .getResource(LEFT_MOVE_PATH + row + "/" + evolution + "-" + column + PNG_EXT));
                        this.rightMove[row][evolution][column] = ImageIO.read(this.getClass()
                                .getResource(RIGHT_MOVE_PATH + row + "/" + evolution + "-" + column + PNG_EXT));
                    }
                }

                for (int evolution = 0; evolution < NUM_EVOLUTIONS; evolution++) {
                    for (int column = 0; column < NUM_ATTACK_SPRITES; column++) {
                        this.leftAttack[row][evolution][column] = ImageIO.read(this.getClass()
                                .getResource(LEFT_ATTACK_PATH + row + "/" + evolution + "-" + column + PNG_EXT));
                        this.rightAttack[row][evolution][column] = ImageIO.read(this.getClass()
                                .getResource(RIGHT_ATTACK_PATH + row + "/" + evolution + "-" + column + PNG_EXT));
                    }
                }
            }

            // 타워 및 터렛 이미지 로드
            for (int i = 0; i < NUM_TOWER_TURRET_SPRITES; i++) {
                this.leftTower[i] = ImageIO.read(this.getClass().getResource(LEFT_TOWER_PATH + i + PNG_EXT));
                this.leftTurret[i] = ImageIO.read(this.getClass().getResource(LEFT_TURRET_PATH + i + PNG_EXT));

                this.rightTower[i] = ImageIO.read(this.getClass().getResource(RIGHT_TOWER_PATH + i + PNG_EXT));
                this.rightTurret[i] = ImageIO.read(this.getClass().getResource(RIGHT_TURRET_PATH + i + PNG_EXT));
            }
            
            // 투사체 이미지 로드
            for (int evolution = 0; evolution < NUM_EVOLUTIONS; evolution++) {
                for (int projectile = 0; projectile < NUM_DIFFERENT_PROJECTILES; projectile++) {
                    this.leftProjectiles[evolution][projectile] = ImageIO.read(
                            this.getClass().getResource(LEFT_PROJECTILE_PATH + evolution + "/" + projectile + PNG_EXT));
                    this.rightProjectiles[evolution][projectile] = ImageIO.read(this.getClass()
                            .getResource(RIGHT_PROJECTILE_PATH + evolution + "/" + projectile + PNG_EXT));
                }
            }

            // 메뉴 아이템 로드
            for (int i = 0; i < NUM_MENU_ICONS; i++) {
                this.menuItems[i] = ImageIO.read(this.getClass().getResource(MAIN_ICONS_PATH + i + PNG_EXT));
            }

        } catch (Exception e) {
            System.out.println("error loading image " + e.getMessage());
        }
    }
    
    // 게임 그리기
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!this.gamePaused) {
            // 게임 진행 중
            g.drawImage(background, 0, 0, null);

            // 게임 오버 및 유닛 관리 로직
            playerOne.automate(playerTwo);
            gameOverSecond = playerTwo.checkGameOver();
            if (gameOverSecond) {
                JPanel overPanel = new GameOverPanel(firstName);
                this.cardsPanel.add(overPanel, "over");
                this.cardLayout.show(cardsPanel, "over");
            }
            playerTwo.automate(playerOne);
            gameOverFirst = playerOne.checkGameOver();

            if (gameOverFirst) {
                JPanel overPanel = new GameOverPanel(secondName);
                cardsPanel.add(overPanel, "over");
                cardLayout.show(cardsPanel, "over");
            }

            // gold gained by killing other player's troops
            int playerTwoGained = playerOne.removeDeadCreatures();
            int playerOneGained = playerTwo.removeDeadCreatures();
            playerOne.gainGold(playerOneGained);
            playerTwo.gainGold(playerTwoGained);
            playerOne.draw(g);
            playerTwo.draw(g);

            // Menu
            for (int i = 0; i < NUM_CC_INFO; i++) {
                // Left Side Creature Stats
                g.drawImage(this.menuItems[i], LEFT_FIRST_ICON_POS.x, LEFT_FIRST_ICON_POS.y + (i * 2 * ICON_SEPARATOR),
                        null);
                // Right Side Creature Stats
                g.drawImage(this.menuItems[i], RIGHT_FIRST_ICON_POS.x,
                        RIGHT_FIRST_ICON_POS.y + (i * 2 * ICON_SEPARATOR), null);
            }

            g.setColor(Color.RED);
            g.setFont(new Font("Tahoma", Font.BOLD, 15));

            // draw creature icons
            for (int j = 0; j < NUM_CC_ICONS; j++) {
                g.drawImage(this.creatureCreationIcons[j][playerOne.getCurrentEvolution()],
                        LEFT_FIRST_CC_POS.x + (j * 2 * ICON_WIDTH), LEFT_FIRST_CC_POS.y, null);
                // Right side
                g.drawImage(this.creatureCreationIcons[j][playerTwo.getCurrentEvolution()],
                        RIGHT_FIRST_CC_POS.x + (j * 2 * ICON_WIDTH), RIGHT_FIRST_CC_POS.y, null);
            }

            // Drawing of turret to spawn
            for (int i = 0; i < MAX_TURRET_SPOTS; i++) {
                g.setFont(new Font("Tahoma", Font.BOLD, 20));
                g.setColor(Color.black);
                g.drawString(LEFT_TURRET_KEYS[i], LEFT_TURRET_POS[i].x, LEFT_TURRET_POS[i].y);
                g.drawString(RIGHT_TURRET_KEYS[i], RIGHT_TURRET_POS[i].x, RIGHT_TURRET_POS[i].y);
            }

            // Drawing of heart next to tower health bar
            g.drawImage(this.menuItems[HEALTH], playerOne.getTower().getPosition().x - 30,
                    playerOne.getTower().getPosition().y - 30, null);
            g.drawImage(this.menuItems[HEALTH], playerTwo.getTower().getPosition().x + 113,
                    playerTwo.getTower().getPosition().y - 30, null);

            // Gold, Evo, Pause, Sound
            g.setFont(new Font("Tahoma", Font.BOLD, 15));
            g.drawImage(this.menuItems[GOLD], 700, 60, null);
            g.drawImage(this.menuItems[GOLD], 400, 60, null);
            g.drawString(EVOLUTION, 400, 100);
            g.drawString(EVOLUTION, 700, 100);

            g.drawString(PAUSE_STRING, 500, SCREEN_HEIGHT - 81);
            g.drawImage(this.menuItems[PAUSE], 510, SCREEN_HEIGHT - 69, null);
            g.drawString(VOLUME_STRING, 647, SCREEN_HEIGHT - 81);

            if (this.playingMusic) {
                g.drawImage(this.menuItems[VOLUME_ON], 642, SCREEN_HEIGHT - 69, null);
            } else {
                g.drawImage(this.menuItems[VOLUME_OFF], 642, SCREEN_HEIGHT - 69, null);
            }

        } else {
        	// 일시정지 화면
            g.setFont(new Font("Tahoma", Font.BOLD, 100));
            g.drawString("PAUSED", SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2);
        }
    }
    	
    // 음악 재생
    public void playMusic() {
        music.setFramePosition(0);
        music.start();
    }

    public void stopPlayingMusic() {
        music.stop();
    }

    public boolean getPlayingMusic() {
        return this.playingMusic;
    }

    public boolean getGamePaused() {
        return this.gamePaused;
    }

    public void setGamePaused(boolean set) {
        this.gamePaused = set;
    }

    public void setPlayingMusic(boolean set) {
        this.playingMusic = set;
    }

}

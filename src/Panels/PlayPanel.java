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
import Entities.Creature;
import Entities.EnemyAI;
import Entities.Player;

public class PlayPanel extends JPanel implements GameConstants, EntityConstants {

    private Player playerOne, playerTwo;
    private BufferedImage background;
    private boolean playingMusic = true;
    private boolean gameOverFirst = false;
    private boolean gameOverSecond = false;
    private GameFrame gameFrame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String firstName, secondName;
    private boolean gamePaused = false;

    // image arrays
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

    private BufferedImage[] menuItems = new BufferedImage[NUM_MENU_ICONS];
    private BufferedImage[][] creatureCreationIcons = new BufferedImage[NUM_DIFFERENT_CREATURES][NUM_EVOLUTIONS];
    static AudioInputStream audioStream;
    static Clip music;
    
    public PlayPanel() {
        setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        importImages();
        try {
            background = ImageIO.read(this.getClass().getResource(BACKGROUND_PATH + "Game.jpg"));
        } catch (Exception e) {
        }

        if (music == null) {
            try {
                // 음악 객체를 처음 초기화할 때만 설정합니다.
                audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource("../Assets/backgroundMusic.wav"));
                music = AudioSystem.getClip();
                music.open(audioStream);
            } catch (Exception e) {
                System.out.println("Error loading background music: " + e.getMessage());
            }
        }


        firstName = LoginPanel.firstLoginField.getText();
        secondName = LoginPanel.secondLoginField.getText();

        this.playerOne = new Player(LEFT_TEAM, firstName, leftMove, leftAttack, leftProjectiles, leftTower, leftTurret);
        this.playerTwo = new Player(RIGHT_TEAM, secondName, rightMove, rightAttack, rightProjectiles, rightTower,
                rightTurret);
        
        // GameKeyListener 초기화 (PlayPanel을 전달)
        GameKeyListener gameKeyListener = new GameKeyListener(playerOne, playerTwo, this, false);
        addKeyListener(gameKeyListener);
    }
    // 플레이어 이름을 설정하는 메서드 추가
    public void setPlayerNames(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;

        // 플레이어 객체 생성 또는 업데이트
        this.playerOne = new Player(LEFT_TEAM, firstName, leftMove, leftAttack, leftProjectiles, leftTower, leftTurret);
        this.playerTwo = 
        		new Player(RIGHT_TEAM, secondName, rightMove, rightAttack, rightProjectiles, rightTower, rightTurret);
    }
    

    public void runGame() {
        playMusic();

        gameFrame = (GameFrame) SwingUtilities.getWindowAncestor(this);
        cardsPanel = gameFrame.getCardsPanel();
        cardLayout = (CardLayout) cardsPanel.getLayout();

        final Timer paintTimer = new Timer(PAINT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                repaint();
            }
        });
        paintTimer.start();

        final Timer summonTimer = new Timer(SUMMON_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!playerOne.getSummonQueue().isEmpty() && !getGamePaused()) {
                    playerOne.summonCreature();
                }
                if (!playerTwo.getSummonQueue().isEmpty() && !gamePaused) {
                    playerTwo.summonCreature();
                }
            }
        });
        summonTimer.start();

        final Timer goldTimer = new Timer(GOLD_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!gamePaused) {
                    playerOne.gainGold();
                    playerTwo.gainGold();
                }
            }
        });
        goldTimer.start();
    }


    public void importImages() {
        try {
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

            for (int i = 0; i < NUM_TOWER_TURRET_SPRITES; i++) {
                this.leftTower[i] = ImageIO.read(this.getClass().getResource(LEFT_TOWER_PATH + i + PNG_EXT));
                this.leftTurret[i] = ImageIO.read(this.getClass().getResource(LEFT_TURRET_PATH + i + PNG_EXT));

                this.rightTower[i] = ImageIO.read(this.getClass().getResource(RIGHT_TOWER_PATH + i + PNG_EXT));
                this.rightTurret[i] = ImageIO.read(this.getClass().getResource(RIGHT_TURRET_PATH + i + PNG_EXT));
            }

            for (int evolution = 0; evolution < NUM_EVOLUTIONS; evolution++) {
                for (int projectile = 0; projectile < NUM_DIFFERENT_PROJECTILES; projectile++) {
                    this.leftProjectiles[evolution][projectile] = ImageIO.read(
                            this.getClass().getResource(LEFT_PROJECTILE_PATH + evolution + "/" + projectile + PNG_EXT));
                    this.rightProjectiles[evolution][projectile] = ImageIO.read(this.getClass()
                            .getResource(RIGHT_PROJECTILE_PATH + evolution + "/" + projectile + PNG_EXT));
                }
            }

            for (int i = 0; i < NUM_MENU_ICONS; i++) {
                this.menuItems[i] = ImageIO.read(this.getClass().getResource(MAIN_ICONS_PATH + i + PNG_EXT));
            }

        } catch (Exception e) {
            System.out.println("error loading image " + e.getMessage());
        }
    }

    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!this.gamePaused) {
            // Draw background
            g.drawImage(background, 0, 0, null);

            // Automate player actions and check game over
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

            // Gold gained from killing creatures
            int playerTwoGained = playerOne.removeDeadCreatures();
            int playerOneGained = playerTwo.removeDeadCreatures();
            playerOne.gainGold(playerOneGained);
            playerTwo.gainGold(playerTwoGained);

            // Draw player units
            playerOne.draw(g);
            playerTwo.draw(g);

            // Draw health bars for playerOne's units
            g.setColor(Color.RED);
            for (Creature creature : playerOne.getCreatures()) {
                if (creature != null && creature.getHealth() > 0) {
                    int healthBarWidth = 50;
                    int healthBarHeight = 5;
                    int xOffset = (creature.getWidth() - healthBarWidth) / 2;
                    int yOffset = -10;

                    // Background of health bar
                    g.fillRect(creature.getPosition().x + xOffset, creature.getPosition().y + yOffset, healthBarWidth, healthBarHeight);

                    // Foreground (current health)
                    g.setColor(Color.GREEN);
                    int currentHealthBarWidth = (int) ((double) creature.getHealth() / creature.getMaxHealth() * healthBarWidth);
                    g.fillRect(creature.getPosition().x + xOffset, creature.getPosition().y + yOffset, currentHealthBarWidth, healthBarHeight);
                    g.setColor(Color.RED); // Reset color for the next health bar
                }
            }

            // Draw health bars for playerTwo's units
            for (Creature creature : playerTwo.getCreatures()) {
                if (creature != null && creature.getHealth() > 0) {
                    int healthBarWidth = 50;
                    int healthBarHeight = 5;
                    int xOffset = (creature.getWidth() - healthBarWidth) / 2;
                    int yOffset = -10;

                    // Background of health bar
                    g.fillRect(creature.getPosition().x + xOffset, creature.getPosition().y + yOffset, healthBarWidth, healthBarHeight);

                    // Foreground (current health)
                    g.setColor(Color.GREEN);
                    int currentHealthBarWidth = (int) ((double) creature.getHealth() / creature.getMaxHealth() * healthBarWidth);
                    g.fillRect(creature.getPosition().x + xOffset, creature.getPosition().y + yOffset, currentHealthBarWidth, healthBarHeight);
                    g.setColor(Color.RED); // Reset color for the next health bar
                }
            }

            // Draw menu items and UI
            for (int i = 0; i < NUM_CC_INFO; i++) {
                g.drawImage(this.menuItems[i], LEFT_FIRST_ICON_POS.x, LEFT_FIRST_ICON_POS.y + (i * 2 * ICON_SEPARATOR), null);
                g.drawImage(this.menuItems[i], RIGHT_FIRST_ICON_POS.x, RIGHT_FIRST_ICON_POS.y + (i * 2 * ICON_SEPARATOR), null);
            }

            g.setColor(Color.RED);
            g.setFont(new Font("Tahoma", Font.BOLD, 15));

            for (int j = 0; j < NUM_CC_ICONS; j++) {
                g.drawImage(this.creatureCreationIcons[j][playerOne.getCurrentEvolution()],
                        LEFT_FIRST_CC_POS.x + (j * 2 * ICON_WIDTH), LEFT_FIRST_CC_POS.y, null);
                g.drawImage(this.creatureCreationIcons[j][playerTwo.getCurrentEvolution()],
                        RIGHT_FIRST_CC_POS.x + (j * 2 * ICON_WIDTH), RIGHT_FIRST_CC_POS.y, null);
            }

            for (int i = 0; i < MAX_TURRET_SPOTS; i++) {
                g.setFont(new Font("Tahoma", Font.BOLD, 20));
                g.setColor(Color.black);
                g.drawString(LEFT_TURRET_KEYS[i], LEFT_TURRET_POS[i].x, LEFT_TURRET_POS[i].y);
                g.drawString(RIGHT_TURRET_KEYS[i], RIGHT_TURRET_POS[i].x, RIGHT_TURRET_POS[i].y);
            }

            g.drawImage(this.menuItems[HEALTH], playerOne.getTower().getPosition().x - 30,
                    playerOne.getTower().getPosition().y - 30, null);
            g.drawImage(this.menuItems[HEALTH], playerTwo.getTower().getPosition().x + 113,
                    playerTwo.getTower().getPosition().y - 30, null);

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
            g.setFont(new Font("Tahoma", Font.BOLD, 100));
            g.drawString("PAUSED", SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2);
        }
    }


    public void playMusic() {
        // 음악이 이미 재생 중인지 확인합니다.
        if (music.isRunning()) {
            return; // 이미 음악이 재생 중이면 새로운 재생을 시작하지 않습니다.
        }
        music.setFramePosition(0);
        music.start();
    }
    public void stopPlayingMusic() {
        if (music.isRunning()) {
            music.stop(); // 음악이 실행 중일 때만 멈춥니다.
        }
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

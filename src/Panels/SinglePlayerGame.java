package Panels;
import Core.Skill;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.CardLayout;
import Core.SkillType;
import Core.SlowSkill;
import Core.TimeWarpSkill;

import java.awt.Color;
import java.awt.Font;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import Core.Skill;
import Core.SkillType;
import Core.DamageSkill;
import Core.EntityConstants;
import Core.ExecuteSkill;
import Core.GameConstants;
import Core.GameFrame;
import Core.GameKeyListener;
import Entities.Creature;
import Entities.EnemyAI;
import Entities.Player;
import Entities.Projectile;
import Entities.Wall;

public class SinglePlayerGame extends JPanel implements GameConstants, EntityConstants {

    private static final int UPDATE_DELAY = 0;
	private Player playerOne;
    private Player playerTwo;
    private EnemyAI enemyAI;
    private BufferedImage background;
    private boolean playingMusic = true;
    private boolean gameOverFirst = false;
    private boolean gameOverSecond = false;
    private GameFrame gameFrame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private String firstName;
    private String secondName = "AI";
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

    private String difficulty;
    private double enemyAIDifficultyFactor;
    
    private GameKeyListener keyListener;
    private JButton skill1Button;
    private JButton skill2Button;

    public SinglePlayerGame() {
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        
        importImages();  // 이미지 로드

        // playerOne과 playerTwo 초기화
        firstName = "Player";
        this.playerOne = new Player(LEFT_TEAM, firstName, leftMove, leftAttack, leftProjectiles, leftTower, leftTurret);
        this.playerTwo = new Player(RIGHT_TEAM, secondName, rightMove, rightAttack, rightProjectiles, rightTower, rightTurret);

        // EnemyAI 초기화
        this.enemyAI = new EnemyAI(playerTwo, playerOne, 0.5); // 쉬움 난이도

        // GameKeyListener 추가
        GameKeyListener gameKeyListener = new GameKeyListener(playerOne, null, this, true);
        addKeyListener(gameKeyListener);

        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
            }
        });

        try {
            background = ImageIO.read(this.getClass().getResource(BACKGROUND_PATH + "Game.jpg"));
        } catch (Exception e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        if (music == null) {
            try {
                audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource("../Assets/backgroundMusic.wav"));
                music = AudioSystem.getClip();
                music.open(audioStream);
            } catch (Exception e) {
                System.out.println("Error loading background music: " + e.getMessage());
            }
        }
    }
    
    private ArrayList<SkillType> selectedSkills; // 선택된 스킬 저장
    private JButton skillButton1, skillButton2;

    public void setSelectedSkills(ArrayList<SkillType> selectedSkills) {
        if (selectedSkills == null || selectedSkills.size() != 2) {
            System.out.println("Skill selection is invalid or not set.");
            return;
        }

        this.selectedSkills = selectedSkills;
        System.out.println("Skills set for player: " + selectedSkills);

        // 스킬 버튼 추가
        addSkillButtons();
        repaint();
    }
    
    private void addSkillButtons() {
        // selectedSkills 방어 코드
        if (selectedSkills == null || selectedSkills.size() != 2) {
            System.out.println("Skill selection is invalid or not set.");
            return;
        }

        // 기존 버튼 제거
        if (skillButton1 != null) remove(skillButton1);
        if (skillButton2 != null) remove(skillButton2);

        try {
            skillButton1 = new JButton(selectedSkills.get(0).name());
            skillButton2 = new JButton(selectedSkills.get(1).name());

            skillButton1.setBounds(100, SCREEN_HEIGHT - 100, 150, 50);
            skillButton2.setBounds(300, SCREEN_HEIGHT - 100, 150, 50);

            skillButton1.addActionListener(e -> activateSkill(selectedSkills.get(0)));
            skillButton2.addActionListener(e -> activateSkill(selectedSkills.get(1)));

            add(skillButton1);
            add(skillButton2);

            revalidate();
            repaint();

            System.out.println("Skill buttons added: " + selectedSkills.get(0) + ", " + selectedSkills.get(1));
        } catch (Exception e) {
            System.out.println("Error adding skill buttons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void activateSkill(SkillType skill) {
        switch (skill) {
        	case WALL:
        		Point wallPosition = new Point(400, 400); // 벽 생성 위치
        		playerOne.addWall(wallPosition); // 벽 생성
        		System.out.println("Wall added at position: " + wallPosition);
        		repaint();
        		break;
            case STUN:
                playerTwo.stunAllCreatures(3000); // 3초 동안 스턴 적용
                System.out.println("Stun skill activated.");
                break;
            case DAMAGE:
                // 데미지 스킬 생성 및 사용
                DamageSkill firestorm = new DamageSkill("Firestorm", SkillType.DAMAGE, 0.1);
                firestorm.activate(playerOne, playerTwo); // 플레이어 1이 상대 플레이어에게 사용
                System.out.println("Damage skill 'Firestorm' activated.");
                break;
            case SLOW:
                SlowSkill slowSkill = new SlowSkill("Icy Blast", 0.5, 5000); // 50% 감소, 5초 지속
                slowSkill.activate(playerOne, playerTwo);
                System.out.println("Slow skill activated.");
                break;
            case EXECUTE:
                ExecuteSkill executeSkill = new ExecuteSkill("Execute", SkillType.EXECUTE, 1.0); // 성공 확률 100%
                executeSkill.activate(playerOne, playerTwo);
                System.out.println("Execute skill activated.");
                break;
            case RANDOM:
                new Skill("랜덤", SkillType.RANDOM).activate(playerOne, playerTwo);
                break;
            case TIME_WARP:
                TimeWarpSkill timeWarpSkill = new TimeWarpSkill("Time Warp", 0.3, 0.5, 5000); // 아군 30% 증가, 적 50% 감소
                timeWarpSkill.activate(playerOne, playerTwo);
                System.out.println("Time Warp skill activated.");
                break;
        }
    }
    
    public void setPlayerSkills(ArrayList<Skill> selectedSkills) {
        skill1Button = new JButton(selectedSkills.get(0).getName());
        skill2Button = new JButton(selectedSkills.get(1).getName());

        skill1Button.addActionListener(e -> selectedSkills.get(0).activate(playerOne, playerTwo));
        skill2Button.addActionListener(e -> selectedSkills.get(1).activate(playerOne, playerTwo));

        add(skill1Button);
        add(skill2Button);

        skill1Button.setBounds(100, SCREEN_HEIGHT - 100, 150, 50);
        skill2Button.setBounds(300, SCREEN_HEIGHT - 100, 150, 50);
    }

    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty.toLowerCase();
        double initialGold = 0.0;

        switch (this.difficulty) {
            case "easy":
                enemyAIDifficultyFactor = 0.5; // 쉬움: 낮은 공격력 및 소환 빈도
                initialGold = 200.0; // 쉬움 난이도의 초기 골드
                break;
            case "normal":
                enemyAIDifficultyFactor = 1.0; // 보통: 기본값                
                initialGold = 300.0; // 보통 난이도의 초기 골드

                break;
            case "hard":
                enemyAIDifficultyFactor = 1.5; // 어려움: 높은 공격력 및 소환 빈도
                initialGold = 500.0; // 어려움 난이도의 초기 골드
                break;
            default:
                enemyAIDifficultyFactor = 1.0; // 기본값
                initialGold = 300.0; // 기본값
                break;
        }

        // EnemyAI 인스턴스 초기화
        this.enemyAI = new EnemyAI(playerTwo, playerOne, enemyAIDifficultyFactor);
    }
    
    public void setPlayerNames(String firstName) {
        this.firstName = firstName;
        this.playerOne = new Player(LEFT_TEAM, firstName, leftMove, leftAttack, leftProjectiles, leftTower, leftTurret);
    }

    public void runGame() {
    	System.out.println("Game loop started.");
        playMusic();

        gameFrame = (GameFrame) SwingUtilities.getWindowAncestor(this);
        cardsPanel = gameFrame.getCardsPanel();
        cardLayout = (CardLayout) cardsPanel.getLayout();

        final Timer paintTimer = new Timer(PAINT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
            	 System.out.println("Paint timer tick...");
                repaint();
            }
        });
        paintTimer.start();

        final Timer summonTimer = new Timer(SUMMON_DELAY, new ActionListener() {
           
        	@Override
            public void actionPerformed(final ActionEvent e) {
                System.out.println("Summon timer tick...");
                if (!playerOne.getSummonQueue().isEmpty() && !getGamePaused()) {
                    playerOne.summonCreature();
                    System.out.println("Player 1 summoned a creature.");
                }
                if (!gamePaused) {
                    enemyAI.automate(playerOne);
                    System.out.println("AI automated action completed.");
                }
            }
        });
        summonTimer.start();

        final Timer goldTimer = new Timer(GOLD_DELAY, new ActionListener() {
        	
            @Override
            public void actionPerformed(final ActionEvent e) {
            	System.out.println("Gold timer tick...");
                if (!gamePaused) {
                    playerOne.gainGold();
                    playerTwo.gainGold();
                    System.out.println("Gold updated for both players.");
                }
            }
        });
        
        Timer updateTimer = new Timer(UPDATE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerOne.updateCreatures(); // 플레이어 1 유닛 상태 업데이트
                playerTwo.updateCreatures(); // 플레이어 2 유닛 상태 업데이트
            }
        });
        updateTimer.start();
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        System.out.println("paintComponent called.");
        
        // 충돌 확인
        playerOne.checkWallCollisions();
        
        // 스킬 버튼 위치 확인
        if (skillButton1 != null && skillButton2 != null) {
            System.out.println("Skill button 1 location: " + skillButton1.getBounds());
            System.out.println("Skill button 2 location: " + skillButton2.getBounds());
        }
    
        // 포커스가 없으면 다시 요청
        if (!isFocusOwner()) {
            requestFocusInWindow();
        }
        
        if (!this.gamePaused) {
            g.drawImage(background, 0, 0, null);

            // 플레이어와 AI 자동화 처리
            playerOne.automate(playerTwo);
            enemyAI.automate(playerOne); // AI 유닛 동작 및 이동 처리
            // 게임 종료 확인
            gameOverSecond = playerTwo.checkGameOver();
            if (gameOverSecond) {
                JPanel overPanel = new GameOverPanel(firstName);
                this.cardsPanel.add(overPanel, "over");
                this.cardLayout.show(cardsPanel, "over");
            }
            gameOverFirst = playerOne.checkGameOver();
            if (gameOverFirst) {
                JPanel overPanel = new GameOverPanel(secondName);
                cardsPanel.add(overPanel, "over");
                cardLayout.show(cardsPanel, "over");
            }

            // 죽은 유닛 정리 및 골드 획득
            int playerTwoGained = playerOne.removeDeadCreatures();
            int playerOneGained = playerTwo.removeDeadCreatures();
            playerOne.gainGold(playerOneGained);
            playerTwo.gainGold(playerTwoGained);

            // 유닛 및 AI의 움직임 반영
            playerOne.draw(g); // 플레이어 유닛 그리기
            playerTwo.draw(g); // AI 유닛도 움직인 후 그리기
            
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
            // 플레이어의 벽 그리기
            if (playerOne != null) {
                playerOne.drawWalls(g);
            }
            
            // 적 투사체와 플레이어의 벽 충돌 처리
            Projectile[] projectilesArray = playerTwo.getProjectiles().toArray(new Projectile[0]);
            for (int i = 0; i < projectilesArray.length; i++) {
                Projectile projectile = projectilesArray[i];
                if (projectile != null) { // Null 체크
                    for (Wall wall : playerOne.getWalls()) {
                        if (projectile.checkCollideWithWall(wall) && wall.isActive()) {
                            projectilesArray[i] = null; // 배열에서 투사체 제거
                            System.out.println("Projectile collided with wall and was removed.");
                            break;
                        }
                    }
                }
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
        if (music.isRunning()) {
            return;
        }
        music.setFramePosition(0);
        music.start();
    }

    public void stopPlayingMusic() {
        if (music.isRunning()) {
            music.stop();
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

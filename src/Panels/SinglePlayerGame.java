package Panels;
import Core.Skill;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.BasicStroke;
import java.awt.CardLayout;
import Core.SkillType;
import Core.SlowSkill;

import Core.TimeWarpSkill;
import Core.TowerStrikeSkill;
import Core.WeakenSkill;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import Core.Skill;
import Core.SkillType;
import Core.AllKillSkill;
import Core.DamageSkill;
import Core.DemonicDiceSkill;
import Core.EntityConstants;
import Core.ExecuteSkill;
import Core.GameConstants;
import Core.GameFrame;
import Core.GameKeyListener;
import Core.GodsWrathSkill;
import Core.HealSkill;
import Core.HealTowerSkill;
import Core.MartialLawSkill;
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
    private JPanel settingsMenu;

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

    private DemonicDiceSkill demonicDiceSkill; // Demonic Dice 스킬 객체
    private List<Creature> killedCreatures = new ArrayList<>(); // 즉사된 유닛 리스트

    
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

    //신의 격노 변수
    private boolean skillEffectActive = false;
    private GodsWrathSkill godsWrathSkill;
    
    private TowerStrikeSkill towerStrikeSkill; // 적 타워 공격 스킬 변수
    
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
            //AllKill skill 
            case ALL_KILL:
                AllKillSkill allKillSkill = new AllKillSkill("All Kill", SkillType.ALL_KILL);
                allKillSkill.activate(playerOne, playerTwo);
                System.out.println("AllKill skill activated.");
                break;
            //신의 격노 스킬
            case GODS_WRATH:
                godsWrathSkill = new GodsWrathSkill("God's Wrath", SkillType.GODS_WRATH, 100); // 데미지 100
                godsWrathSkill.activate(playerOne, playerTwo);
                skillEffectActive = true;

                // 효과가 일정 시간 후 비활성화되도록 타이머 설정
                new javax.swing.Timer(1000, e -> skillEffectActive = false).start();
                break;
            
            case TOWER_STRIKE:
                towerStrikeSkill = new TowerStrikeSkill("Tower Strike", SkillType.TOWER_STRIKE, 100); // 데미지 100
                towerStrikeSkill.activate(playerOne, playerTwo);
                skillEffectActive = true;

                // 레이저 효과를 일정 시간 후 제거
                new javax.swing.Timer(1000, e -> skillEffectActive = false).start();
                break;
                
            case DEMONIC_DICE: //악마의 주사위 -> 아군 적군 상관없이 3명 재거
                DemonicDiceSkill demonicDiceSkill = new DemonicDiceSkill("Demonic Dice", SkillType.DEMONIC_DICE, 3); // 랜덤 3명 즉사
                demonicDiceSkill.activate(playerOne, playerTwo);
                break;
                
            case HEAL:
                HealSkill healSkill = new HealSkill("Healing Wave", SkillType.HEAL, 0.3); // 30% 체력 회복
                healSkill.activate(playerOne, playerTwo);
                System.out.println("Heal skill activated.");
                break;
                
            case HEAL_TOWER:
                HealTowerSkill healTowerSkill = new HealTowerSkill("Tower Repair", SkillType.HEAL_TOWER, 100); // 100 HP 회복
                healTowerSkill.activate(playerOne, playerTwo);
                break;
                
            //적군 약화
            case WEAKEN:
                WeakenSkill weakenSkill = new WeakenSkill("Weakening Curse", 0.3, 5000); // 30% 감소, 5초 지속
                weakenSkill.activate(playerOne, playerTwo);
                System.out.println("Weaken skill activated.");
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
            
            // 번개 효과 그리기 - 신의 격노
         // "신의 격노" 스킬 효과 그리기
            if (skillEffectActive && godsWrathSkill != null) {
                godsWrathSkill.draw(g);
            }
            
            if (skillEffectActive && towerStrikeSkill != null) {
                towerStrikeSkill.draw(g);
            }
            
            
         // Demonic Dice 스킬 효과 그리기
            if (demonicDiceSkill != null) {
                demonicDiceSkill.draw(g, killedCreatures); // 선택된 유닛 그래픽 효과 표시
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
        } else { //디자인 수정 
        	g.setFont(new Font("Tahoma", Font.BOLD, 100));

        	// 텍스트를 화면 상단에 배치
        	int x = SCREEN_WIDTH / 3; // 가로 위치
        	int y = SCREEN_HEIGHT / 10; // 세로 위치

        	// "PAUSED" 텍스트 그림자 효과
        	Graphics2D g2d = (Graphics2D) g;
        	g2d.setFont(new Font("Tahoma", Font.BOLD, 100));
        	g2d.setColor(new Color(50, 50, 50, 180)); // 회색 그림자 (투명도 180)
        	g2d.drawString("PAUSED", x + 5, y + 5); // 그림자를 약간 아래로 이동

        	// "PAUSED" 텍스트 그라데이션 효과
        	GradientPaint gradient = new GradientPaint(
        	    x, y, Color.BLUE, // 텍스트 위쪽 색상
        	    x, y + 50, Color.CYAN // 텍스트 아래쪽 색상
        	);
        	g2d.setPaint(gradient);
        	g2d.drawString("PAUSED", x, y);

        	// 텍스트 외곽선 효과
        	//g2d.setStroke(new BasicStroke(2f)); // 외곽선 두께 설정
        	//g2d.setColor(Color.BLACK); // 외곽선 색상
        	//g2d.draw(new java.awt.geom.Rectangle2D.Double(x - 10, y - 70, 350, 110)); // 외곽선 박스 그리기

        	// 추가적인 텍스트 아래 설명 추가
        	g2d.setFont(new Font("Tahoma", Font.PLAIN, 40));
        	g2d.setColor(Color.WHITE);
        	String subText = "Press SPACE to Resume";
        	g2d.drawString(subText, x - 20, y + 60);

        }
    }
    
    public void showSettingsMenu() {
        // 설정 메뉴가 처음 생성되는 경우
        if (settingsMenu == null) {
            settingsMenu = new JPanel();
            settingsMenu.setLayout(null); // 절대 레이아웃 사용
            settingsMenu.setBounds(400, 150, 500, 400); // 설정 메뉴 위치 및 크기 설정
            settingsMenu.setBackground(new Color(0, 0, 0, 200)); // 반투명한 검은 배경 추가

            // "뒤로가기" 버튼 생성
            JButton backButton = new JButton("뒤로가기"); // 버튼 텍스트 설정
            backButton.setBounds(150, 50, 200, 50); // 버튼 위치 및 크기 설정
            backButton.setFont(new Font("맑은 고딕", Font.BOLD, 18)); // 폰트 스타일 및 크기
            backButton.setForeground(Color.WHITE); // 버튼 텍스트 색상
            backButton.setBackground(new Color(34, 139, 34)); // 버튼 배경색
            backButton.setFocusPainted(false); // 클릭 시 버튼 테두리 제거
            backButton.addActionListener(e -> hideSettingsMenu()); // 클릭 이벤트: 설정 메뉴 숨기기
            settingsMenu.add(backButton); // 설정 메뉴에 버튼 추가

            // "메인 메뉴로 돌아가기" 버튼 생성
            JButton mainMenuButton = new JButton("메인 메뉴로 돌아가기"); // 버튼 텍스트 설정
            mainMenuButton.setBounds(150, 120, 200, 50); // 버튼 위치 및 크기 설정
            mainMenuButton.setFont(new Font("맑은 고딕", Font.BOLD, 18)); // 폰트 스타일 및 크기
            mainMenuButton.setForeground(Color.WHITE); // 버튼 텍스트 색상
            mainMenuButton.setBackground(new Color(178, 34, 34)); // 버튼 배경색
            mainMenuButton.setFocusPainted(false); // 클릭 시 버튼 테두리 제거
            mainMenuButton.addActionListener(e -> {
                System.out.println("메인 메뉴로 돌아갑니다."); // 로그 출력
                if (gameFrame != null) { // GameFrame이 유효한 경우
                    CardLayout layout = (CardLayout) gameFrame.getCardsPanel().getLayout(); // CardLayout 가져오기
                    layout.show(gameFrame.getCardsPanel(), "menu"); // "menu" 패널로 전환
                    stopPlayingMusic(); // 배경 음악 중지
                }
            });
            settingsMenu.add(mainMenuButton); // 설정 메뉴에 버튼 추가

            // 볼륨 조절 라벨 생성
            JLabel volumeLabel = new JLabel("볼륨 조절"); // 라벨 텍스트 설정
            volumeLabel.setBounds(180, 200, 150, 30); // 라벨 위치 및 크기 설정
            volumeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16)); // 폰트 스타일 및 크기
            volumeLabel.setForeground(Color.WHITE); // 라벨 텍스트 색상
            settingsMenu.add(volumeLabel); // 설정 메뉴에 라벨 추가

            // 볼륨 조절 슬라이더 생성
            JSlider volumeSlider = new JSlider(0, 100, 50); // 최소값 0, 최대값 100, 기본값 50
            volumeSlider.setBounds(100, 240, 300, 50); // 슬라이더 위치 및 크기 설정
            volumeSlider.setMajorTickSpacing(25); // 주요 눈금 간격 설정
            volumeSlider.setMinorTickSpacing(5); // 부 눈금 간격 설정
            volumeSlider.setPaintTicks(true); // 눈금 표시 활성화
            volumeSlider.setPaintLabels(true); // 눈금 라벨 표시 활성화
            volumeSlider.addChangeListener(e -> { // 슬라이더 값 변경 이벤트
                int volume = volumeSlider.getValue(); // 슬라이더 값 가져오기
                setVolume(volume); // 음량 설정
                System.out.println("Volume set to: " + volume); // 로그 출력
            });
            settingsMenu.add(volumeSlider); // 설정 메뉴에 슬라이더 추가

            // 설정 메뉴를 현재 패널에 추가
            this.add(settingsMenu);
        }

        // 게임을 일시 정지하고 설정 메뉴 표시
        setGamePaused(true); // 게임 일시 정지
        settingsMenu.setVisible(true); // 설정 메뉴 표시
        this.repaint(); // 화면 갱신
    }



    public void hideSettingsMenu() {
        if (settingsMenu != null) {
            settingsMenu.setVisible(false);
        }
        setGamePaused(false); // 게임 재개
        this.repaint();
    }

    public void draw(Graphics g, List<Creature> killedCreatures) {
        g.setColor(Color.RED); // 빨간색 표시
        for (Creature creature : killedCreatures) {
            Point position = creature.getPosition();
            g.fillOval(position.x - 10, position.y - 10, 20, 20); // 유닛 위에 붉은 원 표시
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
    
    
    /**
     * 게임 배경 음악의 볼륨을 설정합니다.
     *
     * @param volume 사용자 입력 슬라이더 값 (0~100)
     */
    private void setVolume(int volume) {
        if (music != null && music.isRunning()) { // 배경 음악이 실행 중인지 확인
            try {
                FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN); // 볼륨 제어 가져오기
                float volumeRange = gainControl.getMaximum() - gainControl.getMinimum(); // 볼륨 범위 계산
                float adjustedVolume = gainControl.getMinimum() + (volume / 100.0f) * volumeRange; // 입력 값을 범위에 맞게 조정
                gainControl.setValue(adjustedVolume); // 볼륨 값 설정
            } catch (IllegalArgumentException e) {
                System.out.println("Volume control not supported: " + e.getMessage()); // 오류 로그 출력
            }
        }
    }

}

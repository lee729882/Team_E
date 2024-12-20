package Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Panels.LoginPanel;
import Panels.MenuPanel;
import Panels.ModeSelectionPanel;
import Panels.RulesPanel;
import Panels.SinglePlayerDifficultyPanel;
import Panels.PlayPanel;
import Panels.SinglePlayerGame;
import Panels.SkillSelectionPanel;

public class GameFrame extends JFrame implements GameConstants {

    JPanel cardsPanel, modeSelectionPanel, loginPanel, menuPanel, rulesPanel, playPanel, overPanel, singlePlayerGame, singlePlayerDifficultyPanel, skillSelectionPanel;

    public GameFrame() {
        // set up frame
        setTitle("War Times");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setFocusable(false);

        // set a custom cursor
        try {
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                    new ImageIcon(this.getClass().getResource("../Assets/cursor" + PNG_EXT)).getImage(), new Point(0, 0),
                    "custom cursor"));
        } catch (Exception e) {
            System.out.println("Custom cursor not found: " + e.getMessage());
        }

        cardsPanel = new JPanel(new CardLayout());
        modeSelectionPanel = new ModeSelectionPanel();
        loginPanel = new LoginPanel();
        menuPanel = new MenuPanel();
        rulesPanel = new RulesPanel();
        playPanel = new PlayPanel(); // PlayPanel 인스턴스 추가
        singlePlayerGame = new SinglePlayerGame();
        singlePlayerDifficultyPanel = new SinglePlayerDifficultyPanel(new DifficultyButtonListener());
        skillSelectionPanel = new SkillSelectionPanel(e -> {
            System.out.println("Skill selection confirmed.");
            ArrayList<SkillType> selectedSkills = ((SkillSelectionPanel) skillSelectionPanel).getSelectedSkills();
            System.out.println("Selected skills: " + selectedSkills);
            ((SinglePlayerGame) singlePlayerGame).setSelectedSkills(selectedSkills);
            CardLayout cl = (CardLayout) cardsPanel.getLayout();
            cl.show(cardsPanel, "single_player");
            System.out.println("Switched to single_player panel.");
            ((SinglePlayerGame) singlePlayerGame).runGame(); // 게임 실행 호출
        });

        cardsPanel.add(modeSelectionPanel, "mode_selection");
        cardsPanel.add(loginPanel, "login");
        cardsPanel.add(menuPanel, "menu");
        cardsPanel.add(rulesPanel, "rules");
        cardsPanel.add(singlePlayerGame, "single_player");
        cardsPanel.add(singlePlayerDifficultyPanel, "single_player_difficulty");
        cardsPanel.add(playPanel, "multi_player");
        cardsPanel.add(skillSelectionPanel, "skill_selection");

        add(cardsPanel);
        setVisible(true);

        // 싱글 플레이 버튼 액션 리스너 수정
        ((ModeSelectionPanel) modeSelectionPanel).getSinglePlayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Single player button clicked.");
                CardLayout cl = (CardLayout) cardsPanel.getLayout();
                if (playPanel instanceof PlayPanel) {
                    ((PlayPanel) playPanel).stopPlayingMusic();
                }
                cl.show(cardsPanel, "single_player_difficulty");
                System.out.println("Switched to single_player_difficulty panel.");

                singlePlayerDifficultyPanel.setFocusable(true);
                singlePlayerDifficultyPanel.requestFocusInWindow();
            }
        });

        // 멀티 플레이 버튼 액션 리스너 추가
        ((ModeSelectionPanel) modeSelectionPanel).getMultiplayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Multiplayer button clicked.");
                CardLayout cl = (CardLayout) cardsPanel.getLayout();
                ((PlayPanel) playPanel).stopPlayingMusic();
                cl.show(cardsPanel, "login");
                System.out.println("Switched to login panel.");

                ((LoginPanel) loginPanel).getOKButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Login OK button clicked.");
                        ((PlayPanel) playPanel).setPlayerNames(
                                ((LoginPanel) loginPanel).getFirstPlayerName(),
                                ((LoginPanel) loginPanel).getSecondPlayerName()
                        );

                        cl.show(cardsPanel, "multi_player");
                        ((PlayPanel) playPanel).runGame();

                        playPanel.setFocusable(true);
                        playPanel.requestFocusInWindow();
                        System.out.println("Switched to multi_player panel.");
                    }
                });
            }
        });
    }

    public JPanel getCardsPanel() {
        return this.cardsPanel;
    }

    private class DifficultyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Difficulty Selected: " + e.getActionCommand());
            String difficulty = e.getActionCommand();
            CardLayout cl = (CardLayout) cardsPanel.getLayout();

            ((SinglePlayerGame) singlePlayerGame).setDifficulty(difficulty);

            System.out.println("Switching to Skill Selection Panel...");
            cl.show(cardsPanel, "skill_selection");
            skillSelectionPanel.repaint();
            skillSelectionPanel.revalidate();
            skillSelectionPanel.setFocusable(true);
            SwingUtilities.invokeLater(() -> skillSelectionPanel.requestFocusInWindow());
            System.out.println("Skill Selection Panel displayed.");
        }
    }
}
package Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Panels.LoginPanel;
import Panels.MenuPanel;
import Panels.ModeSelectionPanel;
import Panels.RulesPanel;
import Panels.SinglePlayerDifficultyPanel;
import Panels.PlayPanel;
import Panels.SinglePlayerGame;

public class GameFrame extends JFrame implements GameConstants {

    JPanel cardsPanel, modeSelectionPanel, loginPanel, menuPanel, rulesPanel, playPanel, overPanel, singlePlayerGame, singlePlayerDifficultyPanel;

    public GameFrame() {
        // set up frame
        setTitle("War Times");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setFocusable(false);

        // set a custom cursor
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(this.getClass().getResource("../Assets/cursor" + PNG_EXT)).getImage(), new Point(0, 0),
                "custom cursor"));

        cardsPanel = new JPanel(new CardLayout());
        modeSelectionPanel = new ModeSelectionPanel();
        loginPanel = new LoginPanel();
        menuPanel = new MenuPanel();
        rulesPanel = new RulesPanel();
        playPanel = new PlayPanel(); // PlayPanel 인스턴스 추가
        singlePlayerGame = new SinglePlayerGame();
        singlePlayerDifficultyPanel = new SinglePlayerDifficultyPanel(new DifficultyButtonListener()); // 수정된 부분

        cardsPanel.add(modeSelectionPanel, "mode_selection");
        cardsPanel.add(loginPanel, "login");
        cardsPanel.add(menuPanel, "menu");
        cardsPanel.add(rulesPanel, "rules");
        cardsPanel.add(singlePlayerGame, "single_player"); // SinglePlayerGame 추가
        cardsPanel.add(singlePlayerDifficultyPanel, "single_player_difficulty"); // SinglePlayerDifficultyPanel 추가
        cardsPanel.add(playPanel, "multi_player"); // PlayPanel 추가

        add(cardsPanel);
        setVisible(true);

        // 싱글 플레이 버튼 액션 리스너 수정
        ((ModeSelectionPanel) modeSelectionPanel).getSinglePlayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardsPanel.getLayout();
                if (playPanel instanceof PlayPanel) {
                    ((PlayPanel) playPanel).stopPlayingMusic(); // 이전 음악 중지
                }
                cl.show(cardsPanel, "single_player_difficulty"); // 'single_player_difficulty'로 이동

                // 싱글 플레이 난이도 선택 패널에 포커스를 설정하여 키 리스너가 작동하도록 함
                singlePlayerDifficultyPanel.setFocusable(true);
                singlePlayerDifficultyPanel.requestFocusInWindow();
            }
        });

        // 멀티 플레이 버튼 액션 리스너 추가
        ((ModeSelectionPanel) modeSelectionPanel).getMultiplayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardsPanel.getLayout();
                ((PlayPanel) playPanel).stopPlayingMusic(); // 이전 음악 중지
                cl.show(cardsPanel, "login"); // 로그인 화면으로 이동
                
                // 로그인 완료 후 PlayPanel로 이동하는 로직
                ((LoginPanel) loginPanel).getOKButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 플레이어 이름 설정
                        ((PlayPanel) playPanel).setPlayerNames(
                                ((LoginPanel) loginPanel).getFirstPlayerName(),
                                ((LoginPanel) loginPanel).getSecondPlayerName()
                        );

                        // 게임 화면으로 전환
                        cl.show(cardsPanel, "multi_player");
                        ((PlayPanel) playPanel).runGame(); // 인자 없이 runGame() 호출

                        // 멀티 플레이 패널에 포커스를 설정하여 키 리스너가 작동하도록 함
                        playPanel.setFocusable(true);
                        playPanel.requestFocusInWindow();
                    }
                });
            }
        });
    }

    public JPanel getCardsPanel() {
        return this.cardsPanel;
    }

 // DifficultyButtonListener 수정
    private class DifficultyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String difficulty = e.getActionCommand(); // 버튼의 actionCommand로 난이도 가져옴
            CardLayout cl = (CardLayout) cardsPanel.getLayout();

            // SinglePlayerGame의 난이도 설정
            ((SinglePlayerGame) singlePlayerGame).setDifficulty(difficulty);
            
            // SinglePlayerGame 화면으로 전환
            cl.show(cardsPanel, "single_player");

            // 게임 실행
            ((SinglePlayerGame) singlePlayerGame).runGame();
        }
    }

}



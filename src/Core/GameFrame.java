package Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Panels.LoginPanel;
import Panels.MenuPanel;
import Panels.ModeSelectionPanel;
import Panels.RulesPanel;
import Panels.PlayPanel;
import Panels.SinglePlayerGame;

public class GameFrame extends JFrame implements GameConstants {

    JPanel cardsPanel, modeSelectionPanel, loginPanel, menuPanel, rulesPanel, playPanel, overPanel, singlePlayerGame;

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

        cardsPanel.add(modeSelectionPanel, "mode_selection");
        cardsPanel.add(loginPanel, "login");
        cardsPanel.add(menuPanel, "menu");
        cardsPanel.add(rulesPanel, "rules");
        cardsPanel.add(singlePlayerGame, "single_player"); // SinglePlayerGame 추가
        cardsPanel.add(playPanel, "multi_player"); // PlayPanel 추가

        add(cardsPanel);
        setVisible(true);

        // 싱글 플레이 버튼 액션 리스너 추가
        ((ModeSelectionPanel) modeSelectionPanel).getSinglePlayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardsPanel.getLayout();
                if (playPanel instanceof PlayPanel) {
                    ((PlayPanel) playPanel).stopPlayingMusic(); // 이전 음악 중지
                }
                cl.show(cardsPanel, "single_player");
                ((SinglePlayerGame) singlePlayerGame).runGame();

                // 싱글 플레이 패널에 포커스를 설정하여 키 리스너가 작동하도록 함
                singlePlayerGame.setFocusable(true);
                singlePlayerGame.requestFocusInWindow();
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
}


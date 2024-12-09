package Core;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Panels.PlayPanel;
import Panels.RulesPanel;
import Panels.SinglePlayerGame;

public class ButtonListener implements ActionListener {
    GameFrame gameFrame;
    JPanel holdingPanel;
    JPanel cardsPanel;
    String source;
    CardLayout layout;

    public ButtonListener(JPanel target) {
        this.holdingPanel = target;
    }

    public void actionPerformed(ActionEvent event) {
        this.source = ((JButton) event.getSource()).getActionCommand();
        gameFrame = (GameFrame) SwingUtilities.getRoot(this.holdingPanel);
        cardsPanel = gameFrame.getCardsPanel();
        layout = (CardLayout) cardsPanel.getLayout();

        if (this.source.equals("OK")) {
            PlayPanel playPanel = new PlayPanel(); // 새로운 PlayPanel 생성
            cardsPanel.add(playPanel, "play"); // "play"라는 이름으로 카드 레이아웃에 추가
            layout.show(cardsPanel, "play"); // "play" 화면으로 전환
            playPanel.requestFocusInWindow(); // PlayPanel에 포커스 설정
            playPanel.runGame(); // 게임 실행 (인자 없이 runGame 메서드 호출)
        } else if (this.source.equals("OK - rules")) {
            ((RulesPanel) gameFrame.rulesPanel).reset();
            layout.show(cardsPanel, "mode_selection"); // ModeSelectionPanel로 전환

        } else if (this.source.equals("single")) {
            SinglePlayerGame singlePlayerGame = new SinglePlayerGame(); // 싱글 플레이 게임 생성
            cardsPanel.add(singlePlayerGame, "single");
            layout.show(cardsPanel, "single_player"); // SinglePlayerGame 화면으로 전환

        } else if (this.source.equals("multi")) {
            PlayPanel multiPlayPanel = new PlayPanel(); // Replace with multiplayer logic
            cardsPanel.add(multiPlayPanel, "multi_play");
            layout.show(cardsPanel, "login");
            multiPlayPanel.requestFocusInWindow();
            multiPlayPanel.runGame(); // 게임 실행 (인자 없이 runGame 메서드 호출)

        } else if (this.source.equals("settings")) {
            // 설정 동작 (필요시 설정 화면 추가)
            layout.show(cardsPanel, "rules"); // 예시로 rules 화면 표시

        } else if (this.source.equals("rules")) {
            layout.show(cardsPanel, "rules");

        } else if (this.source.equals("next")) {
            ((RulesPanel) this.holdingPanel).nextSlide();

        } else if (this.source.equals("start")) {
            PlayPanel playPanel = new PlayPanel();
            cardsPanel.add(playPanel, "play");
            layout.show(cardsPanel, "play");
            playPanel.requestFocusInWindow();
            playPanel.runGame(); // 게임 실행 (인자 없이 runGame 메서드 호출)

        } else if (this.source.equals("quit")) {
            gameFrame.dispose();
        }
    }

}
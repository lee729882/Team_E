package Core;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Panels.PlayPanel;
import Panels.RulesPanel;

// 버튼 클릭 이벤트를 처리하는 클래스
public class ButtonListener implements ActionListener {
    GameFrame gameFrame; // 메인 게임 프레임 참조
    JPanel holdingPanel; // 이벤트를 발생시킨 버튼이 포함된 패널
    JPanel cardsPanel; // CardLayout으로 관리되는 메인 패널
    String source; // 버튼의 액션 명령 (ActionCommand)
    CardLayout layout; // CardLayout 객체

    // 생성자: 버튼 리스너를 초기화하고 해당 패널을 설정
    public ButtonListener(JPanel target) {
        this.holdingPanel = target;
    }

    // 버튼 클릭 이벤트 처리 메서드
    @Override
    public void actionPerformed(ActionEvent event) {
        // 버튼의 ActionCommand를 가져옴
        this.source = ((JButton) event.getSource()).getActionCommand();
        // holdingPanel을 포함하는 최상위 GameFrame을 가져옴
        gameFrame = (GameFrame) SwingUtilities.getRoot(this.holdingPanel);
        // GameFrame에서 CardLayout을 사용하는 메인 패널(cardsPanel)을 가져옴
        cardsPanel = gameFrame.getCardsPanel();
        layout = (CardLayout) cardsPanel.getLayout();

        // "OK" 버튼 클릭 시 메인 메뉴로 이동
        if (this.source.equals("OK")) {
            layout.show(cardsPanel, "menu");

        // "OK - rules" 버튼 클릭 시 규칙 패널을 초기화하고 메인 메뉴로 이동
        } else if (this.source.equals("OK - rules")) {
            ((RulesPanel) gameFrame.rulesPanel).reset();
            layout.show(cardsPanel, "menu");

        // "rules" 버튼 클릭 시 규칙 패널로 이동
        } else if (this.source.equals("rules")) {
            layout.show(cardsPanel, "rules");

        // "next" 버튼 클릭 시 규칙 패널에서 다음 슬라이드로 이동
        } else if (this.source.equals("next")) {
            ((RulesPanel) this.holdingPanel).nextSlide();

        // "start" 버튼 클릭 시 게임을 시작
        } else if (this.source.equals("start")) {
            // PlayPanel을 생성하고 cardsPanel에 추가
            PlayPanel playPanel = new PlayPanel();
            cardsPanel.add(playPanel, "play");
            // PlayPanel로 전환
            layout.show(cardsPanel, "play");
            // 포커스를 PlayPanel로 설정
            playPanel.requestFocusInWindow();
            // 게임 실행
            playPanel.runGame(playPanel);

        // "quit" 버튼 클릭 시 애플리케이션 종료
        } else if (this.source.equals("quit")) {
            gameFrame.dispose();
        }
    }
}

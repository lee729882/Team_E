package Core;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Panels.LoginPanel;
import Panels.MenuPanel;
import Panels.RulesPanel;

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.Toolkit;

// 게임의 메인 프레임을 정의하는 클래스
// - 다양한 패널(Login, Menu, Rules 등)을 CardLayout을 통해 관리
public class GameFrame extends JFrame implements GameConstants {

    // 다양한 화면을 관리하기 위한 패널
    JPanel cardsPanel, loginPanel, menuPanel, rulesPanel, playPanel, overPanel;

    // 생성자: 프레임과 화면 구성 요소를 초기화
    public GameFrame() {
        // 프레임 설정
        setTitle("War of Ages"); // 게임 제목
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기 버튼 클릭 시 프로그램 종료
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT); // 화면 크기 설정
        setResizable(false); // 크기 조정 불가
        setLocationRelativeTo(null); // 화면 중앙에 위치
        setFocusable(false); // 프레임에 포커스 비활성화

        // 사용자 정의 커서 설정
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(this.getClass().getResource("../Assets/cursor" + PNG_EXT)).getImage(), // 커서 이미지
                new Point(0, 0), // 커서 포인트
                "custom cursor" // 커서 이름
        ));

        // CardLayout을 사용하는 패널 생성
        cardsPanel = new JPanel(new CardLayout());
        loginPanel = new LoginPanel(); // 로그인 화면 패널
        menuPanel = new MenuPanel(); // 메뉴 화면 패널
        rulesPanel = new RulesPanel(); // 규칙 화면 패널

        // 각 화면을 CardLayout에 추가
        cardsPanel.add(loginPanel, "login"); // "login" 이름으로 로그인 화면 추가
        cardsPanel.add(menuPanel, "menu"); // "menu" 이름으로 메뉴 화면 추가
        cardsPanel.add(rulesPanel, "rules"); // "rules" 이름으로 규칙 화면 추가

        // CardLayout 패널을 프레임에 추가
        add(cardsPanel);
        setVisible(true); // 프레임 표시
    }

    // CardLayout 패널을 반환하는 메서드
    public JPanel getCardsPanel() {
        return this.cardsPanel;
    }

}

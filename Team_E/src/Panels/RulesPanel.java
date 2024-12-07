package Panels;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Core.ButtonListener;
import Core.GameConstants;

// 게임 규칙 패널(RulesPanel) 클래스
// - 게임의 규칙을 화면에 표시하는 역할을 수행
public class RulesPanel extends JPanel implements GameConstants {

    private JLabel firstLabel, secondLabel; // 첫 번째와 두 번째 규칙 페이지의 레이블
    private JButton nextButton, OKButton; // "Next" 버튼과 "OK" 버튼
    private ImageIcon firstBackground, secondBackground; // 첫 번째와 두 번째 규칙 페이지의 배경 이미지

    // 생성자: 규칙 패널 초기화
    public RulesPanel() {
        setLayout(null); // 레이아웃 매니저 사용하지 않음

        // 첫 번째 규칙 페이지의 배경 이미지 설정
        firstBackground = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "RulesFirst.jpg"));
        this.firstLabel = new JLabel(firstBackground); // 첫 번째 페이지 레이블 생성

        // "Next" 버튼 생성 및 설정
        this.nextButton = new JButton("Next"); // 다음 페이지로 이동 버튼
        nextButton.setActionCommand("next"); // 버튼의 액션 커맨드 설정
        nextButton.addActionListener(new ButtonListener(this)); // 버튼 클릭 이벤트 리스너 추가

        // 컴포넌트 추가
        add(nextButton); // "Next" 버튼 추가
        add(firstLabel); // 첫 번째 규칙 페이지 레이블 추가

        // 컴포넌트 위치 및 크기 설정
        firstLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT); // 첫 번째 페이지 레이블 위치 및 크기
        nextButton.setBounds(505, 580, 180, 55); // "Next" 버튼 위치 및 크기
    }

    // 두 번째 규칙 페이지로 이동
    public void nextSlide() {
        // 첫 번째 페이지 구성 제거
        this.remove(firstLabel);
        this.remove(nextButton);

        // 두 번째 규칙 페이지의 배경 이미지 설정
        secondBackground = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "RulesSecond.jpg"));
        secondLabel = new JLabel(secondBackground); // 두 번째 페이지 레이블 생성

        // "OK" 버튼 생성 및 설정
        OKButton = new JButton("OK"); // 규칙 확인 후 메뉴로 돌아가기 버튼
        OKButton.setActionCommand("OK - rules"); // 버튼의 액션 커맨드 설정
        OKButton.addActionListener(new ButtonListener(this)); // 버튼 클릭 이벤트 리스너 추가

        // 컴포넌트 추가
        this.add(OKButton); // "OK" 버튼 추가
        this.add(secondLabel); // 두 번째 규칙 페이지 레이블 추가

        // 컴포넌트 위치 및 크기 설정
        OKButton.setBounds(909, 35, 180, 55); // "OK" 버튼 위치 및 크기
        secondLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT); // 두 번째 페이지 레이블 위치 및 크기
    }

    // 규칙 패널 초기화
    // - 두 번째 페이지에서 첫 번째 페이지로 돌아갈 때 사용
    public void reset() {
        // 두 번째 페이지 구성 제거
        this.remove(secondLabel);
        this.remove(OKButton);

        // 첫 번째 페이지 구성 추가
        this.add(nextButton); // "Next" 버튼 추가
        this.add(firstLabel); // 첫 번째 페이지 레이블 추가

        // 컴포넌트 위치 및 크기 설정
        firstLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT); // 첫 번째 페이지 레이블 위치 및 크기
        nextButton.setBounds(505, 580, 180, 55); // "Next" 버튼 위치 및 크기
    }
}

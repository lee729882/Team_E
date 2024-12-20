package Panels;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Core.GameConstants;
import Entities.EnemyAI;

public class SinglePlayerDifficultyPanel extends JPanel implements GameConstants {

    // 버튼 멤버 변수 선언
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;

    private EnemyAI enemyAI;

    public SinglePlayerDifficultyPanel(ActionListener difficultyListener) {
        setLayout(null);

        // 패널 크기 설정
        Dimension panelSize = new Dimension(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        setPreferredSize(panelSize);

        // 배경 이미지 추가
        ImageIcon background = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "Menu.jpg"));
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(background);
        backgroundLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        // 버튼 생성
        easyButton = createRoundedButton("쉬움");
        normalButton = createRoundedButton("보통");
        hardButton = createRoundedButton("어려움");

        // 버튼 크기와 간격
        int buttonWidth = 250;
        int buttonHeight = 60;
        int buttonSpacing = 20;

        // 버튼 위치 계산 (아래 중앙 정렬)
        int centerX = (BACKGROUND_WIDTH - buttonWidth) / 2 ;
        int baseY = BACKGROUND_HEIGHT - (3 * buttonHeight + 2 * buttonSpacing + 50); // 아래쪽에서 50px 위에 배치

        easyButton.setBounds(centerX, baseY, buttonWidth, buttonHeight);
        normalButton.setBounds(centerX, baseY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        hardButton.setBounds(centerX, baseY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
        
        // ActionCommand 설정
        easyButton.setActionCommand("easy");
        normalButton.setActionCommand("normal");
        hardButton.setActionCommand("hard");

        // ActionListener 추가
        easyButton.addActionListener(difficultyListener);
        normalButton.addActionListener(difficultyListener);
        hardButton.addActionListener(difficultyListener);
        
        // 컴포넌트 추가
        add(easyButton);
        add(normalButton);
        add(hardButton);
        add(backgroundLabel); // 배경을 맨 마지막에 추가

        // 배경을 맨 아래로 설정
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
    
    // 둥근 버튼 스타일링 메서드
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 버튼 배경색
                g2.setColor(hovered ? new Color(169, 169, 169) : new Color(105, 105, 105)); // 마우스 오버 시 밝은 회색
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); // 둥근 사각형

                // 테두리
                g2.setColor(new Color(255, 215, 0)); // 금색 테두리
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 30, 30));

                // 텍스트 그리기
                super.paintComponent(g2);
                g2.dispose();
            }

            {
                // 마우스 이벤트 추가
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true; // 마우스가 버튼 위로 올라옴
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false; // 마우스가 버튼을 벗어남
                        repaint();
                    }
                });
            }
        };

        // 텍스트와 투명도 설정
        button.setFont(new Font("굴림체", Font.BOLD, 20)); // 강렬한 전쟁 테마 서체
        button.setForeground(Color.WHITE); // 텍스트 색상
        button.setFocusPainted(false); // 포커스 표시 제거
        button.setOpaque(false); // 투명 배경
        button.setContentAreaFilled(false); // 배경 색상 제거
        button.setBorder(new EmptyBorder(10, 10, 10, 10)); // 여백 설정

        return button;
    }

    // 각 버튼에 접근할 수 있도록 메서드 제공
    public JButton getEasyButton() {
        return easyButton;
    }

    public JButton getNormalButton() {
        return normalButton;
    }

    public JButton getHardButton() {
        return hardButton;
    }
}


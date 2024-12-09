package Panels;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Core.GameConstants;

public class ModeSelectionPanel extends JPanel implements GameConstants {

    private JButton singlePlayerButton;
    private JButton multiplayerButton;

    public ModeSelectionPanel() {
        setLayout(null);

        // 패널 크기 설정
        Dimension panelSize = new Dimension(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        setPreferredSize(panelSize);

        // 배경 이미지 추가
        ImageIcon background = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "Menu.jpg"));
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(background);
        backgroundLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        // 버튼 추가
        singlePlayerButton = createRoundedButton("싱글 플레이");
        multiplayerButton = createRoundedButton("멀티 플레이");
        JButton settingsButton = createRoundedButton("설명");

        // 버튼 크기와 간격
        int buttonWidth = 250;
        int buttonHeight = 60;
        int buttonSpacing = 20;

        // 버튼 위치 계산 (아래 중앙 정렬)
        int centerX = (BACKGROUND_WIDTH - buttonWidth) / 2;
        int baseY = BACKGROUND_HEIGHT - (3 * buttonHeight + 2 * buttonSpacing + 50); // 아래쪽에서 50px 위에 배치

        singlePlayerButton.setBounds(centerX, baseY, buttonWidth, buttonHeight);
        multiplayerButton.setBounds(centerX, baseY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        settingsButton.setBounds(centerX, baseY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);

        // 컴포넌트 추가
        add(singlePlayerButton);
        add(multiplayerButton);
        add(settingsButton);
        add(backgroundLabel); // 배경을 맨 마지막에 추가

        // 배경을 맨 아래로 설정
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }

    // 싱글 플레이 버튼 반환 메서드
    public JButton getSinglePlayerButton() {
        return singlePlayerButton;
    }

    // 멀티 플레이 버튼 반환 메서드
    public JButton getMultiplayerButton() {
        return multiplayerButton;
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
}

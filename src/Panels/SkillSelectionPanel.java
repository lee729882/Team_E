package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import Core.SkillType;

/**
 * SkillSelectionPanel 클래스는 사용자가 게임에서 사용할 두 가지 스킬을 선택할 수 있는 UI를 제공합니다.
 */
public class SkillSelectionPanel extends JPanel {

    private ArrayList<SkillType> selectedSkills; // 선택된 스킬 저장
    private JButton confirmButton; // 선택 완료 버튼
    private JLabel previewLabel1, previewLabel2; // 선택한 스킬 미리보기

    public SkillSelectionPanel(ActionListener confirmListener) {
        // 패널 기본 설정
        setLayout(null); // 레이아웃 비활성화
        setBackground(new Color(30, 30, 30)); // 패널 배경색 설정
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // 패널 테두리 설정

        // 선택된 스킬 초기화
        selectedSkills = new ArrayList<>();

        // 배경 이미지 추가
        JLabel background = new JLabel(new ImageIcon(this.getClass().getResource("../Assets/Backgrounds/")));
        background.setBounds(0, 0, 1200, 675);
        add(background);

        // 스킬 버튼 생성
        SkillType[] skills = SkillType.values();
        int x = 150, y = 200; // 버튼 초기 위치
        for (SkillType skill : skills) {
            JButton skillButton = new JButton(skill.name());
            skillButton.setBounds(x, y, 150, 50);

            // 버튼 스타일 적용
            skillButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            skillButton.setBackground(new Color(70, 130, 180)); // 버튼 배경색
            skillButton.setForeground(Color.WHITE); // 버튼 글자색
            skillButton.setFocusPainted(false);
            skillButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            // 버튼 클릭 이벤트
            skillButton.addActionListener(e -> {
                if (selectedSkills.size() < 2 && !selectedSkills.contains(skill)) {
                    selectedSkills.add(skill);
                    updatePreview();
                }
            });
            add(skillButton);

            // 버튼 간격 조정
            x += 200;
            if (x > 800) {
                x = 150;
                y += 80;
            }
        }

        // 미리보기 라벨 설정
        previewLabel1 = new JLabel("선택 1: 없음", SwingConstants.CENTER);
        previewLabel1.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        previewLabel1.setForeground(Color.WHITE);
        previewLabel1.setBounds(150, 50, 400, 30);
        add(previewLabel1);

        previewLabel2 = new JLabel("선택 2: 없음", SwingConstants.CENTER);
        previewLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        previewLabel2.setForeground(Color.WHITE);
        previewLabel2.setBounds(650, 50, 400, 30);
        add(previewLabel2);

        // 추가 설명 라벨 설정
        JLabel instructionLabel = new JLabel("사용할 스킬을 두 개 선택하세요.", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        instructionLabel.setForeground(Color.YELLOW);
        instructionLabel.setBounds(300, 100, 600, 40);
        add(instructionLabel);

        // 확인 버튼 설정
        confirmButton = new JButton("확인");
        confirmButton.setBounds(500, 550, 200, 50);
        confirmButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        confirmButton.setEnabled(false); // 스킬 선택 전까지 비활성화
        confirmButton.setBackground(new Color(34, 139, 34)); // 버튼 배경색
        confirmButton.setForeground(Color.WHITE); // 버튼 글자색
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        confirmButton.addActionListener(e -> {
            if (selectedSkills.size() == 2) {
                confirmListener.actionPerformed(e);
            }
        });
        add(confirmButton);

        // 툴팁 추가
        for (SkillType skill : skills) {
            String tooltip = "스킬 설명: " + skill.name() + "은(는) 매우 강력한 스킬입니다.";
            JButton skillButton = findButtonBySkillName(skill.name());
            if (skillButton != null) {
                skillButton.setToolTipText(tooltip);
            }
        }

        // 컴포넌트들 아래에 배경을 추가
        add(background);
        setComponentZOrder(background, getComponentCount() - 1); // 배경을 뒤로 보냄
    }

    /**
     * 선택한 스킬 미리보기 업데이트
     */
    private void updatePreview() {
        if (selectedSkills.size() > 0) {
            previewLabel1.setText("선택 1: " + selectedSkills.get(0).name());
        }
        if (selectedSkills.size() > 1) {
            previewLabel2.setText("선택 2: " + selectedSkills.get(1).name());
            confirmButton.setEnabled(true); // 두 개 선택 시 확인 버튼 활성화
        }
    }

    /**
     * 선택된 스킬 반환
     *
     * @return 선택된 스킬 리스트
     */
    public ArrayList<SkillType> getSelectedSkills() {
        return selectedSkills;
    }

    /**
     * 특정 이름의 버튼을 찾습니다.
     *
     * @param skillName 스킬 이름
     * @return 해당 이름의 JButton 또는 null
     */
    private JButton findButtonBySkillName(String skillName) {
        for (Component component : getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals(skillName)) {
                    return button;
                }
            }
        }
        return null;
    }

    /**
     * 추가적인 디자인 요소 (예: 배경 그림자 효과) 설정
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
    }
}

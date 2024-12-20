package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import Core.SkillType;
import Core.ButtonListener;
import Core.GameConstants;


public class SkillSelectionPanel extends JPanel {
    private ArrayList<SkillType> selectedSkills; // 선택된 스킬 저장
    private JButton confirmButton; // 선택 완료 버튼
    private JLabel previewLabel1, previewLabel2; // 선택한 스킬 미리보기

    public SkillSelectionPanel(ActionListener confirmListener) {
        setLayout(null); // 레이아웃 비활성화

        // 선택된 스킬 초기화
        selectedSkills = new ArrayList<>();

        // 배경 패널
        JLabel background = new JLabel(new ImageIcon(this.getClass().getResource("../Assets/Backgrounds/")));
        background.setBounds(0, 0, 1200, 675);
        add(background);

        // 스킬 버튼 생성
        SkillType[] skills = SkillType.values();
        int x = 150, y = 200; // 버튼 초기 위치
        for (SkillType skill : skills) {
            JButton skillButton = new JButton(skill.name());
            skillButton.setBounds(x, y, 150, 50);
            skillButton.addActionListener(e -> {
                if (selectedSkills.size() < 2 && !selectedSkills.contains(skill)) {
                    selectedSkills.add(skill);
                    updatePreview();
                }
            });
            add(skillButton);
            x += 200;
            if (x > 800) {
                x = 150;
                y += 80;
            }
        }

        // 미리보기 라벨
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

        // 확인 버튼
        confirmButton = new JButton("확인");
        confirmButton.setBounds(500, 550, 200, 50);
        confirmButton.setEnabled(false); // 스킬 선택 전까지 비활성화
        confirmButton.addActionListener(e -> {
            if (selectedSkills.size() == 2) {
                confirmListener.actionPerformed(e);
            }
        });
        add(confirmButton);

        // 컴포넌트들 아래에 배경을 추가
        add(background);
        setComponentZOrder(background, getComponentCount() - 1); // 배경을 뒤로 보냄
    }

    // 선택한 스킬 업데이트
    private void updatePreview() {
        if (selectedSkills.size() > 0) {
            previewLabel1.setText("선택 1: " + selectedSkills.get(0).name());
        }
        if (selectedSkills.size() > 1) {
            previewLabel2.setText("선택 2: " + selectedSkills.get(1).name());
            confirmButton.setEnabled(true); // 두 개 선택 시 확인 버튼 활성화
        }
    }

    // 선택된 스킬 반환
    public ArrayList<SkillType> getSelectedSkills() {
        return selectedSkills;
    }
}

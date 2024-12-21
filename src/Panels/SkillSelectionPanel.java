package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import Core.SkillType;

/**
 * SkillSelectionPanel 클래스는 사용자가 게임에서 사용할 두 가지 스킬을 선택하고 초기화할 수 있는 UI를 제공합니다.
 */
public class SkillSelectionPanel extends JPanel {

    private ArrayList<SkillType> selectedSkills; // 선택된 스킬 저장
    private JButton confirmButton; // 선택 완료 버튼
    private JButton resetButton; // 초기화 버튼
    private JLabel previewLabel1, previewLabel2; // 선택한 스킬 미리보기
    private JLabel instructionLabel; // 사용 설명 라벨
    private JLabel backgroundImage; // 배경 이미지

    public SkillSelectionPanel(ActionListener confirmListener) {
        // 패널 기본 설정
        setLayout(null); // 레이아웃 비활성화
        setBackground(new Color(30, 30, 30)); // 패널 배경색 설정
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // 패널 테두리 설정

        // 선택된 스킬 초기화
        selectedSkills = new ArrayList<>();

        // 배경 이미지 추가
        initializeBackground();

        // 스킬 버튼 생성
        initializeSkillButtons();

        // 미리보기 라벨 설정
        initializePreviewLabels();

        // 추가 설명 라벨 설정
        initializeInstructionLabel();

        // 확인 및 초기화 버튼 설정 (아래로 배치)
        initializeControlButtons(confirmListener);

        // 배경을 뒤로 보내기
        setComponentZOrder(backgroundImage, getComponentCount() - 1);
    }

    /**
     * 배경 이미지 초기화
     */
    private void initializeBackground() {
        backgroundImage = new JLabel(new ImageIcon(this.getClass().getResource("../Assets/Backgrounds/")));
        backgroundImage.setBounds(0, 0, 1200, 675);
        add(backgroundImage);
    }

    /**
     * 스킬 버튼 생성 및 초기화
     */
    private void initializeSkillButtons() {
        SkillType[] skills = SkillType.values();
        int x = 150, y = 200; // 버튼 초기 위치

        for (SkillType skill : skills) {
            JButton skillButton = createSkillButton(skill, x, y);

            // 버튼 클릭 이벤트
            skillButton.addActionListener(e -> {
                if (selectedSkills.size() < 2 && !selectedSkills.contains(skill)) {
                    selectedSkills.add(skill);
                    updatePreview();
                }
            });
            add(skillButton);

            // 버튼 위치 조정
            x += 200;
            if (x > 800) {
                x = 150;
                y += 80;
            }
        }
    }

    /**
     * 스킬 버튼 생성
     */
    private JButton createSkillButton(SkillType skill, int x, int y) {
        JButton skillButton = new JButton(skill.name());
        skillButton.setBounds(x, y, 150, 50);

        // 버튼 스타일 설정
        skillButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        skillButton.setBackground(new Color(70, 130, 180)); // 버튼 배경색
        skillButton.setForeground(Color.WHITE); // 버튼 글자색
        skillButton.setFocusPainted(false);
        skillButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        skillButton.setToolTipText(skill.getDescription()); // 툴팁 설정

        return skillButton;
    }

    /**
     * 미리보기 라벨 설정
     */
    private void initializePreviewLabels() {
        previewLabel1 = createPreviewLabel("선택 1: 없음", 150, 50);
        previewLabel2 = createPreviewLabel("선택 2: 없음", 650, 50);

        add(previewLabel1);
        add(previewLabel2);
    }

    /**
     * 미리보기 라벨 생성
     */
    private JLabel createPreviewLabel(String text, int x, int y) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 400, 30);
        return label;
    }

    /**
     * 추가 설명 라벨 설정
     */
    private void initializeInstructionLabel() {
        instructionLabel = new JLabel("사용할 스킬을 두 개 선택하세요.", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        instructionLabel.setForeground(Color.YELLOW);
        instructionLabel.setBounds(300, 100, 600, 40);
        add(instructionLabel);
    }

    /**
     * 확인 및 초기화 버튼 설정 (아래로 배치)
     */
    private void initializeControlButtons(ActionListener confirmListener) {
        confirmButton = createControlButton("확인", 400, 600, new Color(34, 139, 34)); // 아래로 배치
        resetButton = createControlButton("초기화", 600, 600, new Color(255, 69, 0)); // 아래로 배치

        confirmButton.addActionListener(e -> {
            if (selectedSkills.size() == 2) {
                confirmListener.actionPerformed(e);
            }
        });

        resetButton.addActionListener(e -> resetSelection());

        add(confirmButton);
        add(resetButton);
    }

    /**
     * 컨트롤 버튼 생성
     */
    private JButton createControlButton(String text, int x, int y, Color background) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 150, 50);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
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
     * 선택 초기화
     */
    private void resetSelection() {
        selectedSkills.clear(); // 선택된 스킬 초기화
        previewLabel1.setText("선택 1: 없음");
        previewLabel2.setText("선택 2: 없음");
        confirmButton.setEnabled(false); // 확인 버튼 비활성화
        System.out.println("선택 초기화 완료");
    }

    /**
     * 선택된 스킬 반환
     */
    public ArrayList<SkillType> getSelectedSkills() {
        return selectedSkills;
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

package Panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Core.ButtonListener;
import Core.GameConstants;

//LoginPanel 클래스에 getOKButton 메서드 추가
public class LoginPanel extends JPanel implements GameConstants {

 static JTextField firstLoginField, secondLoginField;
 private JButton OKButton; // OK 버튼을 멤버 변수로 변경

 public LoginPanel() {
     setLayout(null);

     // 배경 이미지 설정
     ImageIcon background = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "Login.jpg"));
     JLabel backgroundLabel = new JLabel();
     backgroundLabel.setIcon(background);

     // 라벨 설정
     JLabel firstLoginLabel = new JLabel("플레이어 1 이름: ");
     JLabel secondLoginLabel = new JLabel("플레이어 2 이름: ");
     
     Font koreanFont = new Font("맑은 고딕", Font.PLAIN, 20); // 한글 지원 폰트 설정
     firstLoginLabel.setFont(koreanFont);
     secondLoginLabel.setFont(koreanFont);

     firstLoginLabel.setForeground(Color.WHITE); // 텍스트 색상
     secondLoginLabel.setForeground(Color.WHITE);

     // 텍스트 필드
     firstLoginField = new JTextField(20);
     secondLoginField = new JTextField(20);

     // OK 버튼
     OKButton = new JButton("OK");
     OKButton.setActionCommand("OK");

     // 컴포넌트 추가
     add(firstLoginLabel);
     add(secondLoginLabel);
     add(firstLoginField);
     add(secondLoginField);
     add(OKButton);
     add(backgroundLabel);

     // 위치 설정
     backgroundLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
     firstLoginLabel.setBounds(350, 295, 300, 40);
     firstLoginField.setBounds(670, 295, 250, 40);
     secondLoginLabel.setBounds(350, 365, 300, 40);
     secondLoginField.setBounds(670, 365, 250, 40);
     OKButton.setBounds(505, 550, 200, 60);

     // 버튼 리스너 추가
     OKButton.addActionListener(new ButtonListener(this));
 }

 // 플레이어 1 이름 반환 메서드
 public String getFirstPlayerName() {
     return firstLoginField.getText();
 }

 // 플레이어 2 이름 반환 메서드
 public String getSecondPlayerName() {
     return secondLoginField.getText();
 }

 // OK 버튼을 반환하는 메서드 추가
 public JButton getOKButton() {
     return OKButton;
 }
}

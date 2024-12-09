package Panels;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Core.ButtonListener;
import Core.GameConstants;

public class MenuPanel extends JPanel implements GameConstants {

    public MenuPanel() {
        setLayout(null);
        ImageIcon background = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "Menu.jpg"));
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(background);


        add(backgroundLabel);

        JButton singlePlayerButton = new JButton("Single Player");
        JButton multiplayerButton = new JButton("Multiplayer");
        singlePlayerButton.setActionCommand("single");
        multiplayerButton.setActionCommand("multi");
        add(singlePlayerButton);
        add(multiplayerButton);
        singlePlayerButton.setBounds(300, 500, 300, 60);
        multiplayerButton.setBounds(650, 500, 300, 60);
        singlePlayerButton.addActionListener(new ButtonListener(this));
        multiplayerButton.addActionListener(new ButtonListener(this));
        backgroundLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

    }

}

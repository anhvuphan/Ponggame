import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PongGame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PongGame::showMenu);
    }

    public static void showMenu() {
        JFrame menuFrame = new JFrame("Pong Game - Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(400, 300);
        menuFrame.setLayout(new GridLayout(3, 1));

        JLabel title = new JLabel("Pong Game", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 24));
        menuFrame.add(title);

        JButton singlePlayerButton = new JButton("Single Player (vs AI)");
        singlePlayerButton.setFont(new Font("Consolas", Font.PLAIN, 18));
        singlePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new GameFrame(true); // Chế độ chơi đơn
            }
        });

        JButton multiplayerButton = new JButton("Multiplayer (2 Players)");
        multiplayerButton.setFont(new Font("Consolas", Font.PLAIN, 18));
        multiplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new GameFrame(false); // Chế độ hai người chơi
            }
        });

        menuFrame.add(singlePlayerButton);
        menuFrame.add(multiplayerButton);

        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }
}

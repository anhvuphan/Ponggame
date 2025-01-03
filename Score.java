import java.awt.*;

public class Score extends Rectangle {

    static int GAME_WIDTH, GAME_HEIGHT;
    int player1, player2;

    Score(int GAME_WIDTH, int GAME_HEIGHT) {
        Score.GAME_WIDTH = GAME_WIDTH;
        Score.GAME_HEIGHT = GAME_HEIGHT;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);
        g.drawString(String.format("%02d", player1), GAME_WIDTH / 2 - 100, 50);
        g.drawString(String.format("%02d", player2), GAME_WIDTH / 2 + 20, 50);
    }
}


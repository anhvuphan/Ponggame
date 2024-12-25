import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    int id;
    int yVelocity;
    int speed = 10;

    Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.id = id;
    }

    public void keyPressed(KeyEvent e) {
        if (id == 1) {
            if (e.getKeyCode() == KeyEvent.VK_W) setYDirection(-speed);
            if (e.getKeyCode() == KeyEvent.VK_S) setYDirection(speed);
        } else if (id == 2) {
            if (e.getKeyCode() == KeyEvent.VK_UP) setYDirection(-speed);
            if (e.getKeyCode() == KeyEvent.VK_DOWN) setYDirection(speed);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (id == 1 || id == 2) setYDirection(0);
    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    public void move() {
        y += yVelocity;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Vẽ bóng đổ
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(x + 5, y + 5, width, height);

        // Vẽ paddle với gradient
        GradientPaint gradient = new GradientPaint(
                x, y, id == 1 ? Color.BLUE : Color.RED, 
                x + width, y + height, Color.WHITE
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, width, height);

        // Viền paddle
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x, y, width, height);
    }
}
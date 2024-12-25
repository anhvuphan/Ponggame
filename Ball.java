import java.awt.*;
import java.util.Random;

public class Ball extends Rectangle {

    int xVelocity, yVelocity;
    int initialSpeed = 2;
    int hitCount = 0;

    Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
        Random random = new Random();
        setXDirection(random.nextBoolean() ? initialSpeed : -initialSpeed);
        setYDirection(random.nextBoolean() ? initialSpeed : -initialSpeed);
    }

    public void setXDirection(int xDirection) {
        xVelocity = xDirection;
    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    public void increaseSpeed() {
        xVelocity += xVelocity > 0 ? 1 : -1;
        yVelocity += yVelocity > 0 ? 1 : -1;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Vẽ bóng đổ
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillOval(x + 5, y + 5, width, height);

        // Gradient cho bóng
        GradientPaint gradient = new GradientPaint(
                x, y, Color.WHITE, 
                x + width, y + height, Color.GRAY
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x, y, width, height);

        // Highlight ánh sáng
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(x + width / 4, y + height / 4, width / 2, height / 2);

        // Viền bóng
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x, y, width, height);
    }
}

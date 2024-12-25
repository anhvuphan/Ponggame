import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * 0.5555);
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1, paddle2;
    Ball ball;
    Score score;
    Image backgroundImage;
    boolean isPaused = false; // Biến kiểm tra trạng thái tạm dừng
    boolean isSinglePlayer = false; // Biến kiểm tra chế độ chơi đơn

    GamePanel(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        // Load hình nền
        try {
            backgroundImage = Toolkit.getDefaultToolkit().getImage("Background.jpg");
        } catch (Exception e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball(
                (GAME_WIDTH / 2) - (BALL_DIAMETER / 2),
                random.nextInt(GAME_HEIGHT - BALL_DIAMETER),
                BALL_DIAMETER,
                BALL_DIAMETER
        );
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        // Vẽ hình nền
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }

        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);

        // Hiển thị thông báo "PAUSED" nếu trò chơi đang tạm dừng
        if (isPaused) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 50));
            g.drawString("PAUSED", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
        if (!isPaused) { // Chỉ di chuyển nếu trò chơi không bị tạm dừng
            paddle1.move();
            if (isSinglePlayer) {
                aiMove(); // Di chuyển AI nếu chế độ chơi đơn
            } else {
                paddle2.move();
            }
            ball.move();
        }
    }

    private void aiMove() {
        // AI di chuyển paddle2 để theo bóng
        if (ball.y < paddle2.y + (PADDLE_HEIGHT / 2)) {
            paddle2.setYDirection(-paddle2.speed);
        } else if (ball.y > paddle2.y + (PADDLE_HEIGHT / 2)) {
            paddle2.setYDirection(paddle2.speed);
        } else {
            paddle2.setYDirection(0);
        }
        paddle2.move();
    }

    public void checkCollision() {
        if (isPaused) return; // Không kiểm tra va chạm nếu tạm dừng

        // Ball bounces off top and bottom edges
        if (ball.y <= 0 || ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }

        // Ball bounces off paddles
        if (ball.intersects(paddle1) || ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            ball.yVelocity += ball.yVelocity > 0 ? 1 : -1;
            ball.setXDirection(ball.intersects(paddle1) ? ball.xVelocity : -ball.xVelocity);
        }

        // Prevent paddles from moving out of bounds
        paddle1.y = Math.max(0, Math.min(paddle1.y, GAME_HEIGHT - PADDLE_HEIGHT));
        paddle2.y = Math.max(0, Math.min(paddle2.y, GAME_HEIGHT - PADDLE_HEIGHT));

        // Update score and reset positions
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
        } else if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
        }
    }

    @Override
    public void run() {
        double nsPerTick = 1_000_000_000.0 / 60.0;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    public class AL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // Tạm dừng / Tiếp tục khi nhấn phím P
            if (e.getKeyCode() == KeyEvent.VK_P) {
                isPaused = !isPaused;
            }
            paddle1.keyPressed(e);
            if (!isSinglePlayer) {
                paddle2.keyPressed(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            if (!isSinglePlayer) {
                paddle2.keyReleased(e);
            }
        }
    }
}

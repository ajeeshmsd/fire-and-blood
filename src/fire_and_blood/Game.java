package fire_and_blood;

import fire_and_blood.graphics.Screen;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {
    public static final long serialVersionUID = 1L;

    // Size of the Game
    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;

    private Thread thread;
    private JFrame frame;
    private Boolean running = false;

    private BufferedImage image;
    private int[] pixels;

    Screen screen;

    public Game() {
        Dimension size = new Dimension(width * scale, height*scale);
        setPreferredSize(size);

        screen = new Screen(width, height);

        frame = new JFrame();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            update();
            render();
        }

    }

    public void update() {

    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();

        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        screen.render();

        for(int i = 0; i < pixels.length; ++i)
        {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();

    }
    public static void main(String[] args) {
        Game game = new Game();

        game.frame.setResizable(false);
        game.frame.setTitle("Fire and Blood");
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.start();
    }
}

package thegame.object.brick;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StrongBrick extends Brick {

    private BufferedImage normalImage;
    private BufferedImage crackedImage;
    public boolean cracked = false;

    public StrongBrick(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color, 2);
        loadImages();
    }

    public void hit() {
        if (isDestroyed()) return;
        hitsTaken++;
        if (hitsTaken == 1) {
            cracked = true;
        }
    }

    private void loadImages() {
        try {
            normalImage = ImageIO.read(getClass().getResource("/thegame/Picture/bricktim.png"));
            crackedImage = ImageIO.read(getClass().getResource("/thegame/Picture/Source/gachtimvo.png"));
        } catch (IOException e) {
            System.out.println("khong the tai anh strongbrick");
        }
    }
    public void draw(Graphics2D g2d){
        if(isDestroyed()) return;
        if(cracked && crackedImage !=null){
            g2d.drawImage(crackedImage, x, y, width, height, null);
        } else if(normalImage != null){
            g2d.drawImage(normalImage, x, y, width, height, null);
        } else {
            g2d.setColor(color);
            g2d.fillRect(x, y, width, height);
        }
    }
    public Image getCurrentImage() {
        if (isDestroyed()) return null;
        if (cracked && crackedImage != null) return crackedImage;
        if (normalImage != null) return normalImage;
        return null;
    }
}
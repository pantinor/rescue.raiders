package rescue.raiders.objects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.io.File;
import javax.imageio.ImageIO;

public class AlignCopterSprites {

    private static final int COLS = 7;
    private static final int ROWS = 3;

    private static final Rectangle[] SPRITE_REGIONS = new Rectangle[]{
        // copter frame 0
        new Rectangle(11, 6, 97, 69),
        // copter frame 1
        new Rectangle(114, 2, 97, 69),
        // copter frame 2
        new Rectangle(215, 0, 97, 69),
        // copter frame 3
        new Rectangle(319, 3, 97, 69),
        // copter frame 4
        new Rectangle(424, 4, 97, 69),
        // copter frame 5
        new Rectangle(533, 3, 97, 69),
        // copter frame 6
        new Rectangle(633, 3, 97, 69),
        // copter frame 7
        new Rectangle(2, 82, 97, 69),
        // copter frame 8
        new Rectangle(109, 83, 97, 69),
        // copter frame 9
        new Rectangle(206, 82, 97, 69),
        // copter frame 10
        new Rectangle(306, 80, 97, 69),
        // copter frame 11
        new Rectangle(417, 81, 97, 69),
        // copter frame 12
        new Rectangle(516, 75, 97, 69),
        // copter frame 13
        new Rectangle(618, 75, 97, 69),
        // copter frame 14
        new Rectangle(0, 164, 97, 69),
        // copter frame 15
        new Rectangle(111, 163, 97, 69),
        // copter frame 16
        new Rectangle(209, 164, 97, 69),
        // copter frame 17
        new Rectangle(307, 164, 97, 69),
        // copter frame 18
        new Rectangle(410, 160, 97, 69),
        // copter frame 19
        new Rectangle(511, 159, 97, 69),
        // copter frame 20
        new Rectangle(609, 161, 97, 69)
    };

    public static void main(String[] args) throws Exception {
        BufferedImage src = ImageIO.read(new File("src/main/resources/assets/image/enemy-copter.png"));

        int cellW = 97;
        int cellH = 69;

        int dstW = cellW * COLS;
        int dstH = cellH * ROWS;

        BufferedImage dst = new BufferedImage(dstW, dstH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dst.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, dstW, dstH);
        g.setComposite(AlphaComposite.SrcOver);

        for (int i = 0; i < SPRITE_REGIONS.length; i++) {
            Rectangle r = SPRITE_REGIONS[i];
            if (r.width > 0 && r.height > 0) {

                int sx = r.x;
                int sy = r.y;
                int sw = Math.min(r.width, src.getWidth() - sx);
                int sh = Math.min(r.height, src.getHeight() - sy);

                // Crop the region out of the source image
                BufferedImage sprite = src.getSubimage(sx, sy, sw, sh);

                // Trim transparent padding from the sprite
                BufferedImage cropped;
                int w = sprite.getWidth(), h = sprite.getHeight();
                int minX = w, minY = h, maxX = -1, maxY = -1;
                for (int yy = 0; yy < h; yy++) {
                    for (int xx = 0; xx < w; xx++) {
                        int argb = sprite.getRGB(xx, yy);
                        if ((argb & 0xFF000000) != 0) {
                            // this pixel is NOT fully transparent
                            minX = Math.min(minX, xx);
                            minY = Math.min(minY, yy);
                            maxX = Math.max(maxX, xx);
                            maxY = Math.max(maxY, yy);
                        }
                    }
                }
                if (maxX >= minX && maxY >= minY) {
                    cropped = sprite.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
                } else {
                    // If the sprite is fully transparent, use it as-is
                    cropped = sprite;
                }

                // Calculate where to draw so trimmed sprite aligns at the bottom-left of its cell with padding
                int row = i / COLS;
                int col = i % COLS;
                int dx = col * cellW + cellW - cropped.getWidth();
                int dy = row * cellH + cellH - cropped.getHeight();
                g.drawImage(cropped, dx, dy, null);
            }
        }


        g.dispose();

        File out = new File("src/main/resources/assets/image/enemy-copter-aligned.png");
        ImageIO.write(dst, "PNG", out);
        System.out.println("Wrote aligned sheet to: " + out.getPath());
    }
}

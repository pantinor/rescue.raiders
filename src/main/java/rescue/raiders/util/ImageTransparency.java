package rescue.raiders.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageTransparency {

    public static int MARKER_RED;
    public static int MARKER_GREEN;
    public static int MARKER_BLUE;

    public static void main(final String[] arguments) throws Exception {

        final String inputFileName = "D:\\work\\gdx-armor\\assets\\image\\rescue.png";
        final String outputFileName = "D:\\work\\gdx-armor\\assets\\image\\rescue.copy.png";

        try {
            convertAreaToTransparent(inputFileName, outputFileName, 424, 22, 534, 110, 190, 190, 190);
            convertAreaToTransparent(outputFileName, outputFileName, 46, 214, 540, 324, 160, 160, 160);
            convertAreaToTransparent(outputFileName, outputFileName, 46, 354, 540, 466, 160, 160, 160);
            convertAreaToTransparent(outputFileName, outputFileName, 46, 493, 540, 603, 160, 160, 160);
        } catch (Exception e) {
        }

    }

    public static void convert(String inputFileName, String outputFileName) {
        try {

            BufferedImage source = ImageIO.read(new File(inputFileName));
            int rgb = source.getRGB(0, 0);
            convert(inputFileName, outputFileName, rgb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convert(String inputFileName, String outputFileName, int rgb) {
        try {

            System.out.println("Copying file " + inputFileName + " to " + outputFileName);

            MARKER_RED = (rgb >> 16) & 0xFF;
            MARKER_GREEN = (rgb >> 8) & 0xFF;
            MARKER_BLUE = rgb & 0xFF;

            BufferedImage source = ImageIO.read(new File(inputFileName));
            BufferedImage imageWithTransparency = makeColorTransparent(source, new Color(rgb));
            //BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);
            ImageIO.write(imageWithTransparency, "PNG", new File(outputFileName));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void convert(BufferedImage source, String outputFileName, int rgb) {
        try {

            System.out.println("converting to " + outputFileName);

            MARKER_RED = (rgb >> 16) & 0xFF;
            MARKER_GREEN = (rgb >> 8) & 0xFF;
            MARKER_BLUE = rgb & 0xFF;

            BufferedImage imageWithTransparency = makeColorTransparent(source, new Color(rgb));
            ImageIO.write(imageWithTransparency, "PNG", new File(outputFileName));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void convertAreaToTransparent(String inputFileName, String outputFileName,
            final int startX, final int startY, final int endX, final int endY,
            final int minRed, final int minGreen, final int minBlue) {
        try {

            System.out.println("converting to " + outputFileName);
            BufferedImage source = ImageIO.read(new File(inputFileName));
            BufferedImage imageWithTransparency = makeColorTransparentInArea(source, startX, startY, endX, endY, minRed, minGreen, minBlue);
            ImageIO.write(imageWithTransparency, "PNG", new File(outputFileName));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static BufferedImage makeColorTransparent(BufferedImage im, final Color color) {

        final ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB();

            public final int filterRGB(int x, int y, int rgb) {

                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                if (red == MARKER_RED && green == MARKER_GREEN && blue == MARKER_BLUE) {
                    // Mark the alpha bits as zero - transparent
                    rgb = 0x00FFFFFF & rgb;
                }

                alpha = (rgb >> 24) & 0xff;
                red = (rgb >> 16) & 0xFF;
                green = (rgb >> 8) & 0xFF;
                blue = rgb & 0xFF;

                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        Image i = Toolkit.getDefaultToolkit().createImage(ip);

        sun.awt.image.ToolkitImage source = (sun.awt.image.ToolkitImage) i;
        source.preload(null);
        try {
            Thread.sleep(250);
        } catch (Exception e) {
        }
        return source.getBufferedImage();

    }

    public static BufferedImage makeColorTransparentInArea(BufferedImage im,
            final int startX, final int startY, final int endX, final int endY,
            final int minRed, final int minGreen, final int minBlue) {

        final ImageFilter filter = new RGBImageFilter() {

            public final int filterRGB(int x, int y, int rgb) {

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                boolean within = (x >= startX && x <= endX && y >= startY && y <= endY);

                if (within && (red >= minRed && green >= minGreen && blue >= minBlue)) {
                    rgb = 0x00FFFFFF & rgb;
                }

                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        Image i = Toolkit.getDefaultToolkit().createImage(ip);

        sun.awt.image.ToolkitImage source = (sun.awt.image.ToolkitImage) i;
        source.preload(null);
        try {
            Thread.sleep(250);
        } catch (Exception e) {
        }
        return source.getBufferedImage();

    }

    public static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;

    }

    public static BufferedImage createTransparentImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setComposite(composite);
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, width, height);
        return image;
    }

    public static BufferedImage cropTransparent(double[] alpha, BufferedImage t) {
        // Find the bounding box
        WritableRaster r = t.getRaster();
        int minx = -1;
        int miny = -1;
        int maxx = r.getWidth();
        int maxy = r.getHeight();
        double[] pv = new double[4];
        int x0 = 0;
        int x1 = r.getWidth();
        int y0 = 0;
        int y1 = r.getHeight();

        // min y
        boolean contentFound = false;
        for (int y = y0; y < y1 && !contentFound; y++) {
            for (int x = x0; x < x1; x++) {
                r.getPixel(x, y, pv);
                if (!Arrays.equals(pv, alpha)) {
                    contentFound = true;
                    miny = y;
                    break;
                }
            }
        }

        // max y
        contentFound = false;
        for (int y = y1 - 1; y > 0 && !contentFound; y--) {
            for (int x = x0; x < x1; x++) {
                r.getPixel(x, y, pv);
                if (!Arrays.equals(pv, alpha)) {
                    contentFound = true;
                    maxy = y + 1;
                    break;
                }
            }
        }

        // min x
        contentFound = false;
        for (int x = x0; x < x1 && !contentFound; x++) {
            for (int y = y0; y < y1; y++) {
                r.getPixel(x, y, pv);
                if (!Arrays.equals(pv, alpha)) {
                    contentFound = true;
                    minx = x;
                    break;
                }
            }
        }

        // max x
        contentFound = false;
        for (int x = x1 - 1; x > x0 && !contentFound; x--) {
            for (int y = y0; y < y1; y++) {
                r.getPixel(x, y, pv);
                if (!Arrays.equals(pv, alpha)) {
                    contentFound = true;
                    maxx = x + 1;
                    break;
                }
            }
        }

        minx = minx < x0 ? x0 : minx;
        miny = miny < y0 ? y0 : miny;
        maxx = maxx > x1 ? x1 : maxx;
        maxy = maxy > y1 ? y1 : maxy;

        int nw = maxx - minx;
        int nh = maxy - miny;

        return t.getSubimage(minx, miny, nw, nh);
    }

}

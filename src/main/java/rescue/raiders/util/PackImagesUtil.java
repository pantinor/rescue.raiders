package rescue.raiders.util;

import java.awt.image.BufferedImage;
import java.io.File;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import java.io.FilenameFilter;

import javax.imageio.ImageIO;

public class PackImagesUtil {

    public static File dir = new File("D:\\work\\gdx-armor\\assets\\image");

    public static void main(String[] argv) throws Exception {

        
        File[] pngs = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("png");
            }
        });
        
        Settings settings = new Settings();
        settings.maxWidth = 1024;
        settings.maxHeight = 2024;
        settings.paddingX = 3;
        settings.paddingY = 3;
        settings.fast = true;
        settings.pot = false;
        settings.grid = false;
        
        TexturePacker tp = new TexturePacker(settings);

        
        for (File f : pngs) {
        	BufferedImage image = ImageIO.read(f);
        	String name = f.getName().replace(".png", "");
        	tp.addImage(image, name);
        }
        
        tp.pack(new File("."), "rescue");


        System.out.println("done");
    }

}

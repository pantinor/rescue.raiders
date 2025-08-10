package rescue.raiders.objects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergeAtlasesIntoOne {

    public static void main(String[] args) throws Exception {

        List<File> pngs = List.of(
                new File("src/main/resources/assets/image/explosions.png"),
                new File("src/main/resources/assets/image/bomb-explosion.png"),
                new File("src/main/resources/assets/image/huge-explosion.png")
        );

        List<File> atlases = List.of(
                new File("src/main/resources/assets/image/explosions.atlas"),
                new File("src/main/resources/assets/image/bomb-explosion.atlas"),
                new File("src/main/resources/assets/image/huge-explosion.atlas")
        );

        File outputDir = new File("target/packed-merged");
        String packFileName = "merged";

        packAllIntoOne(pngs, atlases, outputDir, packFileName);
    }

    public static void packAllIntoOne(List<File> standalonePngs, List<File> atlasFiles, File outputDir, String packFileName) throws Exception {

        TexturePacker.Settings s = new TexturePacker.Settings();
        s.grid = true;             
        s.rotation = false;        
        s.useIndexes = true;          
        s.maxWidth = 8192;  
        s.maxHeight = 8192;

        TexturePacker packer = new TexturePacker(new File("."), s);

        for (File atlas : atlasFiles) {
            if (atlas.exists() && atlas.isFile()) {
                addAtlasToPacker(packer, atlas);
            }
        }

        outputDir.mkdirs();

        packer.pack(outputDir, packFileName);

        System.out.println("Wrote: " + new File(outputDir, packFileName + ".png"));
        System.out.println("Wrote: " + new File(outputDir, packFileName + ".atlas"));
    }

    private static void addAtlasToPacker(TexturePacker packer, File atlasFile) throws IOException {
        FileHandle pack = new FileHandle(atlasFile);
        FileHandle imagesDir = pack.parent();
        TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData(pack, imagesDir, false);

        BufferedImage page = null;
        for (TextureAtlas.TextureAtlasData.Page p : data.getPages()) {
            page = ImageIO.read(p.textureFile.file());
        }

        for (TextureAtlas.TextureAtlasData.Region r : data.getRegions()) {
            int cropW = r.rotate ? r.height : r.width;
            int cropH = r.rotate ? r.width : r.height;
            BufferedImage sub = page.getSubimage(r.left, r.top, cropW, cropH);

            String name = r.name + (r.index >= 0 ? "_" + r.index : "");
            packer.addImage(sub, name);
        }
    }

}

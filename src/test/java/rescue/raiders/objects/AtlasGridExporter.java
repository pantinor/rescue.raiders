package rescue.raiders.objects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class AtlasGridExporter extends ApplicationAdapter {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("AtlasGridExporter");
        cfg.setWindowedMode(320, 200);
        new Lwjgl3Application(new AtlasGridExporter(), cfg);
    }

    @Override
    public void create() {
        try {
            exportAtlasGrid();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            Gdx.app.exit();
        }
    }

    private void exportAtlasGrid() {

        String regionName = "bomb-explosion";

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/image/explosions.atlas"));

        try {
            Array<AtlasRegion> regions = atlas.findRegions(regionName);

            if (regions.size == 0) {
                throw new IllegalStateException("No regions in atlas");
            }

            int cellW = 0, cellH = 0;
            for (AtlasRegion r : regions) {
                cellW = Math.max(cellW, r.getRegionWidth());
                cellH = Math.max(cellH, r.getRegionHeight());
            }

            System.out.printf("CELL SIZE W: %d H: %d\n", cellW, cellH);

            int cols = Math.min(12, Math.max(1, regions.size));
            int rows = (int) Math.ceil(regions.size / (double) cols);

            int imgW = cols * cellW;
            int imgH = rows * cellH;

            Map<Texture, Pixmap> pagePixmaps = new HashMap<>();
            atlas.getTextures().forEach(texture -> {
                TextureData td = texture.getTextureData();
                if (!td.isPrepared()) {
                    td.prepare();
                }
                pagePixmaps.put(texture, td.consumePixmap());
            });

            Map<AtlasRegion, Pixmap> regionPixmaps = new HashMap<>();
            for (AtlasRegion r : regions) {
                Pixmap src = pagePixmaps.get(r.getTexture());
                if (src == null) {
                    continue;
                }

                int rW = r.getRegionWidth();
                int rH = r.getRegionHeight();

                int srcX = r.getRegionX();
                int srcY = r.getRegionY();

                Pixmap dst = new Pixmap(rW, rH, src.getFormat());
                dst.drawPixmap(src, srcX, srcY, rW, rH, 0, 0, rW, rH);

                regionPixmaps.put(r, dst);
            }

            Pixmap target = new Pixmap(imgW, imgH, Pixmap.Format.RGBA8888);
            try {
                target.setColor(0, 0, 0, 0);
                target.fill();

                for (int i = 0; i < regions.size; i++) {
                    AtlasRegion r = regions.get(i);
                    Pixmap rp = regionPixmaps.get(r);

                    int rW = rp.getWidth();
                    int rH = rp.getHeight();

                    int col = i % cols;
                    int row = i / cols;

                    int cellX = col * cellW;
                    int cellY = row * cellH;

                    int dstX = cellX + (cellW - rW) / 2;// center horizontally inside padded area
                    int dstY = cellY + (cellH - rH);// bottom-align inside padded area

                    target.drawPixmap(rp, 0, 0, rW, rH, dstX, dstY, rW, rH);

                    System.out.println(regionName);
                    System.out.println("  xy: " + cellX + "," + cellY);
                    System.out.println("  size: " + cellW + "," + cellH);
                    System.out.println("  index: " + i);

                }

                PixmapIO.writePNG(new FileHandle("grid.png"), target);
            } finally {
                target.dispose();

                for (Pixmap rp : regionPixmaps.values()) {
                    if (rp != null) {
                        rp.dispose();
                    }
                }

                for (Pixmap p : pagePixmaps.values()) {
                    if (p != null) {
                        p.dispose();
                    }
                }
            }

        } finally {
            atlas.dispose();
        }
    }
}

package rescue.raiders.objects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class RowAtlasPacker {

    private static final int PAD_X = 0;       // space between frames in a row
    private static final int ROW_GAP = 10;     // space between rows
    private static final int MARGIN_X = 0;    // left/right margin
    private static final int MARGIN_Y = 0;    // top/bottom margin
    private static final int MAX_W = 8192;    // clamp final PNG width
    private static final int MAX_H = 8192;    // clamp final PNG height

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

        packRows(pngs, atlases, outputDir, packFileName);
    }

    public static void packRows(List<File> loosePngs, List<File> atlasFiles, File outDir, String outName) throws Exception {
        Map<String, List<Frame>> groups = new TreeMap<>();

        for (File a : atlasFiles) {
            addAtlas(groups, a);
        }

        for (List<Frame> row : groups.values()) {
            row.sort(Comparator
                    .comparingInt((Frame fr) -> fr.index < 0 ? Integer.MAX_VALUE : fr.index)
                    .thenComparing(fr -> fr.name)
            );
        }

        Layout layout = computeLayout(groups);
        if (layout.width > MAX_W || layout.height > MAX_H) {
            System.err.printf("WARNING: output %dx%d exceeds clamp (%dx%d).%n", layout.width, layout.height, MAX_W, MAX_H);
        }
        int W = Math.min(layout.width, MAX_W);
        int H = Math.min(layout.height, MAX_H);

        RenderResult render = render(groups, W, H, layout.rowRects);

        outDir.mkdirs();
        File pngFile = new File(outDir, outName + ".png");
        ImageIO.write(render.image, "png", pngFile);

        File atlasFile = new File(outDir, outName + ".atlas");
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(atlasFile), "UTF-8"))) {
            for (AtlasEntry e : render.entries) {
                pw.println(e.regionName);
                pw.println("  xy: " + e.x + ", " + e.y);
                pw.println("  size: " + e.w + ", " + e.h);
                pw.println("  index: " + e.index);
            }
        }

        System.out.println("Wrote: " + pngFile.getAbsolutePath());
        System.out.println("Wrote: " + atlasFile.getAbsolutePath());
    }

    private static void addAtlas(Map<String, List<Frame>> groups, File atlasFile) throws IOException {
        FileHandle pack = new FileHandle(atlasFile);
        TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData(pack, pack.parent(), false);

        BufferedImage page = null;
        for (TextureAtlas.TextureAtlasData.Page p : data.getPages()) {
            page = ImageIO.read(p.textureFile.file());
        }

        for (TextureAtlas.TextureAtlasData.Region r : data.getRegions()) {
            int cropW = r.rotate ? r.height : r.width;
            int cropH = r.rotate ? r.width : r.height;
            BufferedImage sub = page.getSubimage(r.left, r.top, cropW, cropH);
            add(groups, r.name, new Frame(r.name, r.index, sub));
        }
    }

    private static void add(Map<String, List<Frame>> groups, String name, Frame frame) {
        groups.computeIfAbsent(name, k -> new ArrayList<>()).add(frame);
    }

    private record RowRect(int y, int height, int width) {

    }

    private record Layout(int width, int height, Map<String, RowRect> rowRects) {

    }

    private static Layout computeLayout(Map<String, List<Frame>> groups) {
        int y = MARGIN_Y;
        int totalW = 0;
        Map<String, RowRect> rows = new HashMap<>();

        for (Map.Entry<String, List<Frame>> e : groups.entrySet()) {
            List<Frame> row = e.getValue();
            int rowH = 0;
            int rowW = MARGIN_X; // start with left margin
            boolean first = true;
            for (Frame fr : row) {
                rowH = Math.max(rowH, fr.h);
                if (!first) {
                    rowW += PAD_X;
                }
                rowW += fr.w;
                first = false;
            }
            rowW += MARGIN_X; // right margin
            rows.put(e.getKey(), new RowRect(y, rowH, rowW));
            totalW = Math.max(totalW, rowW);
            y += rowH + ROW_GAP;
        }

        int totalH = (y - ROW_GAP) + MARGIN_Y; // add bottom margin, remove last gap
        totalW = Math.max(totalW, MARGIN_X * 2); // at least margins

        return new Layout(totalW, totalH, rows);
    }

    private static RenderResult render(Map<String, List<Frame>> groups, int W, int H, Map<String, RowRect> rowRects) {
        BufferedImage out = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, W, H);

        List<AtlasEntry> entries = new ArrayList<>();

        for (Map.Entry<String, List<Frame>> e : groups.entrySet()) {
            String name = e.getKey();
            List<Frame> row = e.getValue();
            RowRect rr = rowRects.get(name);

            int x = MARGIN_X;
            int y = rr.y;

            for (Frame fr : row) {
                // draw at top-left of row; y grows downward (top-left coordinate system)
                g.drawImage(fr.img, x, y, null);

                entries.add(new AtlasEntry(name, x, y, fr.w, fr.h, fr.index));

                x += fr.w + PAD_X;
            }
        }

        g.dispose();
        return new RenderResult(out, entries);
    }

    private record RenderResult(BufferedImage image, List<AtlasEntry> entries) {

    }

    private record AtlasEntry(String regionName, int x, int y, int w, int h, int index) {

    }

    private static class Frame {

        final String name;
        final int index; // -1 if none
        final BufferedImage img;
        final int w, h;

        Frame(String name, int index, BufferedImage img) {
            this.name = name;
            this.index = index;
            this.img = img;
            this.w = img.getWidth();
            this.h = img.getHeight();
        }
    }
}

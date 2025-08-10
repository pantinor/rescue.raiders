package rescue.raiders.objects;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JsonToAtlas {

    private static final String IND = "  "; // 2 spaces

    static class Region {

        Integer idx;
        int x, y, w, h;
    }

    public static void main(String[] args) throws Exception {

        Path inPath = Path.of("src/main/resources/assets/image/temp.json");
        String outPath = "temp.atlas";
        String basenameOverride = null;

        String jsonText = Files.readString(inPath);
        JsonValue root = new JsonReader().parse(jsonText);

        String image = root.getString("image", "atlas.png");
        String baseName = (basenameOverride != null && !basenameOverride.isBlank())
                ? basenameOverride
                : stripExtension(image);

        // Parse regions
        List<Region> regions = new ArrayList<>();
        JsonValue regionsNode = root.get("regions");
        if (regionsNode != null) {
            for (JsonValue r : regionsNode) {
                JsonValue rect = r.get("rect");
                if (rect == null || rect.size < 4) {
                    continue;
                }

                Region reg = new Region();
                reg.x = rect.getInt(0);
                reg.y = rect.getInt(1);
                reg.w = rect.getInt(2);
                reg.h = rect.getInt(3);
                reg.idx = r.has("idx") ? r.getInt("idx") : null;

                regions.add(reg);
            }
        }

        // Sort by idx if present
        regions.sort(Comparator.comparing((Region r) -> r.idx, Comparator.nullsLast(Integer::compareTo)));

        // Output
        StringBuilder sb = new StringBuilder();
        sb.append("format: RGBA8888\n");
        sb.append("filter: Nearest,Nearest\n");
        sb.append("repeat: none\n");

        for (Region r : regions) {
            sb.append(baseName).append("\n");
            sb.append(IND).append("xy: ").append(r.x).append(",").append(r.y).append("\n");
            sb.append(IND).append("size: ").append(r.w).append(",").append(r.h).append("\n");
            sb.append(IND).append("index: ").append(r.idx != null ? r.idx : 0).append("\n");
        }

        System.out.print(sb.toString());
    }

    private static String stripExtension(String filename) {
        int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
        String justName = (slash >= 0) ? filename.substring(slash + 1) : filename;
        int dot = justName.lastIndexOf('.');
        return (dot >= 0) ? justName.substring(0, dot) : justName;
    }
}

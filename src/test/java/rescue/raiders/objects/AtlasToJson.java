package rescue.raiders.objects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import static rescue.raiders.objects.ZoomableAtlasViewer.SCREEN_HEIGHT;
import static rescue.raiders.objects.ZoomableAtlasViewer.SCREEN_WIDTH;

public class AtlasToJson extends ApplicationAdapter {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("AtlasViewer");
        cfg.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        new Lwjgl3Application(new AtlasToJson(), cfg);
    }

    @Override
    public void create() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/image/soldier.atlas"));
        String json = convert(atlas, "soldier.png");

        Gdx.files.local("soldier.json").writeString(json, false);

        System.out.println("JSON saved to soldier.json");
        Gdx.app.exit();
    }

    public static String convert(TextureAtlas atlas, String imageName) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"fill_rate\": 0,\n");
        sb.append("  \"grid_height\": 0,\n");
        sb.append("  \"grid_width\": 0,\n");
        sb.append("  \"height\": 0,\n");
        sb.append("  \"image\": \"").append(imageName).append("\",\n");
        sb.append("  \"name\": \"tmp\",\n");
        sb.append("  \"padding_x\": 0,\n");
        sb.append("  \"padding_y\": 0,\n");
        sb.append("  \"pot\": false,\n");
        sb.append("  \"regions\": [\n");

        Array<AtlasRegion> regions = atlas.getRegions();
        for (int i = 0; i < regions.size; i++) {
            AtlasRegion r = regions.get(i);
            sb.append("    {\n");
            sb.append("      \"idx\": ").append(r.index).append(",\n");
            sb.append("      \"name\": \"").append(r.name).append("\",\n");
            sb.append("      \"origin\": [0, 0],\n");
            sb.append("      \"rect\": [")
                    .append(r.getRegionX()).append(", ")
                    .append(r.getRegionY()).append(", ")
                    .append(r.getRegionWidth()).append(", ")
                    .append(r.getRegionHeight()).append("],\n");
            sb.append("      \"rotated\": ").append(r.rotate).append("\n");
            sb.append("    }");
            if (i < regions.size - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("  ]\n");
        sb.append("}\n");
        return sb.toString();
    }

}

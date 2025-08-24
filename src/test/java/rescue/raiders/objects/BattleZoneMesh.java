package rescue.raiders.objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class BattleZoneMesh {

    private static final String VIS_GEN_BATTLEZONE_MESH = "battlezone-mesh";
    private static final String VIS_GEN_BATTLEZONE_MAP = "battlezone-map";

    private static final String P_VTAB_OFFSET = "vtabOffset";
    private static final String P_CTAB_OFFSET = "ctabOffset";
    private static final String P_TABLE_INDEX = "tableIndex";
    private static final String P_MAP_T_F_OFFSET = "mapTFOffset";
    private static final String P_MAP_X_OFFSET = "mapXOffset";
    private static final String P_MAP_Z_OFFSET = "mapZOffset";

    private final byte[] fileData;
    private final AddressMapTranslate addrTrans;
    private final ByteBuffer bb; // little-endian view

    private static final String MESH_vis00388e = "W1 V 6 512 -640 -512 -512 -640 -512 -512 -640 512 512 -640 512 0 640 0 0 0 0 E 8 0 4 4 1 1 0 0 3 3 4 4 2 2 3 2 1 P 0";
    private static final String MESH_vis003890 = "W1 V 9 512 -640 -512 -512 -640 -512 -512 -640 512 512 -640 512 512 640 -512 -512 640 -512 -512 640 512 512 640 512 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 5 1 2 6 7 3 P 0";
    private static final String MESH_vis003892 = "W1 V 25 512 -640 -736 -512 -640 -736 -512 -640 968 512 -640 968 568 -416 -1024 -568 -416 -1024 -568 -416 1248 568 -416 1248 344 -240 -680 -344 -240 -680 -344 -240 680 344 -240 680 168 96 -512 -168 96 -512 40 -16 -128 -40 -16 -128 -40 -96 128 40 -96 128 -40 -16 1120 -40 -96 1120 40 -16 1120 40 -96 1120 0 96 -512 0 160 -512 0 0 0 E 38 23 22 12 13 14 20 20 18 18 15 15 14 14 17 17 16 16 19 19 21 21 17 15 16 19 18 20 21 3 0 0 4 4 7 7 6 6 2 2 3 3 7 7 11 11 10 10 6 6 5 5 9 9 10 10 13 13 9 9 8 8 11 11 12 12 8 8 4 4 5 5 1 1 2 1 0 P 0";
    private static final String MESH_vis003894 = "W1 V 6 40 -96 -40 40 -16 -40 -40 -16 -40 -40 -96 -40 0 -56 80 0 0 0 E 8 0 4 4 1 1 0 0 3 3 4 4 2 2 3 2 1 P 0";
    private static final String MESH_btread0 = "W1 V 7 552 -472 -948 -552 -472 -948 536 -552 -844 -536 -552 -844 516 -632 -736 -516 -632 -736 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_btread1 = "W1 V 7 556 -456 -972 -556 -456 -972 540 -536 -868 -540 -536 -868 520 -616 -764 -520 -616 -764 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_btread2 = "W1 V 7 564 -432 -1000 -564 -432 -1000 544 -512 -896 -544 -512 -896 528 -592 -792 -528 -592 -792 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_btread3 = "W1 V 7 568 -416 -1024 -568 -416 -1024 548 -496 -920 -548 -496 -920 532 -576 -816 -532 -576 -816 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_ftread0 = "W1 V 7 568 -416 1248 -568 -416 1248 548 -496 1152 -548 -496 1152 532 -576 1056 -532 -576 1056 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_ftread1 = "W1 V 7 564 -432 1224 -564 -432 1224 544 -512 1128 -544 -512 1128 528 -592 1032 -528 -592 1032 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_ftread2 = "W1 V 7 556 -456 1200 -556 -456 1200 540 -536 1104 -540 -536 1104 520 -616 1008 -520 -616 1008 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_ftread3 = "W1 V 7 552 -472 1176 -552 -472 1176 536 -552 1080 -536 -552 1080 516 -632 984 -516 -632 984 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_vis0038a6 = "W1 V 6 800 -640 -800 -800 -640 -800 -800 -640 800 800 -640 800 0 800 0 0 0 0 E 8 0 4 4 1 1 0 0 3 3 4 4 2 2 3 2 1 P 0";
    private static final String MESH_vis0038a8 = "W1 V 9 80 160 0 160 200 80 160 240 80 80 280 0 -80 160 0 -160 200 80 -160 240 80 -80 280 0 0 0 0 E 10 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 7 3 P 0";
    private static final String MESH_vis0038aa = "W1 V 2 0 0 0 0 0 0 E 0 P 0";
    private static final String MESH_vis0038ac = "W1 V 9 640 -640 -640 -640 -640 -640 -640 -640 640 640 -640 640 640 -80 -640 -640 -80 -640 -640 -80 640 640 -80 640 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 5 1 2 6 7 3 P 0";
    private static final String MESH_vis0038ae = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_vis0038b0 = "W1 V 9 120 -640 -240 -64 -560 -376 -160 -768 720 120 -640 640 64 -160 -40 -32 -120 0 160 -400 56 -200 -480 120 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 6 6 7 7 5 5 4 5 1 7 2 3 6 P 0";
    private static final String MESH_vis0038b2 = "W1 V 15 344 -296 -588 -344 -296 -588 -344 -976 588 344 -976 588 168 -96 -272 -168 -96 -272 40 -376 0 -40 -376 0 -40 -576 180 40 -576 180 -40 -1000 1080 -40 -1072 1040 40 -1000 1080 40 -1072 1040 0 0 0 E 21 0 1 1 2 2 3 3 0 0 4 4 5 5 1 5 2 3 4 6 12 12 10 10 7 7 6 6 9 9 8 8 11 11 13 13 9 7 8 11 10 12 13 P 0";
    private static final String MESH_vis0038b4 = "W1 V 9 80 160 0 160 200 80 160 240 80 80 280 0 -80 160 0 -160 200 80 -160 240 80 -80 280 0 0 0 0 E 10 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 7 3 P 0";
    private static final String MESH_vis0038b6 = "W1 V 9 120 -640 -240 -64 -560 -376 -160 -768 720 120 -640 640 64 -160 -40 -32 -120 0 160 -400 56 -200 -480 120 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 6 6 7 7 5 5 4 5 1 7 2 3 6 P 0";
    private static final String MESH_vis0038b8 = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_vis0038ba = "W1 V 27 -144 0 -384 -72 96 -384 72 96 -384 144 0 -384 72 -96 -384 -72 -96 -384 -288 0 -96 -192 192 -96 192 192 -96 288 0 -96 192 -192 -96 -192 -192 -96 0 0 1152 0 0 1392 144 -336 -144 -144 -336 -144 -144 -336 144 144 -336 144 48 -184 -48 -48 -184 -48 -48 -168 48 48 -168 48 0 192 -96 -72 96 528 72 96 528 0 288 48 0 0 0 E 43 13 12 12 6 6 0 0 1 1 7 7 8 8 9 9 10 10 11 11 6 6 7 7 12 12 8 8 2 2 3 3 9 9 12 12 10 10 4 4 5 5 11 11 12 24 23 23 22 22 24 24 25 25 23 25 22 1 2 3 4 5 0 18 19 19 20 20 21 21 18 18 14 14 15 15 16 16 17 17 14 15 19 20 16 17 21 P 0";
    private static final String MESH_vis0038bc = "W1 V 21 -5120 64 224 -3840 64 224 -3200 176 672 -3520 288 1120 -3200 400 1600 -3840 512 2048 -5120 512 2048 -4480 176 672 -4160 176 672 -4480 288 1120 -4160 400 1600 -4480 400 1600 -3200 64 224 -2240 176 32 -1280 64 224 -2240 512 2048 -2560 224 896 -2240 256 1024 -1920 224 896 -2240 336 1344 0 0 0 E 20 0 1 1 2 2 3 3 4 4 5 5 6 6 0 7 8 8 9 9 10 10 11 11 7 12 13 13 14 14 15 15 12 16 17 17 18 18 19 19 16 P 0";
    private static final String MESH_vis0038be = "W1 V 9 120 -640 -240 -64 -560 -376 -160 -768 720 120 -640 640 64 -160 -40 -32 -120 0 160 -400 56 -200 -480 120 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 6 6 7 7 5 5 4 5 1 7 2 3 6 P 0";
    private static final String MESH_vis0038c0 = "W1 V 9 72 -368 -300 168 -368 -232 272 -472 -232 272 -568 -300 -168 -408 -96 -12 -384 40 260 -648 40 232 -808 -96 0 0 0 E 10 1 2 2 3 3 7 7 6 6 5 5 4 4 0 0 1 1 5 6 2 P 0";
    private static final String MESH_vis0038c2 = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_vis0038c4 = "W1 V 5 12 -576 -80 -112 -864 472 44 24 800 16 -536 88 0 0 0 E 6 0 2 2 1 1 3 3 0 0 1 2 3 P 0";
    private static final String MESH_vis0038c6 = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_vis0038c8 = "W1 V 9 72 -368 -300 168 -368 -232 272 -472 -232 272 -568 -300 -168 -408 -96 -12 -384 40 260 -648 40 232 -808 -96 0 0 0 E 10 1 2 2 3 3 7 7 6 6 5 5 4 4 0 0 1 1 5 6 2 P 0";
    private static final String MESH_vis0038ca = "W1 V 22 -640 64 224 -320 400 1600 640 400 1600 960 64 224 1280 400 1600 2240 400 1600 2240 64 224 3840 64 224 5440 112 448 4480 176 672 4480 224 896 5120 288 1120 4480 336 1344 4480 400 1600 5440 448 1824 3840 512 2048 2880 176 672 2880 512 2048 -1920 512 2048 -1920 400 1600 -960 400 1600 0 0 0 E 22 0 1 1 2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9 10 10 11 11 12 12 13 13 14 14 15 15 7 7 16 16 17 17 18 18 19 19 20 20 0 P 0";
    private static final String MESH_vis0038cc = "W1 V 26 -4800 -512 -2048 -2240 -512 -2048 -3520 -400 -1600 -2240 -64 -224 -4800 -64 -224 -3520 -176 -672 -320 -512 -2048 -320 -64 -224 -1600 -400 -1600 -960 -400 -1600 -960 -176 -672 -1600 -176 -672 0 -512 -2048 640 -288 -1120 2560 -512 -2048 4160 -448 -1824 3200 -400 -1600 3200 -336 -1344 3840 -288 -1120 3200 -224 -896 3200 -176 -672 4160 -112 -448 2560 -64 -224 1920 -288 -1120 0 -64 -224 0 0 0 E 28 1 0 0 5 5 4 4 3 3 2 2 1 1 3 3 7 7 6 6 1 9 8 8 11 11 10 10 9 14 22 22 23 23 24 24 12 12 13 13 14 14 15 15 16 16 17 17 18 18 19 19 20 20 21 21 22 P 0";
    private static final String MESH_vis0038ce = "W1 V 18 0 -80 -240 -160 -80 -160 -240 -80 0 -160 -80 160 0 -80 240 160 -80 160 240 -80 0 160 -80 -160 0 160 -960 -680 160 -680 -960 160 0 -680 160 680 0 160 960 680 160 680 960 160 0 680 160 -680 0 560 0 0 0 0 E 32 16 8 8 9 9 16 16 10 10 11 11 16 16 12 12 13 13 16 16 14 14 15 15 16 0 7 7 15 15 8 8 0 0 1 1 9 9 10 10 2 2 3 3 11 11 12 12 4 4 5 5 13 13 14 14 6 6 7 6 5 4 3 2 1 P 0";
    private static final String MESH_vis0038d0 = "W1 V 26 -368 -640 1456 -552 -640 -456 552 -640 -456 368 -640 1456 -456 -184 -456 456 -184 -456 0 -552 1096 -272 -232 -272 -272 -184 -456 272 -184 -456 272 -232 -272 -184 88 -272 -184 88 -456 184 88 -456 184 88 -272 -88 -88 1280 -88 -88 88 88 -88 88 88 -88 1280 -88 0 1280 -88 0 -88 88 0 -88 88 0 1280 0 88 -456 0 552 -456 0 0 0 E 34 0 1 1 4 4 0 0 3 3 2 2 5 5 3 2 1 4 5 9 10 10 6 6 14 14 13 13 9 9 8 8 7 7 6 6 11 11 12 12 8 12 13 14 11 19 22 22 21 21 20 20 16 16 15 15 18 18 17 17 16 15 19 22 18 17 21 23 24 P 0";
    private static final String MESH_spatter0 = "W1 V 9 -52 -360 0 -36 -360 36 0 -360 52 36 -360 36 52 -360 0 36 -360 -36 0 -360 -52 -36 -360 -36 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_spatter1 = "W1 V 9 -100 -400 0 -72 -400 72 0 -400 100 72 -400 72 100 -400 0 72 -400 -72 0 -400 -100 -72 -400 -72 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_spatter2 = "W1 V 9 -152 -440 0 -108 -440 108 0 -440 152 108 -440 108 152 -440 0 108 -440 -108 0 -440 -152 -108 -440 -108 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_spatter3 = "W1 V 9 -200 -480 0 -144 -480 144 0 -480 200 144 -480 144 200 -480 0 144 -480 -144 0 -480 -200 -144 -480 -144 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_spatter4 = "W1 V 9 -252 -520 0 -176 -520 176 0 -520 252 176 -520 176 252 -520 0 176 -520 -176 0 -520 -252 -176 -520 -176 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_spatter5 = "W1 V 9 -300 -560 0 -212 -560 212 0 -560 300 212 -560 212 300 -560 0 212 -560 -212 0 -560 -300 -212 -560 -212 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_spatter6 = "W1 V 9 -352 -600 0 -264 -600 264 0 -600 352 264 -600 264 352 -600 0 264 -600 -264 0 -600 -352 -264 -600 -264 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_spatter7 = "W1 V 9 -400 -640 0 -284 -640 284 0 -640 400 284 -640 284 400 -640 0 284 -640 -284 0 -640 -400 -284 -640 -284 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";

    public static void main(String[] args) throws Exception {
        String dis65 = "C:\\Users\\panti\\Downloads\\va-battlezone\\Battlezone.dis65";
        String rom = "C:\\Users\\panti\\Downloads\\va-battlezone\\Battlezone";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(dis65));

        int fileLen = root.get("FileDataLength").asInt();
        System.out.println("FileDataLength=" + fileLen);
        System.out.println("FileDataCrc32=" + root.get("FileDataCrc32").asInt());
        System.out.println("CpuName=" + root.get("ProjectProps").get("CpuName").asText());

        byte[] romBytes = Files.readAllBytes(Paths.get(rom));
        AddressMapTranslate trans = AddressMapTranslate.fromDis65(root, fileLen);
        BattleZoneMesh mesh = new BattleZoneMesh(romBytes, trans);

        if (root.has("Visualizations")) {
            JsonNode visArray = root.get("Visualizations");

            // Meshes
            for (JsonNode vis : visArray) {
                if (!VIS_GEN_BATTLEZONE_MESH.equals(vis.get("VisGenIdent").asText(""))) {
                    continue;
                }
                JsonNode p = vis.get("VisGenParams");
                int vtab = p.get(P_VTAB_OFFSET).asInt();
                int ctab = p.get(P_CTAB_OFFSET).asInt();
                int idx = p.get(P_TABLE_INDEX).asInt();

                VisWireframe wf = mesh.generateWireframe(vtab, ctab, idx);
                String tag = vis.has("Tag") ? vis.get("Tag").asText() : "(untagged)";
                if (wf != null) {
                    //System.out.printf("Mesh %-16s idx=%d -> vertices=%d edges=%d points=%d%n",
                    //        tag, idx, wf.getVertices().size(), wf.getEdges().size(), wf.getPoints().size());

                    System.out.printf("private static final String MESH_%-16s=\"%s\";\n", tag, wireframeToString(wf));

                } else {
                    System.out.printf("Mesh %-16s idx=%d -> FAILED%n", tag, idx);
                }
            }

            // First map
            for (JsonNode vis : visArray) {
                if (!VIS_GEN_BATTLEZONE_MAP.equals(vis.get("VisGenIdent").asText(""))) {
                    continue;
                }
                JsonNode p = vis.get("VisGenParams");
                VisBitmap8 bmp = mesh.generateMap(
                        p.get(P_MAP_T_F_OFFSET).asInt(),
                        p.get(P_MAP_X_OFFSET).asInt(),
                        p.get(P_MAP_Z_OFFSET).asInt()
                );
                if (bmp != null) {
                    System.out.println("Map OK: " + bmp.getWidth() + "x" + bmp.getHeight()
                            + " palette=" + bmp.getPalette().size());
                } else {
                    System.out.println("Map generation failed");
                }
                break;
            }
        }
    }

    public BattleZoneMesh(byte[] fileData, AddressMapTranslate addrTrans) {
        this.fileData = fileData;
        this.addrTrans = addrTrans;
        this.bb = ByteBuffer.wrap(fileData).order(ByteOrder.LITTLE_ENDIAN);
    }

    private VisWireframe generateWireframe(int vtabOffset, int ctabOffset, int index) {
        if (!inRange16(vtabOffset) || !inRange16(ctabOffset)) {
            System.err.println("Invalid parameter");
            return null;
        }
        // Bounds for pointer fetch
        int ptrOffV = vtabOffset + index * 2;
        int ptrOffC = ctabOffset + index * 2;
        if (!inRange16(ptrOffV) || !inRange16(ptrOffC)) {
            System.err.println("Pointer out of range");
            return null;
        }

        try {
            int vertexAddr = Short.toUnsignedInt(bb.getShort(ptrOffV));
            int cmdAddr = Short.toUnsignedInt(bb.getShort(ptrOffC));
            if (vertexAddr == 0 || cmdAddr == 0) {
                System.err.println("No shape at this index");
                return null;
            }

            int vertexOffset = addrTrans.addressToOffset(vtabOffset, vertexAddr);
            int cmdOffset = addrTrans.addressToOffset(ctabOffset, cmdAddr);
            if (vertexOffset < 0) {
                System.err.println("Invalid vertex address $" + toHex4(vertexAddr));
                return null;
            }
            if (cmdOffset < 0) {
                System.err.println("Invalid cmd address $" + toHex4(cmdAddr));
                return null;
            }

            int vertexSize = Byte.toUnsignedInt(bb.get(vertexOffset++)) - 1;
            if (vertexSize < 0 || (vertexOffset + vertexSize) > fileData.length) {
                System.err.println("Bad vertex block size");
                return null;
            }

            VisWireframe vw = new VisWireframe();

            // decode vertices (X,Y,Z 16-bit LE) → (xc=-Y, yc=Z*2, zc=X)
            for (int i = 0; i < vertexSize; i += 6) {
                short xROM = bb.getShort(vertexOffset + i);
                short yROM = bb.getShort(vertexOffset + i + 2);
                short zROM = bb.getShort(vertexOffset + i + 4);

                int zc = xROM;
                int xc = -yROM;
                int yc = zROM * 2;

                vw.addVertex(xc, yc, zc);
            }

            int centerVertex = vw.addVertex(0, 0, 0);
            int numVertices = centerVertex; // exclude synthetic center
            int curVertex = centerVertex;

            // command stream
            while (cmdOffset < fileData.length) {
                int val = Byte.toUnsignedInt(bb.get(cmdOffset));
                if (val == 0xff) {
                    break;
                }
                int vindex = val >> 3;
                int cmd = val & 0x07;

                if (usesVertex(cmd) && vindex >= numVertices) {
                    System.err.printf("Invalid vertex ref (vindex=%d max=%d cmd=%d at +%s)%n",
                            vindex, numVertices, cmd, toHex6(cmdOffset));
                    return null;
                }

                switch (cmd) {
                    case 0:
                        curVertex = vindex;
                        vw.addPoint(vindex);
                        break; // point
                    case 1:
                        break;                                          // intensity (ignored)
                    case 2:
                        curVertex = vindex;
                        break;                       // move
                    case 3:
                        curVertex = vindex;
                        break;                       // center then move
                    case 4:
                        vw.addEdge(curVertex, vindex);
                        curVertex = vindex;
                        break; // line
                    case 5:
                        break; // scaled pattern ($347A) — not rendered
                    case 6:
                        break; // no-op
                    case 7:
                        System.err.println("Found invalid draw cmd 7");
                        return null;
                }
                cmdOffset++;
            }

            if (!vw.validate(null)) {
                System.err.println("Data error: invalid edges/points");
                return null;
            }
            return vw;

        } catch (IndexOutOfBoundsException ex) {
            System.err.println("Ran off end of file");
            return null;
        }
    }

    private static boolean usesVertex(int cmd) {
        return cmd == 0 || cmd == 2 || cmd == 3 || cmd == 4;
    }

    private VisBitmap8 generateMap(int tfOffset, int xOffset, int zOffset) {
        if (!inRange(tfOffset) || !inRange(xOffset) || !inRange(zOffset)) {
            System.err.println("Invalid parameter");
            return null;
        }

        VisBitmap8 vb = new VisBitmap8(256, 256);
        // palette
        vb.addColor(0xff, 0x00, 0x00, 0x00); // 0
        vb.addColor(0xff, 0xc0, 0x40, 0xc0); // 1 narrow pyramid
        vb.addColor(0xff, 0x40, 0xe0, 0x40); // 2 tall box
        vb.addColor(0xff, 0x40, 0x40, 0xe0); // 3 wide pyramid
        vb.addColor(0xff, 0xe0, 0x40, 0x40); // 4 short box
        vb.addColor(0xff, 0xff, 0xff, 0xff); // 5 white

        final int xshift = -10, zshift = -6;

        // crosshair
        byte color = 5;
        for (int i = 0; i <= 2; i++) {
            vb.setPixelIndex(129 + xshift, 128 + i + zshift, color);
            vb.setPixelIndex(128 + xshift + i, 129 + zshift, color);
        }

        for (int index = 0; index < 255; index++) {
            int type = Byte.toUnsignedInt(fileData[tfOffset + index * 2]);
            if (type == 0xff) {
                break;
            }

            int rawx = Byte.toUnsignedInt(fileData[xOffset + index * 2 + 1]);
            int rawz = Byte.toUnsignedInt(fileData[zOffset + index * 2 + 1]);

            int xpos = 128 - s8(rawz) + xshift;
            int zpos = 128 - s8(rawx) + zshift;

            switch (type) {
                case 0x00: // narrow pyramid
                    color = 1;
                    vb.setPixelIndex(xpos + 2, zpos, color);
                    for (int i = 1; i < 5; i++) {
                        vb.setPixelIndex(xpos + 2 - i / 2, zpos + i, color);
                        vb.setPixelIndex(xpos + 2 + i / 2, zpos + i, color);
                    }
                    break;
                case 0x01: // tall box
                    color = 2;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (i != 1 || j == 0 || j == 4) {
                                vb.setPixelIndex(xpos + i, zpos + j, color);
                            }
                        }
                    }
                    break;
                case 0x0c: // wide pyramid
                    color = 3;
                    for (int i = 0; i < 5; i++) {
                        vb.setPixelIndex(xpos + 4 - i, zpos + i, color);
                        vb.setPixelIndex(xpos + 4 + i, zpos + i, color);
                    }
                    break;
                case 0x0f: // short box
                    color = 4;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 1; j < 4; j++) {
                            vb.setPixelIndex(xpos + i, zpos + j, color);
                        }
                    }
                    break;
                default:
                    System.err.println("Found invalid object type $" + toHex2(type));
                    return null;
            }
        }
        return vb;
    }

    private boolean inRange(int off) {
        return off >= 0 && off < fileData.length;
    }

    private boolean inRange16(int off) {
        return off >= 0 && off + 1 < fileData.length;
    }

    private static int s8(int b) {
        return (b & 0x80) != 0 ? b - 256 : b;
    }

    private static String toHex2(int v) {
        return String.format("%02x", v & 0xff);
    }

    private static String toHex4(int v) {
        return String.format("%04x", v & 0xffff);
    }

    private static String toHex6(int v) {
        return String.format("%06x", v & 0xffffff);
    }

    private static final class AddressMapTranslate {

        static final class Seg {

            final int cpuStart, fileStart, length;

            Seg(int cpuStart, int fileStart, int length) {
                this.cpuStart = cpuStart;
                this.fileStart = fileStart;
                this.length = length;
            }

            boolean contains(int addr) {
                return addr >= cpuStart && addr < cpuStart + length;
            }

            int toFileOff(int addr) {
                return fileStart + (addr - cpuStart);
            }
        }
        private final List<Seg> segs;

        public AddressMapTranslate(List<Seg> segs) {
            this.segs = segs;
        }

        public int addressToOffset(int tableBaseOffset, int address) {
            for (Seg s : segs) {
                if (s.contains(address)) {
                    return s.toFileOff(address);
                }
            }
            return -1;
        }

        public static AddressMapTranslate fromDis65(JsonNode root, int fileLen) {
            JsonNode am = root.get("AddressMap");
            List<Seg> segs = new ArrayList<>(am.size());
            for (int i = 0; i < am.size(); i++) {
                int fileStart = am.get(i).get("Offset").asInt();
                int cpuStart = am.get(i).get("Addr").asInt();
                int length = am.get(i).get("Length").asInt();
                segs.add(new Seg(cpuStart, fileStart, length));
            }
            for (int i = 0; i < segs.size(); i++) {
                Seg s = segs.get(i);
                if (s.length < 0) {
                    int nextFileStart = (i + 1 < segs.size()) ? segs.get(i + 1).fileStart : fileLen;
                    segs.set(i, new Seg(s.cpuStart, s.fileStart, nextFileStart - s.fileStart));
                }
            }
            return new AddressMapTranslate(segs);
        }
    }

    private static final class VisBitmap8 {

        private final int width, height;
        private final byte[] pixels;
        private final List<int[]> palette = new ArrayList<>();

        public VisBitmap8(int width, int height) {
            this.width = width;
            this.height = height;
            this.pixels = new byte[width * height];
        }

        public void addColor(int a, int r, int g, int b) {
            palette.add(new int[]{a & 0xff, r & 0xff, g & 0xff, b & 0xff});
        }

        public void setPixelIndex(int x, int y, int idx) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return;
            }
            pixels[y * width + x] = (byte) (idx & 0xff);
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public byte[] getPixels() {
            return pixels;
        }

        public List<int[]> getPalette() {
            return palette;
        }
    }

    private static final class VisWireframe {

        private static final class Vertex {

            public final int x, y, z;

            Vertex(int x, int y, int z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }
        }

        private static final class Edge {

            public final int a, b;

            Edge(int a, int b) {
                this.a = a;
                this.b = b;
            }
        }

        private final List<Vertex> vertices = new ArrayList<>();
        private final List<Edge> edges = new ArrayList<>();
        private final List<Integer> points = new ArrayList<>();

        public int addVertex(int x, int y, int z) {
            vertices.add(new Vertex(x, y, z));
            return vertices.size() - 1;
        }

        public void addEdge(int a, int b) {
            edges.add(new Edge(a, b));
        }

        public void addPoint(int vi) {
            points.add(vi);
        }

        public boolean validate(StringBuilder outMsg) {
            int n = vertices.size();
            for (Edge e : edges) {
                if (e.a < 0 || e.a >= n || e.b < 0 || e.b >= n) {
                    if (outMsg != null) {
                        outMsg.append("Edge out of range");
                    }
                    return false;
                }
            }
            for (int p : points) {
                if (p < 0 || p >= n) {
                    if (outMsg != null) {
                        outMsg.append("Point out of range");
                    }
                    return false;
                }
            }
            return true;
        }

        public List<Vertex> getVertices() {
            return vertices;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public List<Integer> getPoints() {
            return points;
        }
    }

    // --- NO JSON, compact text wire format ---
    // Grammar: W1 V <n> x y z ... E <m> a b ... P <k> i ...
    // Lines may contain '#' comments which are ignored.
    private static String wireframeToString(VisWireframe wf) {
        var vs = wf.getVertices();
        var es = wf.getEdges();
        var ps = wf.getPoints();

        StringBuilder sb = new StringBuilder(32 + vs.size() * 12 + es.size() * 8 + ps.size() * 4);
        sb.append("W1 V ").append(vs.size());
        for (var v : vs) {
            sb.append(' ').append(v.x).append(' ').append(v.y).append(' ').append(v.z);
        }

        sb.append(" E ").append(es.size());
        for (var e : es) {
            sb.append(' ').append(e.a).append(' ').append(e.b);
        }

        sb.append(" P ").append(ps.size());
        for (int i = 0; i < ps.size(); i++) {
            sb.append(' ').append(ps.get(i));
        }

        return sb.toString();
    }

    private static VisWireframe wireframeFromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("null wireframe string");
        }
        String[] t = s.trim().split("\\s+");  // split on ANY whitespace
        int i = 0;

        // Header
        if (i >= t.length || !"W1".equals(t[i++])) {
            throw new IllegalArgumentException("Bad header (expected W1)");
        }

        // Vertices
        if (i >= t.length || !"V".equals(t[i++])) {
            throw new IllegalArgumentException("Missing V section");
        }
        int vCount = Integer.parseInt(t[i++]);
        VisWireframe wf = new VisWireframe();
        for (int k = 0; k < vCount; k++) {
            if (i + 2 >= t.length) {
                throw new IllegalArgumentException("Truncated vertices");
            }
            int x = Integer.parseInt(t[i++]);
            int y = Integer.parseInt(t[i++]);
            int z = Integer.parseInt(t[i++]);
            wf.addVertex(x, y, z);
        }

        // Edges
        if (i >= t.length || !"E".equals(t[i++])) {
            throw new IllegalArgumentException("Missing E section");
        }
        int eCount = Integer.parseInt(t[i++]);
        for (int k = 0; k < eCount; k++) {
            if (i + 1 >= t.length) {
                throw new IllegalArgumentException("Truncated edges");
            }
            int a = Integer.parseInt(t[i++]);
            int b = Integer.parseInt(t[i++]);
            // optional light bounds check; skip invalid
            if (a >= 0 && b >= 0 && a < wf.getVertices().size() && b < wf.getVertices().size()) {
                wf.addEdge(a, b);
            }
        }

        // Points (optional but expected in your writer)
        if (i < t.length) {
            if (!"P".equals(t[i++])) {
                throw new IllegalArgumentException("Missing P section");
            }
            int pCount = Integer.parseInt(t[i++]);
            for (int k = 0; k < pCount; k++) {
                if (i >= t.length) {
                    throw new IllegalArgumentException("Truncated points");
                }
                int pi = Integer.parseInt(t[i++]);
                if (pi >= 0 && pi < wf.getVertices().size()) {
                    wf.addPoint(pi);
                }
            }
        }

        return wf;
    }

    private static final Vector3 TMP1 = new Vector3();
    private static final Vector3 TMP2 = new Vector3();
    private static final Vector3 TMP3 = new Vector3();
    private static final Vector3 TMP4 = new Vector3();
    private static final Vector3 TMP5 = new Vector3();
    private static final Vector3 TMP6 = new Vector3();

    private static Model buildWireframeModel(VisWireframe wf, Color color, float unitScale, float pointSize) {

        final List<BattleZoneMesh.VisWireframe.Vertex> verts = wf.getVertices();

        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        Material mat = new Material(ColorAttribute.createDiffuse(color));
        MeshPartBuilder b = mb.part("wire", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, mat);
        b.setColor(color);

        // Edges → lines
        for (BattleZoneMesh.VisWireframe.Edge e : wf.getEdges()) {
            if (e.a < 0 || e.a >= verts.size() || e.b < 0 || e.b >= verts.size()) {
                continue;
            }
            BattleZoneMesh.VisWireframe.Vertex va  = verts.get(e.a);
            BattleZoneMesh.VisWireframe.Vertex vb = verts.get(e.b);

            TMP1.set(va.x * unitScale, va.y * unitScale, va.z * unitScale);
            TMP2.set(vb.x * unitScale, vb.y * unitScale, vb.z * unitScale);
            b.line(TMP1, TMP2);
        }

        // Points → tiny crosses (3 axes)
        if (pointSize > 0f) {
            float r = pointSize;
            for (int vi : wf.getPoints()) {
                if (vi < 0 || vi >= verts.size()) {
                    continue;
                }
                BattleZoneMesh.VisWireframe.Vertex v = verts.get(vi);
                float x = v.x * unitScale, y = v.y * unitScale, z = v.z * unitScale;

                // X axis
                TMP1.set(x - r, y, z);
                TMP2.set(x + r, y, z);
                b.line(TMP1, TMP2);

                // Y axis
                TMP3.set(x, y - r, z);
                TMP4.set(x, y + r, z);
                b.line(TMP3, TMP4);

                // Z axis
                TMP5.set(x, y, z - r);
                TMP6.set(x, y, z + r);
                b.line(TMP5, TMP6);
            }
        }

        return mb.end();
    }

    private static ModelInstance buildWireframeInstance(BattleZoneMesh.VisWireframe wf, Color color, float unitScale, float pointSize, Matrix4 transformOut) {
        Model model = buildWireframeModel(wf, color, unitScale, pointSize);
        ModelInstance instance = new ModelInstance(model);
        if (transformOut != null) {
            instance.transform.set(transformOut);
        }
        return instance;
    }

}

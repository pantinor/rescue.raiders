package rescue.raiders.objects;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import jakarta.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.collision.BoundingBox;

public class BattleZone implements ApplicationListener, InputProcessor {

    private static final Vector3 TMP1 = new Vector3();
    private static final Vector3 TMP2 = new Vector3();
    private static final Vector3 TMP3 = new Vector3();
    private static final Vector3 TMP4 = new Vector3();
    private static final Vector3 TMP5 = new Vector3();
    private static final Vector3 TMP6 = new Vector3();

    private static final String LAND1_HEX = "40000000E01F2060185C2800506000002060E01F20602000C07FC01F000040008060C01F4060000020002000C07F2000E01FE01F4060F01F4060F01F6060A0003000F41F05E0E05AFC5AFA5AFD1FF4FF0300F4FFF71F0CE0FD1F0CE003000CE009000CE0E346E046FE460C00F5FF0300F35FFC1FF15F5B5CF51FFB5FF31FFE5FF11F064003001B00A05EFF1F06A00B0006A0FF1FFCBF0200FFBF094AFD1FFDBF030001A00100FDBFA043010001406A1F070000C0";
    private static final String LAND2_HEX = "0000200030004060D01F20603000E01FF01F206020004060C01FA0602000807F2000E07FE01FC01FE01F6060000080002000A060E01F000000C0";
    private static final String LAND3_HEX = "2000000000004060E01F00002000C07F0000400020004060E01FC01FE01F6060200020602000C07F00002060E01F206010002060F01F2060E01FA07F2000600020004060E01F40601000A00FF01F206010002060D01F0000080060601800C07F10002060F01F4060E81FE07F2800E07FF01F400000002060E01F000000C0";
    private static final String LAND4_HEX = "20000000E01F40602000C01FE01F80600000A00020006060E01F20602000E01F20002060C01F2060000020004000C07FE01F200020002060E01F2060E01F000000C0";
    private static final String LAND5_HEX = "2000000020004060C01F2060000020004000C07FE01F200010002060D01F6060000080002000A060E01F000000C0";
    private static final String LAND6_HEX = "2000000000004060E01FE0603000E07FE01FC07FF01F600040006060D81F4060185460004060F91F036005000560FA1F036008000560A01F000000C0";
    private static final String LAND7_HEX = "60000000A01F406040006060C01F4060000040004000807FE01F400020004060C01F80602000C01F10004060D01F20603000E01FD01F606000C0";
    private static final String LAND8_HEX = "0000C0002000E060E01F40601000E01F30004060C01F000000C0";

    private static final String MESH_NARROW_PYRAMID = "W1 V 6 512 -640 -512 -512 -640 -512 -512 -640 512 512 -640 512 0 640 0 0 0 0 E 8 0 4 4 1 1 0 0 3 3 4 4 2 2 3 2 1 P 0";
    private static final String MESH_TALL_BOX = "W1 V 9 512 -640 -512 -512 -640 -512 -512 -640 512 512 -640 512 512 640 -512 -512 640 -512 -512 640 512 512 640 512 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 5 1 2 6 7 3 P 0";
    private static final String MESH_SLOW_TANK = "W1 V 25 512 -640 -736 -512 -640 -736 -512 -640 968 512 -640 968 568 -416 -1024 -568 -416 -1024 -568 -416 1248 568 -416 1248 344 -240 -680 -344 -240 -680 -344 -240 680 344 -240 680 168 96 -512 -168 96 -512 40 -16 -128 -40 -16 -128 -40 -96 128 40 -96 128 -40 -16 1120 -40 -96 1120 40 -16 1120 40 -96 1120 0 96 -512 0 160 -512 0 0 0 E 38 23 22 12 13 14 20 20 18 18 15 15 14 14 17 17 16 16 19 19 21 21 17 15 16 19 18 20 21 3 0 0 4 4 7 7 6 6 2 2 3 3 7 7 11 11 10 10 6 6 5 5 9 9 10 10 13 13 9 9 8 8 11 11 12 12 8 8 4 4 5 5 1 1 2 1 0 P 0";
    private static final String MESH_PROJECTILE = "W1 V 6 40 -96 -40 40 -16 -40 -40 -16 -40 -40 -96 -40 0 -56 80 0 0 0 E 8 0 4 4 1 1 0 0 3 3 4 4 2 2 3 2 1 P 0";
    private static final String MESH_REAR_TREAD_0 = "W1 V 7 552 -472 -948 -552 -472 -948 536 -552 -844 -536 -552 -844 516 -632 -736 -516 -632 -736 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_REAR_TREAD_1 = "W1 V 7 556 -456 -972 -556 -456 -972 540 -536 -868 -540 -536 -868 520 -616 -764 -520 -616 -764 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_REAR_TREAD_2 = "W1 V 7 564 -432 -1000 -564 -432 -1000 544 -512 -896 -544 -512 -896 528 -592 -792 -528 -592 -792 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_REAR_TREAD_3 = "W1 V 7 568 -416 -1024 -568 -416 -1024 548 -496 -920 -548 -496 -920 532 -576 -816 -532 -576 -816 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_FRONT_TREAD_0 = "W1 V 7 568 -416 1248 -568 -416 1248 548 -496 1152 -548 -496 1152 532 -576 1056 -532 -576 1056 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_FRONT_TREAD_1 = "W1 V 7 564 -432 1224 -564 -432 1224 544 -512 1128 -544 -512 1128 528 -592 1032 -528 -592 1032 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_FRONT_TREAD_2 = "W1 V 7 556 -456 1200 -556 -456 1200 540 -536 1104 -540 -536 1104 520 -616 1008 -520 -616 1008 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_FRONT_TREAD_3 = "W1 V 7 552 -472 1176 -552 -472 1176 536 -552 1080 -536 -552 1080 516 -632 984 -516 -632 984 0 0 0 E 3 0 1 2 3 4 5 P 0";
    private static final String MESH_WIDE_PYRAMID = "W1 V 6 800 -640 -800 -800 -640 -800 -800 -640 800 800 -640 800 0 800 0 0 0 0 E 8 0 4 4 1 1 0 0 3 3 4 4 2 2 3 2 1 P 0";
    private static final String MESH_RADAR1 = "W1 V 9 80 160 0 160 200 80 160 240 80 80 280 0 -80 160 0 -160 200 80 -160 240 80 -80 280 0 0 0 0 E 10 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 7 3 P 0";
    private static final String MESH_PROJECTILE_EXPLOSION = "W1 V 2 0 0 0 0 0 0 E 0 P 0";
    private static final String MESH_SHORT_BOX = "W1 V 9 640 -640 -640 -640 -640 -640 -640 -640 640 640 -640 640 640 -80 -640 -640 -80 -640 -640 -80 640 640 -80 640 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 5 1 2 6 7 3 P 0";
    private static final String MESH_CHUNK0_TANK_10 = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_CHUNK1_TANK_11 = "W1 V 9 120 -640 -240 -64 -560 -376 -160 -768 720 120 -640 640 64 -160 -40 -32 -120 0 160 -400 56 -200 -480 120 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 6 6 7 7 5 5 4 5 1 7 2 3 6 P 0";
    private static final String MESH_CHUNK2_TANK = "W1 V 15 344 -296 -588 -344 -296 -588 -344 -976 588 344 -976 588 168 -96 -272 -168 -96 -272 40 -376 0 -40 -376 0 -40 -576 180 40 -576 180 -40 -1000 1080 -40 -1072 1040 40 -1000 1080 40 -1072 1040 0 0 0 E 21 0 1 1 2 2 3 3 0 0 4 4 5 5 1 5 2 3 4 6 12 12 10 10 7 7 6 6 9 9 8 8 11 11 13 13 9 7 8 11 10 12 13 P 0";
    private static final String MESH_RADAR2 = "W1 V 9 80 160 0 160 200 80 160 240 80 80 280 0 -80 160 0 -160 200 80 -160 240 80 -80 280 0 0 0 0 E 10 0 1 1 2 2 3 3 0 0 4 4 5 5 6 6 7 7 4 7 3 P 0";
    private static final String MESH_CHUNK1_TANK_14 = "W1 V 9 120 -640 -240 -64 -560 -376 -160 -768 720 120 -640 640 64 -160 -40 -32 -120 0 160 -400 56 -200 -480 120 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 6 6 7 7 5 5 4 5 1 7 2 3 6 P 0";
    private static final String MESH_CHUNK0_TANK_15 = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_MISSILE = "W1 V 27 -144 0 -384 -72 96 -384 72 96 -384 144 0 -384 72 -96 -384 -72 -96 -384 -288 0 -96 -192 192 -96 192 192 -96 288 0 -96 192 -192 -96 -192 -192 -96 0 0 1152 0 0 1392 144 -336 -144 -144 -336 -144 -144 -336 144 144 -336 144 48 -184 -48 -48 -184 -48 -48 -168 48 48 -168 48 0 192 -96 -72 96 528 72 96 528 0 288 48 0 0 0 E 43 13 12 12 6 6 0 0 1 1 7 7 8 8 9 9 10 10 11 11 6 6 7 7 12 12 8 8 2 2 3 3 9 9 12 12 10 10 4 4 5 5 11 11 12 24 23 23 22 22 24 24 25 25 23 25 22 1 2 3 4 5 0 18 19 19 20 20 21 21 18 18 14 14 15 15 16 16 17 17 14 15 19 20 16 17 21 P 0";
    private static final String MESH_LOGO_BA = "W1 V 21 -5120 64 224 -3840 64 224 -3200 176 672 -3520 288 1120 -3200 400 1600 -3840 512 2048 -5120 512 2048 -4480 176 672 -4160 176 672 -4480 288 1120 -4160 400 1600 -4480 400 1600 -3200 64 224 -2240 176 32 -1280 64 224 -2240 512 2048 -2560 224 896 -2240 256 1024 -1920 224 896 -2240 336 1344 0 0 0 E 20 0 1 1 2 2 3 3 4 4 5 5 6 6 0 7 8 8 9 9 10 10 11 11 7 12 13 13 14 14 15 15 12 16 17 17 18 18 19 19 16 P 0";
    private static final String MESH_CHUNK1_MISSILE = "W1 V 9 120 -640 -240 -64 -560 -376 -160 -768 720 120 -640 640 64 -160 -40 -32 -120 0 160 -400 56 -200 -480 120 0 0 0 E 12 0 1 1 2 2 3 3 0 0 4 4 6 6 7 7 5 5 4 5 1 7 2 3 6 P 0";
    private static final String MESH_CHUNK4_MISSILE_19 = "W1 V 9 72 -368 -300 168 -368 -232 272 -472 -232 272 -568 -300 -168 -408 -96 -12 -384 40 260 -648 40 232 -808 -96 0 0 0 E 10 1 2 2 3 3 7 7 6 6 5 5 4 4 0 0 1 1 5 6 2 P 0";
    private static final String MESH_CHUNK0_MISSILE_1A = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_CHUNK5_MISSILE = "W1 V 5 12 -576 -80 -112 -864 472 44 24 800 16 -536 88 0 0 0 E 6 0 2 2 1 1 3 3 0 0 1 2 3 P 0";
    private static final String MESH_CHUNK0_MISSILE_1C = "W1 V 7 0 -544 220 80 -376 -320 -80 -192 340 0 -712 -184 80 -512 -124 -80 -416 -116 0 0 0 E 10 0 3 3 5 5 2 2 0 0 1 1 2 2 5 5 4 4 1 4 3 P 0";
    private static final String MESH_CHUNK4_MISSILE_1D = "W1 V 9 72 -368 -300 168 -368 -232 272 -472 -232 272 -568 -300 -168 -408 -96 -12 -384 40 260 -648 40 232 -808 -96 0 0 0 E 10 1 2 2 3 3 7 7 6 6 5 5 4 4 0 0 1 1 5 6 2 P 0";
    private static final String MESH_LOGO_TTLE = "W1 V 22 -640 64 224 -320 400 1600 640 400 1600 960 64 224 1280 400 1600 2240 400 1600 2240 64 224 3840 64 224 5440 112 448 4480 176 672 4480 224 896 5120 288 1120 4480 336 1344 4480 400 1600 5440 448 1824 3840 512 2048 2880 176 672 2880 512 2048 -1920 512 2048 -1920 400 1600 -960 400 1600 0 0 0 E 22 0 1 1 2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9 10 10 11 11 12 12 13 13 14 14 15 15 7 7 16 16 17 17 18 18 19 19 20 20 0 P 0";
    private static final String MESH_LOGO_ZONE = "W1 V 26 -4800 -512 -2048 -2240 -512 -2048 -3520 -400 -1600 -2240 -64 -224 -4800 -64 -224 -3520 -176 -672 -320 -512 -2048 -320 -64 -224 -1600 -400 -1600 -960 -400 -1600 -960 -176 -672 -1600 -176 -672 0 -512 -2048 640 -288 -1120 2560 -512 -2048 4160 -448 -1824 3200 -400 -1600 3200 -336 -1344 3840 -288 -1120 3200 -224 -896 3200 -176 -672 4160 -112 -448 2560 -64 -224 1920 -288 -1120 0 -64 -224 0 0 0 E 28 1 0 0 5 5 4 4 3 3 2 2 1 1 3 3 7 7 6 6 1 9 8 8 11 11 10 10 9 14 22 22 23 23 24 24 12 12 13 13 14 14 15 15 16 16 17 17 18 18 19 19 20 20 21 21 22 P 0";
    private static final String MESH_SAUCER = "W1 V 18 0 -80 -240 -160 -80 -160 -240 -80 0 -160 -80 160 0 -80 240 160 -80 160 240 -80 0 160 -80 -160 0 160 -960 -680 160 -680 -960 160 0 -680 160 680 0 160 960 680 160 680 960 160 0 680 160 -680 0 560 0 0 0 0 E 32 16 8 8 9 9 16 16 10 10 11 11 16 16 12 12 13 13 16 16 14 14 15 15 16 0 7 7 15 15 8 8 0 0 1 1 9 9 10 10 2 2 3 3 11 11 12 12 4 4 5 5 13 13 14 14 6 6 7 6 5 4 3 2 1 P 0";
    private static final String MESH_SUPER_TANK = "W1 V 26 -368 -640 1456 -552 -640 -456 552 -640 -456 368 -640 1456 -456 -184 -456 456 -184 -456 0 -552 1096 -272 -232 -272 -272 -184 -456 272 -184 -456 272 -232 -272 -184 88 -272 -184 88 -456 184 88 -456 184 88 -272 -88 -88 1280 -88 -88 88 88 -88 88 88 -88 1280 -88 0 1280 -88 0 -88 88 0 -88 88 0 1280 0 88 -456 0 552 -456 0 0 0 E 34 0 1 1 4 4 0 0 3 3 2 2 5 5 3 2 1 4 5 9 10 10 6 6 14 14 13 13 9 9 8 8 7 7 6 6 11 11 12 12 8 12 13 14 11 19 22 22 21 21 20 20 16 16 15 15 18 18 17 17 16 15 19 22 18 17 21 23 24 P 0";

    private static final String MESH_SPATTER_0 = "W1 V 9 -52 -360 0 -36 -360 36 0 -360 52 36 -360 36 52 -360 0 36 -360 -36 0 -360 -52 -36 -360 -36 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_SPATTER_1 = "W1 V 9 -100 -400 0 -72 -400 72 0 -400 100 72 -400 72 100 -400 0 72 -400 -72 0 -400 -100 -72 -400 -72 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_SPATTER_2 = "W1 V 9 -152 -440 0 -108 -440 108 0 -440 152 108 -440 108 152 -440 0 108 -440 -108 0 -440 -152 -108 -440 -108 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_SPATTER_3 = "W1 V 9 -200 -480 0 -144 -480 144 0 -480 200 144 -480 144 200 -480 0 144 -480 -144 0 -480 -200 -144 -480 -144 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_SPATTER_4 = "W1 V 9 -252 -520 0 -176 -520 176 0 -520 252 176 -520 176 252 -520 0 176 -520 -176 0 -520 -252 -176 -520 -176 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_SPATTER_5 = "W1 V 9 -300 -560 0 -212 -560 212 0 -560 300 212 -560 212 300 -560 0 212 -560 -212 0 -560 -300 -212 -560 -212 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_SPATTER_6 = "W1 V 9 -352 -600 0 -264 -600 264 0 -600 352 264 -600 264 352 -600 0 264 -600 -264 0 -600 -352 -264 -600 -264 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";
    private static final String MESH_SPATTER_7 = "W1 V 9 -400 -640 0 -284 -640 284 0 -640 400 284 -640 284 400 -640 0 284 -640 -284 0 -640 -400 -284 -640 -284 0 0 0 E 0 P 8 0 1 2 3 4 5 6 7";

    private static final List<String> ALL_MESH_STRINGS = List.of(
            MESH_NARROW_PYRAMID,
            MESH_TALL_BOX,
            MESH_SLOW_TANK,
            MESH_PROJECTILE,
            MESH_REAR_TREAD_0,
            MESH_REAR_TREAD_1,
            MESH_REAR_TREAD_2,
            MESH_REAR_TREAD_3,
            MESH_FRONT_TREAD_0,
            MESH_FRONT_TREAD_1,
            MESH_FRONT_TREAD_2,
            MESH_FRONT_TREAD_3,
            MESH_WIDE_PYRAMID,
            MESH_RADAR1,
            MESH_PROJECTILE_EXPLOSION,
            MESH_SHORT_BOX,
            MESH_CHUNK0_TANK_10,
            MESH_CHUNK1_TANK_11,
            MESH_CHUNK2_TANK,
            MESH_RADAR2,
            MESH_CHUNK1_TANK_14,
            MESH_CHUNK0_TANK_15,
            MESH_MISSILE,
            MESH_LOGO_BA,
            MESH_CHUNK1_MISSILE,
            MESH_CHUNK4_MISSILE_19,
            MESH_CHUNK0_MISSILE_1A,
            MESH_CHUNK5_MISSILE,
            MESH_CHUNK0_MISSILE_1C,
            MESH_CHUNK4_MISSILE_1D,
            MESH_LOGO_TTLE,
            MESH_LOGO_ZONE,
            MESH_SAUCER,
            MESH_SUPER_TANK,
            MESH_SPATTER_0,
            MESH_SPATTER_1,
            MESH_SPATTER_2,
            MESH_SPATTER_3,
            MESH_SPATTER_4,
            MESH_SPATTER_5,
            MESH_SPATTER_6,
            MESH_SPATTER_7
    );

    private static final List<Wireframe> ALL_WF = ALL_MESH_STRINGS.stream().map(BattleZone::wireframeFromString).toList();

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("BAttle Zone");
        cfg.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        new Lwjgl3Application(new BattleZone(), cfg);
    }

    private static final int SCREEN_WIDTH = 640 * 2;
    private static final int SCREEN_HEIGHT = 480 * 2;

    // map 
    private static final float WORLD_SCALE = 0.3f;
    private static final float MODEL_SCALE = 0.0008f;

    // background 
    private static final int ANGLES = 512;
    private static final float UNITS_PER_ANGLE = 8f;
    private static final float SEG_W_UNITS = 512f;
    private static final int SEG_COUNT = 8;
    private static final float STRIP_UNITS = SEG_W_UNITS * SEG_COUNT;
    private int angle9 = 0;
    private final int horizonAdj = 0;
    private static final List<List<Vector>> LAND = landScapeVectors();

    // volcano particles  
    private static final int VOLCANO_SEG_INDEX = 5;
    private static final int VOLCANO_PARTICLES = 5;
    private static final int VOLCANO_TOP_Y_UNITS = 94; // top of volcano is +94 units above horizon
    private static final int VOLCANO_GROUND_Y = -45; // ground is -94 units below the origin
    private static final int VOLCANO_X_OFFSET_UNITS = -5; // offset from RIGHT edge of the section; 0 = right edge

    private final VolcanoParticle[] volcanoParticles = new VolcanoParticle[VOLCANO_PARTICLES];

    // --- input state for smooth motion ---
    private boolean wDown, aDown, sDown, dDown;
    private final float yawSpeedDeg = 90f;
    private final float moveSpeed = 4f;
    private float headingDeg = 0f;

    private ModelBatch modelBatch;
    private PerspectiveCamera cam;
    private OrthographicCamera backGroundCam;
    private Environment environment;
    private final List<GameModelInstance> modelInstances = new ArrayList<>();
    private ShapeRenderer sr;

    private EnemyAI.Enemy enemy;
    private final EnemyAI.Context enemyCtx = new EnemyAI.Context();
    private int nmiCount = 0;

    private static final float PROJECTILE_SPEED = 0.40f;
    private static final int PROJECTILE_TTL_FRAMES = 0x7F;
    private Model projectileModel;
    private Projectile projectile;

    @Override
    public void create() {

        backGroundCam = new OrthographicCamera();
        backGroundCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backGroundCam.update();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.near = 0.1f;
        cam.far = 1000;
        cam.update();

        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(this);

        environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.05f, 0.05f, 0.05f, 1f));

        modelBatch = new ModelBatch();

        cam.position.set(0, 0.5f, 0);
        cam.lookAt(0, 0.5f, 15);

        GameModelInstance tank = buildWireframeInstance(ALL_WF.get(2), Color.RED, MODEL_SCALE, -1f, 3f, 0.5f, 3f);
        modelInstances.add(tank);

        enemy = new EnemyAI.Enemy(tank);
        enemy.pos.x = 0;
        enemy.pos.z = 15;
        enemy.facing = 360;
        enemyCtx.collisionChecker = this::collidesAnyModelXZ;

        projectileModel = buildWireframeModel(wireframeFromString(MESH_PROJECTILE), Color.YELLOW, MODEL_SCALE, -1f);
        projectile = new Projectile();
        enemyCtx.shooter = () -> projectile.spawnFromEnemy(enemy);

        loadMapObstacles();

        createAxes();

        for (int i = 0; i < volcanoParticles.length; i++) {
            volcanoParticles[i] = new VolcanoParticle();
        }

    }

    @Override
    public void render() {

        Gdx.graphics.setTitle("Battle Zone - FPS: " + Gdx.graphics.getFramesPerSecond());

        float dt = Gdx.graphics.getDeltaTime();

        cam.update();

        // --- yaw/pan with A/D ---
        float yaw = 0f;
        if (aDown) {
            yaw += yawSpeedDeg * dt;   // pan left
        }
        if (dDown) {
            yaw -= yawSpeedDeg * dt;   // pan right
        }
        if (yaw != 0f) {
            cam.rotate(Vector3.Y, yaw);
            headingDeg = (headingDeg + yaw) % 360f;
        }

        // --- move forward/back with W/S (XZ plane only) ---
        float move = 0f;
        if (wDown) {
            move += moveSpeed * dt;
        }
        if (sDown) {
            move -= moveSpeed * dt;
        }
        if (move != 0f) {
            Vector3 forwardXZ = new Vector3(cam.direction.x, 0f, cam.direction.z).nor().scl(move);
            cam.position.add(forwardXZ);
        }

        enemyCtx.playerX = cam.position.x;
        enemyCtx.playerZ = cam.position.z;
        enemyCtx.nmiCount = ++this.nmiCount;
        enemyCtx.projectileBusy = (projectile != null && projectile.active);

        EnemyAI.update(enemy, enemyCtx);

        if (projectile != null) {
            projectile.update();
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);

        modelBatch.render(axesInstance);

        for (ModelInstance i : modelInstances) {
            modelBatch.render(i, environment);
        }

        modelBatch.end();

        //background
        backGroundCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backGroundCam.update();

        sr.setProjectionMatrix(backGroundCam.combined);

        float hd = (headingDeg % 360f + 360f) % 360f;
        angle9 = (int) (hd / 360f * ANGLES) % ANGLES;

        drawBackground2D();
        drawHUD();

    }

    private static final class Wireframe {

        public static final class Vertex {

            public final int x, y, z;

            Vertex(int x, int y, int z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }
        }

        public static final class Edge {

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

    private static Wireframe wireframeFromString(String s) {
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
        Wireframe wf = new Wireframe();
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

    private static Model buildWireframeModel(Wireframe wf, Color color, float unitScale, float pointSize) {

        final List<Wireframe.Vertex> verts = wf.getVertices();

        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        Material mat = new Material(ColorAttribute.createDiffuse(color));
        MeshPartBuilder b = mb.part("wire", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, mat);
        b.setColor(color);

        // Edges → lines
        for (Wireframe.Edge e : wf.getEdges()) {
            if (e.a < 0 || e.a >= verts.size() || e.b < 0 || e.b >= verts.size()) {
                continue;
            }
            Wireframe.Vertex va  = verts.get(e.a);
            Wireframe.Vertex vb = verts.get(e.b);

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
                Wireframe.Vertex v = verts.get(vi);
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

    private static GameModelInstance buildWireframeInstance(Wireframe wf, Color color, float unitScale, float pointSize, float x, float y, float z) {
        Model model = buildWireframeModel(wf, color, unitScale, pointSize);
        GameModelInstance instance = new GameModelInstance(model);
        instance.transform.setToTranslation(x, y, z);
        return instance;
    }

    public static final class GameModelInstance extends ModelInstance {

        final BoundingBox boundingBox = new BoundingBox();

        public GameModelInstance(Model model) {
            super(model);
            this.calculateBoundingBox(boundingBox);
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                wDown = true;
                return true;
            case Input.Keys.A:
                aDown = true;
                return true;
            case Input.Keys.S:
                sDown = true;
                return true;
            case Input.Keys.D:
                dDown = true;
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                wDown = false;
                return true;
            case Input.Keys.A:
                aDown = false;
                return true;
            case Input.Keys.S:
                sDown = false;
                return true;
            case Input.Keys.D:
                dDown = false;
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        backGroundCam.setToOrtho(false, width, height);
        backGroundCam.update();
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean scrolled(float f, float f1) {
        return false;
    }

    private static final class Vector {

        final int dx, dy, intensity;
        final boolean shortVec;

        Vector(int dx, int dy, int in, boolean s) {
            this.dx = dx;
            this.dy = dy;
            this.intensity = in;
            this.shortVec = s;
        }

        @Override
        public String toString() {
            return (shortVec ? "SVEC" : "VCTR") + String.format(" dx=%+d dy=%+d in=%d", dx, dy, intensity);
        }
    }

    private static int signExtend(int v, int bits) {
        int mask = 1 << (bits - 1);
        return (v ^ mask) - mask;
    }

    private static int rd16LE(byte[] b, int off) {
        return (b[off] & 0xFF) | ((b[off + 1] & 0xFF) << 8);
    }

    private static List<Vector> parseLandscapeVectors(String hex) {
        byte[] b = DatatypeConverter.parseHexBinary(hex);
        List<Vector> out = new ArrayList<>();
        int pc = 0;
        int curInt = 0;

        while (pc + 2 <= b.length) {
            int w0 = rd16LE(b, pc);

            // VRTS?
            if (w0 == 0xC000) {
                break;
            }

            // SVEC? (bit 14 set)
            if ((w0 & 0x4000) != 0) {
                // dy: bits 12..8 (5-bit signed) * 2
                int dy5 = (w0 >>> 8) & 0x1F;
                int dy = signExtend(dy5, 5) * 2;

                // dx: bits 4..0 (5-bit signed) * 2
                int dx5 = (w0) & 0x1F;
                int dx = signExtend(dx5, 5) * 2;

                out.add(new Vector(dx, dy, curInt, true));
                pc += 2;
                continue;
            }

            // VCTR (long): needs a second word
            if (pc + 4 > b.length) {
                break; // safety
            }
            int w1 = rd16LE(b, pc + 2);

            // dy: 10-bit signed from w0 (bits 9..0) -> low8 + bits 9..8 from w0[11..10]
            int dy10 = ((w0 >>> 10) & 0x03) << 8 | (w0 & 0xFF);
            int dy = signExtend(dy10, 10);

            // dx: 10-bit signed from w1 (bits 9..0) -> low8 + bits 9..8 from w1[11..10]
            int dx10 = ((w1 >>> 10) & 0x03) << 8 | (w1 & 0xFF);
            int dx = signExtend(dx10, 10);

            // intensity (latched)
            curInt = (w1 >>> 12) & 0x0F;

            out.add(new Vector(dx, dy, curInt, false));
            pc += 4;
        }

        return out;
    }

    private static List<List<Vector>> landScapeVectors() {
        return List.of(
                parseLandscapeVectors(LAND1_HEX),
                parseLandscapeVectors(LAND2_HEX),
                parseLandscapeVectors(LAND3_HEX),
                parseLandscapeVectors(LAND4_HEX),
                parseLandscapeVectors(LAND5_HEX),
                parseLandscapeVectors(LAND6_HEX),
                parseLandscapeVectors(LAND7_HEX),
                parseLandscapeVectors(LAND8_HEX)
        );
    }

    private void drawBackground2D() {

        sr.begin(ShapeRenderer.ShapeType.Line);

        final int w = SCREEN_WIDTH;
        final int h = SCREEN_HEIGHT;

        final float unit2px = w / 1024f;
        final float segWpx = SEG_W_UNITS * unit2px;

        final float horizonY = h * 0.5f - ((horizonAdj >> 4) * unit2px);

        sr.setColor(0f, 0.7f, 0f, 1f);
        sr.line(0, horizonY, w, horizonY);

        // Scroll: 8 units per angle, wrap at 4096
        int scrollUnits = (int) ((angle9 * UNITS_PER_ANGLE) % STRIP_UNITS);
        if (scrollUnits < 0) {
            scrollUnits += STRIP_UNITS;
        }

        int segIndex = (int) (scrollUnits / SEG_W_UNITS) & 7;
        float offsetInSegUnits = scrollUnits % SEG_W_UNITS;
        float startX = -offsetInSegUnits * unit2px;

        for (int i = 0; i < 3; i++) {
            int idx = (segIndex + i) & 7;
            float sx = startX + i * segWpx;
            drawLandscapeSection(LAND.get(idx), sx, horizonY, unit2px);
        }

        // volcano is tied to a specific landscape segment; origin is at the RIGHT edge of that segment
        int offsetIdx = (VOLCANO_SEG_INDEX - segIndex) & 7;       // how many segments ahead
        float volcanoRightEdgeX = startX + offsetIdx * segWpx + segWpx;

        float originXpx = volcanoRightEdgeX + VOLCANO_X_OFFSET_UNITS * unit2px;
        float originYpx = horizonY + VOLCANO_TOP_Y_UNITS * unit2px;

        updateVolcanoParticles();
        drawVolcanoParticles(originXpx, originYpx, unit2px, w);

        sr.end();
    }

    private void drawLandscapeSection(List<Vector> section, float originX, float originY, float unit2px) {
        float x = originX, y = originY;
        for (Vector v : section) {
            float nx = x + v.dx * unit2px;
            float ny = y + v.dy * unit2px;

            if (v.intensity > 0) {
                float level = Math.min(1f, Math.max(0f, v.intensity / 3f));// 0..1 brightness
                sr.setColor(0f, level, 0f, 1f); // <-- green only
                sr.line(x, y, nx, ny);
            }
            x = nx;
            y = ny;
        }
    }

    private void drawHUD() {
        float reticleIntensity = 0.5f;

        Gdx.gl.glLineWidth(2);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(0f, reticleIntensity, 0f, 1f);

        //reticle
        sr.line(640 - 50, 480 + 25, 640 - 50, 480 + 25 + 25);
        sr.line(640 + 50, 480 + 25, 640 + 50, 480 + 25 + 25);
        sr.line(640, 480 + 50, 640, 480 + 50 + 50);
        sr.line(640 - 50, 480 + 50, 640 + 50, 480 + 50);

        sr.line(640 - 50, 480 - 25, 640 - 50, 480 - 25 - 25);
        sr.line(640 + 50, 480 - 25, 640 + 50, 480 - 25 - 25);
        sr.line(640, 480 - 50, 640, 480 - 50 - 50);
        sr.line(640 - 50, 480 - 50, 640 + 50, 480 - 50);

        //radar screen
        //score and lives left
        sr.end();
        Gdx.gl.glLineWidth(1);
    }

    private static final class VolcanoParticle {

        int ttl;   // 0..31
        float x;     // position in "vector units" relative to the volcano origin
        float y;
        float vx;    // signed velocity in units/frame (ROM is 8-bit signed)
        float vy;    // signed velocity in units/frame (gravity lowers this by 1 each update)

        void reset() {
            ttl = 0;
            x = y = vx = vy = 0;
        }
    }

    private void updateVolcanoParticles() {
        for (int i = 0; i < volcanoParticles.length; i++) {
            VolcanoParticle p = volcanoParticles[i];

            if (p.ttl <= 0) {
                if (MathUtils.random(7) == 0) {
                    p.ttl = 0x1f;
                    int speed = MathUtils.random(1, 4);
                    boolean goRight = MathUtils.randomBoolean();
                    p.vx = goRight ? speed : -(speed + 1);
                    p.vy = MathUtils.random(5, 12);
                    p.x = 0;
                    p.y = 0;
                }
                continue;
            }

            p.ttl--;
            if (p.ttl == 0) {
                p.x = p.y = 0;
                continue;
            }

            float AIR_SLOW = 0.55f;

            p.vy -= 1f * AIR_SLOW;
            p.y += p.vy * AIR_SLOW;

            if (p.y < VOLCANO_GROUND_Y) {
                p.reset();
                continue;
            }

            // horizontal unchanged
            p.x += p.vx;
        }
    }

    private void drawVolcanoParticles(float originXpx, float originYpx, float unit2px, int screenW) {
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (VolcanoParticle p : volcanoParticles) {
            if (p.ttl <= 0) {
                continue;
            }

            float px = originXpx + p.x * unit2px;
            float py = originYpx + p.y * unit2px;

            // quick cull
            if (px < -10 || px > screenW + 10) {
                continue;
            }

            // brightness from ttl
            float g = MathUtils.clamp(((p.ttl >> 2) / 7f) * 2f, 0f, 2f);
            sr.setColor(0, g, 0, 1f);

            float r = MathUtils.random(1f, 2f);
            sr.circle(px, py, r);
        }

        // Restore Line mode for the rest of the background pass
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
    }

    private void loadMapObstacles() {

        int[][] coords = new int[][]{
            {12, 96, 96, 0},
            {15, 128, 64, 16},
            {12, 128, 256, 32},
            {15, 64, 256, 64},
            {12, 256, 256, 24},
            {0, 256, 64, 40},
            {1, 256, 128, 48},
            {0, 64, 128, 56},
            {1, 80, 48, 64},
            {15, 192, 104, 72},
            {12, 137, 60, 80},
            {0, 184, 64, 88},
            {1, 168, 244, 96},
            {15, 236, 116, 104},
            {12, 232, 152, 112},
            {0, 152, 156, 120},
            {1, 16, 228, 128},
            {15, 8, 180, 136},
            {12, 64, 204, 144},
            {0, 92, 196, 152},
            {1, 84, 140, 160}
        };

        for (int[] info : coords) {
            Wireframe wf = ALL_WF.get(info[0]);

            float x = (info[1] - 128) * WORLD_SCALE;
            float z = (info[2] - 128) * WORLD_SCALE;
            float y = 0f + 0.5f;

            GameModelInstance inst = buildWireframeInstance(wf, Color.GREEN, MODEL_SCALE, -1f, x, y, z);
            float deg = info[3] * (360f / 256f);
            inst.transform.rotate(Vector3.Y, deg);
            modelInstances.add(inst);
        }

    }

    private final class Projectile {

        GameModelInstance inst;
        boolean active;
        int ttl;
        final Vector3 pos = new Vector3();
        final Vector3 vel = new Vector3();

        void spawnFromEnemy(EnemyAI.Enemy e) {
            if (active) {
                return;
            }
            if (inst == null) {
                inst = new GameModelInstance(projectileModel);
            }
            pos.set(e.pos.x, e.pos.y, e.pos.z);
            float rad = e.facing * MathUtils.PI2 / EnemyAI.ANGLE_STEPS;
            vel.set(MathUtils.sin(rad), 0f, MathUtils.cos(rad)).scl(PROJECTILE_SPEED);
            ttl = PROJECTILE_TTL_FRAMES;
            inst.transform.setToTranslation(pos);
            if (!modelInstances.contains(inst)) {
                modelInstances.add(inst);
            }
            active = true;
        }

        void update() {
            if (!active) {
                return;
            }
            pos.x += vel.x;
            pos.z += vel.z;
            ttl--;
            inst.transform.setToTranslation(pos);
            if (ttl <= 0) {
                deactivate();
            }
        }

        void deactivate() {
            active = false;
            if (inst != null) {
                modelInstances.remove(inst);
            }
        }
    }

    private boolean overlapsInstanceXZ(GameModelInstance inst, float x, float z, float radius) {
        inst.boundingBox.mul(inst.transform);

        float minX = inst.boundingBox.min.x, maxX = inst.boundingBox.max.x;
        float minZ = inst.boundingBox.min.z, maxZ = inst.boundingBox.max.z;

        float cx = MathUtils.clamp(x, minX, maxX);
        float cz = MathUtils.clamp(z, minZ, maxZ);

        float dx = x - cx, dz = z - cz;
        return dx * dx + dz * dz <= radius * radius;
    }

    private boolean collidesAnyModelXZ(float x, float z) {
        for (GameModelInstance inst : modelInstances) {
            if (enemy != null && inst == enemy.instance) {
                continue;
            }
            if (projectile != null && projectile.active && inst == projectile.inst) {
                continue;
            }

            float ENEMY_COLLISION_RADIUS = 0.4f;

            if (overlapsInstanceXZ(inst, x, z, ENEMY_COLLISION_RADIUS)) {
                return true;
            }
        }
        return false;
    }

    final float GRID_MIN = 0f;
    final float GRID_MAX = 20f;
    final float GRID_STEP = .5f;
    public Model axesModel;
    public ModelInstance axesInstance;

    private void createAxes() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        // grid
        MeshPartBuilder builder = modelBuilder.part("grid", GL30.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.LIGHT_GRAY);
        for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
            builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
            builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
        }

        // axes
        builder = modelBuilder.part("axes", GL30.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.RED);
        builder.line(0, 0, 0, 500, 0, 0);
        builder.setColor(Color.GREEN);
        builder.line(0, 0, 0, 0, 500, 0);
        builder.setColor(Color.BLUE);
        builder.line(0, 0, 0, 0, 0, 500);

        axesModel = modelBuilder.end();
        axesInstance = new ModelInstance(axesModel);
    }

}

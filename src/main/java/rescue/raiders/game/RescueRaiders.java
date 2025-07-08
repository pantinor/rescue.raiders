/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescue.raiders.game;

import java.awt.image.BufferedImage;
import rescue.raiders.levels.Level;
import rescue.raiders.levels.Level1;
import rescue.raiders.objects.Explosion;
import rescue.raiders.objects.Helicopter;
import rescue.raiders.objects.Tank;
import rescue.raiders.util.AtlasCache;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import rescue.raiders.objects.ActorType;
import rescue.raiders.objects.Engineer;
import rescue.raiders.objects.Infantry;
import rescue.raiders.objects.Jeep;

public class RescueRaiders extends Game implements InputProcessor {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 768;
    public static final int HUD_HEIGHT = 100;
    public static final int STATUS_BAR_HEIGHT = 15;

    public static final int FIELD_WIDTH = 14000;
    public static final int FIELD_HEIGHT = 48;

    public static final int SPAWN = 0;
    public static final int ENEMY_SPAWN = FIELD_WIDTH;

    OrthographicCamera camera;

    SpriteBatch staticBatch;
    SpriteBatch batchMiniMap;

    Stage stage;
    Image hud;
    Image floor;
    public static Helicopter heli;

    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Test";
        cfg.width = SCREEN_WIDTH;
        cfg.height = SCREEN_HEIGHT;
        new LwjglApplication(new RescueRaiders(), cfg);

    }

    @Override
    public void create() {
        Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("assets/image/cursor-cross.png"));
        Cursor customCursor = Gdx.graphics.newCursor(cursorPixmap, 8, 8);
        Gdx.graphics.setCursor(customCursor);
        cursorPixmap.dispose(); // Safe to dispose after setting

        AtlasCache.add("copter", "assets/image/wirly-bird.atlas");
        AtlasCache.add("launcher", "assets/image/rocket-launcher.atlas");
        AtlasCache.add("tank", "assets/image/cartoon-tank.atlas");
        AtlasCache.add("jeep", "assets/image/jeep.atlas");
        AtlasCache.add("soldier", "assets/image/soldier.atlas");
        AtlasCache.add("truck", "assets/image/covered-truck.atlas");
        AtlasCache.add("backgrounds", "assets/image/backgrounds.atlas");
        AtlasCache.add("turret", "assets/image/turret.atlas");
        AtlasCache.add("balloon", "assets/image/meteors.atlas");
        AtlasCache.add("chain", "assets/image/backgrounds.atlas");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage = new Stage(new ScreenViewport(camera));

        batchMiniMap = new SpriteBatch();

        staticBatch = new SpriteBatch();

        heli = (Helicopter) ActorType.HELI.getInstance();
        heli.setPosition(400, FIELD_HEIGHT);
        stage.addActor(heli);

        TextureRegion tr = new TextureRegion(makeFloorSection(AtlasCache.get("backgrounds"), FIELD_WIDTH + 2000, 5));
        int fx = 0;
        for (int i = 0; i < 5; i++) {
            floor = new Image(tr);
            floor.setPosition(fx - 1000, 0);
            floor.setUserObject(heli.createMiniIcon(Color.GRAY, 435, 3));
            stage.addActor(floor);
            fx += tr.getRegionWidth();
        }

        hud = new Image(fillRectangle(SCREEN_WIDTH, HUD_HEIGHT, Color.DARK_GRAY));
        hud.setY(SCREEN_HEIGHT - HUD_HEIGHT);

        Level l1 = new Level1();
        l1.addObjects(stage);

        Explosion ex = new Explosion(460, FIELD_HEIGHT);
        stage.addActor(ex);

        Gdx.input.setInputProcessor(new InputMultiplexer(this, heli));

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = heli.getX();

        //if (heli.hits(floor)) {
        //	heli.checkCrash();
        //}
        stage.act();
        stage.draw();

        staticBatch.begin();
        hud.draw(staticBatch, .7f);
        heli.drawStatusBars(staticBatch);
        staticBatch.end();

        batchMiniMap.begin();
        Array<com.badlogic.gdx.scenes.scene2d.Actor> actors = stage.getActors();
        for (com.badlogic.gdx.scenes.scene2d.Actor actor : actors) {
            Object obj = actor.getUserObject();
            if (obj != null && obj instanceof TextureRegion) {
                float x = ((actor.getX() / FIELD_WIDTH) * hud.getWidth());
                float y = ((actor.getY() / SCREEN_HEIGHT) * hud.getHeight()) + SCREEN_HEIGHT - HUD_HEIGHT;
                TextureRegion tr = (TextureRegion) obj;
                batchMiniMap.draw(tr, x, y);
            }
        }
        batchMiniMap.end();

    }

    public static float yup(float y) {
        return SCREEN_HEIGHT - y;
    }

    public void dispose() {

    }

    public static Texture fillRectangle(int width, int height, Color color) {
        Pixmap pix = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fillRectangle(0, 0, width, height);
        Texture t = new Texture(pix);
        pix.dispose();
        return t;
    }

    public static Texture makeFloorSection(TextureAtlas atlas, int totalFloorWidth, int numFloorPieces) {
        // Find the region named "ground" in the atlas
        AtlasRegion groundRegion = atlas.findRegion("ground");
        if (groundRegion == null) {
            throw new IllegalArgumentException("Region 'ground' not found in the atlas.");
        }

        int pieceWidth = groundRegion.getRegionWidth();
        int pieceHeight = groundRegion.getRegionHeight();

        int numberPiecesInEachSection = (totalFloorWidth / pieceWidth) / numFloorPieces;
        int finalTextureWidth = numberPiecesInEachSection * pieceWidth;

        // Create a new Pixmap large enough to hold all repeated pieces
        Pixmap result = new Pixmap(finalTextureWidth, pieceHeight, Pixmap.Format.RGBA8888);

        // Extract the Pixmap from the ground region
        Texture groundTexture = groundRegion.getTexture();
        groundTexture.getTextureData().prepare();
        Pixmap fullPixmap = groundTexture.getTextureData().consumePixmap();

        // Extract only the groundRegion part from the fullPixmap
        Pixmap groundPixmap = new Pixmap(pieceWidth, pieceHeight, Pixmap.Format.RGBA8888);
        groundPixmap.drawPixmap(
                fullPixmap,
                0, 0, // destX, destY
                groundRegion.getRegionX(), // srcX
                groundRegion.getRegionY(), // srcY
                pieceWidth, pieceHeight // srcWidth, srcHeight
        );

        // Now tile the groundPixmap into the result
        for (int x = 0; x < finalTextureWidth; x += pieceWidth) {
            result.drawPixmap(groundPixmap, x, 0);
        }

        // Clean up
        groundPixmap.dispose();
        fullPixmap.dispose();

        return new Texture(result, true);
    }

    public static Pixmap createPixmap(BufferedImage image) {

        int w = image.getWidth();
        int h = image.getHeight();

        Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pix.setColor(0f, 0f, 0f, .45f);
        pix.fillRectangle(0, 0, w, h);

        int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = pixels[y * w + x];
                pix.drawPixel(x, y, getRGBA(pixel));
            }
        }

        return pix;
    }

    public static int getRGBA(int rgb) {
        int a = rgb >> 24;
        a &= 0x000000ff;
        int rest = rgb & 0x00ffffff;
        rest <<= 8;
        rest |= a;
        return rest;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Keys.T:
                Tank tank = (Tank) ActorType.TANK.getInstance();
                stage.addActor(tank);
                break;
            case Keys.E:
                Engineer engineer = (Engineer) ActorType.ENGINEER.getInstance();
                engineer.setPosition(SPAWN, FIELD_HEIGHT);
                engineer.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
                stage.addActor(engineer);
                break;
            case Keys.I:
                Infantry infantry = (Infantry) ActorType.INFANTRY.getInstance();
                infantry.setPosition(SPAWN, FIELD_HEIGHT);
                infantry.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
                stage.addActor(infantry);
                break;
            case Keys.J:
                Jeep jeep = (Jeep) ActorType.JEEP.getInstance();
                jeep.setPosition(SPAWN, FIELD_HEIGHT);
                jeep.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
                stage.addActor(jeep);
                break;
            case Keys.L:
//                Actor launcher = new Actor("launcher-raising", AtlasCache.get("launcher"), 0.05f, 1f, true);
//                launcher.setPosition(SPAWN, FIELD_HEIGHT);
//                launcher.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
//                stage.addActor(launcher);
//                break;

        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            heli.shoot(stage);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean scrolled(float f, float f1) {
        return false;
    }

}

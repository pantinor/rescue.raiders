/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescue.raiders.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
    public static final int SCREEN_HEIGHT = 600;

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

	//Rectangle floor = new Rectangle(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Test";
        cfg.width = SCREEN_WIDTH;
        cfg.height = SCREEN_HEIGHT;
        new LwjglApplication(new RescueRaiders(), cfg);

    }

    @Override
    public void create() {

        AtlasCache.add("copter", "assets/image/wirly-bird.atlas");
        AtlasCache.add("launcher", "assets/image/rocket-launcher.atlas");
        AtlasCache.add("tank", "assets/image/cartoon-tank.atlas");
        AtlasCache.add("jeep", "assets/image/jeep.atlas");
        AtlasCache.add("soldier", "assets/image/soldier.atlas");
        AtlasCache.add("truck", "assets/image/covered-truck.atlas");
        AtlasCache.add("backgrounds", "assets/image/backgrounds.atlas");
        AtlasCache.add("turret", "assets/image/turret.atlas");
        AtlasCache.add("balloon", "assets/image/meteors.atlas");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage = new Stage(new ScreenViewport(camera));

        batchMiniMap = new SpriteBatch();

        staticBatch = new SpriteBatch();
        
        heli = (Helicopter)ActorType.HELI.getInstance();
        heli.setPosition(400, FIELD_HEIGHT);
        stage.addActor(heli);

        TextureRegion tr = new TextureRegion(makeFloorSection(AtlasCache.get("backgrounds"), FIELD_WIDTH + 2000, 5));
        int fx = 0;
        for (int i = 0; i < 5; i++) {
            floor = new Image(tr);
            floor.setPosition(fx - 1000, 0);
            floor.setUserObject(heli.createMiniIcon(Color.GRAY, 275, 3));
            stage.addActor(floor);
            fx += tr.getRegionWidth();
        }

        hud = new Image(fillRectangle(SCREEN_WIDTH, 40, Color.DARK_GRAY));
        hud.setY(SCREEN_HEIGHT - 40);

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
        staticBatch.end();

        batchMiniMap.begin();
        Array<com.badlogic.gdx.scenes.scene2d.Actor> actors = stage.getActors();
        for (com.badlogic.gdx.scenes.scene2d.Actor actor : actors) {
            Object obj = actor.getUserObject();
            if (obj != null && obj instanceof TextureRegion) {
                float x = ((actor.getX() / FIELD_WIDTH) * hud.getWidth());
                float y = ((actor.getY() / SCREEN_HEIGHT) * hud.getHeight()) + SCREEN_HEIGHT - 40;
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

    public Texture fillRectangle(int width, int height, Color color) {
        Pixmap pix = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fillRectangle(0, 0, width, height);
        Texture t = new Texture(pix);
        pix.dispose();
        return t;
    }

    public static Texture makeFloorSection(TextureAtlas atlas, int totalFloorWIdth, int numFloorPieces) {
        Texture t = null;

        try {
            AtlasRegion ar = (AtlasRegion) atlas.findRegion("ground");
            BufferedImage sheet = ImageIO.read(new File("assets/image/backgrounds.png"));

            int h = ar.getRegionHeight();
            int w = ar.getRegionWidth();

            int numberPiecesInEachSection = (totalFloorWIdth / w) / numFloorPieces;
            int twidth = numberPiecesInEachSection * w;

            BufferedImage canvas = new BufferedImage(twidth, h, BufferedImage.TYPE_INT_ARGB);
            BufferedImage sub = sheet.getSubimage(ar.getRegionX(), ar.getRegionY(), w, h);

            for (int x = 0; x < twidth; x += w) {
                canvas.getGraphics().drawImage(sub, x, 0, w, h, null);
            }

            Pixmap p = createPixmap(canvas);
            t = new Texture(p);
            p.dispose();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;

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
                Tank  tank = (Tank)ActorType.TANK.getInstance();
                stage.addActor(tank);
                break;
            case Keys.E:
                Engineer engineer = (Engineer)ActorType.ENGINEER.getInstance();
                engineer.setPosition(SPAWN, FIELD_HEIGHT);
                engineer.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
                stage.addActor(engineer);
                break;
            case Keys.I:
                Infantry infantry = (Infantry)ActorType.INFANTRY.getInstance();
                infantry.setPosition(SPAWN, FIELD_HEIGHT);
                infantry.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
                stage.addActor(infantry);
                break;
            case Keys.J:
                Jeep jeep = (Jeep)ActorType.JEEP.getInstance();
                jeep.setPosition(SPAWN, FIELD_HEIGHT);
                jeep.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
                stage.addActor(jeep);
                break;
//            case Keys.L:
//                Actor launcher = new Actor("launcher-raising", AtlasCache.get("launcher"), 0.05f, 1f, true);
//                launcher.setPosition(SPAWN, FIELD_HEIGHT);
//                launcher.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
//                stage.addActor(launcher);
//                break;
            case Keys.SPACE:
                heli.shoot(stage);
                break;
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
        // TODO Auto-generated method stub
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
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

}

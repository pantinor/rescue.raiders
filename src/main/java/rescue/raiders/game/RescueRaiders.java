package rescue.raiders.game;

import rescue.raiders.levels.Level;
import rescue.raiders.objects.Copter;
import rescue.raiders.util.AtlasCache;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import rescue.raiders.levels.Level1;
import rescue.raiders.objects.ActorType;
import rescue.raiders.objects.BlueTank;
import rescue.raiders.objects.CoveredTruck;
import rescue.raiders.objects.Engineer;
import rescue.raiders.objects.Infantry;
import rescue.raiders.objects.Jeep;
import rescue.raiders.objects.RocketLauncher;
import rescue.raiders.objects.TanTank;
import rescue.raiders.objects.TreadTruck;
import rescue.raiders.util.Stars;
import rescue.raiders.util.ExplosionTriangle;

public class RescueRaiders extends Game implements InputProcessor {

    public static final int SCREEN_WIDTH = 1600;
    public static final int SCREEN_HEIGHT = 900;
    public static final int HUD_HEIGHT = 100;
    public static final int STATUS_BAR_HEIGHT = 15;

    public static final int FIELD_WIDTH = 14000;
    public static final int FIELD_HEIGHT = 48;

    public static final int SPAWN = 0;
    public static final int ENEMY_SPAWN = FIELD_WIDTH;

    public static final int HELI_START_X = 542;
    public static final int HELI_START_Y = 105;

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private SpriteBatch batchMiniMap;

    private GameStage stage;

    private Texture hud;
    private Texture floor;
    private Texture turretRangeTexture;
    private Texture enemyTurretRangeTexture;

    private Stars stars;

    private DelayedRemovalArray<ExplosionTriangle> explosionTriangles = new DelayedRemovalArray<>();

    private ShapeRenderer shapeRenderer;

    public Copter heli;

    public static BitmapFont FONT;
    public static RescueRaiders GAME;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Rescue Raiders");
        cfg.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        new Lwjgl3Application(new RescueRaiders(), cfg);
    }

    @Override
    public void create() {

        GAME = this;

        FONT = new BitmapFont();

        Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("assets/image/cursor-cross.png"));
        Cursor customCursor = Gdx.graphics.newCursor(cursorPixmap, 8, 8);
        Gdx.graphics.setCursor(customCursor);
        cursorPixmap.dispose(); // Safe to dispose after setting

        AtlasCache.add("copter", "assets/image/wirly-bird.atlas");
        AtlasCache.add("shobu-copter", "assets/image/shobu-copter.atlas");
        AtlasCache.add("launcher", "assets/image/rocket-launcher.atlas");
        AtlasCache.add("tan-tank", "assets/image/tan-tank.atlas");
        AtlasCache.add("grey-tank", "assets/image/grey-tank.atlas");
        AtlasCache.add("blue-tank", "assets/image/blue-tank.atlas");
        AtlasCache.add("large-tank", "assets/image/large-tank.atlas");
        AtlasCache.add("jeep", "assets/image/jeep.atlas");
        AtlasCache.add("soldier", "assets/image/soldier.atlas");
        AtlasCache.add("truck", "assets/image/covered-truck.atlas");
        AtlasCache.add("backgrounds", "assets/image/backgrounds.atlas");
        AtlasCache.add("turret", "assets/image/turret.atlas");
        AtlasCache.add("balloon", "assets/image/meteors.atlas");
        AtlasCache.add("chain", "assets/image/backgrounds.atlas");
        AtlasCache.add("rocket", "assets/image/rocket.atlas");
        AtlasCache.add("rocket-launcher", "assets/image/rocket-launcher.atlas");
        AtlasCache.add("covered-truck", "assets/image/covered-truck.atlas");
        AtlasCache.add("tread-truck", "assets/image/tread-truck.atlas");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage = new GameStage(new ScreenViewport(camera));

        batchMiniMap = new SpriteBatch();

        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();

        heli = (Copter) ActorType.HELI.getInstance();
        heli.setPosition(HELI_START_X, HELI_START_Y);

        floor = makeGroundTexture(AtlasCache.get("backgrounds"), FIELD_WIDTH + 2000, 1);
        hud = miniDisplayBackground(SCREEN_WIDTH, HUD_HEIGHT);

        turretRangeTexture = filledSemiCircleUp(180, Color.GREEN);
        enemyTurretRangeTexture = filledSemiCircleUp(180, Color.SCARLET);

        Level l1 = new Level1(stage);
        l1.addObjects();

        stage.addActor(heli);

        stage.setHelicopter(heli);

        stars = new Stars(SCREEN_WIDTH, SCREEN_HEIGHT, 200);

        Gdx.input.setInputProcessor(new InputMultiplexer(this, heli));

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!heli.isDestroyed()) {
            camera.position.x = heli.getX();
        }

        shapeRenderer.setProjectionMatrix(camera.combined);

        stars.render(Gdx.graphics.getDeltaTime());

        stage.act();
        stage.draw();

        batch.begin();
        batch.draw(floor, -1000, 0);
        batch.draw(hud, 0, SCREEN_HEIGHT - HUD_HEIGHT);
        heli.drawStatusBars(batch);
        batch.end();

        batchMiniMap.begin();
        Array<com.badlogic.gdx.scenes.scene2d.Actor> actors = stage.getActors();
        for (com.badlogic.gdx.scenes.scene2d.Actor actor : actors) {
            float x = (actor.getX() / FIELD_WIDTH) * hud.getWidth();
            float y = (actor.getY() / SCREEN_HEIGHT) * hud.getHeight() + SCREEN_HEIGHT - HUD_HEIGHT - 2;
            if (actor instanceof rescue.raiders.objects.Actor ga) {
                if (ga.type() == ActorType.TURRET) {
                    batchMiniMap.draw(turretRangeTexture, x - 90, y);
                } else if (ga.type() == ActorType.ENEMY_TURRET) {
                    batchMiniMap.draw(enemyTurretRangeTexture, x - 90, y);
                }
            }
        }
        for (com.badlogic.gdx.scenes.scene2d.Actor actor : actors) {
            float x = (actor.getX() / FIELD_WIDTH) * hud.getWidth();
            float y = (actor.getY() / SCREEN_HEIGHT) * hud.getHeight() + SCREEN_HEIGHT - HUD_HEIGHT - 2;
            Object obj = actor.getUserObject();
            if (obj != null && obj instanceof Texture tx) {
                batchMiniMap.draw(tx, x, y);
            }
            if (obj != null && obj instanceof TextureRegion tx) {
                batchMiniMap.draw(tx, x, y);
            }
        }
        batchMiniMap.end();

        for (ExplosionTriangle tri : explosionTriangles) {
            tri.render(Gdx.graphics.getDeltaTime());
        }

        explosionTriangles.begin();
        for (int i = 0; i < explosionTriangles.size; i++) {
            if (explosionTriangles.get(i).getTime() > .8f) {
                explosionTriangles.removeIndex(i);
            }
        }
        explosionTriangles.end();

    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Keys.T:
                TanTank tank = (TanTank) ActorType.TANK.getInstance();
                tank.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(tank);
                break;
            case Keys.E:
                Engineer engineer = (Engineer) ActorType.ENGINEER.getInstance();
                engineer.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(engineer);
                break;
            case Keys.I:
                Infantry infantry = (Infantry) ActorType.INFANTRY.getInstance();
                infantry.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(infantry);
                break;
            case Keys.J:
                Jeep jeep = (Jeep) ActorType.JEEP.getInstance();
                jeep.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(jeep);
                break;
            case Keys.Y:
                TreadTruck truck = (TreadTruck) ActorType.TREAD_TRUCK.getInstance();
                truck.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(truck);
                break;
            case Keys.C:
                CoveredTruck ctruck = (CoveredTruck) ActorType.COVERED_TRUCK.getInstance();
                ctruck.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(ctruck);
                break;
            case Keys.L:
                RocketLauncher launcher = (RocketLauncher) ActorType.ROCKET_LAUNCHER.getInstance();
                launcher.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(launcher);
                break;
            case Keys.G:
                BlueTank btank = (BlueTank) ActorType.BLUE_TANK.getInstance();
                btank.setPosition(SPAWN, FIELD_HEIGHT);
                stage.addActor(btank);
                break;

        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            heli.shoot();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
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

    public static float yup(float y) {
        return SCREEN_HEIGHT - y;
    }

    public void addExplosion(float x, float y, boolean facingWest, int count) {
        float base = facingWest ? 180f - 20f : 20f;
        for (int i = 0; i < count; i++) {
            // vary each one by ±15°
            float spread = MathUtils.random(-15f, 15f);
            float triAngle = base + spread;
            explosionTriangles.add(new ExplosionTriangle(shapeRenderer, x, y, triAngle));
        }
    }

    public static final Texture fillRectangle(Color c, int w, int h) {
        Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pix.setColor(c.r, c.g, c.b, .85f);
        pix.fillRectangle(0, 0, w, h);
        Texture t = new Texture(pix);
        pix.dispose();
        return t;
    }

    public static Texture fillRectangleGradient(int width, int height, Color color) {
        Pixmap pix = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        float topGradientHeight = height * 0.45f;
        float bottomGradientHeight = height * 0.45f;
        float middleStart = bottomGradientHeight;
        float middleEnd = height - topGradientHeight;

        for (int y = 0; y < height; y++) {
            float alpha;

            if (y < middleStart) {
                // Bottom 45%: fade in from 25% to 100%
                float t = y / bottomGradientHeight;
                alpha = 0.25f + t * (1.0f - 0.25f);
            } else if (y > middleEnd) {
                // Top 45%: fade out from 100% to 25%
                float t = (y - middleEnd) / topGradientHeight;
                alpha = 1.0f - t * (1.0f - 0.25f);
            } else {
                // Middle 60%: fully opaque
                alpha = 1.0f;
            }

            Color gradientColor = new Color(color.r, color.g, color.b, alpha);
            pix.setColor(gradientColor);
            pix.drawLine(0, y, width - 1, y);
        }

        Texture texture = new Texture(pix);
        pix.dispose();
        return texture;
    }

    public static Texture miniDisplayBackground(int width, int height) {
        Color color = Color.FOREST;
        Pixmap pix = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int y = 0; y < height; y++) {
            float t = (float) y / (height - 1);
            float alpha = 0.25f + t * (1.0f - 0.25f);
            Color gradientColor = new Color(color.r, color.g, color.b, alpha);
            pix.setColor(gradientColor);
            pix.drawLine(0, y, width - 1, y);
        }

        pix.setColor(Color.GREEN);
        pix.fillRectangle(0, height - 3, width, 3);

        Texture texture = new Texture(pix);
        pix.dispose();
        return texture;
    }

    private static Texture filledSemiCircleUp(int diameter, Color color) {
        int height = diameter / 2;
        Pixmap pixmap = new Pixmap(diameter, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(color.r, color.g, color.b, 0.25f);

        float radius = diameter / 2f;
        float centerX = radius;
        float centerY = height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < diameter; x++) {
                float dx = x - centerX;
                float dy = y - centerY;
                if (dx * dx + dy * dy <= radius * radius) {
                    pixmap.drawPixel(x, y);
                }
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public static Texture makeGroundTexture(TextureAtlas atlas, int totalFloorWidth, int numFloorPieces) {
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

    public static float getAngleToTarget(float tx, float ty, float x, float y) {
        float ang = (float) Math.toDegrees(Math.atan2(tx - x, y - ty));
        if (ang < 0) {
            ang = 360 + ang;
        }
        ang = (ang - 90) % 360;
        return ang;
    }

}

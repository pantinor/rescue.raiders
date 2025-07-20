package rescue.raiders.objects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class AtlasViewer extends ApplicationAdapter {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 900;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private TextureAtlas atlas;
    private Texture atlasTexture;
    private Array<TextureAtlas.AtlasRegion> regions;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Animation<TextureRegion> anim1, anim2, anim3, anim4, anim5, anim6;
    private float frameCounter = 0;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("AtlasViewer");
        cfg.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        new Lwjgl3Application(new AtlasViewer(), cfg);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        atlas = new TextureAtlas(Gdx.files.internal("assets/image/enemy-copter.atlas"));
        atlasTexture = atlas.getTextures().first();
        regions = atlas.getRegions();

        Array<TextureAtlas.AtlasRegion> ch = atlas.findRegions("copter1");
        anim1 = new Animation(0.05f, ch, Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> ch2 = atlas.findRegions("copter2");
        anim2 = new Animation(0.05f, ch2, Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> ch3 = atlas.findRegions("copter3");
        anim3 = new Animation(0.05f, ch3, Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> ch4 = atlas.findRegions("copter4");
        anim4 = new Animation(0.05f, ch4, Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> ch5 = atlas.findRegions("copter5");
        anim5 = new Animation(0.05f, ch5, Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> ch6 = atlas.findRegions("copter6");
        anim6 = new Animation(0.05f, ch6, Animation.PlayMode.LOOP);

        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void render() {
        // clear
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        int texHeight = atlasTexture.getHeight();

        batch.begin();
        batch.draw(atlasTexture, 0, SCREEN_HEIGHT - texHeight - 10);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);

        for (TextureAtlas.AtlasRegion region : regions) {
            float drawY = SCREEN_HEIGHT - region.getRegionY() - region.getRegionHeight() - 10;
            shapeRenderer.rect(region.getRegionX(), drawY, region.getRegionWidth(), region.getRegionHeight());
        }
        shapeRenderer.end();

        batch.begin();
        for (TextureAtlas.AtlasRegion region : regions) {
            String meta = "(" + region.getRegionX() + "," + region.getRegionY() + ")" + " " + region.index;
            float drawY = SCREEN_HEIGHT - region.getRegionY() - region.getRegionHeight();
            font.draw(batch, region.name, region.getRegionX(), drawY + font.getLineHeight());
            font.draw(batch, meta, region.getRegionX(), drawY);
        }

        frameCounter += Gdx.graphics.getDeltaTime();

        {
            TextureRegion tr = anim1.getKeyFrame(frameCounter);
            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            batch.draw(tr, 20, 20);
        }
        {
            TextureRegion tr = anim2.getKeyFrame(frameCounter);
            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            batch.draw(tr, 120, 20);
        }
        {
            TextureRegion tr = anim3.getKeyFrame(frameCounter);
            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            batch.draw(tr, 220, 20);
        }
        {
            TextureRegion tr = anim4.getKeyFrame(frameCounter);
            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            batch.draw(tr, 320, 20);
        }
        {
            TextureRegion tr = anim5.getKeyFrame(frameCounter);
            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            batch.draw(tr, 420, 20);
        }
        {
            TextureRegion tr = anim6.getKeyFrame(frameCounter);
            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            batch.draw(tr, 520, 20);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        atlas.dispose();
        font.dispose();
    }
}

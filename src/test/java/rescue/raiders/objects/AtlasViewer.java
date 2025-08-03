package rescue.raiders.objects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
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
    private Animation<TextureRegion> anim1, anim2, anim3, anim4, anim5, anim6, anim7, blades;
    Array<TextureAtlas.AtlasRegion> turning;
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

        atlas = new TextureAtlas(Gdx.files.internal("assets/image/soldier.atlas"));
        atlasTexture = atlas.getTextures().first();
        regions = atlas.getRegions();

        Array<TextureAtlas.AtlasRegion> ch = atlas.findRegions("infantry");
        anim1 = new Animation(0.02f, ch, Animation.PlayMode.LOOP);

        //turning = atlas.findRegions("turning");
        //Array<TextureAtlas.AtlasRegion> flipped = flipRegionsWithPixmap(turning);
        //for (int i = flipped.size - 1; i >= 0; i--) {
            //turning.add(flipped.get(i));
        //}

        //anim2 = new Animation(0.15f, turning, Animation.PlayMode.LOOP);

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
            font.draw(batch, region.name + " [" + region.index + "]", region.getRegionX(), drawY);
        }

        frameCounter += Gdx.graphics.getDeltaTime();

        {
            TextureRegion tr = anim1.getKeyFrame(frameCounter);
            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            batch.draw(tr, 500, 20);
        }
        {
            //TextureRegion tr = anim2.getKeyFrame(frameCounter);
            //tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
            //batch.draw(tr, 120, 20);
        }

//        int x = 0;
//        for (TextureRegion tr : turning) {
//            tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
//            batch.draw(tr, 10 + x, 120);
//            x += 80;
//        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        atlas.dispose();
        font.dispose();
    }

    private Array<TextureAtlas.AtlasRegion> flipRegionsWithPixmap(Array<TextureAtlas.AtlasRegion> regions) {
        Array<TextureAtlas.AtlasRegion> flipped = new Array<>();

        Texture texture = regions.first().getTexture();
        TextureData data = texture.getTextureData();
        if (!data.isPrepared()) {
            data.prepare();
        }
        Pixmap originalPixmap = data.consumePixmap();

        for (TextureAtlas.AtlasRegion region : regions) {
            int width = region.getRegionWidth();
            int height = region.getRegionHeight();

            Pixmap cropped = new Pixmap(width, height, originalPixmap.getFormat());
            cropped.drawPixmap(originalPixmap,
                    0, 0,
                    region.getRegionX(), region.getRegionY(),
                    width, height);

            Pixmap flippedPixmap = new Pixmap(width, height, originalPixmap.getFormat());
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = cropped.getPixel(x, y);
                    flippedPixmap.drawPixel(width - 1 - x, y, pixel);
                }
            }

            Texture flippedTexture = new Texture(flippedPixmap);
            flippedTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            TextureAtlas.AtlasRegion flippedRegion = new TextureAtlas.AtlasRegion(new TextureRegion(flippedTexture));
            flipped.add(flippedRegion);

            cropped.dispose();
        }

        originalPixmap.dispose();
        return flipped;
    }

}

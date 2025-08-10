package rescue.raiders.objects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ZoomableAtlasViewer extends ApplicationAdapter {

    public static final int SCREEN_WIDTH = 1800;
    public static final int SCREEN_HEIGHT = 900;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private TextureAtlas atlas;
    private Texture atlasTexture;
    private Array<TextureAtlas.AtlasRegion> regions;
    private BitmapFont font;

    // World camera for content
    private OrthographicCamera camera;
    // HUD camera for scrollbars
    private OrthographicCamera hudCamera;

    private Animation<TextureRegion> anim1;
    private float frameCounter = 0f;

    // Zoom settings
    private float minZoom = 0.25f;
    private float maxZoom = 4f;
    private float zoomStep = 0.1f;

    // Content (atlas) size
    private int contentWidth;
    private int contentHeight;

    // Scrollbar visuals + interaction
    private final float SB_THICK = 10f;      // scrollbar thickness
    private final float SB_MARGIN = 2f;      // margin from window edges
    private final float SB_MIN_THUMB = 25f;  // minimum thumb size for usability
    private boolean draggingH = false;
    private boolean draggingV = false;
    private float dragOffsetH = 0f;
    private float dragOffsetV = 0f;

    // Grid toggle
    private boolean showGrid = false;
    private static final int GRID_SIZE = 24;

    // UI
    private Stage stage;
    private CheckBox gridCheckBox;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("AtlasViewer");
        cfg.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        new Lwjgl3Application(new ZoomableAtlasViewer(), cfg);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        atlas = new TextureAtlas(Gdx.files.internal("assets/image/shobu-copter.atlas"));
        atlasTexture = atlas.getTextures().first();
        regions = atlas.getRegions();
        contentWidth = atlasTexture.getWidth();
        contentHeight = atlasTexture.getHeight();

        Array<TextureAtlas.AtlasRegion> ch = atlas.findRegions("rocket");
        anim1 = new Animation<>(0.07f, ch, Animation.PlayMode.LOOP);
        
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.classpath("assets/tiny-font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 8;
        font = generator.generateFont(parameter);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(contentWidth / 2f, contentHeight / 2f, 0f);
        camera.update();

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        stage = new Stage(new ScreenViewport());
        gridCheckBox = new CheckBox(" Show grid (24px)", createMinimalCheckBoxSkin());
        gridCheckBox.setChecked(false);
        gridCheckBox.addListener(event -> {
            showGrid = gridCheckBox.isChecked();
            return false;
        });

        gridCheckBox.setPosition(10, SCREEN_HEIGHT - gridCheckBox.getHeight() - 10);
        stage.addActor(gridCheckBox);

        InputAdapter worldInput = new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                float targetZoom = camera.zoom * (1f + amountY * zoomStep);
                camera.zoom = MathUtils.clamp(targetZoom, minZoom, maxZoom);
                clampCameraToContent();
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float y = SCREEN_HEIGHT - screenY;

                float hBarX = SB_MARGIN;
                float hBarY = SB_MARGIN;
                float hBarW = SCREEN_WIDTH - SB_MARGIN * 2 - SB_THICK; // leave room for vertical bar
                float hBarH = SB_THICK;

                float vBarX = SCREEN_WIDTH - SB_THICK - SB_MARGIN;
                float vBarY = SB_MARGIN + SB_THICK + SB_MARGIN; // above horizontal bar
                float vBarW = SB_THICK;
                float vBarH = SCREEN_HEIGHT - (SB_MARGIN * 2 + SB_THICK);

                Thumb hThumb = computeHorizontalThumb(hBarX, hBarY, hBarW, hBarH);
                Thumb vThumb = computeVerticalThumb(vBarX, vBarY, vBarW, vBarH);

                if (pointInRect(screenX, y, hThumb.x, hThumb.y, hThumb.w, hThumb.h)) {
                    draggingH = true;
                    dragOffsetH = screenX - hThumb.x;
                    return true;
                }
                if (pointInRect(screenX, y, vThumb.x, vThumb.y, vThumb.w, vThumb.h)) {
                    draggingV = true;
                    dragOffsetV = y - vThumb.y;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (!draggingH && !draggingV) {
                    return false;
                }

                float y = SCREEN_HEIGHT - screenY;

                float hBarX = SB_MARGIN;
                float hBarY = SB_MARGIN;
                float hBarW = SCREEN_WIDTH - SB_MARGIN * 2 - SB_THICK;

                float vBarX = SCREEN_WIDTH - SB_THICK - SB_MARGIN;
                float vBarY = SB_MARGIN + SB_THICK + SB_MARGIN;
                float vBarH = SCREEN_HEIGHT - (SB_MARGIN * 2 + SB_THICK);

                if (draggingH) {
                    float viewW = camera.viewportWidth * camera.zoom;
                    float thumbW = computeHorizontalThumb(hBarX, hBarY, hBarW, SB_THICK).w;
                    float maxTravel = hBarW - thumbW;
                    float thumbX = MathUtils.clamp(screenX - dragOffsetH, hBarX, hBarX + maxTravel);
                    float pos = (maxTravel <= 0f) ? 0f : (thumbX - hBarX) / maxTravel;
                    if (contentWidth > viewW) {
                        float halfW = viewW / 2f;
                        camera.position.x = halfW + pos * (contentWidth - viewW);
                        camera.update();
                    }
                }
                if (draggingV) {
                    float viewH = camera.viewportHeight * camera.zoom;
                    Thumb vt = computeVerticalThumb(vBarX, vBarY, SB_THICK, vBarH);
                    float maxTravel = vBarH - vt.h;
                    float thumbY = MathUtils.clamp(y - dragOffsetV, vBarY, vBarY + maxTravel);
                    float pos = (maxTravel <= 0f) ? 0f : (thumbY - vBarY) / maxTravel;
                    if (contentHeight > viewH) {
                        float halfH = viewH / 2f;
                        camera.position.y = halfH + pos * (contentHeight - viewH);
                        camera.update();
                    }
                }
                clampCameraToContent();
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                draggingH = false;
                draggingV = false;
                return false;
            }
        };

        InputMultiplexer mux = new InputMultiplexer(stage, worldInput);
        Gdx.input.setInputProcessor(mux);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        clampCameraToContent();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(atlasTexture, 0, 0);

        frameCounter += Gdx.graphics.getDeltaTime();
        TextureRegion tr = anim1.getKeyFrame(frameCounter);
        tr.setRegion(tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(), tr.getRegionHeight());
        batch.draw(tr, 800, 500);

        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        for (TextureAtlas.AtlasRegion region : regions) {
            float rx = region.getRegionX();
            float ry = contentHeight - region.getRegionY() - region.getRegionHeight();
            font.draw(batch, region.name.substring(0, 2) + region.index, rx + 2, ry + region.getRegionHeight() - 6);
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
        for (TextureAtlas.AtlasRegion region : regions) {
            float rx = region.getRegionX();
            float ry = contentHeight - region.getRegionY() - region.getRegionHeight();
            shapeRenderer.rect(rx, ry, region.getRegionWidth(), region.getRegionHeight());
        }
        shapeRenderer.end();

        if (showGrid) {
            drawGrid();
        }

        drawScrollbars();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void drawGrid() {
        // Lines
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0f, 1f, 0f, 0.6f); // green with slight transparency

        // Vertical lines
        for (int x = 0; x <= contentWidth; x += GRID_SIZE) {
            shapeRenderer.line(x, 0, x, contentHeight);
        }
        // Horizontal lines
        for (int y = 0; y <= contentHeight; y += GRID_SIZE) {
            shapeRenderer.line(0, y, contentWidth, y);
        }
        shapeRenderer.end();

        // Labels along left (Y) and top (X)
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Y labels on left edge (x ~ 2)
        font.setColor(0f, 1f, 0f, 1f);
        for (int y = 0; y <= contentHeight; y += GRID_SIZE) {
            font.draw(batch, String.valueOf(y), 2, y + 12);
        }
        // X labels on top edge (y ~ contentHeight - 2)
        for (int x = 0; x <= contentWidth; x += GRID_SIZE) {
            font.draw(batch, String.valueOf(x), x + 2, contentHeight - 2);
        }
        batch.end();
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
    }

    private void drawScrollbars() {
        hudCamera.update();
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float hBarX = SB_MARGIN;
        float hBarY = SB_MARGIN;
        float hBarW = SCREEN_WIDTH - SB_MARGIN * 2 - SB_THICK;
        float hBarH = SB_THICK;

        float vBarX = SCREEN_WIDTH - SB_THICK - SB_MARGIN;
        float vBarY = SB_MARGIN + SB_THICK + SB_MARGIN;
        float vBarW = SB_THICK;
        float vBarH = SCREEN_HEIGHT - (SB_MARGIN * 2 + SB_THICK);

        // Bar backgrounds
        shapeRenderer.setColor(0.15f, 0.15f, 0.15f, 1f);
        shapeRenderer.rect(hBarX, hBarY, hBarW, hBarH);
        shapeRenderer.rect(vBarX, vBarY, vBarW, vBarH);

        // Thumbs
        Thumb hThumb = computeHorizontalThumb(hBarX, hBarY, hBarW, hBarH);
        Thumb vThumb = computeVerticalThumb(vBarX, vBarY, vBarW, vBarH);

        shapeRenderer.setColor(0.45f, 0.45f, 0.45f, 1f);
        shapeRenderer.rect(hThumb.x, hThumb.y, hThumb.w, hThumb.h);
        shapeRenderer.rect(vThumb.x, vThumb.y, vThumb.w, vThumb.h);

        shapeRenderer.end();

        // Bar outlines
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 1f);
        shapeRenderer.rect(hBarX, hBarY, hBarW, hBarH);
        shapeRenderer.rect(vBarX, vBarY, vBarW, vBarH);
        shapeRenderer.end();
    }

    private Thumb computeHorizontalThumb(float barX, float barY, float barW, float barH) {
        float viewW = camera.viewportWidth * camera.zoom;
        float thumbW = (contentWidth <= 0) ? barW : barW * MathUtils.clamp(viewW / contentWidth, 0f, 1f);
        thumbW = Math.max(thumbW, SB_MIN_THUMB);

        float pos;
        if (contentWidth <= viewW) {
            pos = 0f;
        } else {
            float halfW = viewW / 2f;
            float minX = halfW;
            float maxX = contentWidth - halfW;
            float travel = (maxX - minX);
            pos = (camera.position.x - minX) / travel;
            pos = MathUtils.clamp(pos, 0f, 1f);
        }
        float maxThumbTravel = barW - thumbW;
        float thumbX = barX + pos * maxThumbTravel;
        return new Thumb(thumbX, barY, thumbW, barH);
    }

    private Thumb computeVerticalThumb(float barX, float barY, float barW, float barH) {
        float viewH = camera.viewportHeight * camera.zoom;
        float thumbH = (contentHeight <= 0) ? barH : barH * MathUtils.clamp(viewH / contentHeight, 0f, 1f);
        thumbH = Math.max(thumbH, SB_MIN_THUMB);

        float pos;
        if (contentHeight <= viewH) {
            pos = 0f;
        } else {
            float halfH = viewH / 2f;
            float minY = halfH;
            float maxY = contentHeight - halfH;
            float travel = (maxY - minY);
            pos = (camera.position.y - minY) / travel;
            pos = MathUtils.clamp(pos, 0f, 1f);
        }
        float maxThumbTravel = barH - thumbH;
        float thumbY = barY + pos * maxThumbTravel;
        return new Thumb(thumbY, barX, thumbH, barW).swapXY();
    }

    private void clampCameraToContent() {
        float viewW = camera.viewportWidth * camera.zoom;
        float viewH = camera.viewportHeight * camera.zoom;

        float halfW = viewW / 2f;
        float halfH = viewH / 2f;

        float minX = halfW;
        float maxX = Math.max(halfW, contentWidth - halfW);
        float minY = halfH;
        float maxY = Math.max(halfH, contentHeight - halfH);

        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);
        camera.update();
    }

    private boolean pointInRect(float px, float py, float rx, float ry, float rw, float rh) {
        return px >= rx && px <= rx + rw && py >= ry && py <= ry + rh;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        hudCamera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
        // Keep checkbox top-left
        gridCheckBox.setPosition(10, height - gridCheckBox.getHeight() - 10);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        atlas.dispose();
        font.dispose();
        stage.dispose();
    }

    // Small struct for scrollbar thumbs
    private static class Thumb {

        float x, y, w, h;

        Thumb(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        Thumb swapXY() {
            float nx = y, ny = x, nw = h, nh = w;
            x = nx;
            y = ny;
            w = nw;
            h = nh;
            return this;
        }
    }

    // Minimal programmatic skin for a CheckBox (no external assets)
    private Skin createMinimalCheckBoxSkin() {
        Skin skin = new Skin();
        BitmapFont uiFont = new BitmapFont();
        skin.add("default-font", uiFont, BitmapFont.class);

        // Off box
        Pixmap off = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        off.setColor(0.2f, 0.2f, 0.2f, 1f);
        off.fill();
        off.setColor(0.7f, 0.7f, 0.7f, 1f);
        off.drawRectangle(0, 0, 20, 20);

        // On box
        Pixmap on = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        on.setColor(0.2f, 0.2f, 0.2f, 1f);
        on.fill();
        on.setColor(0.7f, 0.7f, 0.7f, 1f);
        on.drawRectangle(0, 0, 20, 20);
        on.setColor(0f, 1f, 0f, 1f);
        on.fillRectangle(4, 4, 12, 12);

        Texture offTex = new Texture(off);
        Texture onTex = new Texture(on);
        off.dispose();
        on.dispose();

        TextureRegionDrawable offDr = new TextureRegionDrawable(new TextureRegion(offTex));
        TextureRegionDrawable onDr = new TextureRegionDrawable(new TextureRegion(onTex));

        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.checkboxOff = offDr;
        style.checkboxOn = onDr;
        style.font = uiFont;
        style.fontColor = com.badlogic.gdx.graphics.Color.WHITE;

        skin.add("default", style);
        return skin;
    }
}

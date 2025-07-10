package rescue.raiders.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Renders a field of twinkling stars.
 */
public class Stars {

    private static final Color[] PALETTE = {
        Color.WHITE,
        Color.SLATE,
        Color.CYAN,
        Color.SKY};

    private static final float STAR_RADIUS = 1.5f;

    private static class Star {

        float x, y;
        Color color;
        float phase, speed;
    }

    private final Array<Star> stars = new Array<>();
    private final ShapeRenderer shape = new ShapeRenderer();
    private final Random rnd = new Random();
    private float time;

    /**
     * @param numStars how many points to draw
     */
    public Stars(int width, int height, int numStars) {

        float cx = width * 0.5f, cy = height * 0.5f;

        for (int i = 0; i < numStars; i++) {
            Star s = new Star();
            // uniform random across the full width/height
            s.x = rnd.nextFloat() * width;
            s.y = rnd.nextFloat() * height;
            s.color = PALETTE[rnd.nextInt(PALETTE.length)];
            // random twinkle speed & phase
            s.speed = 1f + rnd.nextFloat() * 2f;
            s.phase = rnd.nextFloat() * (float) Math.PI * 2f;
            stars.add(s);
        }
    }

    public void render(float delta) {
        time += delta;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Star s : stars) {
            // brightness oscillates between 0.2 and 1.0
            float brightness = 0.6f + 0.4f
                    * (float) Math.sin(time * s.speed + s.phase);
            Color c = s.color.cpy();
            c.a = brightness;
            shape.setColor(c);
            shape.circle(s.x, s.y, STAR_RADIUS);
        }
        shape.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        shape.dispose();
    }

    private static float clamp(float v, float min, float max) {
        return v < min ? min : (v > max ? max : v);
    }
}

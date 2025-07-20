package rescue.raiders.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ExplosionTriangle {

    ShapeRenderer renderer;
    Vector2 position;
    float angle;
    float f = 0.0f;
    Color color;
    float time;
    float speedFactor;

    public ExplosionTriangle(ShapeRenderer renderer, float x, float y, float angle) {
        this.renderer = renderer;
        this.position = new Vector2(x, y);
        this.angle = angle;
        this.f = MathUtils.random(0.2f, .6f);
        this.color = new Color(Color.RED).lerp(Color.YELLOW, MathUtils.random());
        this.speedFactor = MathUtils.random(1f, 2f);
    }

    public void render(float delta) {
        time += delta;

        float speed = speedFactor * 400f;
        position.x += speed * delta * MathUtils.cosDeg(angle);
        position.y += speed * delta * MathUtils.sinDeg(angle);

        renderer.begin(ShapeType.Filled);
        renderer.setColor(color);
        renderer.identity();
        renderer.translate(position.x, position.y, 0);
        renderer.rotate(0, 0, 1, angle);
        renderer.triangle(
                -f * 15f, -f * 13f,
                f * 15f, -f * 13f,
                0f, f * 13f
        );
        renderer.identity();
        renderer.end();
    }

    public float getTime() {
        return time;
    }
}

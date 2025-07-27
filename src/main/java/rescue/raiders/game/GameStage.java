package rescue.raiders.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import rescue.raiders.objects.Copter;

public class GameStage extends Stage {

    private Copter heli;

    public GameStage(Viewport viewport) {
        super(viewport);
    }

    public void setHelicopter(Copter h) {
        this.heli = h;
    }

    public Copter getHelicopter() {
        return heli;
    }
}

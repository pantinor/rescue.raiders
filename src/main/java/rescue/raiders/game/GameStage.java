package rescue.raiders.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import rescue.raiders.objects.Helicopter;

public class GameStage extends Stage {

    private Helicopter heli;

    public GameStage(Viewport viewport) {
        super(viewport);
    }

    public void setHelicopter(Helicopter h) {
        this.heli = h;
    }

    public Helicopter getHelicopter() {
        return heli;
    }
}

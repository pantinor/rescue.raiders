package rescue.raiders.levels;

import static rescue.raiders.game.RescueRaiders.*;

import java.util.ArrayList;
import java.util.List;

import rescue.raiders.objects.Actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import rescue.raiders.objects.ActorType;

public abstract class Level {

    final List<Actor> objects = new ArrayList<>();
    final Stage stage;
    Object[][] layout;

    public Level(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        for (int i = 0; i < layout.length; i++) {
            ActorType t = (ActorType) layout[i][0];
            int x = (int) layout[i][1];
            Actor a = t.getInstance();
            a.setPosition(x, FIELD_HEIGHT);
            objects.add(a);
        }
    }

    public void addObjects() {
        for (Actor m : objects) {
            stage.addActor(m);
        }
    }

}

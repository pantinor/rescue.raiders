package rescue.raiders.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.List;
import rescue.raiders.objects.Copter;

public class GameStage extends Stage {

    private final List<rescue.raiders.objects.Actor> actors = new ArrayList<>();

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

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        if (actor instanceof rescue.raiders.objects.Actor) {
            actors.add((rescue.raiders.objects.Actor) actor);
        }
    }

    public void removeActor(rescue.raiders.objects.Actor actor) {
        this.actors.remove(actor);
    }

    public List<rescue.raiders.objects.Actor> gameActors() {
        return this.actors;
    }

}

package rescue.raiders.levels;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import static rescue.raiders.game.RescueRaiders.ENEMY_SPAWN;
import static rescue.raiders.game.RescueRaiders.FIELD_HEIGHT;
import rescue.raiders.objects.ActorType;
import rescue.raiders.objects.Engineer;
import rescue.raiders.objects.Infantry;
import rescue.raiders.objects.Jeep;
import rescue.raiders.objects.Tank;

public class Level1 extends Level {

    public Level1(Stage stage) {
        super(stage);

        this.layout = new Object[][]{
            {ActorType.BASE, 0},
            {ActorType.PAD, 500},
            {ActorType.HUT, 1000},
            {ActorType.ENEMY_HUT, 1500},
            {ActorType.TURRET, 2000},
            {ActorType.HUT, 3000},
            {ActorType.TURRET, 4000},
            {ActorType.HUT, 5000},
            {ActorType.HUT, 6000},
            {ActorType.ENEMY_HUT, 7000},
            {ActorType.ENEMY_HUT, 8000},
            {ActorType.ENEMY_TURRET, 9000},
            {ActorType.ENEMY_HUT, 10000},
            {ActorType.ENEMY_TURRET, 11000},
            {ActorType.ENEMY_HUT, 12000},
            {ActorType.ENEMY_PAD, 13500},
            {ActorType.ENEMY_BASE, 13850}
        };

        init();

        stage.addAction(Actions.sequence(
                Actions.run(() -> {
                    Tank tank = (Tank) ActorType.ENEMY_TANK.getInstance();
                    tank.setPosition(ENEMY_SPAWN, FIELD_HEIGHT);
                    stage.addActor(tank);
                }),
                Actions.delay(5f),
                Actions.run(() -> {
                    Engineer engineer = (Engineer) ActorType.ENGINEER.getInstance();
                    engineer.setPosition(ENEMY_SPAWN, FIELD_HEIGHT);
                    stage.addActor(engineer);
                }),
                Actions.delay(5f),
                Actions.run(() -> {
                    Infantry infantry = (Infantry) ActorType.INFANTRY.getInstance();
                    infantry.setPosition(ENEMY_SPAWN, FIELD_HEIGHT);
                    stage.addActor(infantry);
                }),
                Actions.delay(5f),
                Actions.run(() -> {
                    Jeep jeep = (Jeep) ActorType.JEEP.getInstance();
                    jeep.setPosition(ENEMY_SPAWN, FIELD_HEIGHT);
                    stage.addActor(jeep);
                })
        ));

    }

}

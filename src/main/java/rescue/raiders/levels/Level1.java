package rescue.raiders.levels;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import static rescue.raiders.game.RescueRaiders.ENEMY_SPAWN;
import static rescue.raiders.game.RescueRaiders.FIELD_HEIGHT;
import rescue.raiders.objects.ActorType;
import rescue.raiders.objects.EnemyCopter;
import rescue.raiders.objects.Engineer;
import rescue.raiders.objects.GreyTank;
import rescue.raiders.objects.Infantry;
import rescue.raiders.objects.Jeep;
import rescue.raiders.objects.LargeTank;

public class Level1 extends Level {

    public Level1(Stage stage) {
        super(stage);

        this.layout = new Object[][]{
            {ActorType.BASE, 0},
            {ActorType.PAD, 500},
            {ActorType.HUT, 1000},
            {ActorType.ENEMY_HUT, 450},
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
//                Actions.run(() -> {
//                    GreyTank tank = (GreyTank) ActorType.GREY_TANK.getInstance();
//                    tank.setPosition(2500, FIELD_HEIGHT);
//                    stage.addActor(tank);
//                }),
//                Actions.delay(5f),
                Actions.run(() -> {
                    Engineer engineer = (Engineer) ActorType.ENEMY_ENGINEER.getInstance();
                    engineer.setPosition(1800, FIELD_HEIGHT);
                    stage.addActor(engineer);
                }),
//                Actions.delay(5f),
//                Actions.run(() -> {
//                    Infantry infantry = (Infantry) ActorType.ENEMY_INFANTRY.getInstance();
//                    infantry.setPosition(2000, FIELD_HEIGHT);
//                    stage.addActor(infantry);
//                }),
                Actions.delay(5f),
                Actions.run(() -> {
                    Jeep jeep = (Jeep) ActorType.ENEMY_JEEP.getInstance();
                    jeep.setPosition(3500, FIELD_HEIGHT);
                    stage.addActor(jeep);
                }),
//                Actions.delay(5f),
//                Actions.run(() -> {
//                    LargeTank tank = (LargeTank) ActorType.ENEMY_LARGE_TANK.getInstance();
//                    tank.setPosition(3000, FIELD_HEIGHT);
//                    stage.addActor(tank);
//                }),
                Actions.delay(5f),
                Actions.run(() -> {
                    EnemyCopter heli = (EnemyCopter) ActorType.ENEMY_HELI.getInstance();
                    heli.setPosition(7000, FIELD_HEIGHT);
                    stage.addActor(heli);

                    SequenceAction takeoff = Actions.sequence(
                            Actions.delay(1f),
                            Actions.run(() -> heli.setUp(true)),
                            Actions.delay(1f),
                            Actions.run(() -> heli.setUp(false))
                    );
                    stage.addAction(takeoff);

                    SequenceAction loop = Actions.sequence(
                            Actions.delay(3f),
                            Actions.run(() -> heli.setLeft(true)),
                            Actions.delay(3f),
                            Actions.run(() -> heli.setLeft(false)),
                            Actions.delay(3f),
                            Actions.run(() -> heli.turn()),
                            Actions.delay(3f),
                            Actions.run(() -> heli.setRight(true)),
                            Actions.delay(3f),
                            Actions.run(() -> heli.setRight(false)),
                            Actions.delay(3f),
                            Actions.run(() -> heli.turn())
                    );

                    stage.addAction(Actions.forever(loop));

                })
        ));

    }

}

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
            {ActorType.HUT, 500},
            {ActorType.ENEMY_HUT, 700}
        };

        init();

    }

}

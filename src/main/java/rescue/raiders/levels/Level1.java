package rescue.raiders.levels;

import com.badlogic.gdx.scenes.scene2d.Stage;
import rescue.raiders.objects.ActorType;

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

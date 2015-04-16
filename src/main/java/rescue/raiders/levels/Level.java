package rescue.raiders.levels;

import static rescue.raiders.game.RescueRaiders.*;

import java.util.ArrayList;
import java.util.List;

import rescue.raiders.objects.AAGun;
import rescue.raiders.objects.Base;
import rescue.raiders.objects.Hut;
import rescue.raiders.objects.Movable;
import rescue.raiders.objects.Pad;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Level {
	
	String layout;
	List<Movable> objects = new ArrayList<>();
	
	public void init() {
		
		String[] temp = layout.split(":");
		
		for (String obj : temp) {
			String[] s = obj.split(",");
			String name = s[0];
			int x = Integer.parseInt(s[1]);
			Movable m = null;
			if (name.equals("hut") || name.equals("enemy-hut")) {
				m = new Hut(name);
			} else if (name.equals("pad")) {
				m = new Pad(name);
			} else if (name.equals("base") || name.equals("enemy-base")) {
				m = new Base(name);
			} else if (name.equals("turret") || name.equals("enemy-turret")) {
				m = new AAGun(name);
			}
			
			if (m != null) {
				m.setPosition(x, FIELD_HEIGHT);
				objects.add(m);
			}
		}
	}
	
	public void addObjects(Stage stage) {
		for (Movable m: objects) {
			stage.addActor(m);
		}
	}
	

}

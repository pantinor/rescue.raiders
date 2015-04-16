package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Color;

public class AAGun extends Movable {
	
	public AAGun(String name) {
		super(name, AtlasCache.get("turret"), .65f, false);
		this.setUserObject(createMiniIcon(Color.RED, 4,4));

	}
	
	
	

}
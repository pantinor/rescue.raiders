package rescue.raiders.objects;

import com.badlogic.gdx.graphics.Color;


public class Base extends Movable {
	
	public Base(String name) {
		
		super(name, AtlasCache.get("backgrounds"), .65f, false);
		this.setUserObject(createMiniIcon(Color.BLUE, 12,8));

	}
	
	
	

}

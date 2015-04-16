package rescue.raiders.objects;

import rescue.raiders.util.AtlasCache;

import com.badlogic.gdx.graphics.Color;


public class Pad extends Movable {
	
	public Pad(String name) {
		super(name, AtlasCache.get("backgrounds"), .65f, false);
		this.setUserObject(createMiniIcon(Color.GREEN, 10,4));

	}
	
	
	

}

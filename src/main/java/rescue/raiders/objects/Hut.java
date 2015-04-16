package rescue.raiders.objects;

import java.util.Random;

import rescue.raiders.util.AtlasCache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;


public class Hut extends Movable {
	
	private Movable balloon;
	private boolean balloonAdded = false;
	
	public Hut(String name) {
		
		super(name, AtlasCache.get("backgrounds"), .65f, false);
		this.setUserObject(createMiniIcon(Color.GRAY, 8,8));
		
		this.balloon = new Movable("meteor", AtlasCache.get("meteor"), .2f, 1f, false);
		this.balloon.setUserObject(createMiniIcon(Color.ORANGE, 3,3));		
	}
	
	@Override
	public void setPosition(float x, float y) {
		
		super.setPosition(x, y);
		
		this.balloon.setX(x + 60);
		this.balloon.setY(new Random().nextInt(400-120) + 120);
		
		this.balloon.clearActions();
		
		SequenceAction seq1 = Actions.action(SequenceAction.class);
		//seq1.addAction(Actions.delay(.05f));
		//seq1.addAction(Actions.run(new MeteorAction()));
		seq1.addAction(Actions.moveTo(x + 60, 400, 8f));
		seq1.addAction(Actions.moveTo(x + 60, 120, 8f));
		this.balloon.addAction(Actions.forever(seq1));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if (!balloonAdded) {
			this.getStage().addActor(this.balloon);
			balloonAdded = true;
		}
		
		frameCounter += Gdx.graphics.getDeltaTime();
		batch.draw(tr, this.getX(), this.getY(), tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
	}
	

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rescue.raiders.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rescue.raiders.levels.Level;
import rescue.raiders.levels.Level1;
import rescue.raiders.objects.AtlasCache;
import rescue.raiders.objects.Bullet;
import rescue.raiders.objects.Explosion;
import rescue.raiders.objects.Helicopter;
import rescue.raiders.objects.Movable;
import rescue.raiders.objects.Tank;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Main extends Game implements InputProcessor {

	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 600;
	
	public static final int FIELD_WIDTH = 14000;
	public static final int FIELD_HEIGHT = 24;
	
	public static final int SPAWN = 0;
	public static final int ENEMY_SPAWN = FIELD_WIDTH;


	OrthographicCamera camera;

	SpriteBatch staticBatch;
	SpriteBatch batchMiniMap;
	
	Stage stage;
	Image hud;
	Image floor;
	Helicopter heli;
	
	//Rectangle floor = new Rectangle(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
	
	public static void main(String[] args) {

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Test";
		cfg.width = SCREEN_WIDTH;
		cfg.height = SCREEN_HEIGHT;
		new LwjglApplication(new Main(), cfg);

	}

	public void create() {
		
		AtlasCache.add("copter", "assets/image/wirly-bird.atlas");
		AtlasCache.add("launcher", "assets/image/rocket-launcher.atlas");
		AtlasCache.add("tank", "assets/image/cartoon-tank.atlas");
		AtlasCache.add("jeep", "assets/image/jeep.atlas");
		AtlasCache.add("soldier", "assets/image/soldier.atlas");
		AtlasCache.add("truck", "assets/image/covered-truck.atlas");
		AtlasCache.add("backgrounds", "assets/image/backgrounds.atlas");
		AtlasCache.add("turret", "assets/image/turret.atlas");
		AtlasCache.add("meteor", "assets/image/meteors.atlas");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		stage = new Stage(new ScreenViewport(camera));
		
	    batchMiniMap = new SpriteBatch();
	    
	    staticBatch = new SpriteBatch();
	    
	    TextureRegion tr = new TextureRegion(makeFloor(AtlasCache.get("backgrounds")));
        floor = new Image(tr);
		
		hud = new Image(fillRectangle(SCREEN_WIDTH, 40, Color.DARK_GRAY));
		hud.setY(SCREEN_HEIGHT - 40);
		
		Level l1 = new Level1();
		l1.addObjects(stage);
		
		heli = new Helicopter("copter", AtlasCache.get("copter"), 0.02f, 1f, false);
		heli.setPosition(400, FIELD_HEIGHT);
		stage.addActor(heli);
				
		Explosion ex = new Explosion(460,FIELD_HEIGHT);
		stage.addActor(ex);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(this, heli));

	}
	


	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.x = heli.getX();
		
		//if (heli.hits(floor)) {
		//	heli.checkCrash();
		//}
		
		stage.act();
		stage.draw();
		
		staticBatch.begin();
		floor.draw(staticBatch, 1f);
		hud.draw(staticBatch, .7f);	
		staticBatch.end();
		
		batchMiniMap.begin();
        Array<Actor> actors = stage.getActors();
		for (Actor actor : actors) {
			Object obj = actor.getUserObject();
			if (obj != null && obj instanceof TextureRegion) {
				float x = ((actor.getX() / FIELD_WIDTH) * hud.getWidth());
				float y = ((actor.getY() / SCREEN_HEIGHT) * hud.getHeight()) + SCREEN_HEIGHT - 40;
				TextureRegion tr = (TextureRegion)obj;
				batchMiniMap.draw(tr, x, y);
			}
		}
		batchMiniMap.end();

	}

	public static float yup(float y) {
		return SCREEN_HEIGHT - y;
	}


	public void dispose() {
		
	}
	
	public Texture fillRectangle(int width, int height, Color color) {
		Pixmap pix = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pix.setColor(color);
		pix.fillRectangle(0, 0, width, height);
		Texture t = new Texture(pix);
		pix.dispose();
		return t;
	}
	
	public static Texture makeFloor(TextureAtlas atlas) {
		
		Texture t = null;
		try {
			AtlasRegion ar = (AtlasRegion)atlas.findRegion("ground");
			BufferedImage sheet = ImageIO.read(new File("assets/image/backgrounds.png"));
	
			int h = ar.getRegionHeight();
			int w = ar.getRegionWidth();
			BufferedImage canvas = new BufferedImage(FIELD_WIDTH, h, BufferedImage.TYPE_INT_ARGB);
			BufferedImage sub = sheet.getSubimage(ar.getRegionX(), ar.getRegionY(), w, h);
	
			for (int x = 0; x < FIELD_WIDTH; x+=w) {
				canvas.getGraphics().drawImage(sub,x,0,w,h,null);
			}
			
			Pixmap p = createPixmap(canvas);
			t = new Texture(p);
			p.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return t;
		
	}
	
	public static Pixmap createPixmap(BufferedImage image) {
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGBA8888);
		pix.setColor(0f, 0f, 0f, .45f);
		pix.fillRectangle(0, 0, w, h);
		
		int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int pixel = pixels[y * w + x];
				pix.drawPixel(x, y, getRGBA(pixel));
			}
		}
		
		return pix;	
	}
	 
	public static int getRGBA(int rgb) {
		int a = rgb >> 24;
		a &= 0x000000ff;
		int rest = rgb & 0x00ffffff;
		rest <<= 8;
		rest |= a;
		return rest;
	}

	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.T:
			Tank tank = new Tank("tank", false);
			stage.addActor(tank);
			break;
		case Keys.E:
			Movable engineer = new Movable("engineer", AtlasCache.get("soldier"), 0.10f, 1f, true);
			engineer.setPosition(SPAWN, FIELD_HEIGHT);
			engineer.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
			stage.addActor(engineer);
			break;
		case Keys.I:
			Movable soldier = new Movable("infantry", AtlasCache.get("soldier"), 0.10f, 1f, true);
			soldier.setPosition(SPAWN, FIELD_HEIGHT);
			soldier.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
			stage.addActor(soldier);
			break;
		case Keys.J:
			Movable jeep = new Movable("jeep", AtlasCache.get("jeep"), 0.10f, 1f, true);
			jeep.setPosition(SPAWN, FIELD_HEIGHT);
			jeep.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
			stage.addActor(jeep);
			break;
		case Keys.L:
			Movable launcher = new Movable("launcher-raising", AtlasCache.get("launcher"), 0.05f, 1f, true);
			launcher.setPosition(SPAWN, FIELD_HEIGHT);
			launcher.addAction(Actions.moveTo(FIELD_WIDTH, FIELD_HEIGHT, 160f));
			stage.addActor(launcher);
			break;
		case Keys.SPACE:
			heli.shoot(stage);
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
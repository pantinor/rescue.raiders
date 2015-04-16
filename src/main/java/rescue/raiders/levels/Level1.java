package rescue.raiders.levels;

public class Level1 extends Level {
	
	public Level1() {
		
		this.layout = 
			"base,0:"+
			"pad,500:"+
			"hut,1000:"+
			"turret,2000:"+
			"hut,3000:"+
			"turret,4000:"+
			"hut,5000:"+
			"hut,6000:"+
			"enemy-hut,7000:"+
			"enemy-hut,8000:"+
			"enemy-turret,9000:"+
			"enemy-hut,10000:"+
			"enemy-turret,11000:"+
			"enemy-hut,12000:"+
			"pad,13500:"+
			"enemy-base,13850";
		
		init();
		
	}

}

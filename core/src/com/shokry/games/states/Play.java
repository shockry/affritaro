package com.shokry.games.states;

import static com.shokry.games.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shokry.games.Game;
import com.shokry.games.entities.Bridge;
import com.shokry.games.entities.Bullet;
import com.shokry.games.entities.Crystal;
import com.shokry.games.entities.Gun;
import com.shokry.games.entities.HUD;
import com.shokry.games.entities.Ladder;
import com.shokry.games.entities.MovingPlatform;
import com.shokry.games.entities.Player;
import com.shokry.games.entities.Spike;
import com.shokry.games.entities.Spring;
import com.shokry.games.entities.Tunnel;
import com.shokry.games.entities.enemySnail;
import com.shokry.games.handlers.B2DVars;
import com.shokry.games.handlers.GameContactListener;
import com.shokry.games.handlers.GameInput;
import com.shokry.games.handlers.Background;
import com.shokry.games.handlers.BoundedCamera;
import com.shokry.games.handlers.GameStateManager;

public class Play extends GameState {

	private boolean debug = true;
	
	private static final float WAIT_TIME = 3f;

	public static int crystalNum;
	public static int bulletNum;
	public static float time ;
	public static int health ; //modified in Contact listener

	public static World world;
	private Box2DDebugRenderer b2dRenderer;
	private GameContactListener cl;
	private BoundedCamera b2dCam;

	public static Player player;

	public static boolean facingRight = true;
	public static boolean rightFacingBodyCreated;
	public static boolean leftFacingBodyCreated;

	private TiledMap tileMap;
	private int tileMapWidth;
	private int tileMapHeight;
	private int tileSize;
	private OrthogonalTiledMapRenderer tmRenderer;

	private Array<Crystal> crystals;
	private Array<Spike> spikes;
	private Array<Ladder> ladders;
	private Array<MovingPlatform> movingPlaforms;
	private Array<Bullet> bullets;
	private Array<Gun> guns;
	private Array<Tunnel> tunnels;
	private Array<Bridge> bridges;
	private Array<enemySnail> enemySnails;
	private Array<Spring> springs;

	// Make a platform velocity constant variable
	private boolean platformMovingRight;

	private Background[] backgrounds;
	private HUD hud;

	public static int level;

	public Play(GameStateManager gsm) {

		super(gsm);

		// set up the box2d world and contact listener
		world = new World(new Vector2(0, -7f), true);
		cl = new GameContactListener();
		world.setContactListener(cl);
		b2dRenderer = new Box2DDebugRenderer();
		
		

		//Creating objects from the tile map
		createPlayer();
		createWalls();
		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
		
		createCrystals();
		player.setTotalCrystals(crystals.size);

		createSpikes();
		createGuns();
		createLadders();
		createMovingPlatforms();
		createTunnels();
		//Falling bridges (count down-fall)
		createBridges();
		//Enemy snails
		createSnails();
		//Jumping springs
		createSprings();
		
		// create backgrounds
		Texture bgs = Game.res.getTexture("bgs");
		TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
		TextureRegion clouds = new TextureRegion(bgs, 0, 240, 320, 240);
		TextureRegion mountains = new TextureRegion(bgs, 0, 480, 320, 240);
		backgrounds = new Background[3];
		backgrounds[0] = new Background(sky, cam, 0f);
		backgrounds[1] = new Background(clouds, cam, 0.1f);
		backgrounds[2] = new Background(mountains, cam, 0.2f);

		// create hud
		hud = new HUD(player);

		// set up box2d cam
		b2dCam = new BoundedCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0,
				(tileMapHeight * tileSize) / PPM);
	}

	/**
	 * Creates the player. Sets up the box2d body and sprites.
	 */
	private void createPlayer() {
		bullets = new Array <Bullet>();

		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(60 / PPM, 120 / PPM);
		bdef.fixedRotation = true;

		// create body from bodydef
		Body body = world.createBody(bdef);

		// create box shape for player collision box
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(13 / PPM, 13 / PPM);

		// create fixturedef for player collision box
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1;
		fdef.friction = 0;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_BLOCK | B2DVars.BIT_CRYSTAL
				| B2DVars.BIT_SPIKE | B2DVars.BIT_LADDER | B2DVars.BIT_PLATFORM
				| B2DVars.BIT_GUN | B2DVars.BIT_TUNNEL | B2DVars.BIT_BRIDGE 
				| B2DVars.BIT_SNAIL | B2DVars.BIT_SPRING;

		// create player collision box fixture
		body.createFixture(fdef).setUserData("player");;
		shape.dispose();

		// create box shape for player foot
		shape = new PolygonShape();
		shape.setAsBox(13 / PPM, 3 / PPM, new Vector2(0, -13 / PPM), 0);

		// create fixturedef for player foot
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_BLOCK;;

		// create player foot fixture
		body.createFixture(fdef).setUserData("foot");

		shape.dispose();

		// create new player
		player = new Player(body);
		body.setUserData(player);

		// final tweaks, manually set the player body mass to 1 kg
		MassData md = body.getMassData();
		md.mass = 1;
		body.setMassData(md);
	}

	/**
	 * Sets up the tile map collidable tiles. Reads in tile map layers and sets
	 * up box2d bodies.
	 */
	private void createWalls() {

		// load tile map and map renderer
		try {
			tileMap = new TmxMapLoader()
					.load("res/maps/level" + level + ".tmx");
		} catch (Exception e) {
			System.out.println("Cannot find file: res/maps/level" + level
					+ ".tmx");
			Gdx.app.exit();
		}
		tileMapWidth = (int) tileMap.getProperties()
				.get("width", Integer.class);
		tileMapHeight = (int) tileMap.getProperties().get("height",
				Integer.class);
		tileSize = (int) tileMap.getProperties()
				.get("tilewidth", Integer.class);
		tmRenderer = new OrthogonalTiledMapRenderer(tileMap);

		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		createBlocks(layer, B2DVars.BIT_BLOCK);

	}

	/**
	 * Creates box2d bodies for all non-null tiles in the specified layer and
	 * assigns the specified category bits.
	 * 
	 * @param layer
	 *            the layer being read
	 * @param bits
	 *            category bits assigned to fixtures
	 */
	private void createBlocks(TiledMapTileLayer layer, short bits) {

		// tile size
		float ts = layer.getTileWidth();

		// go through all cells in layer
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {

				// get cell
				Cell cell = layer.getCell(col, row);

				// check that there is a cell
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				// create body from cell
				BodyDef bdef = new BodyDef();
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts
						/ PPM);
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
				v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
				v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
				cs.createChain(v);
				FixtureDef fd = new FixtureDef();
				fd.friction = 0.3f;
				fd.shape = cs;
				fd.filter.categoryBits = bits;
				fd.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_SNAIL | B2DVars.BIT_SPRING;
				world.createBody(bdef).createFixture(fd);
				cs.dispose();

			}
		}

	}

	/**
	 * Set up box2d bodies for crystals in tile map "crystals" layer
	 */
	private void createCrystals() {
		// create list of crystals
		crystals = new Array<Crystal>();

		// get all crystals in "crystals" layer,
		// create bodies for each, and add them
		// to the crystals list
		MapLayer ml = tileMap.getLayers().get("crystals");
		if (ml == null) {
			return;
		}

		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			float x = 0, y = 0;
			if (mo instanceof EllipseMapObject) {
				Ellipse e = ((EllipseMapObject) mo).getEllipse();
				x = e.x / PPM;
				y = e.y / PPM;
			}
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x;
				y = r.y;
			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();
			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / PPM);
			cfdef.shape = cshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("crystal");
			Crystal c = new Crystal(body);
			body.setUserData(c);
			crystals.add(c);
			cshape.dispose();

		}
	}

	/**
	 * Set up box2d bodies for spikes in "spikes" layer
	 */
	private void createSpikes() {

		spikes = new Array<Spike>();

		MapLayer ml = tileMap.getLayers().get("spikes");
		if (ml == null)
			return;

		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			float x = 0, y = 0;
			if (mo instanceof EllipseMapObject) {
				Ellipse e = ((EllipseMapObject) mo).getEllipse();
				x = e.x / PPM;
				y = e.y / PPM;
			}
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x;
				y = r.y;

			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();
			CircleShape cshape = new CircleShape();
			cshape.setRadius(5 / PPM);
			cfdef.shape = cshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_SPIKE;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("spike");
			Spike s = new Spike(body);
			body.setUserData(s);
			spikes.add(s);
			cshape.dispose();
		}
	}

	/**
	 * Set up box2d bodies for ladders in "ladder" layer
	 */
	private void createLadders() {

		ladders = new Array<Ladder>();

		MapLayer ml = tileMap.getLayers().get("ladder");
		if (ml == null)
			return;

		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			float x = 0, y = 0;
			float w = 0, h = 0;
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x / PPM;
				y = (r.y * 2) / PPM;

				w = r.width;
				h = r.height;
			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();

			PolygonShape pshape = new PolygonShape();
			pshape.setAsBox((w / 2) / PPM, (h / 4) / PPM);

			cfdef.shape = pshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_LADDER;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER ;//| B2DVars.BIT_BULLET;
			body.createFixture(cfdef).setUserData("ladder");
			Ladder s = new Ladder(body);
			body.setUserData(s);
			ladders.add(s);

			pshape.dispose();
		}
	}

	/**
	 * Set up box2d bodies for moving platforms in "platform" layer
	 */
	private void createMovingPlatforms() {

		movingPlaforms = new Array<MovingPlatform>();

		MapLayer ml = tileMap.getLayers().get("platform");
		if (ml == null)
			return;

		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.KinematicBody;
			float x = 0, y = 0, w, h;
			Rectangle r = ((RectangleMapObject) mo).getRectangle();
			x = r.x / PPM;
			y = (r.y) / PPM;
			w = 70f / 2;
			h = 70f / 2;

			PolygonShape pshape = new PolygonShape();
			pshape.setAsBox((w) / PPM, (h + 2f) / PPM);
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);

			FixtureDef cfdef = new FixtureDef();
			cfdef.shape = pshape;
			cfdef.filter.categoryBits = B2DVars.BIT_PLATFORM;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("movingPlatform");
			MovingPlatform s = new MovingPlatform(body);
			s.setCoordinates(x, y);
			body.setUserData(s);
			movingPlaforms.add(s);
			pshape.dispose();
		}

	}

	/**
	 * Set up box2d bodies for guns in "guns" layer
	 */
	private void createGuns() {
		
		guns = new Array<Gun>();

		MapLayer ml = tileMap.getLayers().get("gun");
		if (ml == null){
			return;
		}
			
		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			float x = 0, y = 0, w = 0, h = 0;
			
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x / PPM;
				y = (r.y) / PPM;
				w = Game.res.getTexture("raygun").getWidth()/2;
				h = Game.res.getTexture("raygun").getHeight()/2;
			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();

			PolygonShape pshape = new PolygonShape();
			pshape.setAsBox((w / 2) / PPM, (h / 2) / PPM);

			cfdef.shape = pshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_GUN;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("gun");
			Gun s = new Gun(body);
			body.setUserData(s);
			guns.add(s);
			pshape.dispose();
		}
	}

	/**
	 * Set up box2d bodies for tunnels in "tunnel" layer
	 */
	private void createTunnels() {
		
		tunnels = new Array<Tunnel>();

		MapLayer ml = tileMap.getLayers().get("tunnel");
		if (ml == null){
			return;
		}
			
		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			float x = 0, y = 0, w = 0, h = 0;
			
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x / PPM;
				y = (r.y) / PPM;
				w = Game.res.getTexture("tunnel").getWidth();
				h = Game.res.getTexture("tunnel").getHeight();
			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();

			PolygonShape pshape = new PolygonShape();
			pshape.setAsBox((w / 2) / PPM, (h / 2) / PPM);

			cfdef.shape = pshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_TUNNEL;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("tunnel");
			Tunnel s = new Tunnel(body);
			body.setUserData(s);
			tunnels.add(s);
			pshape.dispose();
		}
	}
	
	/**
	 * Set up box2d bodies for bridges in "bridge" layer
	 */
	private void createBridges() {
		
		bridges = new Array<Bridge>();

		MapLayer ml = tileMap.getLayers().get("bridge");
		if (ml == null){
			return;
		}
			
		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.KinematicBody;
			float x = 0, y = 0, w = 0, h = 0;
			
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x / PPM;
				y = (r.y) / PPM;
				w = Game.res.getTexture("bridge").getWidth();
				h = Game.res.getTexture("bridge").getHeight();
			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();

			PolygonShape pshape = new PolygonShape();
			pshape.setAsBox((w / 2) / PPM, (h/2) / PPM);

			cfdef.shape = pshape;
			cfdef.filter.categoryBits = B2DVars.BIT_BRIDGE;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("bridge");
			Bridge s = new Bridge(body);
			body.setUserData(s);
			bridges.add(s);
			pshape.dispose();
		}
	}
	
	/**
	 * Set up box2d bodies for snail enemies in "enemySnail" layer
	 */
	private void createSnails() {
		
		enemySnails = new Array<enemySnail>();

		MapLayer ml = tileMap.getLayers().get("enemySnail");
		if (ml == null){
			return;
		}
			
		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.DynamicBody;
			float x = 0, y = 0, w = 0, h = 0;
			
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x / PPM;
				y = (r.y) / PPM;
				w = 54;
				h = 31;
			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();

			PolygonShape pshape = new PolygonShape();
			pshape.setAsBox((w / 2) / PPM, (h/2) / PPM);

			cfdef.shape = pshape;
			cfdef.filter.categoryBits = B2DVars.BIT_SNAIL;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BLOCK | B2DVars.BIT_BULLET;
			body.createFixture(cfdef).setUserData("snail");
			pshape.dispose();
			
			pshape = new PolygonShape();
			pshape.setAsBox(5 / PPM, (h/2) / PPM, new Vector2((float) -0.25, 2 / PPM), 0);

			// create fixturedef for snail head
			cfdef.shape = pshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_SNAIL;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;

			// create snail head fixture
			body.createFixture(cfdef).setUserData("snailHead");

			pshape.dispose();
			
			enemySnail s = new enemySnail(body);
			body.setUserData(s);
			enemySnails.add(s);

		}
	}
	
	
	/**
	 * Set up box2d bodies for springs in "spring" layer
	 */
	private void createSprings() {
		
		springs = new Array<Spring>();

		MapLayer ml = tileMap.getLayers().get("spring");
		if (ml == null){
			return;
		}
			
		for (MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.DynamicBody;
			float x = 0, y = 0, w = 0, h = 0;
			
			if (mo instanceof RectangleMapObject) {
				Rectangle r = ((RectangleMapObject) mo).getRectangle();
				x = r.x / PPM;
				y = (r.y) / PPM;
				w = Game.res.getTexture("springboardDown").getWidth();
				h = Game.res.getTexture("springboardDown").getHeight();
			}
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();

			PolygonShape pshape = new PolygonShape();
			pshape.setAsBox((w / 2) / PPM, (h/2) / PPM);

			cfdef.shape = pshape;
			
			cfdef.filter.categoryBits = B2DVars.BIT_SPRING;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BLOCK;
			body.createFixture(cfdef).setUserData("spring");
			pshape.dispose();
			
			pshape = new PolygonShape();
			pshape.setAsBox((w/2) / PPM, 3 / PPM, new Vector2(0, 17 / PPM), 0);

			// create fixturedef for snail head
			cfdef.shape = pshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_SPRING;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;

			body.createFixture(cfdef).setUserData("springTop");

			pshape.dispose();
			
			Spring s = new Spring(body);
			body.setUserData(s);
			springs.add(s);
		}
	}
	
	
	/**
	 * Apply upward force to player body.
	 */
	private void playerJump() {
		System.out.println(cl.playerCanJump());
		if (cl.playerCanJump()>0) {
			player.getBody().setLinearVelocity(
					player.getBody().getLinearVelocity().x, 0);
			player.getBody().applyForceToCenter(0, 200, true);
			Game.res.getSound("jump").play();
		}
	}

	public void handleInput() {

		// keyboard input
		if (GameInput.isPressed(GameInput.BTNJMP)) {
			playerJump();
		}

		if (GameInput.isDown(GameInput.BTNRIGHT)) {
			moveRight(true, true);
		}
		if (GameInput.isMvReleased(GameInput.BTNRIGHT)) {
			moveRight(false, true);
		}

		if (GameInput.isDown(GameInput.BTNLEFT)) {
			moveRight(true, false);
		}
		if (GameInput.isMvReleased(GameInput.BTNLEFT)) {
			moveRight(false, false);
		}

		if (GameInput.isDown(GameInput.BTNUP)) {
			climb(true);
		}
		if (GameInput.isClimbPressed(GameInput.BTNUP)) {
			climb(false);
		}

		if (GameInput.isPressed(GameInput.BTNFIRE)) {
			if(bulletNum>0)
			fire();
		}

		// mouse/touch input for Android
		// left side of screen to switch blocks
		// right side of screen to jump
		
		if (GameInput.isDown()) {
			if (GameInput.x < Gdx.graphics.getWidth() / 2) {
				moveRight(true, false);
			} else {
				moveRight(true, true);
			}
			
		} if(GameInput.isPressed()){
			if (GameInput.y < Gdx.graphics.getHeight() / 2){
				playerJump();
		}
			
		}

	}

	private void fire() {
		
		BodyDef cdef = new BodyDef();
		cdef.type = BodyType.DynamicBody;
		float x = 0, y = 0;
		
		x = player.getBody().getPosition().x;
		y = player.getBody().getPosition().y;

		cdef.position.set(x, y);
		Body body = world.createBody(cdef);
		FixtureDef cfdef = new FixtureDef();

		CircleShape pshape = new CircleShape();
		pshape.setRadius((Game.res.getTexture("bullet").getWidth() / 2) / PPM);

		cfdef.shape = pshape;
		cfdef.filter.categoryBits = B2DVars.BIT_BULLET;
		cfdef.filter.maskBits = B2DVars.BIT_SNAIL | B2DVars.BIT_BLOCK;
		body.createFixture(cfdef).setUserData("bullet");
		Bullet bullet = new Bullet(body);
		body.setUserData(bullet);
		if(facingRight)
		body.setLinearVelocity(2f, 0);
		else body.setLinearVelocity(-2f, 0);
		body.setGravityScale(0f);
		bullets.add(bullet);
		pshape.dispose();
		bulletNum--;
		Game.res.getSound("WeaponBlow").play();
	}

	private void climb(boolean upArrowPressed) {
		if (cl.isPlayerClimb()) {
			if (upArrowPressed) {
				player.getBody().setActive(true);
				player.getBody().setLinearVelocity(
						player.getBody().getLinearVelocity().x, 1f);
			} else {
				player.getBody().setActive(false);
			}
		}

	}

	private void moveRight(boolean pressed, boolean right) {

		if (pressed) {
			player.getBody().setActive(true);
			if (right) {
				player.getBody().setLinearVelocity(1f,
						player.getBody().getLinearVelocity().y);
				facingRight = true;
				if (!rightFacingBodyCreated && !cl.isPlayerClimb()) {
					player = new Player(player.getBody());
					rightFacingBodyCreated = true;
					leftFacingBodyCreated = false;
				}
			} else {
				player.getBody().setLinearVelocity(-1f,
						player.getBody().getLinearVelocity().y);
				facingRight = false;
				if (!leftFacingBodyCreated && !cl.isPlayerClimb()) {
					player = new Player(player.getBody());
					leftFacingBodyCreated = true;
					rightFacingBodyCreated = false;
				}
			}
		} else if (GameInput.isMvReleased(GameInput.BTNLEFT)
				&& GameInput.isMvReleased(GameInput.BTNRIGHT)) {
			if (!cl.isPlayerOnPlatform())
				player.getBody().setLinearVelocity(0f,
						player.getBody().getLinearVelocity().y);
			player.animatePlayer(true);
		}
	}

	public void update(float dt) {

		// check input
		handleInput();

		// update box2d world
		world.step(Game.STEP, 1, 1);
	
		
		if(cl.isPlayerOnBridge()){
			time += dt;
			if(time>=WAIT_TIME){
			cl.getCurrentBridge().setLinearVelocity(0, -3f);
			// Reset timer (not set to 0)
			time -= WAIT_TIME;
			}
		} 
		
		// check for collected crystals
		Array<Body> bodies = cl.getBodies();
		 
		for (int i = 0; i < bodies.size; i++) {
			Body b = bodies.get(i);
			if(b.getFixtureList().first().getUserData().equals("crystal")){
			crystals.removeValue((Crystal) b.getUserData(), true);
			world.destroyBody(bodies.get(i));
			crystalNum++;
			Game.res.getSound("crystal").play();
			} else if(b.getFixtureList().first().getUserData().equals("bullet")){
				bullets.removeValue((Bullet) b.getUserData(), true);
				world.destroyBody(bodies.get(i));
			}
			else if(b.getFixtureList().first().getUserData().equals("gun")){
				guns.removeValue((Gun) b.getUserData(), true);
				world.destroyBody(bodies.get(i));
			}
			else if(b.getFixtureList().first().getUserData().equals("snail")){
				enemySnails.removeValue((enemySnail) b.getUserData(), true);
				world.destroyBody(bodies.get(i));
				Game.res.getSound("enemyHit").play();
			}
		}
		bodies.clear();

		// update player
		player.update(dt);
		
		if(bullets != null)
			for (int i = 0; i < bullets.size; i++) {
				bullets.get(i).update(dt);
			}

		// check player win
		if (player.getBody().getPosition().x * PPM > tileMapWidth * tileSize) {
			Game.res.getSound("levelselect").play();
			gsm.setState(GameStateManager.LEVEL_SELECT);
		}

		// check player failed
		if (player.getBody().getPosition().y < 0) {
			Game.res.getSound("hit").play();
			gsm.setState(GameStateManager.LEVEL_SELECT);
		}
		if (cl.isPlayerDead()) {
			Game.res.getSound("hit").play();
			gsm.setState(GameStateManager.LEVEL_SELECT);
		}
		if (cl.isPlayerOnPlatform()) {
			if (platformMovingRight == false) {
				player.getBody().setLinearVelocity(-.5f,
						player.getBody().getLinearVelocity().y);
			} else {
				player.getBody().setLinearVelocity(.5f,
						player.getBody().getLinearVelocity().y);
			}
		}
		
		if (cl.isPlayerOnSpring()) {		
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x,
					4f);
		}
		
		//Check if player is in a tunnel
		if(cl.isPlayerInTunnel()) {
			player.getBody().setTransform(player.getBody().getPosition().x+2f,
					player.getBody().getPosition().y, player.getBody().getAngle());
		}

		// update crystals
		for (int i = 0; i < crystals.size; i++) {
			crystals.get(i).update(dt);
		}

		// update spikes
		for (int i = 0; i < spikes.size; i++) {
			spikes.get(i).update(dt);
		}

		// update ladders
		for (int i = 0; i < ladders.size; i++) {
			ladders.get(i).update(dt);
		}

		// update moving platforms
		for (int i = 0; i < movingPlaforms.size; i++) {
			movingPlaforms.get(i).update(dt);
			if (movingPlaforms.get(i).getPosition().x > movingPlaforms.get(i)
					.getCoordinates().x + .5) {
				movingPlaforms.get(i).getBody().setLinearVelocity(-.5f, 0);
				platformMovingRight = false;
			} else if (movingPlaforms.get(i).getPosition().x <= movingPlaforms
					.get(i).getCoordinates().x) {
				movingPlaforms.get(i).getBody().setLinearVelocity(.5f, 0);
				platformMovingRight = true;
			}
		}
		
		// update guns
		for (int i = 0; i < guns.size; i++) {
			guns.get(i).update(dt);
		}
		
		// update tunnels
		for (int i = 0; i < tunnels.size; i++) {
			tunnels.get(i).update(dt);
		}

		// update bridges
		for (int i = 0; i < bridges.size; i++) {
			bridges.get(i).update(dt);
		}
		// update jumping springs
		for (int i = 0; i < springs.size; i++) {
			springs.get(i).update(dt);
		}
		
		// update snails
		for (int i = 0; i < enemySnails.size; i++) {
			enemySnails.get(i).update(dt);
		}
	}

	public void render() {

		// camera to follow player
		cam.setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4,
				Game.V_HEIGHT / 2);
		cam.update();

		// draw bgs
		sb.setProjectionMatrix(hudCam.combined);
		for (int i = 0; i < backgrounds.length; i++) {
			backgrounds[i].render(sb);
		}

		// draw tilemap
		tmRenderer.setView(cam);
		tmRenderer.render();

		// Set camera
		sb.setProjectionMatrix(cam.combined);

		// draw ladders
		for (int i = 0; i < ladders.size; i++) {
			ladders.get(i).render(sb);
		}

		// draw player
		player.render(sb);
		if (bullets !=null)
		for (int i = 0; i < bullets.size; i++) {
			bullets.get(i).render(sb);
		}

		// draw crystals
		for (int i = 0; i < crystals.size; i++) {
			crystals.get(i).render(sb);
		}

		// draw spikes
		for (int i = 0; i < spikes.size; i++) {
			spikes.get(i).render(sb);
		}

		// draw moving platforms
		for (int i = 0; i < movingPlaforms.size; i++) {
			movingPlaforms.get(i).render(sb);
		}
		
		// draw guns
		for (int i = 0; i < guns.size; i++) {
			guns.get(i).render(sb);
		}
		
		// draw tunnels
		for (int i = 0; i < tunnels.size; i++) {
			tunnels.get(i).render(sb);
		}

		// draw bridges
		for (int i = 0; i < bridges.size; i++) {
			bridges.get(i).render(sb);
		}
		// draw jumping springs
		for (int i = 0; i < springs.size; i++) {
			springs.get(i).render(sb);
		}
		
		// draw Snails
		for (int i = 0; i < enemySnails.size; i++) {
			enemySnails.get(i).render(sb);
		}
		
		// draw hud
		sb.setProjectionMatrix(hudCam.combined);
		hud.render(sb);

		// debug draw box2d
		if (debug) {
			b2dCam.setPosition(player.getPosition().x + Game.V_WIDTH / 4 / PPM,
					Game.V_HEIGHT / 2 / PPM);
			b2dCam.update();
			b2dRenderer.render(world, b2dCam.combined);
		}

	}

	public void dispose() {
		// everything is in the resource manager
		// com.shokry.games.handlers.Content
	}

}
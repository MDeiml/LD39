package com.mdeiml.ld39;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;

public class LD39Game extends ApplicationAdapter {

	public static final float TILE_SIZE = 48;
	public static final float UPDATE_DELTA = 1/60f;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private ArrayList<Entity> entities;
	private Robot player;

	// Box2d
	private float unprocessed;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	public Texture sprites;
    private TextureRegion batteryFull;
    private TextureRegion batteryEmpty;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / TILE_SIZE, Gdx.graphics.getHeight() / TILE_SIZE);
		entities = new ArrayList<Entity>();

		Box2D.init();
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new ContactListener() {
			public void beginContact(Contact contact) {
				Entity a = (Entity)contact.getFixtureA().getBody().getUserData();
				Entity b = (Entity)contact.getFixtureB().getBody().getUserData();
				a.addTouchingEntity(b);
				b.addTouchingEntity(a);
			}
			public void endContact(Contact contact) {
				Entity a = (Entity)contact.getFixtureA().getBody().getUserData();
				Entity b = (Entity)contact.getFixtureB().getBody().getUserData();
				a.removeTouchingEntity(b);
				b.removeTouchingEntity(a);
			}
			public void postSolve(Contact contact, ContactImpulse impulse) {}
			public void preSolve(Contact contact, Manifold manifold) {
				if(!contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor() &&
				   contact.getFixtureA().getBody().getType() == BodyDef.BodyType.DynamicBody &&
				   contact.getFixtureB().getBody().getType() == BodyDef.BodyType.DynamicBody) {
					contact.getFixtureA().getBody().setLinearVelocity(new Vector2(0,0));
					contact.getFixtureB().getBody().setLinearVelocity(new Vector2(0,0));
				}
			}
		});
		debugRenderer = new Box2DDebugRenderer();

		sprites = new Texture("sprites.png");
        batteryFull = new TextureRegion(sprites, 32, 0, 6, 64);
        batteryEmpty = new TextureRegion(sprites, 38, 0, 6, 64);

		Texture testTex = new Texture("badlogic.jpg");
		player = new Robot(new Vector2(0,0), this, new TextureRegion(testTex));
		new StandardEnemy(new Vector2(-3, 0), this);
	}

	@Override
	public void render () {
		// Update
		unprocessed += Gdx.graphics.getDeltaTime();
		while(unprocessed >= UPDATE_DELTA) {
			int size = entities.size();
			for(int i = 0; i < size; i++) {
				entities.get(i).update();
			}
			for(int i = 0; i < entities.size(); i++) {
				if(entities.get(i).isDead()) {
					world.destroyBody(entities.get(i).getBody());
					entities.remove(i);
					i--;
				}
			}
			world.step(UPDATE_DELTA, 6, 2);
			unprocessed -= UPDATE_DELTA;
		}

		// Render
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		debugRenderer.render(world, camera.combined);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int i = entities.size()-1; i >= 0; i--) {
			entities.get(i).render(batch);
		}
		batch.draw(batteryEmpty, 0.5f-Gdx.graphics.getWidth()/TILE_SIZE/2, 1-Gdx.graphics.getHeight()/TILE_SIZE/2, 6/16f, 64/16f);
		float energy = player.getEnergy()/100;
		int batHeight = (int)(energy * batteryFull.getRegionHeight());
		batch.draw(
			new TextureRegion(batteryFull, 0, batteryFull.getRegionHeight()-batHeight, 6, batHeight),
			0.5f-Gdx.graphics.getWidth()/TILE_SIZE/2,
			1-Gdx.graphics.getHeight()/TILE_SIZE/2,
			6/16f,
			batHeight/16f
		);
		batch.end();
	}

	public World getWorld() {
		return world;
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void wakeAll() {
		for(Entity e : entities) {
			e.getBody().setAwake(true);
		}
	}

	public Vector2 getMousePos() {
		Vector2 pos = new Vector2(Gdx.input.getX()-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2-Gdx.input.getY());
		pos.scl(1/TILE_SIZE);
		return pos;
	}

	public Robot getPlayer() {
		return player;
	}

}

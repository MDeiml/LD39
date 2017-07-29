package com.mdeiml.ld39;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class LD39Game extends ApplicationAdapter {

	public static final float TILE_SIZE = 32;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private ArrayList<Entity> entities;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / TILE_SIZE, Gdx.graphics.getHeight() / TILE_SIZE);
		entities = new ArrayList<Entity>();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(batch);
		}
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i).isDead()) {
				entities.remove(i);
				i--;
			}
		}
		batch.end();
	}
}

package com.mdeiml.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import static com.badlogic.gdx.Input.Keys;

public class Robot extends Entity {

    private static final float ENERGY_MAX = 100;
    private static final float HEALTH_MAX = 100;
    private static final float SPEED = 4;

    private float energy;
    private float health;

    public Robot(Vector2 position, World world, TextureRegion sprite) {
        super(position, new Vector2(1, 1), world, sprite);
        this.energy = ENERGY_MAX;
        this.health = HEALTH_MAX;
    }

    public void update() {
        Vector2 dir = new Vector2(0,0);

        if(Gdx.input.isKeyPressed(Keys.W)) {
            dir = dir.add(new Vector2(0, 1));
        }
        if(Gdx.input.isKeyPressed(Keys.A)) {
            dir = dir.add(new Vector2(-1, 0));
        }
        if(Gdx.input.isKeyPressed(Keys.S)) {
            dir = dir.add(new Vector2(0, -1));
        }
        if(Gdx.input.isKeyPressed(Keys.D)) {
            dir = dir.add(new Vector2(1, 0));
        }

        System.out.println(dir);
        move(dir);

        health = Math.min(HEALTH_MAX, health);
        energy = Math.min(ENERGY_MAX, energy);
        if(energy <= 0 || health <= 0) {
            die();
        }
    }

}

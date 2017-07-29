package com.mdeiml.ld39;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Robot extends Entity {

    private static final float ENERGY_MAX = 100;
    private static final float HEALTH_MAX = 100;

    private float energy;
    private float health;

    public Robot(Vector2 position, TextureRegion sprite) {
        super(position, sprite);
        this.energy = ENERGY_MAX;
        this.health = HEALTH_MAX;
    }

    public void render(SpriteBatch batch) {
        health = Math.min(HEALTH_MAX, health);
        energy = Math.min(ENERGY_MAX, energy);
        if(energy <= 0 || health <= 0) {
            die();
        }
        super.render(batch);
    }

}

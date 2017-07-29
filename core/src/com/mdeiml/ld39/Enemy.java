package com.mdeiml.ld39;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Entity {

    private final float maxHealth;
    private float health;

    public Enemy(Vector2 position, Vector2 size, LD39Game game, TextureRegion sprite, float maxHealth) {
        super(position, size, game, sprite);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public void update() {
        health = Math.min(maxHealth, health);
        if(health <= 0) {
            die();
        }
        super.update();
    }

}

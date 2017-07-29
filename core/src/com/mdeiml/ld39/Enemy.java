package com.mdeiml.ld39;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Entity {

    private final int maxHealth;
    private int health;

    public Enemy(Vector2 position, Vector2 size, LD39Game game, TextureRegion sprite, int maxHealth) {
        super(position, size, game, sprite);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        getBody().setLinearDamping(15);
    }

    public void update() {
        health = Math.min(maxHealth, health);
        if(health <= 0) {
            die();
        }
        super.update();
    }

    public void damage(int d) {
        health -= d;
    }

}

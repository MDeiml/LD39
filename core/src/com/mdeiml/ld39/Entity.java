package com.mdeiml.ld39;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    private Vector2 position;
    private TextureRegion sprite;
    private boolean dead;

    public Entity(Vector2 position, TextureRegion sprite) {
        this.position = position;
        this.sprite = sprite;
        this.dead = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(sprite, position.x, position.y);
    }

    public void die() {
        this.dead = true;
    }

    public boolean isDead() {
        return this.dead;
    }

}

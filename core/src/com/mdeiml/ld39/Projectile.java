package com.mdeiml.ld39;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Projectile extends Entity {

    private final int damage;

    public Projectile(Vector2 pos, LD39Game game, TextureRegion sprite, int damage, Vector2 vel) {
        super(pos, new Vector2(1, 1), game, sprite, BodyDef.BodyType.KinematicBody, true);
        this.damage = damage;
        getBody().setLinearVelocity(vel);
    }

    public void update() {
        Robot r = isTouching(Robot.class);
        if(r != null) {
            r.damage(damage);
            die();
        }
        super.update();
    }

}

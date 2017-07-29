package com.mdeiml.ld39;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {

    private Vector2 size;
    private TextureRegion sprite;
    private boolean dead;
    private Body body;

    public Entity(Vector2 position, Vector2 size, World world, TextureRegion sprite) {
        this.size = new Vector2(size);
        this.sprite = sprite;
        this.dead = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x/2, size.y/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void update() {

    }

    public void render(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        batch.draw(sprite, pos.x - size.x/2, pos.y - size.y/2, size.x, size.y);
    }

    public void move(Vector2 dir) {
        System.out.println(dir);
        body.setLinearVelocity(dir);
    }

    public void die() {
        this.dead = true;
    }

    public boolean isDead() {
        return this.dead;
    }

    public Body getBody() {
        return body;
    }

}

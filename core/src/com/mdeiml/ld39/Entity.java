package com.mdeiml.ld39;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;

public abstract class Entity {

    private Vector2 size;
    private TextureRegion sprite;
    private boolean dead;
    private Body body;
    private LD39Game game;
    private ArrayList<Entity> touchingEntities;

    public Entity(Vector2 position, Vector2 size, LD39Game game, TextureRegion sprite) {
        this(position, size, game, sprite, BodyDef.BodyType.DynamicBody, false);
    }

    public Entity(Vector2 position, Vector2 size, LD39Game game, TextureRegion sprite, BodyDef.BodyType bodyType, boolean isSensor) {
        this.size = new Vector2(size);
        this.sprite = sprite;
        this.dead = false;
        this.game = game;
        this.touchingEntities = new ArrayList<Entity>();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = game.getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x/2, size.y/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = isSensor;

        body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(this);

        game.addEntity(this);
        game.wakeAll();
    }

    public void update() {

    }

    public void render(SpriteBatch batch) {
        Vector2 pos = getPosition();
        batch.draw(sprite, pos.x - size.x/2, pos.y - size.y/2, size.x, size.y);
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

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public LD39Game getGame() {
        return game;
    }

    public void addTouchingEntity(Entity e) {
        touchingEntities.add(e);
    }

    public void removeTouchingEntity(Entity e) {
        while(touchingEntities.remove(e)) {}
    }

    public <T extends Entity> T isTouching(Class<T> type) {
        for(Entity e : touchingEntities) {
            if(type.isAssignableFrom(e.getClass())) {
                return (T)e;
            }
        }
        return null;
    }

    public <T extends Entity> ArrayList<T> isTouchingAll(Class<T> type) {
        ArrayList<T> res = new ArrayList<T>();
        for(Entity e : touchingEntities) {
            if(type.isAssignableFrom(e.getClass())) {
                res.add((T)e);
            }
        }
        return res;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

}

package com.mdeiml.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import static com.badlogic.gdx.Input.Keys;

public class Robot extends Enemy {

    private static final float SPEED = 4;
    private static final float SPEED_BATTERY = 2;
    private static final float ACC_FORCE = 80;
    private static final float STOP_DAMP = 15;

    private static final float DASH_COST = 10;
    private static final float DASH_SPEED = 30;
    private static final float DASH_DURATION = 0.20f;
    private static final float DASH_STOP = 0.05f;

    private static final float ENERGY_MAX = 100;
    private static final float HEALTH_MAX = 100;
    private static final float ENERGY_DECAY = 8;

    private float energy;
    private boolean hasBattery;
    private float dashTimer;

    public Robot(Vector2 position, LD39Game game, TextureRegion sprite) {
        super(position, new Vector2(1, 1), game, sprite, HEALTH_MAX);
        this.energy = ENERGY_MAX;
        this.hasBattery = true;
        this.dashTimer = 0;
    }

    public void update() {
        // Movement
        if(Gdx.input.isKeyJustPressed(Keys.SPACE) && dashTimer <= 0 && !hasBattery) {
            energy -= DASH_COST;
            dashTimer = DASH_DURATION;
            Vector2 dir = getGame().getMousePos().sub(getPosition()).nor();
            getBody().setLinearVelocity(dir.scl(DASH_SPEED));
            getBody().setLinearDamping(1);
        }
        if(dashTimer <= 0 || hasBattery) {
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
            if(dir.isZero()) {
                getBody().setLinearDamping(STOP_DAMP);
            }else {
                getBody().setLinearDamping(0);
                getBody().applyForceToCenter(dir.scl(ACC_FORCE), true);
                Vector2 vel = getBody().getLinearVelocity();
                float l = vel.len();
                float speed = hasBattery ? SPEED_BATTERY : SPEED;
                if(l > speed) {
                    getBody().setLinearVelocity(vel.scl(speed/l));
                }
            }
        }else {
            if(dashTimer < DASH_STOP) {
                getBody().setLinearDamping(STOP_DAMP);
            }
            dashTimer -= LD39Game.UPDATE_DELTA;
        }

        // Health and Energy
        Entity battery = isTouching(Battery.class);
        if(Gdx.input.isKeyJustPressed(Keys.E)) {
            if(hasBattery) {
                new Battery(getPosition(), getGame());
                hasBattery = false;
            }else {
                if(battery != null) {
                    hasBattery = true;
                    battery.die();
                }
            }
        }
        if(hasBattery || battery != null) {
            energy = ENERGY_MAX;
        }else {
            energy -= LD39Game.UPDATE_DELTA * ENERGY_DECAY;
        }
        energy = Math.min(ENERGY_MAX, energy);
        System.out.println(energy);
        if(energy <= 0) {
            die();
        }
        super.update();
    }

}

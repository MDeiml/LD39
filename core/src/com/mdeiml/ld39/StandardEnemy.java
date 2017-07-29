package com.mdeiml.ld39;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StandardEnemy extends LivingEntity {

    private static final float ACC_FORCE = 60;
    private static final float SPEED = 1.9f;
    private static final float STOP_DAMP = 15;

    private static final float TRIGGER_RANGE = 5;
    private static final float FOLLOW_RANGE = 4;

    private static final int ACTION_SHOOT = 1;
    private static final int ACTION_SHOOT_1 = 2;
    private static final float DURATION_SHOOT = 1;
    private static final float SHOOT_OFFSET = 0.5f;
    private static final float PROJECTILE_SPEED = 4;
    private static final float SHOOT_COOLDOWN = 4;

    private static final int ACTION_STOP = 3;
    private static final float DURATION_STOP = 1;

    private boolean triggered;
    private float actionTimer;
    private int actionMode;
    private float shootCooldown;

    public StandardEnemy(Vector2 pos, LD39Game game) {
        super(pos, new Vector2(1, 1), game, new TextureRegion(new Texture("badlogic.jpg")), 2);
        this.triggered = false;
        this.actionTimer = 0;
        this.shootCooldown = 0;
    }

    public void update() {
        Vector2 dir = getGame().getPlayer().getPosition().sub(getPosition());
        float dist = dir.len();
        if(dist < TRIGGER_RANGE) {
            triggered = true;
        }
        getBody().setLinearDamping(STOP_DAMP);
        if(triggered) {
            shootCooldown -= LD39Game.UPDATE_DELTA;
            shootCooldown = Math.max(0, shootCooldown);
            if(actionTimer <= 0) {
                if(dist > FOLLOW_RANGE) {
                    getBody().setLinearDamping(0);
                    getBody().applyForceToCenter(dir.scl(ACC_FORCE/dist), true);
                    Vector2 vel = getBody().getLinearVelocity();
                    float l = vel.len();
                    if(l > SPEED) {
                        getBody().setLinearVelocity(vel.scl(SPEED/l));
                    }
                }else {
                    if(shootCooldown <= 0) {
                        actionMode = ACTION_SHOOT;
                        actionTimer = DURATION_SHOOT;
                        shootCooldown = SHOOT_COOLDOWN;
                    }else {
                        actionMode = ACTION_STOP;
                        actionTimer = DURATION_STOP;
                    }
                }
            }
            if(actionTimer > 0) {
                switch(actionMode) {
                    case ACTION_SHOOT:
                        if(actionTimer < DURATION_SHOOT - SHOOT_OFFSET) {
                            actionMode = ACTION_SHOOT_1;
                            new Projectile(getPosition(), getGame(), new TextureRegion(new Texture("badlogic.jpg")), 1, dir.scl(PROJECTILE_SPEED/dist));
                        }
                        break;
                }
                actionTimer -= LD39Game.UPDATE_DELTA;
            }
        }
        super.update();
    }

}

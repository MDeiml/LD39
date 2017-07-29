package com.mdeiml.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import java.util.ArrayList;
import static com.badlogic.gdx.Input.Buttons;
import static com.badlogic.gdx.Input.Keys;

public class Robot extends LivingEntity {

    private static final float SPEED = 4;
    private static final float SPEED_BATTERY = 2;
    private static final float ACC_FORCE = 80;
    private static final float STOP_DAMP = 15;

    private static final float DASH_COST = 10;
    private static final float DASH_SPEED = 30;
    private static final float DASH_DURATION = 0.20f;
    private static final float DASH_STOP = 0.05f;

    private static final float HIT_COST = 10;
    private static final int HIT_DAMAGE = 1;

    private static final float ENERGY_MAX = 100;
    private static final int HEALTH_MAX = 5;
    private static final float ENERGY_DECAY = 8;

    private float energy;
    private boolean hasBattery;
    private float dashTimer;
    private Animation animation;
    private Animation animationBattery;
    private float animationTimer;

    public Robot(Vector2 position, LD39Game game, TextureRegion sprite) {
        super(position, new Vector2(1, 1), game, new TextureRegion(game.sprites, 0, 0, 16, 16), HEALTH_MAX);
        animation = new Animation(1, new TextureRegion[] {
            new TextureRegion(game.sprites, 0, 0, 16, 16),
            new TextureRegion(game.sprites, 16, 0, 16, 16)
        });
        animationBattery = new Animation(1, new TextureRegion[] {
            new TextureRegion(game.sprites, 0, 16, 16, 16),
            new TextureRegion(game.sprites, 16, 16, 16, 16)
        });
        this.energy = ENERGY_MAX;
        this.hasBattery = true;
        this.dashTimer = 0;
    }

    // Gdx not supporting isButtonJustPressed
    private boolean lastLeft = false;

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
            if(Gdx.input.isButtonPressed(Buttons.LEFT) && !lastLeft && !hasBattery) {
                Vector2 hitDir = getGame().getMousePos().sub(getPosition()).nor();
                new Hit(getPosition().add(hitDir.scl(0.5f)), HIT_DAMAGE);
                energy -= HIT_COST;
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
        lastLeft = Gdx.input.isButtonPressed(Buttons.LEFT);
        super.update();
    }

    public void render(SpriteBatch batch) {
        animationTimer += Gdx.graphics.getDeltaTime();
        Animation a = hasBattery ? animationBattery : animation;
        setSprite(a.getKeyFrame(animationTimer, true));
        super.render(batch);
    }

	private class Hit extends Entity {

        private int damage;

		public Hit(Vector2 pos, int damage) {
			super(pos, new Vector2(1,1), Robot.this.getGame(), new TextureRegion(new Texture("badlogic.jpg")), BodyDef.BodyType.StaticBody, true);
            this.damage = damage;
		}

        public void update() {
            ArrayList<LivingEntity> touchingEnemies = isTouchingAll(LivingEntity.class);
            for(LivingEntity e : touchingEnemies) {
                if(e != Robot.this) {
                    e.damage(damage);
                }
            }
            die();
        }

	}

}

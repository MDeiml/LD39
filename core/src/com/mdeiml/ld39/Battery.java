package com.mdeiml.ld39;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Battery extends Entity {

    public Battery(Vector2 position, LD39Game game) {
        super(position, new Vector2(1, 1), game, new TextureRegion(game.sprites, 0, 32, 16, 16), BodyDef.BodyType.StaticBody, true);
    }

}

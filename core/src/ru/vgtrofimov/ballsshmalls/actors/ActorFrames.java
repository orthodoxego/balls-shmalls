package ru.vgtrofimov.ballsshmalls.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import ru.vgtrofimov.ballsshmalls.Balls;

public class ActorFrames extends Actor {
    TextureRegion[] skin;
    float frame = 0;
    float frameRate;
    int continued;
    boolean enabled = true;

    public ActorFrames(TextureRegion[] skin, int frame, float frameRate, int continued, int x, int y) {
        this.skin = skin;
        this.frame = frame;
        this.frameRate = frameRate;
        this.continued = continued;
        setX(x); setY(y);
        setWidth(skin[0].getRegionWidth());
        setHeight(skin[0].getRegionHeight());
        setScale(1, 1); setRotation(0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        frame += frameRate * delta;
        if ((int) frame >= skin.length) {
            frame = 0;
            continued -= 1;
            if (continued <= 0) enabled = false;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(skin[(int) frame], getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

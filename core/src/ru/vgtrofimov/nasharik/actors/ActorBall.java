package ru.vgtrofimov.nasharik.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Vector;

import ru.vgtrofimov.nasharik.services.Font;
import ru.vgtrofimov.nasharik.services.XY;
import ru.vgtrofimov.nasharik.settings.GameConstant;
import ru.vgtrofimov.nasharik.settings.Setup;
import ru.vgtrofimov.nasharik.settings.Sound;
import ru.vgtrofimov.nasharik.stages.GameStage;

public class ActorBall extends Actor {

    TextureRegion skin;
    TextureRegion shadow;
    Sound sound;
    int correctX, correctY;
    float speedX, speedY, default_speed_x, default_speed_y;
    int world_width, world_height;
    float velocity, default_velocity = 19.8f;
    float gravity = 0.18f;
    float massa = 8f;
    float elastic = 60f;
    ActorRacquet actorRacquet;
    float newScale, tempAlpha;
    Vector<XY> shadow_ball;
    float lifetime_for_shadow = 0;

    public ActorBall(TextureRegion skin, TextureRegion shadow, Sound sound, int x, int y, int speedX, int speedY, int world_width, int world_height) {
        this.skin = skin;
        this.shadow = shadow;
        this.sound = sound;
        setWidth(skin.getRegionWidth());
        setHeight(skin.getRegionWidth());
        correctX = (int) (getWidth() / 2);
        correctY = (int) (getHeight() / 2);
        setRotation(0);
        setX(x);
        setY(y);
        setBounds(x, y, getWidth(), getHeight());
        setOrigin(32, 32);
        setScale(0f, 0f);
        newScale = 0.5f;
        this.default_speed_x = speedX;
        this.default_speed_y = speedY;
        setSpeedX(default_speed_x);
        setSpeedY(default_speed_y);
        this.world_width = world_width;
        this.world_height = world_height;
        velocity = default_velocity;

        shadow_ball = new Vector<>();
        shadow_ball.add(new XY(getX(), getY(), getScaleX()));
        lifetime_for_shadow = 0;
        tempAlpha = 1.0f;
    }

    public void addScalle(float sc) {
        newScale = sc;
        if (newScale > 1.2f) newScale = 1.2f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        lifetime_for_shadow += delta;
        if (lifetime_for_shadow > GameConstant.TIME_TO_STEP_BALL) {
            lifetime_for_shadow = 0;
            if (shadow_ball.size() > 20) {
                shadow_ball.remove(shadow_ball.size() - 1);
            }
            shadow_ball.insertElementAt(new XY(getX(), getY(), getScaleX()), 0);
        }

        if (getScaleX() < newScale) {
            setScale(getScaleX() + delta, getScaleY() + delta);
        }

        if (getSpeedY() != 0)
            setRotation(getRotation() + delta * getSpeedX() * 1);

        setY(getY() + getSpeedY() * delta);
        setX(getX() + getSpeedX() * delta);
        //Balls.log("" + getSpeedY());

        check_move_balls(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (Setup.shadow) {
            for (int i = 0; i < shadow_ball.size(); i++) {
                batch.setColor(1 - i * 0.05f, 1 - i * 0.02f, 1 - i * 0.05f, 0.5f - i * 0.04f);
                batch.draw(skin, shadow_ball.elementAt(i).getX() - correctX, shadow_ball.elementAt(i).getY() - correctY, getOriginX(), getOriginY(), getWidth(), getHeight(), shadow_ball.elementAt(i).getScale() - i * 0.01f, shadow_ball.elementAt(i).getScale() - i * 0.01f, getRotation());
            }
        }

        batch.setColor(1, 1, 1, Math.min(parentAlpha, tempAlpha));
        batch.draw(skin, getX() - correctX, getY() - correctY, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.draw(shadow, getX() - correctX, getY() - correctY, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), 0);

//        Font.play_regular_little.draw(batch, "" + (int) getY(), 10, getY());
//        if (GameStage.isCollision) Font.play_regular_little.draw(batch, "Столкновения", 10, getY() + 20);

    }

    public boolean check_move_balls(float delta) {

        setSpeedY(getSpeedY() + (velocity + massa * gravity) / 4 * getScaleX() * 0.75f);
        setSpeedX(getSpeedX() * 0.998f);

        if (getY() + getHeight() / 2 > actorRacquet.getY() - actorRacquet.getPressed_energy() - actorRacquet.getY_correct_to_fire() * 3) {
            setSpeedY(Math.min(-256, (float) (-getSpeedY() * Math.sqrt(elastic) / massa)));
            velocity = default_velocity;

            if (Math.abs(getSpeedY()) < world_height * 0.03f) setSpeedY(0);
            actorRacquet.setY_correct_to_fire(getHeight() / 2);
        }

        if (getY() + getHeight() / 2 > actorRacquet.getY()) { // - actorRacquet.getPressed_energy()) { // - actorRacquet.getY_correct_to_fire() * 3) {
            // setY(actorRacquet.getY() - actorRacquet.getPressed_energy() - actorRacquet.getY_correct_to_fire() * 3 - getHeight() / 2);
            setY(actorRacquet.getY() - getHeight() * 2);
        }

        if (getY() < correctY) {
            setY(correctY);
            velocity = default_velocity;
            setSpeedY(-getSpeedY());
        }

        if (getX() < skin.getRegionWidth() / 2) {
            setX(skin.getRegionWidth() / 2);
            setSpeedX(-getSpeedX() * 1.15f);
        }

        if (getX() > world_width - skin.getRegionWidth() / 2) {
            setX(world_width - skin.getRegionWidth() / 2);
            setSpeedX(-getSpeedX() * 1.15f);
        }

        if (Math.abs(getSpeedX()) < 5f)
            setSpeedX(0);
        return true;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {

        int modifier = 1;
        if (speedX < 0) modifier = -1;

        if (Math.abs(speedX) > GameStage.game_world_width) {
            speedX = GameStage.game_world_width * modifier;
        }

        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {

        int modifier = 1;
        if (speedY < 0) modifier = -1;

        if (Math.abs(speedY) > GameStage.game_world_height / 10) {
            speedY = GameStage.game_world_height / 10 * modifier;
        }

        this.speedY = speedY;
    }

    public void setEnergySpeedY(float speedY) {
        if (Math.abs(speedY) > this.speedY) {
            this.speedY = speedY;
        }
    }

    public void setActorRacquet(ActorRacquet ar) {
        this.actorRacquet = ar;
    }

    public void setTempAlpha(float tempAlpha) {
        this.tempAlpha = tempAlpha;
    }
}

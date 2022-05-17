package ru.vgtrofimov.ballsshmalls.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import ru.vgtrofimov.ballsshmalls.Balls;
import ru.vgtrofimov.ballsshmalls.settings.Font;
import ru.vgtrofimov.ballsshmalls.stages.GameStage;

public class ActorBall extends Actor {

    TextureRegion skin;
    TextureRegion shadow;
    int correctX, correctY;
    float speedX, speedY, default_speed_x, default_speed_y;
    int world_width, world_height;
    float velocity, default_velocity = 19.8f;
    float gravity = 0.18f;
    float massa = 8f;
    float uprugost = 60f;
    ActorRacquet actorRacquet;
    float newScale;

    public ActorBall(TextureRegion skin, TextureRegion shadow, int x, int y, int speedX, int speedY, int world_width, int world_height) {
        this.skin = skin;
        this.shadow = shadow;
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
    }

    public void addScalle(float sc) {
        newScale = sc;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

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

    public boolean check_move_balls(float delta) {

        setSpeedY(getSpeedY() + (velocity + massa * gravity) / 4 * getScaleX());
        setSpeedX(getSpeedX() * 0.998f);

        // if (getY() + getHeight() > world_height - skin.getRegionHeight()) {
        if (getY() + getHeight() / 2 > actorRacquet.getY() - actorRacquet.getPressed_energy() - actorRacquet.getY_correct_to_fire() * 3) {
            // setY(world_height - getHeight() - skin.getRegionHeight() - getHeight() / 2);
            // setY(actorRacquet.getY() - getHeight() / 2 - actorRacquet.getPressed_energy() - actorRacquet.getY_correct_to_fire() * 3);
            // setY(actorRacquet.getY() - getHeight() / 2);
            setSpeedY(Math.min(-256, (float) (-getSpeedY() * Math.sqrt(uprugost) / massa)));
            velocity = default_velocity;

            if (Math.abs(getSpeedY()) < world_height * 0.03f) setSpeedY(0);
            actorRacquet.setY_correct_to_fire(getHeight() / 2);
        }

        if (getY() + getHeight() / 2 > actorRacquet.getY() - actorRacquet.getPressed_energy() - actorRacquet.getY_correct_to_fire() * 3) {
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.setColor(1, 1, 1, parentAlpha);
        batch.draw(skin, getX() - correctX, getY() - correctY, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.draw(shadow, getX() - correctX, getY() - correctY, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), 0);

        Font.play_regular_14px.draw(batch, "" + (int) getY(), 0, getY());
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

}

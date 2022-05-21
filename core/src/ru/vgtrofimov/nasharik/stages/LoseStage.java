package ru.vgtrofimov.nasharik.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.vgtrofimov.nasharik.Balls;
import ru.vgtrofimov.nasharik.actors.ActorBackground;
import ru.vgtrofimov.nasharik.actors.endgame.ActorKey;
import ru.vgtrofimov.nasharik.actors.endgame.ActorText;
import ru.vgtrofimov.nasharik.screens.GameScreen;
import ru.vgtrofimov.nasharik.services.Font;
import ru.vgtrofimov.nasharik.services.ReturnKey;
import ru.vgtrofimov.nasharik.settings.Setup;
import ru.vgtrofimov.nasharik.settings.Sound;
import ru.vgtrofimov.nasharik.textures.Textures;

public class LoseStage extends StageParent implements ReturnKey, InputProcessor {

    Textures textures;

    public LoseStage(GameScreen gameScreen, Setup setup, Viewport viewport, OrthographicCamera camera, Textures textures, Sound sound) {
        super(gameScreen, setup, sound, viewport, camera);
        this.textures = textures;

        // resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        resize((int) viewport.getWorldWidth(), (int) viewport.getWorldHeight());
        Balls.log(getClass().getName(), "" + viewport.getWorldWidth() + " " + viewport.getWorldHeight());

        ActorBackground actorBackground = new ActorBackground(textures.getBackground(), 3);
        addActor(actorBackground);

        int startY = (int) (camera.viewportHeight * 0.75f / 2);

        ActorText text01 = new ActorText(Font.play_bold_big, "" + setup.getScore(),
                ActorText.ADJUST.CENTER,
                (int) camera.viewportWidth);
        text01.setY(startY);

        ActorText text02 = new ActorText(Font.play_bold_14px, "Рекорд: " + setup.getRecordScore(),
                ActorText.ADJUST.CENTER,
                (int) camera.viewportWidth);
        startY += text01.getHeightText() * 1.2f;
        text02.setY(startY);

        addActor(text01);
        addActor(text02);

        startY += text02.getHeightText() * 4;

        ActorKey keyRestart = new ActorKey(this, KEY_NAME.RESTART,
                textures.getKeyRestart(),
                150, startY,
                96, 96);

        ActorKey keyMenu = new ActorKey(this, KEY_NAME.MENU,
                textures.getKeyMenu(),
                (int) camera.viewportWidth - 246, startY,
                96, 96);

        addActor(keyRestart);
        addActor(keyMenu);

        sound.play(Sound.SOUND.LOSEGAME);
    }

    @Override
    public void resize(int width, int height) {
        // ОБЯЗАТЕЛЬНО В КАЖДЫЙ РЕСАЙЦ!

        // Balls.log("RESIZE EndGS " + width + " " + height);

        int game_world_width = 512;
        int game_world_height = textures.getBackground().getRegionHeight() * Setup.count_background;

        /*
        getViewport().setScreenWidth(game_world_width);
        getViewport().setScreenHeight((int) (game_world_width * (float) height / width));
        getViewport().update(game_world_width, game_world_width * height / width);

         */

        camera.viewportWidth = game_world_width;
        camera.viewportHeight = game_world_width * (float) height / width;
        camera.position.set(camera.viewportWidth / 2,camera.viewportHeight / 2, 0);
        camera.update();

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void pressKey(KEY_NAME key_name) {
        switch (key_name) {
            case MENU:
                sound.stop(Sound.SOUND.LOSEGAME);
                gameScreen.setMenuStage();
                break;
            case RESTART:
                sound.stop(Sound.SOUND.LOSEGAME);
                setup.setLevel(Math.max(0, setup.getLevel() - 1));
                gameScreen.setGameStage();
                break;
        }
        sound.play(Sound.SOUND.CLICK_MENU);
    }

    @Override
    public void pressKey(int key_number) {

    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
            sound.stop(Sound.SOUND.LOSEGAME);
            sound.play(Sound.SOUND.CLICK_MENU);
            gameScreen.setMenuStage();
        }
        return true;
    }
}

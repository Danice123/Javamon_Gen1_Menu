package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.dankins.javamon.display.RenderInfo;

public interface Gen1GameMenuPart {

	void init(final AssetManager assets);

	void renderScreen(final RenderInfo ri, final float delta, final SpriteBatch batch);

	void tickSelf(final float delta);
}

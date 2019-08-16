package dev.dankins.javamon.display.screen.menu.game;

import com.badlogic.gdx.assets.AssetManager;

import dev.dankins.javamon.display.RenderInfo;
import dev.dankins.javamon.display.screen.RenderHelper;

public interface Gen1GameMenuPart {

	void init(final AssetManager assets, final RenderInfo ri, String gameName);

	void renderScreen(final RenderHelper rh, final float delta);

	void tickSelf(final float delta);
}

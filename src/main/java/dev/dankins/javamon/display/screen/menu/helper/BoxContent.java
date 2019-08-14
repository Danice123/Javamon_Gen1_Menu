package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.dankins.javamon.display.RenderInfo;

public interface BoxContent {

	void render(final RenderInfo ri, final SpriteBatch batch, int x, int y);
}

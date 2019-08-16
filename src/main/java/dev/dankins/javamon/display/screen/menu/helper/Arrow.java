package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Arrow {

	private final TextureRegion rightArrow;
	private final TextureRegion rightArrowAlt;
	private final TextureRegion downArrow;
	private final TextureRegion downArrowAlt;

	private final boolean arrowSelected = false;
	private final boolean arrowHidden = false;

	public Arrow(final AssetManager assets) {
		final Texture arrow = assets.get("arrow.png", Texture.class);
		rightArrow = new TextureRegion(arrow, 0, 0, 8, 8);
		rightArrowAlt = new TextureRegion(arrow, 8, 0, 8, 8);
		downArrow = new TextureRegion(arrow, 16, 0, 8, 8);
		downArrowAlt = new TextureRegion(arrow, 24, 0, 8, 8);
	}

}

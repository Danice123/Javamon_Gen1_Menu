package dev.dankins.javamon.display.screen.menu.helper;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.animation.Animation;

public class AnimationBox extends ImageBox {

	private final Animation animation;

	public AnimationBox(final Animation animation) {
		super(new Texture(0, 0, Format.Alpha));
		this.animation = animation;
	}

	@Override
	protected TextureRegion getTexture() {
		return animation.getCurrentFrame();
	}

}

package dev.dankins.javamon.display.screen.menu.content;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.dankins.javamon.display.animation.Animation;

public class AnimationContent extends ImageContent {

	private final Animation animation;

	public AnimationContent(final Animation animation) {
		super();
		this.animation = animation;
	}

	@Override
	protected TextureRegion getTexture() {
		return animation.getCurrentFrame();
	}

}

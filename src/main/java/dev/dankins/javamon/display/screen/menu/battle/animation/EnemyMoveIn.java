package dev.dankins.javamon.display.screen.menu.battle.animation;

import dev.dankins.javamon.display.screen.menu.content.ImageContent;

public class EnemyMoveIn extends Animation {

	private ImageContent image;

	public EnemyMoveIn(ImageContent image) {
		super(0.5f);
		this.image = image;
	}

	@Override
	protected void step(float stage) {
		image.setLeftMargin(100 - (int) (stage * 80));
	}

	@Override
	protected void finalize() {
		image.setLeftMargin(20);
	}

}

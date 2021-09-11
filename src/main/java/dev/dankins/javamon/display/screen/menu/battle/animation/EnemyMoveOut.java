package dev.dankins.javamon.display.screen.menu.battle.animation;

import dev.dankins.javamon.display.screen.menu.content.ImageContent;

public class EnemyMoveOut extends Animation {

	private ImageContent image;

	public EnemyMoveOut(ImageContent image) {
		super(0.5f);
		this.image = image;
	}

	@Override
	protected void step(float stage) {
		image.setLeftMargin((int) (stage * 100));
	}

	@Override
	protected void finalize() {
		image.setLeftMargin(100);
	}

}

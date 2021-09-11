package dev.dankins.javamon.display.screen.menu.battle.animation;

import dev.dankins.javamon.display.screen.menu.content.ImageContent;

public class PlayerMoveOut extends Animation {

	private ImageContent player;

	public PlayerMoveOut(ImageContent player) {
		super(0.5f);
		this.player = player;
	}

	@Override
	protected void step(float stage) {
		player.setLeftMargin((int) (stage * -100));
	}

	@Override
	protected void finalize() {
		player.setLeftMargin(-100);
	}

}

package dev.dankins.javamon.display.screen.menu.battle.animation;

import dev.dankins.javamon.display.screen.menu.content.ImageContent;

public class BattleBegin extends Animation {

	private ImageContent player;
	private ImageContent enemy;

	public BattleBegin(ImageContent player, ImageContent enemy) {
		super(1.0f);
		this.player = player;
		this.enemy = enemy;
		player.setLeftMargin(250);
		enemy.setLeftMargin(-250);
		player.setVisibility(false);
		enemy.setVisibility(false);
	}

	@Override
	protected void step(float stage) {
		player.setLeftMargin(250 - (int) (stage * 250));
		enemy.setLeftMargin(-250 + (int) (stage * 250));
	}

	@Override
	protected void finalize() {
		player.setLeftMargin(0);
		enemy.setLeftMargin(0);
	}

}

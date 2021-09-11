package dev.dankins.javamon.display.screen.menu.battle.animation;

import dev.dankins.javamon.display.screen.menu.content.ImageContent;

public class MonsterFaint extends Animation {

	private ImageContent monster;
	private int originalHeight;
	private int originalTopMargin;

	public MonsterFaint(ImageContent monster) {
		super(0.1f);
		this.monster = monster;
		this.originalHeight = monster.getTexture().getRegionHeight();
		this.originalTopMargin = monster.getTopMargin();
	}

	@Override
	protected void step(float stage) {
		monster.setHeight((int) (originalHeight - originalHeight * stage));
		monster.setTopMargin((int) (originalTopMargin + originalHeight * stage));
	}

	@Override
	protected void finalize() {
		monster.setVisibility(true);
		monster.resetHeight();
		monster.setTopMargin(originalTopMargin);
	}

}

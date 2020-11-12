package dev.dankins.javamon.display.screen.menu.battle.animation;

import dev.dankins.javamon.display.screen.menu.content.ImageContent;

public class MonsterRelease extends Animation {

	private ImageContent monster;
	private int finalHeight;
	private float targetScale;
	private int targetTopMargin;

	public MonsterRelease(ImageContent monster, float targetScale, int targetTopMargin) {
		super(0.25f);
		this.monster = monster;
		this.targetScale = targetScale;
		this.targetTopMargin = targetTopMargin;
		finalHeight = (int) (monster.getTexture().getRegionHeight() * targetScale);
	}

	@Override
	protected void step(float stage) {
		monster.setScale(targetScale * stage);
		monster.setTopMargin((int) (finalHeight - finalHeight * stage + targetTopMargin));
	}

	@Override
	protected void finalize() {
		monster.setScale(targetScale);
		monster.setTopMargin(targetTopMargin);
	}

}

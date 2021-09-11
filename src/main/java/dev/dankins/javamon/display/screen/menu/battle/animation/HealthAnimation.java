package dev.dankins.javamon.display.screen.menu.battle.animation;

public class HealthAnimation extends Animation {

	private float start;
	private float target;
	private HealthAnimationInterface changer;

	public HealthAnimation(float start, float target, HealthAnimationInterface changer) {
		super(0.25f);
		this.start = start;
		this.target = target;
		this.changer = changer;
	}

	@Override
	protected void step(float stage) {
		changer.change(start + stage * (target - start));
	}

	@Override
	protected void finalize() {
		changer.change(target);
	}

}

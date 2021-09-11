package dev.dankins.javamon.display.screen.menu.battle.animation;

import java.util.List;

import dev.dankins.javamon.ThreadUtils;
import dev.dankins.javamon.Timer;

public abstract class Animation extends Timer {

	private float duration;

	public Animation(float duration) {
		super(0);
		this.duration = duration;
	}

	@Override
	public void ring(float delta, float timeSinceRung) {
		float stage = timeSinceRung / duration;
		if (timeSinceRung >= duration) {
			finalize();
			done();
		} else {
			step(stage);
		}
	}

	protected abstract void step(float stage);

	protected abstract void finalize();

	public void run(List<Timer> animations) {
		synchronized (animations) {
			animations.add(this);
		}
		ThreadUtils.waitOnObject(this);
		synchronized (animations) {
			animations.remove(this);
		}
	}
}

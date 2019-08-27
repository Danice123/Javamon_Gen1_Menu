package dev.dankins.javamon;

public abstract class Timer {

	private final float length;
	private float duration = 0;

	public Timer(final float length) {
		this.length = length;
	}

	public void tick(final float delta) {
		duration += delta;
		if (duration > length) {
			ring(delta, duration - length);
		}
	}

	public void reset() {
		duration = 0;
	}

	public abstract void ring(float delta, float timeSinceRung);

	protected void done() {
		ThreadUtils.notifyOnObject(this);
	}

}

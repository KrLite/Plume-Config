package net.krlite.plumeconfig.io;

public enum LineBreak {
	NONE(false, false),
	BEFORE(true, false),
	AFTER(false, true),
	BOTH(true, true);

	private final boolean before;
	private final boolean after;

	LineBreak(boolean before, boolean after) {
		this.before = before;
		this.after = after;
	}

	public boolean isBefore() {
		return before;
	}

	public boolean isAfter() {
		return after;
	}
}

package com.gmail.berndivader.jboolexpr;

final class Boolean implements IBoolean {

	private boolean booleanValue;

	Boolean(final boolean newBooleanValue) {
		this.booleanValue = newBooleanValue;
	}

	public boolean booleanValue() {
		return this.booleanValue;
	}

	public String toString() {
		return "" + this.booleanValue;
	}

}

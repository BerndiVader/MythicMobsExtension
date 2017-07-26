package com.gmail.berndivader.jboolexpr;

final class Boolean implements IBoolean {

	private boolean booleanValue;

	Boolean(final boolean newBooleanValue) {
		this.booleanValue = newBooleanValue;
	}

	@Override
	public boolean booleanValue() {
		return this.booleanValue;
	}

	@Override
	public String toString() {
		return "" + this.booleanValue;
	}

}

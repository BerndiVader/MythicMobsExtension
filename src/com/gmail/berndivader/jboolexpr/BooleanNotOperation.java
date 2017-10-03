package com.gmail.berndivader.jboolexpr;

final class BooleanNotOperation implements IBoolean {

	private IBoolean iBoolean;

	BooleanNotOperation(final IBoolean newIBoolean) {
		if (newIBoolean == null) {
			throw new IllegalArgumentException("newIBoolean is null");
		}
		this.iBoolean = newIBoolean;
	}

	@Override
	public boolean booleanValue() {
		return (!this.iBoolean.booleanValue());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(!");
		buffer.append(this.iBoolean);
		buffer.append(")");
		return buffer.toString();
	}
}

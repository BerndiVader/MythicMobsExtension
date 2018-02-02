package com.gmail.berndivader.mythicmobsext.jboolexpr;

public abstract class BooleanExpression implements IBoolean {
	BooleanExpression() {
		// Nothing
	}

	public static BooleanExpression readLR(final String booleanExpression) throws MalformedBooleanException {
		return new BooleanExpressionLR(booleanExpression);
	}
}

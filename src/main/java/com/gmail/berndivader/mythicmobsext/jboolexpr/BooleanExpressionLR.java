package com.gmail.berndivader.mythicmobsext.jboolexpr;

final class BooleanExpressionLR extends BooleanExpression {

	private String booleanExpression;
	private IBoolean iBoolean;

	BooleanExpressionLR(final String newBooleanExpression) throws MalformedBooleanException {
		this.booleanExpression = newBooleanExpression;
		this.iBoolean = toIBoolean(BooleanUtil.validAndformat(newBooleanExpression), newBooleanExpression.length());
	}

	@Override
	public boolean booleanValue() {
		return this.iBoolean.booleanValue();
	}

	private IBoolean toIBoolean(final String formatedBooleanExpression, final int index)
			throws MalformedBooleanException {
		char lastChar = getLastChar(formatedBooleanExpression);
		if (String.valueOf(lastChar).matches("\\s")) {
			lastChar = ' ';
		}
		String substring = getSubstringWithoutLastChar(formatedBooleanExpression);
		switch (lastChar) {
		case ' ':
			IBoolean boolWhitespace = toIBoolean(substring, index - 1);
			return boolWhitespace;
		case ')':
			String openToEnd = getFromOpenParenthesisToEnd(substring, index - 1);
			String beginToOpen = getFromBeginToOpenParenthesis(substring, index - 1);
			IBoolean boolOpenToEnd = toIBoolean(openToEnd, index - 1);
			IBoolean boolToClose = toIBoolean(boolOpenToEnd, beginToOpen, index - 1);
			return boolToClose;
		case 'T':
			IBoolean boolTrue = toIBoolean(new Boolean(true), substring, index - 4);
			return boolTrue;
		case 'F':
			IBoolean boolFalse = toIBoolean(new Boolean(false), substring, index - 5);
			return boolFalse;
		default:
			throw new MalformedBooleanException("Expected [ ' ', ), true, false ]", index, this.booleanExpression);
		}
	}

	private IBoolean toIBoolean(final IBoolean lastIBoolean, final String formatedBooleanExpression, final int index)
			throws MalformedBooleanException {
		char lastChar = getLastChar(formatedBooleanExpression);
		if (String.valueOf(lastChar).matches("\\s")) {
			lastChar = ' ';
		}
		String substring = getSubstringWithoutLastChar(formatedBooleanExpression);
		switch (lastChar) {
		case ' ':
			IBoolean boolWhitespace = toIBoolean(lastIBoolean, substring, index - 1);
			return boolWhitespace;
		case '.':
			return lastIBoolean;
		case '(':
			IBoolean boolToOpen = toIBoolean(lastIBoolean, substring, index - 1);
			return boolToOpen;
		case '|':
			IBoolean boolFirstOr = toIBoolean(substring, index - 2);
			IBoolean boolOr = new BooleanOrOperation(boolFirstOr, lastIBoolean);
			return boolOr;
		case '&':
			IBoolean boolFirstAnd = toIBoolean(substring, index - 2);
			IBoolean boolAnd = new BooleanAndOperation(boolFirstAnd, lastIBoolean);
			return boolAnd;
		case '!':
			IBoolean boolNot = new BooleanNotOperation(lastIBoolean);
			IBoolean boolAll = toIBoolean(boolNot, substring, index - 1);
			return boolAll;
		default:
			throw new MalformedBooleanException("Expected [ ' ', ), ||, &&, ! ]", index, this.booleanExpression);
		}
	}

	private char getLastChar(final String formatedBooleanExpression) {
		if (formatedBooleanExpression.length() == 0) {
			return '.';
		}
		return formatedBooleanExpression.charAt(formatedBooleanExpression.length() - 1);
	}

	private String getSubstringWithoutLastChar(final String formatedBooleanExpression) {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		return formatedBooleanExpression.substring(0, formatedBooleanExpression.length() - 1);
	}

	private String getFromBeginToOpenParenthesis(final String formatedBooleanExpression, final int index)
			throws MalformedBooleanException {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		int fromIndex = 0;
		int toIndex = getIndexOfOpenParenthesis(formatedBooleanExpression, index);
		return formatedBooleanExpression.substring(fromIndex, toIndex);
	}

	private String getFromOpenParenthesisToEnd(final String formatedBooleanExpression, final int index)
			throws MalformedBooleanException {
		if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
			return "";
		}
		int fromIndex = getIndexOfOpenParenthesis(formatedBooleanExpression, index) + 1;
		int toIndex = formatedBooleanExpression.length();
		return formatedBooleanExpression.substring(fromIndex, toIndex);
	}

	private int getIndexOfOpenParenthesis(final String formatedBooleanExpression, final int index)
			throws MalformedBooleanException {
		int lastIndexOfOpenParenthesis = getLastIndexOf(formatedBooleanExpression, "(",
				formatedBooleanExpression.length());
		int lastIndexOfCloseParenthesis = getLastIndexOf(formatedBooleanExpression, ")",
				formatedBooleanExpression.length());
		while (lastIndexOfCloseParenthesis != -1 && lastIndexOfOpenParenthesis < lastIndexOfCloseParenthesis) {
			lastIndexOfOpenParenthesis = getLastIndexOf(formatedBooleanExpression, "(", lastIndexOfOpenParenthesis);
			lastIndexOfCloseParenthesis = getLastIndexOf(formatedBooleanExpression, ")", lastIndexOfCloseParenthesis);
		}
		if (lastIndexOfOpenParenthesis == -1) {
			int parenthesisIndex = index - (formatedBooleanExpression.length() - lastIndexOfCloseParenthesis);
			throw new MalformedBooleanException("Have a close parenthesis without an open parenthesis",
					parenthesisIndex, this.booleanExpression);
		}
		return lastIndexOfOpenParenthesis;
	}

	private int getLastIndexOf(final String formatedBooleanExpression, final String searchedString, final int toIndex) {
		if (toIndex < 0) {
			return -1;
		} else if (toIndex >= formatedBooleanExpression.length()) {
			return formatedBooleanExpression.lastIndexOf(searchedString);
		} else {
			String newFormatedBooleanExpression = formatedBooleanExpression.substring(0, toIndex);
			return newFormatedBooleanExpression.lastIndexOf(searchedString);
		}
	}

	@Override
	public String toString() {
		return this.iBoolean.toString();
	}

}

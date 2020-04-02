package com.gmail.berndivader.mythicmobsext.targeters;

public enum FilterEnum {
	DEFAULT, SORTBYDISTANCE, NEAREST, SHUFFLE;

	public static FilterEnum get(String s) {
		if (s == null)
			return FilterEnum.DEFAULT;
		try {
			return FilterEnum.valueOf(s.toUpperCase());
		} catch (Exception ex) {
			return FilterEnum.DEFAULT;
		}
	}

}

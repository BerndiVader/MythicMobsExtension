package com.gmail.berndivader.mythicmobsext.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomDouble {
	protected double min;
	protected double max;
	ThreadLocalRandom r;

	public RandomDouble(String range) {
		range = range.toLowerCase();
		this.r = ThreadLocalRandom.current();
		if (range.contains("to")) {
			String split[] = range.split("to");
			min = Double.parseDouble(split[0]);
			max = Double.parseDouble(split[1]);
			if (max < min) {
				double d1 = max;
				max = min;
				min = d1;
			}
		} else {
			min = max = Double.parseDouble(range);
		}
	}

	public double getMin() {
		return this.min;
	}

	public double getMax() {
		return this.max;
	}

	public double rollDouble() {
		return min == max ? min : r.nextDouble(min, max);
	}

	public int rollInteger() {
		return min == max ? (int) min : r.nextInt((int) min, (int) max);
	}

}
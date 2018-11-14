package com.gmail.berndivader.mythicmobsext.utils;

public
class
RangedDouble {
	protected final Operation op;
	protected double min;
	protected double max;

    public RangedDouble(String value) {
        this(value,false);
    }

    public RangedDouble(String value, boolean squared) {
    	if (value.contains("to")) {
            String[]split=value.split("to");
            this.min=Double.valueOf(split[0]);
            this.max=Double.valueOf(split[1]);
            this.op=Operation.RANGE;
        } else if (value.startsWith(">")) {
            String s=value.substring(1);
            this.min=Double.valueOf(s);
            this.max=Double.MAX_VALUE;
            this.op=Operation.GREATER_THAN;
        } else if (value.startsWith("<")) {
            String s=value.substring(1);
            this.min=Double.MIN_VALUE;
            this.max=Double.valueOf(s);
            this.op=Operation.LESS_THAN;
        } else {
            this.min=Double.valueOf(value);
            this.max=Double.valueOf(value);
            this.op=Operation.EQUALS;
        }
        if (squared) {
            this.min*=this.min;
            this.max*=this.max;
        }
    }

    public Operation getOperation() {
        return this.op;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public boolean equals(Object o) {
        if (o instanceof Integer || o instanceof Double || o instanceof Float) {
            double d = o instanceof Integer ? (double)((Integer)o).intValue() * 1.0 : (Double)o;
            switch (this.op) {
                case EQUALS: {
                    return d == this.min;
                }
                case GREATER_THAN: {
                    return d > this.min;
                }
                case LESS_THAN: {
                    return d < this.max;
                }
                case RANGE: {
                    return d >= this.min && d <= this.max;
                }
            }
            return true;
        }
        return false;
    }

    public static enum Operation {
        EQUALS,
        GREATER_THAN,
        LESS_THAN,
        RANGE;
    	
        private Operation() {
        }
    }
}
package com.gmail.berndivader.mmcustomskills26;

public class metaTagValue {
	private Object value;
	private ValueTypes valType;

	public metaTagValue(String value, String type) {
		if (value == null || !metaTagValue.containsType(type))
			type = ValueTypes.DEFAULT.toString();
		this.valType = ValueTypes.valueOf(type);
		switch (this.valType) {
		case DEFAULT:
			this.value = null;
			break;
		case BOOLEAN:
			if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
				this.value = Boolean.parseBoolean(value);
			} else {
				this.value = false;
			}
			break;
		case NUMERIC:
			if (value == null || value.isEmpty()) {
				this.value = 0;
			} else {
				try {
					this.value = Double.valueOf(value);
				} catch (Exception ex) {
					this.value = 0;
				}
			}
			break;
		case STRING:
			this.value = value == null ? "" : value;
			break;
		}
	}

	public Object getValue() {
		return this.value;
	}

	public ValueTypes getType() {
		return this.valType;
	}

	private static boolean containsType(String type) {
		for (ValueTypes t : ValueTypes.values()) {
			if (t.name().equals(type))
				return true;
		}
		return false;
	}

	public static enum ValueTypes {
		DEFAULT, BOOLEAN, STRING, NUMERIC;
	}

}

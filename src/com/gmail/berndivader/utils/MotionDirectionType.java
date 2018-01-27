package com.gmail.berndivader.utils;

public enum MotionDirectionType {
	LEFT,
	FORWARD_LEFT,
	FORWARD,
	FORWARD_RIGHT,
	RIGHT,
	BACKWARD_RIGHT,
	BACKWARD,
	BACKWARD_LEFT;

	public static MotionDirectionType get(String s) {
        if (s==null) return null;
        try {
            return MotionDirectionType.valueOf(s.toUpperCase());
        }
        catch (Exception ex) {
            return null;
        }
    }
	
	public static MotionDirectionType getMotionDirection(double r) {
		if ((0<=r&&r<22.5)
				||(337.5<=r&&r<360.0)) {
			return MotionDirectionType.LEFT;
        } else if (22.5<=r&&r<67.5) {
			return MotionDirectionType.FORWARD_LEFT;
        } else if (67.5<=r&&r<112.5) {
			return MotionDirectionType.FORWARD;
        } else if (112.5<=r&&r<157.5) {
			return MotionDirectionType.FORWARD_RIGHT;
        } else if (157.5<=r&&r<202.5) {
			return MotionDirectionType.RIGHT;
        } else if (202.5<=r&&r<247.5) {
			return MotionDirectionType.BACKWARD_RIGHT;
        } else if (247.5<=r&&r<292.5) {
			return MotionDirectionType.BACKWARD;
        }
		return MotionDirectionType.BACKWARD_LEFT;
	}
}

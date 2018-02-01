package main.java.com.gmail.berndivader.mythicmobsext.utils;

public enum FacingDirectionType {
	NORTH,
	NORTH_EAST,
	EAST,
	SOUTH_EAST,
	SOUTH,
	SOUTH_WEST,
	WEST,
	NORTH_WEST;

	public static FacingDirectionType get(String s) {
        if (s==null) return null;
        try {
            return FacingDirectionType.valueOf(s.toUpperCase());
        }
        catch (Exception ex) {
            return null;
        }
    }
	
	public static FacingDirectionType getFacingDirection(double r) {
		if ((0<=r&&r<22.5)
				||(337.5<=r&&r<360.0)) {
			return FacingDirectionType.NORTH;
        } else if (22.5<=r&&r<67.5) {
			return FacingDirectionType.NORTH_EAST;
        } else if (67.5<=r&&r<112.5) {
			return FacingDirectionType.EAST;
        } else if (112.5<=r&&r<157.5) {
			return FacingDirectionType.SOUTH_EAST;
        } else if (157.5<=r&&r<202.5) {
			return FacingDirectionType.SOUTH;
        } else if (202.5<=r&&r<247.5) {
			return FacingDirectionType.SOUTH_WEST;
        } else if (247.5<=r&&r<292.5) {
			return FacingDirectionType.WEST;
        }
		return FacingDirectionType.NORTH_WEST;
	}
}

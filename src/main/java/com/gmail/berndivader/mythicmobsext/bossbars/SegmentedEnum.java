package com.gmail.berndivader.mythicmobsext.bossbars;

public 
enum
SegmentedEnum 
{
	SEGMENTED_6,
	SEGMENTED_10,
	SEGMENTED_12,
	SEGMENTED_20;
	
	public static SegmentedEnum real(int segment) {
		switch(segment) {
		case 6:
			return SegmentedEnum.SEGMENTED_6;
		case 10:
			return SegmentedEnum.SEGMENTED_10;
		case 12:
			return SegmentedEnum.SEGMENTED_12;
		case 20:
			return SegmentedEnum.SEGMENTED_20;
		}
		return SegmentedEnum.SEGMENTED_6;
	}
}

package com.gmail.berndivader.mythicmobsext.utils.math;

import com.gmail.berndivader.mythicmobsext.utils.Vec3D;

/**
 * Various math utility methods.
 */
public final class MathUtils {

    /**
     * Safe minimum, such that 1 / SAFE_MIN does not overflow.
     *
     * <p>In IEEE 754 arithmetic, this is also the smallest normalized number
     * 2<sup>-1022</sup>. The value of this constant is from Apache Commons
     * Math 2.2.</p>
     */
    public static final double SAFE_MIN = 0x1.0p-1022;

    private MathUtils() {
    }

    /**
     * Modulus, divisor-style.
     *
     * @param a a
     * @param n n
     * @return the modulus
     */
    public static int divisorMod(int a, int n) {
        return (int) (a - n * Math.floor(Math.floor(a) / n));
    }

    /**
     * Returns the cosine of an angle given in degrees. This is better than
     * just {@code Math.cos(Math.toRadians(degrees))} because it provides a
     * more accurate result for angles divisible by 90 degrees.
     *
     * @param degrees the angle
     * @return the cosine of the given angle
     */
    public static double dCos(double degrees) {
        int dInt = (int) degrees;
        if (degrees == dInt && dInt % 90 == 0) {
            dInt %= 360;
            if (dInt < 0) {
                dInt += 360;
            }
            switch (dInt) {
            case 0:
                return 1.0;
            case 90:
                return 0.0;
            case 180:
                return -1.0;
            case 270:
                return 0.0;
            }
        }
        return Math.cos(Math.toRadians(degrees));
    }

    /**
     * Returns the sine of an angle given in degrees. This is better than just
     * {@code Math.sin(Math.toRadians(degrees))} because it provides a more
     * accurate result for angles divisible by 90 degrees.
     *
     * @param degrees the angle
     * @return the sine of the given angle
     */
    public static double dSin(double degrees) {
        int dInt = (int) degrees;
        if (degrees == dInt && dInt % 90 == 0) {
            dInt %= 360;
            if (dInt < 0) {
                dInt += 360;
            }
            switch (dInt) {
            case 0:
                return 0.0;
            case 90:
                return 1.0;
            case 180:
                return 0.0;
            case 270:
                return -1.0;
            }
        }
        return Math.sin(Math.toRadians(degrees));
    }
    
    public static double lerp(double point1,double point2,double alpha) {
        return point1+alpha*(point2-point1);
    }
    
    public static float smoothstep(float f,boolean clamp) {
    	float ff = f*f*(3.0f-2.0f*f);
    	return clamp(ff,clamp);
    }
    
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    } 
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    } 
    
    public static float clamp(float y, boolean clamp) {
    	return clamp?y<0?0:y>1?1:y:y;
    } 
    
    public static double clamp(double y, boolean clamp) {
    	return clamp?y<0?0:y>1?1:y:y;
    } 
    
    public static float normalize(float value,float min,float max) {
        float delta=value-min,delta_1=max-min;
        return delta/delta_1;
    }
    public static double normalize(double value,double min,double max) {
    	double delta=value-min,delta_1=max-min;
        return delta/delta_1;
    }
    
    public static double scale(double value,double min,double max) {
        return value/(max-min)+min;
    }
    
}

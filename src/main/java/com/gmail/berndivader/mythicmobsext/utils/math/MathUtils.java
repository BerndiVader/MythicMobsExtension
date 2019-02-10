package com.gmail.berndivader.mythicmobsext.utils.math;

import org.bukkit.Location;
import org.bukkit.util.Vector;

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
    
    public static Vector calculateVelocity(Vector from,Vector to,double gravity,double heightGain) {
    	
        int endGain=to.getBlockY()-from.getBlockY();
        double horizDist=Math.sqrt(distanceSquared(from,to));
        double maxGain=heightGain>(endGain+heightGain)?heightGain:(endGain+heightGain);

        double a=-horizDist*horizDist/(4*maxGain);
        double b=horizDist;
        double c=-endGain;

        double slope=-b/(2*a)-Math.sqrt(b*b-4*a*c)/(2*a);
        double vy=Math.sqrt(maxGain*(gravity+0.0013675090252708*heightGain));
        double vh=vy/slope;

        int dx=to.getBlockX()-from.getBlockX();
        int dz=to.getBlockZ()-from.getBlockZ();
        double mag=Math.sqrt(dx*dx+dz*dz);
        double dirx=dx/mag;
        double dirz=dz/mag;

        double vx=vh*dirx;
        double vz=vh*dirz;

        return new Vector(vx,vy,vz);
    }

    private static double distanceSquared(Vector from,Vector to) {

        double dx=to.getBlockX()-from.getBlockX();
        double dz=to.getBlockZ()-from.getBlockZ();

        return dx*dx+dz*dz;
    }

    public static Vector spread(Vector from,double yaw,double pitch) {
        Vector vec=from.clone();

        float cosyaw=(float)Math.cos(yaw);
        float cospitch=(float)Math.cos(pitch);
        float sinyaw=(float)Math.sin(yaw);
        float sinpitch=(float)Math.sin(pitch);
        float bX=(float)(vec.getY()*sinpitch+vec.getX()*cospitch);
        float bY=(float)(vec.getY()*cospitch-vec.getX()*sinpitch);
        return new Vector(bX*cosyaw-vec.getZ()*sinyaw,bY,bX*sinyaw+vec.getZ()*cosyaw);
    }

    public static double launchAngle(Location from,Vector to,double v,double elev,double g) {
        Vector victor=from.toVector().subtract(to);
        Double dist=Math.sqrt(Math.pow(victor.getX(), 2) + Math.pow(victor.getZ(), 2));
        double v2=Math.pow(v,2);
        double v4=Math.pow(v,4);
        double derp=g*(g*Math.pow(dist,2)+2*elev*v2);
        if (v4<derp) {
            return Math.atan((2*g*elev+v2)/(2*g*elev+2*v2));
        }
        else {
            return Math.atan((v2-Math.sqrt(v4-derp))/(g*dist));
        }
    }

    public static double hangtime(double launchAngle,double v,double elev,double g) {
        double a=v*Math.sin(launchAngle);
        double b=-2*g*elev;
        if (Math.pow(a,2)+b<0) {
            return 0;
        }
        return (a+Math.sqrt(Math.pow(a,2)+b))/g;
    }    
    
}

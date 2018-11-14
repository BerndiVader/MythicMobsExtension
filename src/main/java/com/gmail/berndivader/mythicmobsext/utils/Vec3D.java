package com.gmail.berndivader.mythicmobsext.utils;

public class Vec3D
implements
Cloneable {
	private double x,y,z;
	
	public Vec3D(double x,double y,double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x=x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y=y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z=z;
	}
	public void set(double x,double y,double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public Vec3D length(Vec3D v3) {
		return new Vec3D(this.x-v3.getX(),this.y-v3.getY(),this.z-v3.getZ());
	}
	public String toString() {
		return "v3d@"+x+","+y+","+z;
	}
	public Vec3D clone() throws CloneNotSupportedException {
		return (Vec3D)super.clone();
	}
}

package com.gmail.berndivader.utils;

public class Vec2D
implements
Cloneable {
	private double x,y;
	
	public Vec2D(double x,double y) {
		this.x=x;
		this.y=y;
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
	public void set(double x,double y) {
		this.x=x;
		this.y=y;
	}
	public Vec2D length(Vec2D v2) {
		return new Vec2D(this.x-v2.getX(),this.y-v2.getY());
	}
	public String toString() {
		return "("+x+", "+y+")";
	}
	public Vec2D clone() throws CloneNotSupportedException {
		return (Vec2D)super.clone();
	}	
}

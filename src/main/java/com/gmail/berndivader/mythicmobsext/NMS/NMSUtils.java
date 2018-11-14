package com.gmail.berndivader.mythicmobsext.NMS;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.bukkit.Server;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.compatibilitylib.CompatibilityUtils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;

public
class
NMSUtils 
extends
CompatibilityUtils
{
	/**
	 * @param e - entity
	 * @return motion vector - {@link Vec3D}
	 */
	public static Vec3D getEntityLastMot(Entity e) {
		Object o=getHandle(e);
		Vec3D v3=null;
		try {
			double dx=(double)class_Entity_motXField.get(o)
					,dy=(double)class_Entity_motYField.get(o)
					,dz=(double)class_Entity_motZField.get(o);
			v3=new Vec3D(-dx,-dy,-dz);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return v3;
	}
	
	/**
	 * @param entity {@link Entity}
	 * @return float - last yaw
	 */
	public static float getLastYawFloat(Entity entity) {
		try {
			return (float)class_Entity_lastYawField.get(getHandle(entity));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0f;
	}
	
	/**
	 * @param server {@link Server}
	 * @return int - ticks
	 */
	public static int getCurrentTick(Server s1) {
		int i1=0;
		Object o1;
		try {
			if ((o1=getHandle(s1))!=null) {
				i1=class_MinecraftServer_currentTickField.getInt(o1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i1;
	}
	
	/**
	 * @param s1 - fieldname {@link String}
	 * @param cl1 - class {@link Class}
	 * @param o1 - instance {@link Object}
	 * @param o2 - value {@link Object}
	 */
	public static void setFinalField(String s1,Class<?>cl1,Object o1,Object o2) {
        try {
            Field f1=cl1.getDeclaredField(s1);
            f1.setAccessible(true);
            Field f2=Field.class.getDeclaredField("modifiers");
            f2.setAccessible(true);
            f2.setInt(f1,f2.getModifiers()&~Modifier.FINAL);
			f1.set(o1,o2);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param s1 - fieldname
	 * @param cl1 - class
	 * @param o1 - instance
	 * @return object {@link Object}
	 */
	public static Object getField(String s1,Class<?>cl1,Object o1) {
		Object o2=null;
		try {
			Field f1=cl1.getDeclaredField(s1);
			f1.setAccessible(true);
			o2=f1.get(o1);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return o2;
	}
	
	/**
	 * @param s1 - fieldname {@link String}
	 * @param cl1 - class {@link Class}
	 * @param o1 - instance {@link Object}
	 * @param o2 - value {@link Object}
	 */
	public static void setField(String s1,Class<?>cl1,Object o1,Object o2) {
        try {
            Field f1=cl1.getDeclaredField(s1);
            f1.setAccessible(true);
			f1.set(o1,o2);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param instance {@link Object}
	 * @param name - fieldname {@link String}
	 * @param value {@link Object}
	 */
	public static void setField(Object instance,String name,Object value) {
		if(instance==null) return;
		Field field;
		try {
			field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param clazz {@link Class}
	 * @param name - fieldname {@link String}
	 * @param instance {@link Object}
	 * @return object {@link Object}
	 */
	public static Object getField(Class<?> clazz, String name, Object instance) {
		Object o1=null;
		try {
			Field field=clazz.getDeclaredField(name);
			field.setAccessible(true);
			o1=field.get(instance);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
		return o1;
	}
	
	/**
	 * @param entity {@link Entity}
	 * @param flag - <br><b>0</b>=burning,<br><b>1</b>=sneaking,<br><b>3</b>=sprinting,<br><b>4</b>=swimming,<br><b>5</b>=invisible,<br><b>6</b>=glowing
	 * @return boolean
	 */
	public static boolean getFlag(Entity entity,int flag) {
		boolean bl1=false;
		try {
			bl1=(boolean)class_Entity_getFlagMethod.invoke(getHandle(entity),flag);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return bl1;
	}

}

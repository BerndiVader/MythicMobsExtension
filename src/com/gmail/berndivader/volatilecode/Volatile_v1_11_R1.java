package com.gmail.berndivader.volatilecode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import io.lumine.xikage.mythicmobs.MythicMobs;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;

import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.PathfinderGoal;
import net.minecraft.server.v1_11_R1.PathfinderGoalFleeSun;
import net.minecraft.server.v1_11_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;

public class Volatile_v1_11_R1 
implements VolatileHandler {
	
	public Volatile_v1_11_R1() {
	}
	
	@Override
	public void aiPathfinderGoal(LivingEntity entity, String uGoal) {
        EntityInsentient e = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
        Field goalsField;
        int i=0;
        String goal=uGoal;
        String data=null;
        String[] parse = uGoal.split(" ");
        if (parse[0].matches("[0-9]*")) {
        	i = Integer.parseInt(parse[0]);
        	if (parse.length>1) {
        		goal = parse[1];
        		if (parse.length>2) {
        			data = parse[2];
        		}
        	}
        }
		try {
			goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
	        goalsField.setAccessible(true);
	        PathfinderGoalSelector goals = (PathfinderGoalSelector)goalsField.get((Object)e);
	        switch (goal) {
	        case "rangedmelee": {
	            if (e instanceof EntityCreature) {
	            	float range = 2.0f;
	            	if (CustomSkillStuff.isNumeric(data)) {
	            		range = Float.parseFloat(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalMeleeRangeAttack((EntityCreature)e, 1.0, true, range));
	            }
	        	break;
	        }
	        case "runfromsun": {
	        	if (e instanceof EntityCreature) {
	        		double speed = 1.0d;
	            	if (CustomSkillStuff.isNumeric(data)) {
	            		speed = Double.parseDouble(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalFleeSun((EntityCreature)e, speed));
	        	}
	        	break;
	        }
	        default: {
	        	List<String>gList=new ArrayList<String>();
	        	gList.add(uGoal);
	            MythicMobs.inst().getVolatileCodeHandler().aiGoalSelectorHandler(entity, gList);
	        }}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
    
	public class PathfinderGoalMeleeRangeAttack extends PathfinderGoalMeleeAttack {
		protected float range;

		public PathfinderGoalMeleeRangeAttack(EntityCreature entityCreature, double d, boolean b, float range) {
			super(entityCreature, d, b);
			this.range=range;
		}

		@Override
		protected double a(EntityLiving entity) {
		    return (double)(this.b.width * this.range * this.b.width * this.range + entity.width);
		}
	}
	
}

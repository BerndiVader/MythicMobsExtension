package com.gmail.berndivader.mythicmobsext.volatilecode.v1_14_R1.pathfindergoals;

import java.util.HashSet;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.PathEntity;
import net.minecraft.server.v1_14_R1.PathPoint;
import net.minecraft.server.v1_14_R1.PathfinderGoal;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_14_R1.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;

public class PathfinderGoalBreakBlocks extends PathfinderGoal {
	protected EntityInsentient entity;
	protected boolean isBreaking;
	protected int chance;
	protected HashSet<Material>materials;

	public PathfinderGoalBreakBlocks(EntityInsentient entity, String mL, int chance) {
		this.isBreaking=false;
		this.entity=entity;
		this.materials=new HashSet<>();
		this.chance=chance>100?100:chance<0?0:chance;
		if (mL!=null) {
			String[]parse=mL.toUpperCase().split(",");
			for(int a=0;a<parse.length;a++) {
				try {
					this.materials.add(Material.valueOf(parse[a]));
				} catch (Exception ex) {
					Main.logger.warning("Material "+parse[a]+" is not valid for PathfinderGoalBreakBlocks.");
					continue;
				}
			}
		}
	}

	public boolean a() {
		return this.entity.isAlive();
	}
	
	public boolean b() {
		if (this.entity.getGoalTarget()!=null
				&& this.entity.getGoalTarget().isAlive()) {
			return true;
		}
		return false;
	}

	public void e() {
		if (!this.canContinue()) return;
		EntityLiving target=this.entity.getGoalTarget();
		Block[] blocks=new Block[2];
        blocks[1] = this.getBreakableTargetBlock(target);
        blocks[0] = blocks[1].getRelative(BlockFace.UP);
        int size=blocks.length;
        for (int a=0;a<size;a++) {
        	if (this.materials.isEmpty()||this.materials.contains(blocks[a].getType())) this.attemptBreakBlock(blocks[a]);
        }
	}
	
	private boolean canContinue() {
		if(Main.random.nextInt(100)<=chance) {
			EntityLiving target=this.entity.getGoalTarget();
			return target!=null&&target.isAlive()&&!this.isBreaking&&!this.isReachable(target);
		}
		return false;
	}
	
    private Block getBreakableTargetBlock(EntityLiving target) {
    	Vector direction=((LivingEntity)this.entity.getBukkitEntity()).getLocation().getDirection();
        double dx=direction.getX();
        double dz=direction.getY();
        int bdx=0;
        int bdz=0;
        if (Math.abs(dx)>Math.abs(dz)) {
            bdx=(dx>0)?1:-1;
        } else {
            bdz=(dx>0)?1:-1;
        }
        return this.entity.world.getWorld().getBlockAt((int) Math.floor(this.entity.locX + bdx),
        		(int) Math.floor(this.entity.locY),
        		(int) Math.floor(this.entity.locZ + bdz));
    }
    
	private void attemptBreakBlock(Block block) {
        Material type=block.getType();
        if(!this.isBreaking&&type!=Material.AIR&&type.isSolid()) {
            if(Main.random.nextInt(100)<=this.chance) {
            	this.isBreaking=true;
                PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20, 4, false, false);
                ((LivingEntity)this.entity.getBukkitEntity()).addPotionEffect(effect);
            	new BukkitRunnable() {
					@Override
					public void run() {
	                	BlockPosition position=new BlockPosition(block.getX(),block.getY(),block.getZ());
	                	if(!CraftEventFactory.callEntityChangeBlockEvent(entity,position,entity.world.getType(position).getBlock().getBlockData()).isCancelled()) {
	                        entity.world.triggerEffect(2001,position,net.minecraft.server.v1_14_R1.Block.getCombinedId(entity.world.getType(position)));
	                        block.breakNaturally();
	                        PathfinderGoalBreakBlocks.this.isBreaking=false;
		                }
					}
				}.runTaskLater(Main.getPlugin(), 20L);
            }
        }
    }
    private boolean isReachable(EntityLiving target) {
    	if (target==null) return true;
        PathEntity pe=this.entity.getNavigation().a(target,0);
        if (pe==null) {
        	return this.entity.onGround!=true;
        } else {
            PathPoint pp=pe.c();
            return pp!=null;
        }
    }
}

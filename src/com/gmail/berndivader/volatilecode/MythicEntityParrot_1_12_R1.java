package com.gmail.berndivader.volatilecode;

import java.util.Set;

import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.google.common.collect.Sets;

import net.minecraft.server.v1_12_R1.ControllerMoveFlying;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityParrot;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MobEffect;
import net.minecraft.server.v1_12_R1.MobEffects;
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;
import net.minecraft.server.v1_12_R1.SoundEffects;
import net.minecraft.server.v1_12_R1.World;

public class MythicEntityParrot_1_12_R1 
extends EntityParrot {
    private static final Set<Item> bJ = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    boolean cd;

	public MythicEntityParrot_1_12_R1(World world) {
		super(world);
        this.setSize(0.5f, 0.9f);
        this.moveController = new ControllerMoveFlying(this);
	}
	
	private boolean cd() {
		return this.cd;
	}
	public void cd(boolean b1) {
		this.cd=b1;
	}
	
    @Override
    public boolean a(EntityHuman eH,EnumHand eh) {
        ItemStack itemstack=eH.b(eh);
        if (!this.isTamed()&&bJ.contains(itemstack.getItem())) {
            if (!eH.abilities.canInstantlyBuild) itemstack.subtract(1);
            if (!this.isSilent()) this.world.a(null,this.locX,this.locY,this.locZ,SoundEffects.eJ,this.bK(),1.0f,1.0f+(this.random.nextFloat()-this.random.nextFloat())*0.2f);
            if (!this.world.isClientSide) {
                if (this.random.nextInt(10)==0&&!CraftEventFactory.callEntityTameEvent(this,eH).isCancelled()) {
                    this.c(eH);
                    this.p(true);
                    this.world.broadcastEntityEffect(this,(byte)7);
                } else {
                    this.p(false);
                    this.world.broadcastEntityEffect(this,(byte)6);
                }
            }
            return true;
        }
        if (itemstack.getItem()==Items.COOKIE) {
        	if (!eH.abilities.canInstantlyBuild&&cd()) {
                boolean b1=this.damageEntity(DamageSource.playerAttack(eH),Float.MAX_VALUE);
                if (b1) {
                	if (eH.z()||!this.be()) {
                       	this.addEffect(new MobEffect(MobEffects.POISON,900));
                       	itemstack.subtract(1);
                       	return true;
                    }
                }
            }
        	// TODO: find where the real packet is send and replace it, instead of this.
        	new BukkitRunnable() {
				@Override
				public void run() {
					((EntityPlayer)eH).playerConnection.sendPacket(new PacketPlayOutSetSlot(0,36+eH.inventory.itemInHandIndex,itemstack));
				}
			}.runTaskLater(Main.getPlugin(),1L);
    		return true;
        }
        if (!this.world.isClientSide && !this.a() && this.isTamed() && this.e(eH)) {
            this.goalSit.setSitting(!this.isSitting());
        }
        return super.a(eH,eh);
    }

}

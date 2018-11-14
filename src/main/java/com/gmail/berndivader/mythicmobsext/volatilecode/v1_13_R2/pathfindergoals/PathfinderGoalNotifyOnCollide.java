package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2.pathfindergoals;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.events.OnEntityCollideEvent;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.World;

public class PathfinderGoalNotifyOnCollide
extends PathfinderGoal {
	int c;
    EntityInsentient e;
    ActiveMob am;
    World w;
    HashMap<UUID, Integer> cd;

    public PathfinderGoalNotifyOnCollide(EntityInsentient e2,int i1) {
    	this.c=i1;
        this.e=e2;
        this.am=Utils.mobmanager.getMythicMobInstance(e.getBukkitEntity());
        this.w=e2.world;
        this.cd=new HashMap<>();
    }

    public boolean a() {
        return this.e.collides;
    }

    public boolean b() {
        for (Map.Entry<UUID, Integer> ee : this.cd.entrySet()) {
            int i1=ee.getValue();
            if (i1--<1) {
                this.cd.remove(ee.getKey());
                continue;
            }
            ee.setValue(i1);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
	public void e() {
        ListIterator<Entity> it1=this.w.getEntities((Entity)this.e,this.e.getBoundingBox()).listIterator();
        while (it1.hasNext()) {
            Entity ee=it1.next();
            if (this.cd.containsKey(ee.getUniqueID())) continue;
            this.cd.put(ee.getUniqueID(),this.c);
            new OnEntityCollideEvent(this.e.getBukkitEntity(),ee.getBukkitEntity());
            this.e.getBukkitEntity().setMetadata(Utils.meta_LASTCOLLIDETYPE,new FixedMetadataValue(Main.getPlugin(),ee.getBukkitEntity().getType().toString()));
            if (am!=null) new TriggeredSkill(SkillTrigger.BLOCK,this.am,BukkitAdapter.adapt(ee.getBukkitEntity()),new Pair[0]);
        }
    }
}
package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.auras.Aura;
import io.lumine.xikage.mythicmobs.utils.Events;
import io.lumine.xikage.mythicmobs.utils.Schedulers;

@ExternalAnnotation(name = "ontrade", author = "Seyarada")
public class OnTrade extends Aura implements ITargetedEntitySkill {
   protected Optional<Skill> onTradeSkill = Optional.empty();
   protected String onTradeSkillName;
   protected boolean cancelEvent;
   protected boolean forceAsPower;
   protected int oldXP;
   protected int newXP;

   public OnTrade(String skill, MythicLineConfig mlc) {
      super(skill, mlc);
      this.onTradeSkillName = mlc.getString(new String[]{"ontradeskill", "ontrade", "os", "s", "skill"}, "trade");
      this.cancelEvent = mlc.getBoolean(new String[]{"cancelevent", "ce"}, false);
      this.forceAsPower = mlc.getBoolean(new String[]{"forceaspower", "fap"}, true);
      MythicMobs.inst().getSkillManager().queueSecondPass(() -> {
         if (this.onTradeSkillName != null) {
            this.onTradeSkill = MythicMobs.inst().getSkillManager().getSkill(this.onTradeSkillName);
         }

      });
   }

   public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
      new OnTrade.Tracker(data, target);
      return true;
   }

   private class Tracker extends Aura.AuraTracker implements IParentSkill, Runnable {
      public Tracker(SkillMetadata data, AbstractEntity entity) {
         super((AbstractEntity)entity, data);
         this.start();
      }

      public void auraStart() {
         this.registerAuraComponent(Events.subscribe(InventoryClickEvent.class).filter((event) -> {
        	if ( event.getInventory().getHolder()!=null && ((Entity) event.getInventory().getHolder()).getType().equals(EntityType.VILLAGER) ) {
        		oldXP = ( (Villager) ((Entity) event.getInventory().getHolder())).getVillagerExperience();
                return ((Entity) event.getInventory().getHolder()).getUniqueId().equals(((AbstractEntity)this.entity.get()).getUniqueId());
        	}
        	return false;
         }).handler((event) -> {
        	 if ( event.getInventory().getHolder()!=null && ((Entity) event.getInventory().getHolder()).getType().equals(EntityType.VILLAGER) ) {
	        	 Schedulers.sync().runLater(() -> {
	        		 newXP = ( (Villager) ((Entity) event.getInventory().getHolder())).getVillagerExperience();
	        		 
	        		 if (newXP!=oldXP) {
		        		 SkillMetadata meta = this.skillMetadata.deepClone();
		                 meta.setEntityTarget(BukkitAdapter.adapt(((Entity) event.getInventory().getHolder())));
		                 if (this.executeAuraSkill(OnTrade.this.onTradeSkill, meta)) {
		                    if (OnTrade.this.cancelEvent) {
		                       event.setCancelled(true);
		                    }
		                 };
	        		 }
	              }, 1L);
        	 }

         }));
         this.executeAuraSkill(OnTrade.this.onStartSkill, this.skillMetadata);
      }
   }
}

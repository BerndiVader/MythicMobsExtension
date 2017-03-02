package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class mmWorldEditSkills implements Listener {
	
	private String MechName;
	private SkillMechanic skill;

	@EventHandler
	public void onMythicWorldEditSkillsLoad(MythicMechanicLoadEvent e) {
		MechName = e.getMechanicName().toLowerCase();
		if (MechName.equals("wesphere")) {
			skill = new mmWorldEditSphere(e.getContainer(), e.getConfig());
			e.register(skill);
		} else if (MechName.equals("wepyramid")) {
			skill = new mmWorldEditPyramid(e.getContainer(), e.getConfig());
			e.register(skill);
		}
	}
}

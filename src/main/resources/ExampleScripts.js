/*
Some javascripts examples of how to use with mechanics, conditions.
*/

var SomeStanceMath=function(data,target,mlc) {
	if (data.getCaster().getClass().getSimpleName()=='ActiveMob') {
		var am=data.getCaster();
		var stance=Number(am.getStance());
		var value=mlc.getString("math","stance");
		am.setStance(String(eval(value)));
	}
	return true;
};

var ScoreboardExample=function(data,target,mlc) {
	if (target instanceof org.bukkit.entity.Entity) {
		var value=mlc.getString("value");
		var scoreboard=Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		var objective=scoreboard.getObjective(mlc.getString("objective"));
		if (objective!=null) {
			var sc=objective.getScore(target.getUniqueId().toString());
			var score=sc.getScore();
			sc.setScore(eval(value));
		}
	}
	return true;
}

var BroadcastMythicMobsVersion=function() {
	Bukkit.getServer().broadcastMessage(mythicmobs.getVersion());
	return true;
}

var ChangeTargetsName=function(data,target,mlc) {
	if (target instanceof org.bukkit.entity.LivingEntity) {
		target.setCustomName(mlc.getString("newname","whatever"));
		target.setCustomNameVisible(true);
		return true;
	}
	return false;	
}

var SetMoblevel=function(data,target,mlc) {
	if (data.getCaster().getClass().getSimpleName()=='ActiveMob') {
		var am=data.getCaster();
		am.setLevel(eval(mlc.getString("level")));
		am.getEntity().getBukkitEntity().setCustomName(am.getLevel());
	}
	return true;
}

var updateHealthDisplay=function(data,target,mlc) {
	if (data.getCaster().getClass().getSimpleName()=='ActiveMob') {
		var am=data.getCaster();
		var entity=data.getCaster().getLivingEntity();
		var display=am.getType().getDisplayName();
		entity.setCustomName(display.replace("<mob.mhp>",entity.getMaxHealth()));
	}
	return true;
}

var SetMobscoreByLevel=function(data,target,mlc) {
	if (target instanceof org.bukkit.entity.LivingEntity) {
		var value=mlc.getString("score");
		var objectiveName=mlc.getString("objective");
		var scoreboard=Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		var objective=scoreboard.getObjective(objectiveName);
			if (objective==null) {
			objective=scoreboard.registerNewObjective(objectiveName,"empty");
		}
		var am=mythicmobs.getMobManager().getMythicMobInstance(target);
		var dummy=1;
		if (am!=null) {
			dummy=am.getLevel();				
		}
		var score=objective.getScore(target.getUniqueId().toString());
		var oldvalue=score.getScore();
		score.setScore(eval(value));
		Bukkit.getServer().broadcastMessage("score "+objectiveName+" set: "+score.getScore());
	}
	return true;
}

var MobScoreCondition=function(mlc,object) {
	return true;
}
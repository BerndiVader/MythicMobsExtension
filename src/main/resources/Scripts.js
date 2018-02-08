var Bukkit=org.bukkit.Bukkit;
var mythicmobs=Bukkit.getPluginManager().getPlugin("MythicMobs");

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
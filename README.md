# CustomSkillMechanics
for MythicMobs 4.0.0 and higher

Here you find some examples of CustomSkillMechanics for the new MythicMobs 4 API

* DamageArmorSkill:

	- damagearmor{armor="all";damage=5;signal=armorbroken}
		armor: all / helmet / chest / leggings / boots
		damage: armor damage amount
		signal: name of the signal which should be send to mob if an armor part is broken.
		        dont set it if you dont want a signal to be send.
				
Example:
```
dmgpig:
  Type: pig
  Display: 'DontHitMe'
  Health: 500
  Damage: 1
  Faction: neutral
  Despawn: true
  AIGoalSelectors:
  - 0 clear
  - 1 randomstroll
  - 2 float
  Skills:
  - damagearmor{armor=all;damage=20;signal=armorbroken} @target ~onDamaged >0 1
  - message{msg="Oh dear! A part of your armor is gone!"} @target ~onSignal:armorbroken
```


* GrenadeSkill:

	- grenade{size=3;amount=1;fuse=20;fire=false;breakblocks=true;utime=60;ueffect=true;undotnt=true}
		size: radius of the explosion
		amount: how many grenades the mob throw at once
		fuse: how long the fuse tick
		fire: should there be fire too? true / false
		breakblocks: damage the blocks? true / false
		utime: how many ticks until restore the blocks
		ueffect: should there be a restore effect? true / false
		undotnt: should the blocks be restored at all? true / false

Example:
```
grenadezombie:
  Type: zombie
  Health: 20
  Skills:
  - grenade{size=2;amount=1;fuse=20;fire=false;breakblocks=true;utime=40;ueffect=true;undotnt=true} @target ~onTimer:200 1
```


* SetRandomLevelSkill:

	- setrandomlevel{min=1;max=10;self=true}
		min: lowest level
		max: highest level
		self: true = targetself / false = target any other targeted mob
		
Example:
```
RndLvlMob:
  Type: zombie
  Skills:
  - setrandomlevel{min=1;max=10;self=true} @self ~onSpawn 1
```


* StealSkill:

	- steal{items=ITEM:AMOUNT,ITEM:AMOUNT,.....;failsignal=steal_fail;oksignal=steal_ok}
		items: Can be a list of valid spigot items. One of the items shuffled by random will be tried to steal from the targeted player.
		failsignal: name of the signal that should be send to the mob if the stealing failed. default signal = steal_fail
		oksignal: name of the signal that should be send to the mob if the stealing was good. default signal = steal_ok
		
	- DropStolenItems
		use this skill and the mob drop all its stolen items. Good idea to use it on death. Or all the items are gone with the mob to herobrine.
		
	There is a buildin cancel damage condition if the mob should make no damage while try to steal. It can be activated if the stance of the mob is "gostealing"
	So if the mob have the gostealing stance set, it will do no damage to its target.

Example mobfile:
```
thiefzombie:
  Type: zombie
  Skills:
  - skill{s=StealSkill} ~onTimer:60 1
  - DropStolenItems ~onDeath 1
```
Example skillfile:
```
StealSkill:
  Cooldown: 1
  TargetConditions:
  - lineofsight true
  - distance{d=<3} true
  Skills:
  - steal{items=DIAMOND_SWORD:1,IRON_SWORD:1,DIAMOND:3,EMERALD:3;failsignal=steal_fail;oksignal=steal_ok} @target 1
```
# CustomSkillMechanics v1.16
for MythicMobs 4.0.1 and Spigot 1.8.8 and higher

#### *** 14.7.2017 *** added bounce=true/false(default) & bred=0.2f(default) & onbounceskill to effect/mythicprojecitle mechanic Use this options to bounce the projectile.
#### *** 13.7.2017 *** added MythicEffectProjectile, fixed pfacedir for mythicprojectiles and some more. See MythicProjectiles for details.
#### *** 10.7.2017 *** added setmeta, delmeta mechanics & hasmeta condition. See MetaMechanics for details.
#### *** 08.7.2017 *** fixed issue for MythicPlayers when change world by teleport or portal.
#### *** 05.7.2017 *** some work on the projectiles & mythicplayers. See CustomProjectiles/MythicPlayers for details.
#### *** 04.7.2017 *** (alpha)implemented MythicPlayers addon. See MythicPlayers for more details and examples.
#### *** 03.7.2017 *** (alpha)added mythicorbitalprojectile & some cleanup. See mythicorbitalprojectile for details and example. (known issue: the voffset applies very very late)
#### *** 02.7.2017 *** added mythicprojectile and customparticles. See customprojectiles mechanic for details and example.
#### *** 30.6.2017 *** last finally added rpgitem armor support to customdamage & added pspin to entityprojectile
#### *** 29.6.2017 *** finally (hopefully) added rpgitem armor support to customdamage mechanic.
#### *** 29.6.2017 *** some optimization of customdamage and maybe fix for rpgitems plugin
#### *** 27.6.2017 *** fixed compatibility with 4.0.1
#### *** 25.6.2017 *** added itemprojectile, blockprojectile & entityprojectile. Beta in meanwhile. Report issues. See CustomPrjectiles for more details. MythicMobs 4.1.0 or higher
#### *** 22.6.2017 *** added targetinsight option to customteleport mechanic. destination targeter now parse variables. Changed sdelay to teleportdelay. See customteleport for more details.
#### *** 21.6.2017 *** added some more improvements to customteleport. See customteleport for details and example.
#### *** 20.6.2017 *** added setowner boolean to customsummon. See customsummon for details.
#### *** 14.6.2017 *** added customteleport mechanic. See customteleport for details. Beta in meanwhile. Released in hurry.
#### *** 14.6.2017 *** added ranged amount to customdamage. See customdamage mechanic for details.
#### *** 11.6.2017 *** added temporary biome condition fix for randomspawners with spawn method ADD. See biomefix for details.
#### *** 10.6.2017 *** added dropmythicitem skill. See dropmythicitem mechanic for details.
#### *** 26.5.2017 *** added ignoredestoffsetyaw (idoy) to customparticleline. See customparticleline mechnic.
#### *** 25.5.2017 *** added customparticleline & added keeptargetyaw and keepcasteryaw to swap mechanic. See customparticleline and swap mechanic for details. MythicMobs 4.1.0 or higher
#### *** 24.5.2017 *** added percentage to customdamage. See customdamage for details.
#### *** 24.5.2017 *** added setthreattarget & clearthreattarget mechanics.
#### *** 24.5.2017 *** added swap mechanic.
#### *** 20.5.2017 *** added useEyeDirection and inFrontBlocks attributes to customsummon mechanic.
#### *** 20.5.2017 *** finally fixed all attribute in mobsinradius.
#### *** 19.5.2017 *** added stun mechanic & isstunned condition.
#### *** 19.5.2017 *** added CustomSummon mechanic. Usage same as the original MythicMobs summon but in addition you can use addx(ax),addy(ay),addz(az) to the location. 
#### *** 19.5.2017 *** fixed casesensitive for all in mobsinradius condition.
#### *** 9.5.2017 **** activated MythicMobs ~onKill trigger for all ActiveMobs (see example at the end of the readme)
#### *** 8.5.2017 **** added lastdamagecause condition.
#### *** 6.5.2017 **** merged with CustomConditions & added mobsinradius condition.
#### *** 30.4.2017 *** added removepotion, feed & oxygen mechanics.
#### *** 26.4.2017 *** fixed issues in customdamage. now that skill work how it should.
#### *** 23.4.2017 *** added customdamage mechanic as a temporary fix for damage mechanic.
#### *** 7.4.2017 **** fixed random bug in damagearmor and added support for negative values. Because of that changed ranged syntax from "1-2" to "1to2"



# Mechanics:



## MythicPlayers Module

	#### new since 1.16:
		- settarget:
			+ selfnotarget=true/false(default) set the players target to self if the player do not have a target at crosshair. Bit hacky workaround for skill not beeing triggered if no target is set.

	With this module you can turn any player on your server into a MythicPlayer. With almost all abilities of a MythicMobs mob. But there are some limitations. Because a player is a player and not a mob
	its very important that you make all the mob yamls that will be used for player ++persistent++ otherwise it will break your server and because of that fact you can only use MythicMobs yamls that have
	persistent set to true. Well thats the only limit.

	### ActivePlayer mechanic:
		Transform the targeted player into a mythicplayer.
		
		m = Any valid MythicMob configuration with persistent option enabled.
	
	### NormalPlayer mechanic:
		Make the mythicplayer player a ordinary player again.
		
		No further options needed here.
		
	### SetTarget mechanic:
		This mechanic is used to update the crosshair target of the player.
		
		selfnotarget = snt = true/false(default) If enabled the target of the player will be set to self. Thats a hacky workaround for skills not being triggered if no target is avail.
		
Example configuration for a full working MythicPlayer (Summon the PlayerMonkey and interact with it to turn into the MythicPlayer mob or damage the mob to be normal player):

Mob yaml file:
```
PlayerMonkey:
  Type: villager
  Display: "&cPlayer Monkey"
  Health: 20
  AIGoalSelectors:
  - 0 clear
  AITargetSelectors:
  - 0 clear
  Skills:
  - activeplayer{m=MythicPlayer} @trigger ~onInteract
  - normalplayer @trigger ~onDamaged 

MythicPlayer:
  Type: player
  Options:
    Persistent: true
  Modules:
    ThreatTable: true
  Skills:
  - firework{t=1;d=0;f=true;tr=true} @selflocation ~onSpawn
  - skill{s=PlayEffectOnTarget} ~onTimer:20
  - particlesphere{particle=angryVillager;amount=10;radius=1} @trigger ~onAttack
  - heal{a=5} @self ~onDamaged <25% 1
  - skill{s=ApplyStealth} @self ~onCrouch
  - skill{s=RemoveStealth} @self ~onUnCrouch
  - message{msg="use"} @self ~onUse
```
Skill yaml file:
```
ApplyStealth:
  Skills:
  - message{msg="crouch"} @self
  - potion{type=INVISIBILITY;duration=999999;level=4} @self
  - particlesphere{particle=cloud;amount=20;radius=1} @self

RemoveStealth:
  Skills:
  - message{msg="uncrouch"} @self
  - particlesphere{particle=cloud;amount=20;radius=1} @self
  - removepotion{p=INVISIBILITY} @self
  
PlayEffectOnTarget:
  Skills:
  - settarget
  - particlesphere{particle=flame;amount=10;radius=1} @target
```


## CustomProjectiles mechanics (for MythicMobs 4.1.0 or higher) based on the idea of muhahahahahe:

	#### new since 1.16:
		- mythicprojectiles/effectprojectile:
			+ pfacedir=true/false(default) Projectile object will face in movement direction.
			+ pfoff=value(0.0 default) The front offset of the object. A value of -1.0 is about the center of the caster mob.
			+ targetable=true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
			+ eyedir=true/false(default) If eyedir is used. The projectile do not need a target. It will be shoot in the entities look direction.
			+ invis=true/false(default) to apply invisible to the projectile.
			+ bounce=true/false(default) if enabled the projectile bounce from the ground. only make sense if gravity is used and stoponground=false.
			+ bred=0.2f(default) the amount the bouncing velocity is reduced.
			+ onbounceskill=onbounce=ob=skillname The skill that will be executed if the projectile hit the ground.
		
		- mythicorbitalprojectiles:
			+ pfacedir=true/false(default) Projectile object will face in movement direction.
			+ targetable=true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
			+ the owner of the orbital projectile will be set to its caster.
			+ tag=value If used this tag will be parsed and added as metatag to the orbitalentity.
			+ invis=true/false(default) to apply invisible to the projectile.
			
	### ItemProjectile:
		Shoot any Item. Use it like the original MythicMobs projectile mechanic. In addition use pitem or pobject to choose any droppable item.
		
		pobject = Any valid bukkit item type.
		
	### BlockProjectile:
		Shoot any FallingBlock. Use it like the original MythicMobs projectile mechanic. In addtion use pblock or pobject to choose any FallingBlock.
		
		pobject = Any valid Bukkit fallingblock type.
		
	### EntityProjectile:
		Shoot any Entity. Use it like the original MythicMobs projectile mechanic. In addtion use pentity or pobject to choose any Entity. Since v1.11 you can use pspin to spin the entity where pspin=VALUE value is the speed.
		
		pobject = Any valid bukkit entity type.
		
	### MythicProjectile / MythicEffectProjectile:
		Shoot any MythicMobs mob as a projectile. In addition use pobject or pmythic to choose a existing MythicMobs mob. See example for details.
		If you dont need the projectile object you can use MythicEffectProjectile and use the futures of mythicprojectile without object.

		pobject = MythicMob used for the projectile object
		pfoff = value(0.0 default) The front offset of the object. A value of -1.0 is about the center of the caster mob.
		pvoff = value(0.0 default) The vertical offset of the object.
		pfacedir = true/false(default) Projectile object will face in movement direction.
		eyedir = true/false(default) If eyedir is used. The projectile do not need a target. It will be shoot in the entities look direction.
		targetable = true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
		pspin = value(0 default) If there is a value != 0 the pobject will be spinned at value speed.
		invis = true/false(default) if invisible will be applied to the pobject
		bounce=true/false(default) if enabled the projectile bounce from the ground. only make sense if gravity is used and stoponground=false.
		bred=0.2f(default) the amount the bouncing velocity is reduced.
		onbounceskill=onbounce=ob=skillname The skill that will be executed if the projectile hit the ground. For bounce example see below.
		
	### MythicOrbitalProjectile:
		Summon a orbital object on the targeted entity. Do not work on locations. The owner of the orbital projectile will be set to its caster. See the example who to use it.
		
		pobject = MythicMob used for the orbital object
		oradx = radius x
		orady = radius y
		oradz = radius z axsis.
		oradsec = how much angle added per sec. (speed)
		pvoff = vertical offset.
		pfacedir = true/false(default) Projectile object will face in movement direction.
		targetable = true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
		tag = if used it will add a parsed metatag to the orbitalentity. Example: tag=<trigger.uuid>
		invis = true/false(default) if invisible will be applied to the pobject
		
```
ItemProjectile:
  Skills:
  - itemprojectile{pitem=DIRT;ontick=IP-Tick;onend=IP-Hit;v=20;i=1;hR=1;vR=1}
IP-Tick:
  Skills:
  - effect:particles{p=cloud;amount=1;speed=0;hS=0.2;vS=0.2} @origin
IP-Hit:
  Skills:
  - effect:particles{p=lava;amount=20;speed=0;hS=0.2;vS=0.2} @origin
  - damage{a=10}
```
MythicProjectile Example:

```
Mobs Yaml: https://pastebin.com/6U6iUxkF
Skill Yaml: https://pastebin.com/Vnanzhmz
Items Yaml: https://pastebin.com/ddkeJaqG
```
MythicOrbitalProjectile Example:
```
MythicOrbitalBullet:
  Skills:
  - mythicorbitalprojectile{pobject=MythicOrbital1;ontick=IP-Tick-dust-blue;i=1;hR=0;vR=0;oradx=0;oradz=1.5;orady=-1.5;oradsec=3;md=20000;se=false;sb=false;pvoff=1}
  - delay 40
  - mythicorbitalprojectile{pobject=MythicOrbital1;ontick=IP-Tick-dust-red;i=1;hR=0;vR=0;oradx=0;oradz=1.5;orady=1.5;oradsec=3;md=20000;se=false;sb=false;pvoff=1}
  - delay 40
  - mythicorbitalprojectile{pobject=MythicOrbital1;ontick=IP-Tick-dust-green;i=1;hR=0;vR=0;oradx=1.5;oradz=1.5;orady=-1.5;oradsec=3;md=20000;se=false;sb=false;pvoff=1}
  - delay 40
  - mythicorbitalprojectile{pobject=MythicOrbital1;ontick=IP-Tick-dust-black;i=1;hR=0;vR=0;oradx=1.5;oradz=1.5;orady=1.5;oradsec=3;md=20000;se=false;sb=false;pvoff=1}
```
BouncingBulletSkills:
```
BouncingBullet:
  Cooldown: 1
  Skills:
  - effectprojectile{ontick=BouncingBulletTicking;onbounce=IP-bounce-effect;v=5;i=1;g=0.2;hR=0;vR=0;sb=false;sfo=0;tyo=0;bounce=true;bred=0.5;md=200;mr=100;pfacedir=true} 
BouncingBulletTicking:
  Skills:
  - mythiceffectprojectile{ontick=IP-Tick-bouncing;v=1;i=1;g=0;hR=0;vR=0;eyedir=true;sfo=0;md=1;pfoff=-4}
IP-Tick-bouncing:
  Skills:
  - customparticles{p=reddust;amount=1;hs=0;vs=0;speed=0;yoffset=0;foffset=0} @origin
IP-bounce-effect:
  Skills:
  - customparticles{p=explosion;amount=5;hs=0;vs=0.0;speed=0;yoffset=0.5;foffset=0} @origin
```


## MetaMechanics & Conditions:

	### setmeta mechchanic:
		Set parsed(!) metadata for the target. You can use all variables that are avaible while the skill is executed.
		
			- setmeta{meta="tag=tagname;value=tagvalue;type=BOOLEAN/NUMERIC/STRING"}
			
		The tags "tag" and "value" can contain any mob variable. Example: [- setmeta{meta=<target.uuid>} @self] add the uuid of the target as metatag to the mob.
		You can also use values and types, but this is more for further purpose. Still you can go form them too.
		
			- setmeta{meta="tag=lastdamagedentity;value=<trigger.uuid>;type=STRING"} @self ~onAttack
			
		This will set the lastdamagedentity tag of the mob to the victims uuid.
		It is possible to set a metadata of a block by using a location targeter. All blocks but air are valid.
		
	### delmeta mechanic:
		Delete a metatag from the targeted entity. Be aware, if you remove tags that are not added by yourself, might break something else!
		
			- delmeta{meta="tag=lastdamagedentity"} @self ~onCombatDrop
			
		This remove the lastdamagedentity tag if the mob stop fighting.
		
	### hasmeta condition:
		With this condition you can check any parsed meta. In its main purpose its a compare condition. Mean its a TargetConditions because it needs 2 entities. But by setting the cs tag (compareself)
		in the condition, you can choose if the target or the caster metas are checked. Its also possible to use a list of tags. All mob variables that are useable at the moment the skill is executed
		can be used.

			Syntax:
			- hasmeta{meta="tag=tagname;value=metavalue;type=BOOLEAN/NUMERIC/STRING";cs=true/false;action=true/false}

			Example:
			- hasmeta{meta="tag=lastdamagedentity;value=<target.uuid>;type=STRING";cs=true;action=true}
			
		This will check the caster entity if it has the tag "lastdamagedentity" and if it contains the uuid of the target. If cs=false it would check the target entity.
		The following condition use a list. ATM it will meet the condition if only 1 of the tags match. This will be changed in the future.
		
			Example:
			- hasmeta{meta="tag=<target.uuid>||tag=<trigger.uuid>";cs=true;action=true}
			
			Checks if the caster mob have the tag target uuid or trigger.uuid.
			
		The following example shows how to make it, that every entity can hit the villager only once. After that the entity have to interact with the villager do get removed and can hit him again one time:

Example for metatag mechanics and condition:
```
Mob yaml:

MetaMonkey:
  Health: 1000
  Type: villager
  Display: "Meta Monkey"
  AIGoalSelectors:
  - 0 clear
  Skills:
  - skill{s=cancelDamageIfMeta;sync=true} @trigger ~onDamaged
  - skill{s=setMetaTag} @trigger ~onDamaged
  - skill{s=delMetaTag} @trigger ~onInteract

  
Skill yaml:

delMetaTag:
  TargetConditions:
  - hasmeta{list="tag=<target.uuid>";action=true;cs=true}
  Skills:
  - message{msg="<mob.name> >> <trigger.name> i remove you from my black list!"} @world
  - delmeta{meta="tag=<trigger.uuid>"} @self
  
setMetaTag:
  TargetConditions:
  - hasmeta{list="tag=<target.uuid>";cs=true;action=false}
  Skills:
  - message{msg="<mob.name> >> <trigger.name> i will remember you!"} @world
  - setmeta{meta="tag=<trigger.uuid>"} @self
  
cancelDamageIfMeta:
  TargetConditions:
  - hasmeta{list="tag=<target.uuid>";cs=true;action=true}
  Skills:
  - cancelevent
```




## customteleport skill:
	
	Advanced teleport mechanic. Use this to teleport from/to variable destinations with variable behaviors.
	Options:	destination= MythicMobs targeter or vanilla targeter. Use "" that the targeter can be parsed.
				noise=n= number, random point around the target
				teleportdelay=tdelay=td= number, delay in ticks between teleportation (if more than 1)
				infrontof=front=f= true/false, teleport in front of target (if target is a entity)
				returntostart=return=r= true/false, if the source entity should return to its start position
				betweenlastentitysignal=bls= signalname to be send to caster mob between the teleportations, where the trigger is the last entity (if target was an entity)
				betweennextentitysignal=bns= signalname to be send to caster mob 
				ignoreowner=io= true/false, if the owner of the caster mob should be ignored.
				maxtargets=mt= number, the maximium number of targets.
				targetinsight=insight=is= true/false, only the targets insight of the current position are used.
```
Example Mob:

Monkey:
  Health: 300
  Type: zombie
  Display: "Monkey"
  AIGoalSelectors:
  - 0 clear
  Skills:
  - customsummon{t=ChainDummy;setowner=true} @selflocation ~onDamaged
  
ChainDummy:
  Type: armor_stand
  Options:
    Invisible: true
    Invincible: true
  Skills:
  - customteleport{destination="@EIR{r=40}";teleportdelay=10;front=false;fs=ende;bns=bns;bls=bls;r=false;io=true;is=true} @self ~onSpawn
  - skill{s=chain} @trigger ~onSignal:bns
  - remove @self ~onSignal:ende
  
#  NOTICE THE "" FOR THE DESTINATION!

Example Skill:

chain:
  Skills:
  - lightning
  - customparticleline{particle=reddust;amount=5;color=#feff90;ys=2.5;vd=1.0;hd=-0.5;distanceBetween=0.5;tyo=1.25}
  
  
Or use some variables in the destination targeter:

  - customteleport{destination="@Location{c=<mob.l.x>,<mob.l.y>,<mob.l.z>}"} @eir{r=40} ~onDamaged
  
```
	
	
	
## dropmythicitem skill:
	
	Drop a mythicitem or a mythicdroptable.
	Options: mythicitem=item=itemtype=type=t=i= Internal name of the mythic item or the mythic droptable.
	         amount=a= the amount of the that will be dropped if a mythic item, the amount of droptable creation if a droptable.
	
	- dropmythicitem{item=MythicItem;a=1} @pir{r=20} ~onAttack
	
	This drop one MythicItem at all players in radius of 20 if the mob targets a entity.



## customparticleline skill (for MythicMobs 4.1.0 or higher):

	The same like the original but added vDestOffset & hDestOffset to adjust the targetlocation.
	Use vDestOffset to adjust the height and use hDestOffset to adjust the x/z position. Use idoy true/false(default) = ignoredestoffsetyaw to ignore the yaw of the targetlocation. 
	With this its possible to target a specific part of the entity.
	
    - customparticleline{particle=reddust;amount=5;color=#feff90;ys=2.5;vd=1.0;hd=-1.0;idoy=true;distanceBetween=0.5;tyo=1.25} @pir{r=10} ~onTimer:5



## setthreattarget & clearthreattarget skill:

	Use setthreattarget to clear the activemobs threattable and add {a=double} targeter to the threat. Default amount is 65536.
	
	- setthreattarget{a=11111} @p
	
	To clear the threattable and trigger the dropCombat Event use:
	
	- clearthreattarget @self



## SwapSkill:

	Swap location of caster and target. Use keeptargetyaw=kty false/true or keepcasteryaw=kcy false/true to keep the original direction or not.
	
	- swap{kty=true;kcy=false} @target



## StunSkill:
	
	Use this mechanic to stun the target. 
	
	- stun{duration=Ticks} @target
	
	Where duration=d=how many ticks the target will be stunned. In addition there is the isstunned condition. Look at Conditions.
	


## CustomSummonSkill:

	Use this mechanic to add ax,ay,az to the targetlocation.
	
	- customsummon{type=WITHER_SKELETON;amount=5;radius=4;ax=5;ay=1;az=2} @self
	
	This example do not check for a safeplace it will spawn the mob no matter if its in block or such. If you want to use safeplace add noise. Example:
	
	- customsummon{type=WITHER_SKELETON;amount=5;radius=4;noise=1;ax=5;ay=1;az=2} @self
	
	This example shows how to spawn mobs relative to the direction of the targeted mob:
	
    - customsummon{t=mobname;ued=true;ifb=1} @self
	
	ued=useEyeDirection=EyeDirection;ifb=inFrontBlocks=inFront
	
	Use setowner (true/false) to set the owner to the mob which casted the custumsummon skill.
	
	- customsummon{t=mobname;setowner=true} @selflocation
	
	This summon the mob mobname and set its owner to the mob which casted the skill.



## OxygenSkill:

	Use this mechanic to add oxygen to the LivingEntity.
	
	- oxygen{a=20} @self
	
	a = amount = the amount of oxygen that will be added. A player has a usual max air amount of 300. A amount of 20 gives about 1 bubble air.
	
	
	
### FeedSkill:

	Add specific amount of food to the players foodlevel.
	
	- feed{a=1} @PIR{r=20}
	
	a = amount = the food amount. The amount of 1 add 1/2 foodlevel.



## RemovePotionSkill or CureSkill:

	Use this mechanic to remove all or a specific potion from the targeted entity. Use removepotion or cure.

	- cure{t=ALL} @self
	- cure{t=INVISIBILITY} @PIR{r=30}
	
	- removepotion{t=ALL} @self
	- removepotion{t=INVISIBILITY} @PIR{r=30}
	
	t = type = p = potion = ALL by default. Use "ALL" to remove all effects or use a single effect or a list like: p=INVISIBILITY,SPEED,HEAL

	

## CustomDamageSkill:

	Use this mechanic for a temporary mythicmobs damage mechanic fix. See http://www.mythicmobs.net/index.php?threads/skill-damage-bypassed-armour.3373/ this thread for details of the issue.
	
	- customdamage{a=1to2;ia=false;pk=false;pi=false;iabs=false}
	
	a = amount = 1 by default. The amount of damage applied. 1 = 1/2 heart. Ranged amount possbile: 1to2
	ia = i = ignorearmor = false by default. If the armor of the target should be ignored or not.
	pk = pkb = preventknockback = false by default. If knockback should be applied or not.
	pi = preventimmunity = false by default. If immunity should be used or not.
	ignoreabs = iabs = false by default. If absorbation should be ignored or not.
	percentage=p=false by default. If true it uses the amount as percent.
	pcur=pc=false by fault. Need that percentage=true. If pc is true is use the percent of current health if false it use percent dmg of maxhealth.
	

	

## EquipSkullSkill:

	This is a fix for custom player heads for 1.8.8 as it appeard to not work with mm versions greater than 2.5.1
	- equipskull{skull=mythicitemname}
		skull or s: and valid MythicMobsItem to equip on head.
		
Example:
```
mob yml:

Monkey:
  Type: zombie
  Display: 'a MythicMobs Monkey'
  AIGoalSelectors:
  - 0 clear
  Skills:
  - equipskull{skull=Pirate} @self ~onSpawn 1
  
item yml:

Pirate:
  Id: 397
  Data: 3
  Options:
    SkinTexture: eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVmMDEyOTdlMmUxYWNkMDQ4ODJhMGM0NGU0OGYxZjE1Y2JiYTI1ODJmOTFiMDgxYzkyOTIwZmVkOGYzMjIwIn19fQ====
```


## DamageArmorSkill:

	- damagearmor{armor=<all>||<list>||<single>;damage=<int>||<ranged>;signal=<string>}
		armor: all / helmet / chest / leggings / boots / hand / offhand - can be single name or all or a list
		damage: armor damage amount as integer or as ranged value for ranged value use "to".
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
  # this will damage the whole armor and handitem & offhanditem with damage value of 20
  - damagearmor{armor=all;damage=20;signal=armorbroken} @target ~onDamaged >0 1
  # this will damage only the chest with a random value between 1 to 20
  - damagearmor{armor=chest;damage=1to20;signal=armorbroken} @target ~onDamaged >0 1
  # this will repear hand,offhand,helmet items with a random value between 20 to 40
  - damagearmor{armor=hand,offhand,helmet;damage=-20to-40;signal=armorbroken} @trigger ~onDamaged >0 1
  - message{msg="Oh dear! A part of your armor is gone!"} @trigger ~onSignal:armorbroken
```


## GrenadeSkill:

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


## SetRandomLevelSkill:

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


## StealSkill:

	- steal{items=ITEM:AMOUNT,ITEM:AMOUNT,.....;failsignal=steal_fail;oksignal=steal_ok}
		items: Can be a list of valid spigot items. One of the items shuffled by random will be tried to steal from the targeted player.
		failsignal: name of the signal that should be send to the mob if the stealing failed. default signal = steal_fail
		oksignal: name of the signal that should be send to the mob if the stealing was good. default signal = steal_ok
		
	- DropStolenItems
		use this skill and the mob drop all its stolen items. Good idea to use it on death. Or all the items are gone with the mob to herobrine.
		
	There is a buildin cancel damage condition if the mob should make no damage while try to steal. It can be activated if the stance of the mob is "gostealing"
	So if the mob have the gostealing stance set, it will do no damage to its target.

Thief example:

mob yml:
```
thief:
  Type: villager
  Display: 'Thief'
  Health: 20
  Damage: 0
  Modules:
    ThreatTable: true
  Options:
    AlwaysShowName: false
    Despawn: true
  AIGoalSelectors:
    - 0 clear
    - 1 meleeattack
    - 2 avoidskeletons
    - 3 avoidzombies
    - 4 randomstroll
    - 5 float
  AITargetSelectors:
    - 0 clear
    - 1 players
  Skills:
    - setstance{stance=gostealing} @self ~onSpawn 1
    - skill{s=FleeGotSomeStuff;sync=true} ~onSignal:steal_ok 1
    - skill{s=FleeButGotNothing;sync=true} ~onSignal:steal_fail 1
    - skill{s=Steal} ~onTimer:60 >0 1
    - DropStolenItems ~onDeath 1
```
skillfile:
```
Steal:
  Cooldown: 1
  TargetConditions:
  - distance{d=<3} true
  - lineofsight true
  Conditions:
  - stance{s=gostealing} true
  Skills:
  - steal{items=DIAMOND_SWORD:1,IRON_SWORD:1,DIAMOND:3,EMERALD:3;failsignal=steal_fail;oksignal=steal_ok} @NearestPlayer 0.75
  
FleeGotSomeStuff:
  Cooldown: 1
  Skills:
    - setstance{stance=flee} @self
    - RunAIGoalSelector{s=clear}
    - delay 2
    - RunAIGoalSelector{s=fleeplayers}
    - effect:smoke @self
    - potion{type=SPEED;duration=200;level=1} @self
    - delay 400
    - effect:smoke @self
    - remove @self
  
FleeButGotNothing:
  Cooldown: 1
  Skills:
    - setstance{stance=flee} @self
    - RunAIGoalSelector{s=clear}
    - delay 2
    - RunAIGoalSelector{s=randomstroll}
    - delay 400
    - effect:smoke @self
    - remove @self
```



# Conditions



```
  Conditions:
  - biomefix{biome=PLAINS,DESSERT;action=true}
```
Check if the target is in a certain biome.
biome=b= A list with valid biome names.
action=a= true / false
```
  TargetConditions:
  - isstunned{a=false}
```
Check if the target is stunned (true) or not (false) The example will match if the target isnt stunned. Please notice that this can be used at the caster, or as TargetConditions.
If used as TargetConditions the targeter for the metaskill is important.
```
  Conditions:
  - lastdamagecause{cause=ENTITY_ATTACK,PROJECTILE,FIRE;damager=PLAYER,ZOMBIE,SKELETON;action=TRUE}
```
Check what caused the last damage to the MythicMob mob. cause and damager can be a single value or a List
cause=c= The cause of the last damage. Valid is "ANY" or Bukkit's DamageCause https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html
damager=attacker= The EntityType of the attacker. Valid is "ANY" or Bukkit's EntityTypes https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html
```
  Conditions:
  - mobsinradius{mobtypes=mythicmobs1,mythicmobs2,mythicentity1,mythicentity2;a=5to10;r=20;action=TRUE}
```
Use this condition to check how many mobs are in radius.
mobtypes=types=mobs=mob=t=m= The mythicmobs or mythicentities to check. Or use ALL for any mythicmobs mob.
amount=a=ranged value to match. example: a=<20 or a=>10 or a=5 or a=5to10 for range
radius=r=radius to check
action=true/false
```
  Conditions:
  - wgstateflag{flag=mob-spawning;action=false}
  - wgstateflag{flag=pvp;action=true}
  - wgstateflag{flag=creeper-explosion;action=false}
  .....
``` 
This condition can be used on every allow/deny flag. If region has no flag set, it inherits the flag of the parent region.

```
  Conditions:
  - wgdenyspawnflag{types=zombie,skeleton;action=true}
```
This condition can be used to check if the region denys the spawning of some entitytypes. If region has no flag set, it inherits the flag of the parent region.

```
  Conditions:
  - factionsflag{flag=monsters;action=true}
```
This condition can be used to check if the faction has a specific flag set to true or false. Here is a list of all flagnames: animals, monsters, peaceful, endergrief, explosions, firespread, friendlyfire, infpower, offlineexplosions, open, permanent, powergain, powerloss, pvp, zombiegrief

```
  Conditions:
  - hastarget{action=true}
```
This condition meets if the mob has a target (true) or no target (false).

```
  TargetConditions:
  - vdistance{d=2to3;action=true}
```
This condition checks for the vertical distance between target and mob. Use ">" for greater "<" smaller or "to" for range.

Example:
```
Mobfile:

Monkey:
  Type: zombie
  Display: "&cMythicMobs Monkey"
  Damage: 1
  Health: 10
  Armor: 1
  DamageModifiers:
  - FALL 0.25
  Skills:
  - skill{s=jumpToTarget} @target ~onTimer:50 1
  
Skillfile:

jumpToTarget:
  Conditions:
  - hastarget{a=true}
  TargetConditions:
  - distance{d=<4} true
  - hdistance{d=2to3;a=true}
  Skills:
  - leap{velocity=120} @target
```

#### ~onKill trigger with lastdamagecause condition example

```
BowMonkey:
  Type: skeleton
  Display: "&cBowMonkey"
  AITargetSelectors:
  - 0 clear
  - 1 monsters
  Skills:
  - skill{s=DamageCauseMeele} @self ~onDamaged
  - skill{s=DamageCauseProjectile} @self ~onDamaged
  - skill{s=KillCauseProjectile} @trigger ~onKill
  - skill{s=KillCauseMeele} @trigger ~onKill

MeeleMonkey:
  Type: zombie
  Display: "&cMeeleMonkey"
  AITargetSelectors:
  - 0 clear
  - 1 monsters  
  Skills:
  - skill{s=DamageCauseMeele} @self ~onDamaged
  - skill{s=DamageCauseProjectile} @self ~onDamaged
  - skill{s=KillCauseProjectile} @trigger ~onKill
  - skill{s=KillCauseMeele} @trigger ~onKill

KillCauseProjectile:
  TargetConditions:
  - lastdamagecause{cause=PROJECTILE;damager=ANY;action=TRUE}
  Skills:
  - message{msg="Sorry <trigger.name> i am no Wilhelm Tell though!"} @world
  
KillCauseMeele:
  TargetConditions:
  - lastdamagecause{cause=ENTITY_ATTACK;damager=ANY;action=TRUE}
  Skills:
  - message{msg="I killed <trigger.name> just with my hands only!"} @world
  
DamageCauseProjectile:
  Conditions:
  - lastdamagecause{cause=PROJECTILE;damager=ANY;action=TRUE}
  Skills:
  - message{msg="Someone try to shoot me down!"} @world
  
DamageCauseMeele:
  Conditions:
  - lastdamagecause{cause=ENTITY_ATTACK;damager=ANY;action=TRUE}
  Skills:
  - message{msg="Help! It punch me in my face!"} @world
```
# MythicMobsExtension v1.238 for MythicMobs 4.1 and Spigot 1.10.2 or higher

### Wiki:
https://github.com/BerndiVader/MythicMobsExtension/wiki

Click [here](https://github.com/BerndiVader/MythicMobsExtension#custom-entities) to see stuff that is not yet in the wiki.

### Changelog:
##### ** 07.12.2017 *** removed debug messages from lastdamagecause & ANY in steal mechanic choose a random, no AIR, inventory slot.
##### ** 07.12.2017 *** fixed NPE in dropmythicitem if trigger is not present.
##### ** 07.12.2017 *** added randomspeed mechanic & movespeed condition. See randomspeed ore movespeed for details.
##### ** 07.12.2017 *** added "ANY" item to steal mechanic. See steal mechanic for details.
##### ** 06.12.2017 *** fixed latest issue in lastdamagecause.
##### ** 06.12.2017 *** added cancellable to MythicMobsExtItemDropEvent.
##### ** 05.12.2017 *** added shuffle option for dropmythicitem mechanic & MythicMobsExtItemDropEvent is called whenever this mechanic is used.
##### ** 04.12.2017 *** fixed issue with playerzoom.
##### ** 02.12.2017 *** changed name of project into MythicMobsExtension.
##### ** 02.12.2017 *** tweaked several conditions & mechanics.
##### ** 02.12.2017 *** added range option to isburning condition. See isburning condition.
##### ** 31.12.2017 *** fixed mythic_parrot level not set properly if parrot comes from mythicmobs spawner.
##### ** 30.12.2017 *** fixed parrot cookie issue. Added Options.CookieDie: false/true or use cancelevent ondamaged. See Parrotfix.
##### ** 29.12.2017 *** fixed minor bug in ownsitem condition.
##### ** 28.12.2017 *** added linebubble mechanic. Goto speechbubble mechanics for details.
##### ** 28.12.2017 *** tweaked steal skill.
##### ** 27.12.2017 *** speechbubbles are now able to have item lines only.
##### ** 27.12.2017 *** fixed castif rtskill.
##### ** 27.12.2017 *** updated stun, goggle and spin mechanics can now be overwritten.
##### ** 27.12.2017 *** added option rtskill to castif mechanic. See castif mechanic for details.
##### ** 23.12.2017 *** added usecounter boolean option to speechbubble & modifybubble mechanic. Added timer option to modifybubble to change the timer.
##### ** 23.12.2017 *** added <nl> placeholder to text option in speechbubble mechanic to force a linebreak.
##### ** 22.12.2017 *** added <additem.[material_name]> placeholder to add an itemline for speechbubble mechanics. See speechbubble for details.
##### ** 22.12.2017 *** added modifybubble & removebubble mechanic. See speechbubble mechanics for details.
##### ** 22.12.2017 *** added id option to speechbubble mechanic.
##### ** 22.12.2107 *** added anim=true/false to speechbubbles.
##### ** 21.12.2017 *** added lastbowtension condition. See bowtension condition for details.
##### ** 20.12.2017 *** added side & forward offset to speechbubbles.
##### ** 20.12.2017 *** added stopai true/false to stun mechanic. See stun mechanic for details.
##### ** 20.12.2017 *** added getbowtension condition. See bowtension condition for details.
##### ** 19.12.2017 *** added a condition to get the damage indicator percentage. See getindicator condition. 
##### ** 18.12.2017 *** added playerzoom, setitemcooldown & oncooldown condition.
##### ** 15.12.2017 *** added testfor condition. See testfor condition for details.
##### ** 15.12.2017 *** added triggerdirection, targetdirection & ownerdirection targeter. See targeters for details.
##### ** 14.12.2017 *** added ownsitemsimple variant condition. See ownsitem condition.
##### ** 14.12.2017 *** added custom variable parse to parsedstance.
##### ** 13.12.2017 *** fixed some MythicPlayer related stuff.
##### ** 11.12.2017 *** added some options to shootattack pathfindergoal.
##### ** 11.12.2017 *** added attack pathfindergoal. Meanwhile 1.12 only. See pathfinders for details.
##### ** 09.12.2017 *** added sameworld targetcondition.
##### ** 09.12.2017 *** added onsolidblock & insolidblock conditions.
##### ** 09.12.2017 *** fixed server shutdown NPE for healthbars if they not enabled.
##### ** 07.12.2017 *** updated all sensitive code to work with 1.10.2 - 1.12.2 servers
##### ** 06.12.2017 *** added shootattack pathfindergoal. See pathfinders for details.
##### ** 05.12.2017 *** added dropinventory mechanic. See dropinventory mechanic.
##### ** 04.12.2017 *** added leashtocaster option for customsummon mechanic.
##### ** 04.12.2017 *** added hasmetasimple condition. Simplyfied hasmeta. See hasmeta condition.
##### ** 03.12.2017 *** fixed bug where conditions action only work with uppercase entries.
##### ** 03.12.2017 *** added modifyarrows on entity body. Change the amount of arrows targets body. See mechanics for details.
##### ** 02.12.2017 *** added fakedeath mechanic. See mechanics for details.
##### ** 02.12.2017 *** added arrowcount condition. See conditions for details.
##### ** 02.12.2017 *** added playloading and closeinventory mechanics. See mechanics for details.
##### ** 28.11.2017 *** added jumping, running, crouching & sleeping conditions. See conditions for details.
##### ** 27.11.2017 *** added playcredits & forcespectate mechanics. (Experimental)
##### ** 26.11.2017 *** fixed setrotation not working properly on 1.12.2
##### ** 26.11.2017 *** added setmobhealth mechanic. See setmobhealth mechanic for details.
##### ** 25.11.2017 *** some minor changed for speechbubbles.
##### ** 21.11.2017 *** tweaked disarm to work for all LivingEntities & added disarm duration. See disarm for details.
##### ** 20.11.2017 *** added setrotation & entitygoggleat mechanic. See setrotation & entitygoggleat for example.
##### ** 20.11.2017 *** fixed async error in speechbubble mechanic & some minor fixes & added an example.
##### ** 20.11.2017 *** changed movement method for blockfloating to fix fallingblocks glitches. Thanks aDaxxas!
##### ** 17.11.2017 *** added speechbubble mechanic. See speechbubble mechanic for details. (requires HolographicDisplay plugin)
##### ** 16.11.2017 *** added gravity option to stun mechanic. See stun mechanic for details.
##### ** 14.11.2017 *** added disarm mechanic. See disarm mechanic for details.
##### ** 11.11.2017 *** added material type air for metamechanics & conditions if used for locations.
##### ** 08.11.2017 *** added hasspawner & samespawner conditions. See conditions for details.
##### ** 05.11.2017 *** added playergoggleat mechanic & isgoggling and isspinning conditions. See playergoggle mechanic for details.
##### ** 05.11.2017 *** tweaked stun skill & added playerspin mechanic. See playerspin mechanic for details.
##### ** 04.11.2017 *** added isvehicle targetcondition. See isvehicle condition for details.
##### ** 04.11.2017 *** tweaked stun skill. facing=true now works for players & passengers will not be ejected.
##### ** 04.11.2017 *** tweaked castif mechanic.
##### ** 31.10.2017 *** added ispresent condition.
##### ** 30.10.2017 *** added extinguish mechanic.
##### ** 29.10.2017 *** tweaked customtargeters.
##### ** 28.10.2017 *** added isburning condition.
##### ** 28.10.2017 *** added forward offset to customteleport mechanic.
##### ** 28.10.2017 *** tweaked customprojectiles abit.
##### ** 26.10.2017 *** added eir, leir & pir condition. See entitiesinradius condition for details.
##### ** 23.10.2017 *** added eyedirection targeter. See targeters for details.
##### ** 20.10.2017 *** added targetstarget & triggerstarget targeter. See targeters for details.
##### ** 16.10.2017 *** fixed infront & behind condition as it was never finished. :)
##### ** 14.10.2017 *** fix for MythicMobs RandomSpawn add not working after server start.
##### ** 14.10.2017 *** fix for NPE in MythicPlayer DamageEvent.
##### ** 12.10.2017 *** added lastdamager & crosshair targeter.
##### ** 11.10.2017 *** added invisible option to customsummon mechanic.
##### ** 11.10.2017 *** added mythicfloating and support for location targets to all floating mechanics. See floating mechanics for details.
##### ** 10.10.2017 *** added blockfloating and entityfloating mechanic.
##### ** 10.10.2017 *** fixed npe at first startup.
##### ** 10.10.2017 *** experimental itemfloating mechanic.
##### *** 9.10.2017 *** fixed several bugs.
##### *** 9.10.2017 *** added playerweather & playertime conditions. 
##### *** 9.10.2017 *** added asequip mechanic. mainly to equip head of armorstand with any Bukkit Material Type.
##### *** 9.10.2017 *** added ownertarget targeters. Targets the target of the owner of the mob. (Only work with mm 4.3 or patched 4.2)
##### *** 9.10.2017 *** added playerweather mechanic. See playerweather for details.
##### *** 9.10.2017 *** final fix for itemprojectile.
##### *** 7.10.2017 *** added owneralive condition. See ownerlaive condition for details.
##### *** 7.10.2017 *** fixed some problems in parsedstance mechanic.
##### *** 7.10.2017 *** added sideOffset & frontOffset to createhelthbar mechanic. See createhealthbar mechanic for details.
##### *** 5.10.2017 *** maybe fix for itemprojectile mechanic.
##### *** 3.10.2017 *** maybe customsummon mechanic npe fix.
##### *** 30.9.2017 *** added infaction and samefaction conditions. See conditions for details.
##### *** 29.9.2017 *** added setfaction mechanic. See setfaction mechanic for details.
##### *** 29.9.2017 *** added usecaster option to setmeta mechanic. See setmeta mechanic for details.
##### *** 24.9.2017 *** added boolean expression to iteminhand condition. See iteminhand condition for details. 
##### *** 24.9.2017 *** added lookatme condition. See lookatme condition for details.
##### *** 24.9.2017 *** fixed lore issue in iteminhand condition & added ownsitem alias for it.
##### *** 24.9.2017 *** added parseddisguise mechanic. See parseddisguise mechanic for details.
##### *** 24.9.2017 *** added facing true/false for stun mechanic. See stun mechanic for details.
##### *** 20.9.2017 *** added relativedirection condition. See relativedirection condition for details.
##### *** 20.9.2017 *** hopefully fixed helper finally.
##### *** 19.9.2017 *** fixed helper class not loading properly.
##### *** 19.9.2017 *** added renameentity mechanic. See renameentity for details
##### *** 19.9.2017 *** added hasitem condition. See hasitem condition for details.
##### *** 18.9.2017 *** improved grenade skill to work with eyedirection and targetdirection. See grenade mechanic for details.
##### *** 17.9.2017 *** some improvments and cleanup.
##### *** 17.9.2017 *** added range value to customsummon amount option.
##### *** 17.9.2017 *** fixed issue where customsummon not working properly at MythicMobs 4.3
##### *** 17.9.2017 *** added https://github.com/lucko/helper loader for further usage.
##### *** 13.9.2017 *** PathfinderGoalReturnHome now 1.10-1.12.1 compatible.
##### *** 13.9.2017 *** added inmotion condition. See conditions for more details.
##### *** 12.9.2017 *** added returnhome pathfindergoal. See advaipathfinder for more details. Meanwhile 1_12 only. Needs some more testing.
##### *** 11.9.2017 *** added Block break chance to PathfinderBreakBlocks. See advaipathfinder for details.
##### *** 11.9.2017 *** added FacingDirection condition. See FacingDirection condition for details
##### *** 10.9.2017 *** improved PathfingerGoalFollowEntity. See advaipathfinder for details.
##### *** 04.9.2017 *** added materialtypes to breakblocks pathfindergoal. See advaipathfinder for details.
##### *** 04.9.2017 *** added breakblocks pathfindergoal. See advaipathfinder for more details.
##### *** 01.9.2017 *** added setcachedowner mechanics. See setcachedowner for details.
##### *** 29.8.2017 *** added tag option to customsummon mechanic. See customsummon for details.
##### *** 27.8.2017 *** added advrandomskill mechanic. See advrandomskill for details.
##### *** 27.8.2017 *** maybefix for issue where healthbars are not removed.
##### *** 27.8.2017 *** fixed an issue where lastdamagecause condition not working propertly.
##### *** 27.8.2017 *** added mobs config node "onDamageForOtherCause: true/false" to activate onDamage trigger for all none entity damage like lava fire etc... use lastdamagecause to react on the damagecause.
##### *** 25.8.2017 *** added counter option to createhealthbar mechanic. See createhealthbar for details.
##### *** 25.8.2017 *** added 1.12.1 support.
##### *** 10.8.2017 *** added changehealthbar mechanic. See changehealthbar mechanic for details.
##### *** 10.8.2017 *** fixed createhealthbar mechanic & added display option to skill. See createhealthbar for details.
##### *** 10.8.2017 *** added createhealthbar mechanic. Requires the holographicdisplay plugin to work. See createhealthbar mechanic for details.
##### *** 10.8.2017 *** fixed (maybe) several issues with other plugins & fixed mobsinradius compare distance with different worlds. 
##### *** 09.8.2017 *** fixed compatibility issues with 1.10 and 1.11 server.
##### *** 06.8.2107 *** added followentity pathfindergoal. See advaipathfinder mechanic for details.
##### *** 06.8.2017 *** added advaipathfinder mechanic, rangedmelee & runfromsun pathfindergoal. See advaipathfinder mechanic for more.
##### *** 04.8.2017 *** since now, take care of the wiki: https://github.com/BerndiVader/mmCustomSkills26/wiki made by muhahahahahe thx alot to him.
##### *** 04.8.2017 *** added parsedstance mechanic & parsedstance target/compare condition. See parsedstance mechanic for details.
##### *** 03.8.2017 *** added meettargeter & elsetargeter to castif mechanic. See castif for details.
##### *** 01.8.2017 *** fixed some bugs. Fixed compatibility with MM 4.2, added infront, behind & attackable/damageable condition. See conditions for more info.
##### *** 26.7.2017 *** added patch to fix NaN in player.dat's AbsorptionAmount tag.
##### *** 20.7.2017 *** some work on item, block & entity projectile. now working with bounce.
##### *** 19.7.2017 *** final fix for location bug in orbit projectile.
##### *** 19.7.2017 *** added unequip mechanic. More work on castif, now possible to use it like boolean expressions. See castif for more info.
##### *** 17.7.2017 *** added inMobArena condition.
##### *** 16.7.2017 *** added castif mechanic. See CastIf mechanic for details.
##### *** 14.7.2017 *** added bounce=true/false(default) & bred=0.2f(default) & onbounceskill to effect/mythicprojecitle mechanic Use this options to bounce the projectile.
##### *** 13.7.2017 *** added MythicEffectProjectile, fixed pfacedir for mythicprojectiles and some more. See MythicProjectiles for details.
##### *** 10.7.2017 *** added setmeta, delmeta mechanics & hasmeta condition. See MetaMechanics for details.
##### *** 08.7.2017 *** fixed issue for MythicPlayers when change world by teleport or portal.
##### *** 05.7.2017 *** some work on the projectiles & mythicplayers. See CustomProjectiles/MythicPlayers for details.
##### *** 04.7.2017 *** (alpha)implemented MythicPlayers addon. See MythicPlayers for more details and examples.
##### *** 03.7.2017 *** (alpha)added mythicorbitalprojectile & some cleanup. See mythicorbitalprojectile for details and example. (known issue: the voffset applies very very late)
##### *** 02.7.2017 *** added mythicprojectile and customparticles. See customprojectiles mechanic for details and example.
##### *** 30.6.2017 *** last finally added rpgitem armor support to customdamage & added pspin to entityprojectile
##### *** 29.6.2017 *** finally (hopefully) added rpgitem armor support to customdamage mechanic.
##### *** 29.6.2017 *** some optimization of customdamage and maybe fix for rpgitems plugin
##### *** 27.6.2017 *** fixed compatibility with 4.0.1
##### *** 25.6.2017 *** added itemprojectile, blockprojectile & entityprojectile. Beta in meanwhile. Report issues. See CustomPrjectiles for more details. MythicMobs 4.1.0 or higher
##### *** 22.6.2017 *** added targetinsight option to customteleport mechanic. destination targeter now parse variables. Changed sdelay to teleportdelay. See customteleport for more details.
##### *** 21.6.2017 *** added some more improvements to customteleport. See customteleport for details and example.
##### *** 20.6.2017 *** added setowner boolean to customsummon. See customsummon for details.
##### *** 14.6.2017 *** added customteleport mechanic. See customteleport for details. Beta in meanwhile. Released in hurry.
##### *** 14.6.2017 *** added ranged amount to customdamage. See customdamage mechanic for details.
##### *** 11.6.2017 *** added temporary biome condition fix for randomspawners with spawn method ADD. See biomefix for details.
##### *** 10.6.2017 *** added dropmythicitem skill. See dropmythicitem mechanic for details.
##### *** 26.5.2017 *** added ignoredestoffsetyaw (idoy) to customparticleline. See customparticleline mechnic.
##### *** 25.5.2017 *** added customparticleline & added keeptargetyaw and keepcasteryaw to swap mechanic. See customparticleline and swap mechanic for details. MythicMobs 4.1.0 or higher
##### *** 24.5.2017 *** added percentage to customdamage. See customdamage for details.
##### *** 24.5.2017 *** added setthreattarget & clearthreattarget mechanics.
##### *** 24.5.2017 *** added swap mechanic.
##### *** 20.5.2017 *** added useEyeDirection and inFrontBlocks attributes to customsummon mechanic.
##### *** 20.5.2017 *** finally fixed all attribute in mobsinradius.
##### *** 19.5.2017 *** added stun mechanic & isstunned condition.
##### *** 19.5.2017 *** added CustomSummon mechanic. Usage same as the original MythicMobs summon but in addition you can use addx(ax),addy(ay),addz(az) to the location. 
##### *** 19.5.2017 *** fixed casesensitive for all in mobsinradius condition.
##### *** 9.5.2017 **** activated MythicMobs ~onKill trigger for all ActiveMobs (see example at the end of the readme)
##### *** 8.5.2017 **** added lastdamagecause condition.
##### *** 6.5.2017 **** merged with CustomConditions & added mobsinradius condition.
##### *** 30.4.2017 *** added removepotion, feed & oxygen mechanics.
##### *** 26.4.2017 *** fixed issues in customdamage. now that skill work how it should.
##### *** 23.4.2017 *** added customdamage mechanic as a temporary fix for damage mechanic.
##### *** 7.4.2017 **** fixed random bug in damagearmor and added support for negative values. Because of that changed ranged syntax from "1-2" to "1to2"



# Custom Entities:

## Mythic Parrot:

Bukkit's entity type name: "mythic_parrot" `/summon mythic_parrot` 
Custom parrot class for better handling of the cookie thing.



# Mechanics:



## MythicPlayers Module

##### `- settarget` (deprecated if you use patched 4.2 or MythicMobs 4.3 use `@crosshair` instead):
`selfnotarget=true/false(default)` set the players target to self if the player do not have a target at crosshair. Bit hacky workaround for skill not being triggered if no target is set.

With this module you can turn any player on your server into a MythicPlayer. With almost all abilities of a MythicMobs mob. But there are some limitations. Because a player is a player and not a mob its very important that you make all the mob yamls that will be used for player ++persistent++ otherwise it will break your server and because of that fact you can only use MythicMobs yamls that have persistent set to true. Well thats the only limit.

### ActivePlayer mechanic:
##### `- activeplayer{m=[MythicPlayerType]} @trigger ~onInteract`

Transform the targeted player into a mythicplayer.

+ `m=[MobType]` Any valid MythicMob configuration with persistent option enabled.
	
### NormalPlayer mechanic:
##### `- normalplayer @self`

Make the mythicplayer player a ordinary player again. No further options needed here.
		
### SetTarget mechanic:
##### `- settarget{snt=[boolean]`

This mechanic is used to update the crosshair target of the player.
+ `selfnotarget=snt=true/false(default)` If enabled the target of the player will be set to self. Thats a hacky workaround for skills not being triggered if no target is avail.
		
Example configuration for a full working MythicPlayer (Summon the PlayerMonkey and interact with it to turn into the MythicPlayer mob or damage the mob to be normal player):

Mob yaml file:

```yaml
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

```yaml
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



## randomspeed mechanic:

Force the target to drop an item from its storagte/equipment to the ground.

##### `- randomspeed{range=[range]}`

Where in `[range]` is the range the new random speed is picked.
Example:

```yaml
newSpeed:
  Skills:
  - randomspeed{range=0.2to0.5} @self ~onInteract
```




## itemfloating / blockfloating / entityfloating / mythicfloating mechanic:

Summon a floating item, block, entity or mythicmobs mob relative to caster or static at location.
	
##### `- itemfloating{i=SPONGE;md=250;yo=0.4;so=-0.5;fo=0.9} ~onSpawn`

##### `- blockfloating{m=DIRT;md=250;yo=2;so=0;fo=0} ~onSpawn`

##### `- entityfloating{e=pig;md=250;yo=2;so=0;fo=0;noai=true} ~onSpawn`

##### `- mythicfloating{m=MythicMobType;md=250;yo=2;so=2;fo=2} ~onSpawn`
	 
Almost all options like in mythicprojectile are useable.



##### `- setitemcooldown{ticks=120} @trigger ~onDamaged`

Set the item used for the attack to a cooldown of 120 ticks.



## setitemcooldown mechanic:

Set the cooldown of the selected item. Only avail for player targets.

##### `- setitemcooldown{ticks=120} @trigger ~onDamaged`

Set the item used for the attack to a cooldown of 120 ticks.



## modifyarrows mechanic:

Modify the amount of arrows on the targets body. Use "ADD, SUB, CLEAR" for mode and integer value for amount.

##### `- modifyarrows{mode=clear;amount=0} @self ~onInteract`

Removes all arrows  from the targeted body.

##### `- modifyarrows{mode=add;amount=4} @self ~onInteract`

Adds 4 arrows to the arrows on targeted entity's body.

##### `- modifyarrows{mode=sub;amount=4} @self ~onInteract`

Subtract 4 arrows from the total amount of arrows on targeted entity's body.

	


## playloading/playcredits mechanic:

Force the player to see the loading screen or the end screen. With a repeat skill its possible to make a blackscreen for the player.

```yaml
playloading:
  Skills:
  - playloading{repeat=60;repeatInterval=1}
  - closeinventory{delay=61}
```


## advaipathfinder mechanic:

Use this mechanic to add custom pathfinder goals or any other mythicmobs pathfindergoal parsed for variables. Its a NoTargetMechanic and therefor always be used at caster.
	  
##### `- advaipathfinder{goal="[goalpriority_value] [pathfindergoal_text] [data_text]"}`
	  
Some examples:
	  
```yaml
- advaipathfinder{goal="clear"}
- advaipathfinder{goal="2 runfromsun 2"}
- advaipathfinder{goal="3 goto 0,0,0"}
- advaipathfinder{goal="4 goto <target.l.x>,<target.l.y>,<target.l.z>"}
- advaipathfinder{goal="1 randomstroll"}
```
		
### Pathfindergoals:

*attack:*
	
##### `advaipathfinder{goal="[goalpriority_value] attack [move_speed] [attack_range]"}`
	
+ Syntax in advaipathfinder: `- advaipathfinder{goal="1 attack 1.5 3"}`
+ Above example make the mob attack its target at a speed of 1.5 and a melee range of 3 blocks.
+ Make the mob attack its targets. **The goal sends the signal AIHIT** whenever it do an attack. Catch the signal in the mob skills to make highly customized attacks!
+ MORE ADVANCED OPTIONS IN PROGRESS!

*shootattack:*

##### `advaipathfinder{goal="[goalpriority_value] shootattack [follow_speed],[range1],[attack_speed],[distance]"}`
+ Syntax in advaipathfinder: `- advaipathfinder{goal="1 shootattack"}`
+ Set goal 1 to PathfindergoalShootAttack. Will make ANY creature entity able to range attack. Sends the signal AISHOOT to customize the projecitle

Example:

```yaml
VillagerHood:
  Type: villager
  Display: "Villager Hood"
  AITargetSelectors:
  - 0 clear
  - 1 players
  Skills:   
  - skill{s=SpawnEvent} @self ~onSpawn
  - shoot{type=ARROW;velocity=2;damage=1} @trigger ~onSignal:AISHOOT

SpawnEvent:
  Skills:
  - advaipathfinder{goal="clear"}
  - advaipathfinder{goal="0 shootattack"}
  - advaipathfinder{goal="1 movetowardstarget"}
  - advaipathfinder{goal="2 randomstroll"}
```
		
*runfromsun:*

##### `advaipathfinder{goal="[goalpriority_value] runfromsun [speed_value]"}`
+ Syntax in advaipathfinder: `- advaipathfinder{goal="1 runfromsun 5"}`
+ Set goal 1 to PathfindergoalFleeSun with a speed of 5
		
*rangedmelee:*

##### `advaipathfinder{goal="[goalpriority_value] rangedmelee [range_value]"}`
	
+ Syntax in advaipathfinder: `- advaipathfinder{goal="1 rangedmelee 5"}`
+ Set goal 1 to PathfinderMeleeAttack with a range of 5 blocks.
		
*followentity:*
	
##### `advaipathfinder{goal="[goalpriority_value] followentity [speed_value],[follow_rad_min],[follow_rad_start] [entity_uuid]"}`
+ Syntax in advaipathfinder: `- advaipathfinder{goal="1 followentity 2,3,10 <trigger.uuid>"}`
+ Set goal 1 to PathfinderFollowEntity with speed 2 to the trigger of the skill, start following if entity outside [follow_rad_start] radius and follow until entity is within [follow_rad_min].
		
*breakblocks:*


##### `advaipathfinder{goal="[goalpriority_value] breakblocks [materialtype_array] [blockbreakchance_value]"}`
	
+ Syntax in advaipathfinder: `- advaipathfinder{goal="1 breakblocks grass,dirt,stone 50"}`
+ Set goal 1 to PathfinderBreakBlocks. Blocks that can be broken are grass, dirt and stone. If none is given all blocks can be broken. Respect worldguards block-break deny flag.
+ The entity have a chance of 50% to break the block. Possible chances 0-100;

*returnhome:*


##### `advaipathfinder{goal="[goalpriority_value] returnhome [speed_value] [x],[y],[z],[travel_radius],[tp_radius],[boolean_ignoretarget]"}`
	
+ Syntax in advaipathfinder: `- advaipathfinder{goal="1 returnhome 2 <mob.l.x>,<mob.l.y>,<mob.l.z>,200,1024,false"}`
+ Set goal 1 to PathfinderGoalReturnHome. The entity will return to the mobs location present at execution of the mechanic. With speed of 2. Mob can travel inside a radius of 200 disq and has a  tp radius of 1024. Means if the mob is outside of 1024 it will be teleported. Because of the last false the mob will not go home aslong it has a target. Set it to true and the mob will run home whenever its outside of its range.
+ In addition there are two signals send:
+ *GOAL_STARTRETURNHOME* - Send to mob if the entity start to travel home.
+ *GOAL_ENDRETURNHOME* - Send to mob if the entity is arrived at home.


## CustomProjectiles mechanics (for MythicMobs 4.1.0 or higher) based on the idea of muhahahahahe:

#### new since 1.16:
- **mythicprojectiles/effectprojectile:**
+ `pfacedir=` true/false(default) Projectile object will face in movement direction.
+ `pfoff=` value(0.0 default) The front offset of the object. A value of -1.0 is about the center of the caster mob.
+ `targetable=` true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
+ `eyedir=` true/false(default) If eyedir is used. The projectile do not need a target. It will be shoot in the entities look direction.
+ `invis=` true/false(default) to apply invisible to the projectile.
+ `bounce=` true/false(default) if enabled the projectile bounce from the ground. only make sense if gravity is used and stoponground=false.
+ `bred=` 0.2f(default) the amount the bouncing velocity is reduced.
+ `onbounceskill=onbounce=ob=` skillname The skill that will be executed if the projectile hit the ground.
		
- **mythicorbitalprojectiles:**
+ `pfacedir=` true/false(default) Projectile object will face in movement direction.
+ `targetable=` true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
+ the owner of the orbital projectile will be set to its caster.
+ `tag=` value If used this tag will be parsed and added as metatag to the orbitalentity.
+ `invis=` true/false(default) to apply invisible to the projectile.
			
### ItemProjectile:
Shoot any Item. Use it like the original MythicMobs projectile mechanic. In addition use pitem or pobject to choose any droppable item.
		
- `pobject=` Any valid bukkit item type.
		
### BlockProjectile:
Shoot any FallingBlock. Use it like the original MythicMobs projectile mechanic. In addtion use pblock or pobject to choose any FallingBlock.
		
- `pobject=` Any valid Bukkit fallingblock type.
		
### EntityProjectile:
Shoot any Entity. Use it like the original MythicMobs projectile mechanic. In addtion use pentity or pobject to choose any Entity. Since v1.11 you can use pspin to spin the entity where pspin=VALUE value is the speed.
		
- `pobject=` Any valid bukkit entity type.
		
### MythicProjectile / MythicEffectProjectile:
Shoot any MythicMobs mob as a projectile. In addition use pobject or pmythic to choose a existing MythicMobs mob. See example for details. If you dont need the projectile object you can use MythicEffectProjectile and use the futures of mythicprojectile without object.

+ `pobject=` MythicMob used for the projectile object
+ `pfoff=` value(0.0 default) The front offset of the object. A value of -1.0 is about the center of the caster mob.
+ `pvoff=` value(0.0 default) The vertical offset of the object.
+ `pfacedir=` true/false(default) Projectile object will face in movement direction.
+ `eyedir=` true/false(default) If eyedir is used. The projectile do not need a target. It will be shoot in the entities look direction.
+ `targetable=` true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
+ `pspin=` value(0 default) If there is a value != 0 the pobject will be spinned at value speed.
+ `invis=` true/false(default) if invisible will be applied to the pobject
+ `bounce=` true/false(default) if enabled the projectile bounce from the ground. only make sense if gravity is used and stoponground=false.
+ `bred=` 0.2f(default) the amount the bouncing velocity is reduced.
+ `onbounceskill=onbounce=ob=` skillname The skill that will be executed if the projectile hit the ground. For bounce example see below.
		
### MythicOrbitalProjectile:
Summon a orbital object on the targeted entity. Do not work on locations. The owner of the orbital projectile will be set to its caster. See the example who to use it.
		
+ `pobject=` MythicMob used for the orbital object
+ `oradx=` radius x
+ `orady=` radius y
+ `oradz=` radius z axsis.
+ `oradsec=` how much angle added per sec. (speed)
+ `pvoff=` vertical offset.
+ `pfacedir=` true/false(default) Projectile object will face in movement direction.
+ `targetable=` true/false(default) Create a metadata on the object called "nottargetable" useful for to exclude the entity from targeters.
+ `tag=` if used it will add a parsed metatag to the orbitalentity. Example: tag=<trigger.uuid>
+ `invis=` true/false(default) if invisible will be applied to the pobject
		
```yaml
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

	Mobs Yaml: https://pastebin.com/6U6iUxkF
	Skill Yaml: https://pastebin.com/Vnanzhmz
	Items Yaml: https://pastebin.com/ddkeJaqG

MythicOrbitalProjectile Example:

```yaml
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

```yaml
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

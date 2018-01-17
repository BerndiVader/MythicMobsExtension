# MythicMobsExtension v1.238h for MythicMobs 4.1 and Spigot 1.10.2 or higher

### Wiki:
https://github.com/BerndiVader/MythicMobsExtension/wiki

Click [here](https://github.com/BerndiVader/MythicMobsExtension#custom-entities) to see stuff that is not yet in the wiki.


### Changelog:
##### ** 17.01.2018 *** fixed setmobhealth issues.
##### ** 17.01.2018 *** fixed network issue for <1.12 versions.
##### ** 16.01.2018 *** fixed parrot issue for <1.12 versions.
##### ** 15.01.2018 *** added randomspeed alias to setspeed mechanic for compatibility.
##### ** 15.01.2018 *** fixed random max>min.
##### ** 14.01.2018 *** added renewrandom option to customrandomskill. See customrandomskill for details.
##### ** 14.01.2018 *** final fix for zoom mechanic.
##### ** 14.01.2018 *** added lastdamageindicator condition. See lastdamageindicator for example.
##### ** 13.01.2018 *** fixed issue in entitiesinradius conditions & added ignoresameblock option.
##### ** 11.01.2018 *** added hasvehicle condition. minor fix in linebubble mechanic
##### ** 11.01.2018 *** fixed parrot mythicspawners issue.
##### ** 10.12.2017 *** fixed customprojectiles compatibility with older MythicMobs versions.
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



## playerzoom mechanic:

Zoom the player's view. Where v is a value of 0 (no zoom) to 1.0f full avail zoom.
Ex:

```yaml
zoom:
  Skills:
  - playerzoom{v=1.0f}
  - playerzoom{delay=180;v=0.0f}
```
First set the zoom to 1 after 180 ticks set the zoom back to 0.



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

This force all targeted players to see the loading screen for 60 ticks. Use closeinventory to return the player into normal view.

```yaml
playblackscreen:
  Skills:
  - playcredits{repeat=60;repeatInterval=1}
  - closeinventory{delay=61}
```

This force all targeted players to see a black screen for 60 ticks. Use closeinventory to return the player into normal view.
	


## dropinventory mechanic:

Force the target to drop an item from its storagte/equipment to the ground.

##### `- dropinventory{item="material=[MATERIAL_TYPE]||ANY,amount=[NUMBER],lore=[TEXT],where=[HAND][OFFHAND][ARMOR][INVENTORY]";pickupdelay=[TICKS];pieces=[NUMBER]} @trigger ~onInteract`

Where in `item=""` all the item information is stored. `pickupdelay` = amount of ticks the item cannot be picked up again. `pieces` how many 
pieces out of the inventory, regardless of itemstacks, are dropped.

Examples:

```yaml
drop2pieces:
  Skills:
  - dropinventory{item="material=DIRT,amount=1,where=HAND";pieces=2} @trigger ~onInteract
  
dropAll:
  Skills:
  - dropinventory{item="material=ANY,amount=64,where=ANY";pieces=41} @trigger ~onDamaged
```

First example drop 2 dirt regardless the item is in an stack or there are just two single dirtstacks in the inventory. The second example
drop the inventory entirely because of where & material = any, amount is atleast a stack and pieces = 41 (max slots of a player) 
	


## closeinventory mechanic:

Force the player to close the current open inventory.

##### `- closeinventory @trigger ~onInteract`
	


## fakedeath mechanic:

Force a fake death sequence of the castermob.

##### `- fakedeath{duration=50} ~onDamaged`

This play the fake death sequence for the mob. After 50 ticks the mob appear again. This can be used to do some real wiered stuff:

```yaml
fakedeath:
  Skills:
  - fakedeath{duration=500}
  - delay 14
  - heal{oh=true}
  - setrotation{yo=2;dur=500} @self
```

This skill for example, will stop the death sequence when the mob is horizontal position for 500 ticks.....
	


## setmobhealth mechanic:

Set the mythicmobs mob health and maxhealth to a new amount. Random amount allowed. Ignore or allow level health modifier. Use set add or multiply
the amount.

##### `- setmobhealth{health=2to5;ignoremodifier=true;set=+} ~onInteract`

add a random value between 2 and 5 to the maxhealth.

##### `- setmobhealth{health=10to20;ignoremodifier=true} ~onInteract`

set the maxhealth to random value between 10 and 20.

##### `- setmobhealth{health=0.5to1;ignoremodifier=true;set=*} ~onInteract`

multiply the maxhealth with a random value between 0.5 and 1.

	


## entitygoggleat/entitygoggle mechanic:

Force the entity to goggle at the targeted entity for duration amount of ticks. Doesnt work too well if the entity have ai.

##### `- entitygoggleat{dur=300} @trigger ~onDamaged`
	


## setrotation mechanic:

Rotates the entity yo degrees relative to its yaw, amount of duration times. Doesnt work too well if the entity have ai.

##### `- setrotation{dur=200;yo=10} @self ~onTimer:5`
	


## speechbubble mechanic (requires holographicdisplay):

Use this mechanic to add something like a speechbubble to your mob. Use it like this. `text` inside of "" is the output text. `linelength` or `ll` is the max charlength of the lines. `offset` or `yo` is the yoffset of the bubble. `time` or `ti` is how long the bubble is shown above the mob.
`so`and `fo`for side and forward offset relative to casters direction, where vertical negative is behind and horizontal negative is right side. `anim`true or false if the text should be animated at popup. (option) `id` is the id of the entity's speechbubble if you want to use modifybubble or removebubble mechanic.
Use `<additem.ITEM_NAME>`inside the `text`option to add an itemline to the bubble. Use any valid bukkit item material name. Use <nl> placeholder inside `text`option to force a new line. Use `usecounter`true/false to enable counter for the bubble.


##### `speechbubble{text="&5<target.name>&f, hello! My name is &3<mob.name>&f Nice to meet you. How are you doing? I think its a pretty nice weather today, isnt it?";ll=20;offset=2.2;so=sideoffset;fo=forwardoffset;time=300} @trigger ~onInteract`

```yaml
BubbleMonkey:
  Type: zombie
  Display: "DingDong"
  AITargetSelectors:
  - 0 clear
  Skills:
  - equip{i=BlackbeardHead:4} @self ~onSpawn
  - speechbubble{text="&5<target.name>&f, hello! My name is &3<mob.name>&f Nice to meet you. How are you doing? I think its a pretty nice weather today, isnt it?";ll=20;offset=2.6;time=300} @trigger ~onInteract
  - speechbubble{text="&5<target.name> &2Stop punching me around!";ll=80;offset=2.6;time=300} @trigger ~onDamaged
```

## modifybubble mechanic:

Use this mechanic to modify a existing bubble for the caster: `modifybubble{id=existing_id;text="new test";ll=linelength;so=sideoffset;fo=forwardoffset;offset=offset;usecounter=true/false;timer=ticks}`

## removebubble mechanic:

Removes an existing bubble from the caster: `removebubble{id=existing_id}`

## linebubble mechanic:

Modify a line of a bubble: `linebubble{id=existing_id;mode=append/replace/remove;oldline="oldlinetext";newline="newlinetext"}`
If bubble with id have a line that contains `oldline` that line will be removed, replaced with newline, or if used append a newline will be added.
Example:

```yaml
BubbleMonkey:
  Type: zombie
  Display: "DingDong"
  AITargetSelectors:
  - 0 clear
  Skills:
  - speechbubble{id=effects;usecounter=false} @self ~onSpawn
# creating a bubble instance at spawn.

BubbleSkill:
  Skills:
#  - linebubble{id=effects;mode=append;newline="<additem.dirt>"}
#  - linebubble{id=effects;mode=remove;oldline="<additem.dirt>"}
#  - linebubble[id=effects;mode=replace;oldline="<additem.dirt>";newline="<additem.stone>"}
```
	


## disarm mechanic:

Disarm the targeted player and store the equipped item in the first free slot in the players inventory. The mechanic is not executed if 
the player has no space in its inventory. If used on a none player the entity is disarmed for duration amount of ticks instead of stored in inventory.

##### `- disarm{duration=200} @trigger ~onDamaged`
	


## playergoggle or playergoggleat mechanic:

Force the targeted player to stare at caster while still is full moveable for duration amount of ticks. Use the `isgoggling` condition 
to determine if the player already goggles at something.

##### `- playergoggleat{d=[duration_ticks]} @any_player_targeter ~onDamaged`
	


## playerspin mechanic:

spin the player target around. Use the `isspinning` condition to determine if  the player is already spinning around.

##### `- playerspin{d=[duration_ticks];s=[spin_speed]} @any_player_targeter ~onDamaged`
	


## extinguish mechanic:

extinguish the target if its on fire.

##### `- extinguish @self ~onDamaged`

if the mob is damaged and in case the mob is on fire it extinguish the mob.
	


## asquip mechanic:

Equip an armor stand with any Bukkit Material Type. Equp the head slot with any Material.
	
##### `- asequip{item=DIAMOND_SWORD:4} ~onSpawn`

If mobtype = armorstand equip diamond sword on head slot.



## playerweather mechanic:

Change the weather and time for the targeted player only. The effect will last duration amount of ticks.
	
##### `- playerweather{weather=w=CLEAR||DOWNFALL;time=t=[time];duration=d=[integer]} @PIR{r=20} ~onSpawn`



## setfaction mechanic:

Set the faction of the targeted entity if its an MythicMobs mob. Any variable present at runtime can be used. (Additional you should look at the conditions infaction and samefaction)
	
```yaml
- setfaction{faction=SomeFaction} @self ~onSpawn
- setfaction{faction=<mob.name> @self ~onSpawn
```


## parseddisguise mechanic:

Same as the build-in disguise mechanic. But additional it parse every variable that is present while the skill is executed.    
	
##### `- parseddisguise{d=PLAYER:<mob.name>:<target.name>} @trigger ~onDamaged`
	


## renameentity mechanic:
    
Rename the targeted entity. Only works on living entities and do not work for players. Use name option for the new name. The mob variables are parsed. You can use all the variables (http://www.mythicmobs.net/manual/doku.php/skills/stringvariables) avaible at runtime. Set visible to true or false if the name should be displayed without hover the entity.
	
##### `- renameentity{name=[PARSEDSTRING];visible=[BOOLEAN]}`
	
	
## setcachedowner mechanic:

set the targeter entity to perma owner of the activemob. Resistent against reload and server restart until the mob is dead. Althought only make sense on despawn false mobs.
	
##### `- setcachedowner @trigger ~onInteract`

	
	
## advrandomskill mechanic:

Use this mechanic to execute a random skill by chance and priority.
	
##### `- advrandomskill{renewrandom=true;skills=priorityrandomskill1:0.1,priorityrandomskill2:0.2,priorityrandomskill3:1} ~onDamaged`
##### `- customrandomskill{rrenewrandom=false(default);skills=priorityrandomskill1:0.1,priorityrandomskill2:0.2,priorityrandomskill3:1} ~onDamaged`
+ This check if rnd match first skill, if yes skill is executed, if not check for the next skill. If none matched yet the last entry with a chance of 1 will always be executed.
+ If renewrandom/newrandom/rnr is set to true a new random is created for the next entry else for all entries the same random is used.
	
##### `- advrandomskill{skills=priorityrandomskill1:0.5,priorityrandomskill2:0.3,priorityrandomskill3:0.2} ~onDamaged`
+ Same as above but if no chance match nothing will be executed.

	

## changehealthbar mechanic:

Use this mechanic to change the display of the mobs healthbar if exists.
	
##### `- changehealthbar{display="[>>>$h<<<]"} @self ~onDamaged`
+ Changes the healthbar display if the mob is damaged.


## createhealthbar mechanic:

Use this mechanic to display a healthbar above the mythicmobs head.
	
##### `- createhealthbar{so=0;fo=0;iy=false;offset=2.5;counter=10;display="[|||$h|||]"} @self ~onSpawn`
	  
Creates a healthbar for the spawned mob with an y-offset of 2.5. And adds so(sideoffset) to left/right of mob and add fo(frontBackOffset). Use iy(ignoreYaw) if you dont want sideoffset affected by the entity's yaw.
	
The healthbar is removed after the mob is removed. Use "$h" as placeholder for the mobs health. If counter is set the healthbar is visible counter amount ticks after the mob is damaged. Use -1 to set it perma visible.

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


## parsedstance mechanic:

##### `- parsedstance{s="<trigger.uuid>"} @self`
##### `- pstance{s="<target.l.dx>,<target.l.dy>,<target.l.dz>,<target.l.w>"} @targetlocation`

Set a the stance of an activemob filled parsed variables, like <mob.uuid> <target.uuid> and so on. In addition see parsedstance condition to compare parsed stances.

*special variables*
+ <target.l.*> are parsed within pstance with the block position of the location if there is a location targeter
+ <target.l.dx dy dz> to get the coordinates as double not the block position.
+ <target.meta.*> where * is the name of the metatag. If used with a location targeter the block metatags are used.
+ <mob.meta.*> where * is the name of the metaag. Ex: `<mob.meta.<trigger.uuid>` reteruns the value stored in casters triggers uuid tag if 
there is one.


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


## MetaMechanics & Conditions:

### setmeta mechchanic:
Set parsed(!) metadata for the target. You can use all variables that are avaible while the skill is executed.

##### `- setmeta{meta="tag=tagname;value=tagvalue;type=BOOLEAN/NUMERIC/STRING";usecaster=BOOLEAN}`
			
The tags "tag" and "value" can contain any mob variable. Example: `[- setmeta{meta=<target.uuid>} @self]` add the uuid of the target as metatag to the mob. You can also use values and types, but this is more for further purpose. Still you can go form them too. If usecaster is set to true the metatag is always set for the caster of the skill. No matter what targeter is used.
		
`- setmeta{meta="tag=lastdamagedentity;value=<trigger.uuid>;type=STRING"} @self ~onAttack`
			
This will set the lastdamagedentity tag of the mob to the victims uuid. It is possible to set a metadata of a block by using a location targeter. All blocks including air are valid.
		
### delmeta mechanic:

Delete a metatag from the targeted entity. Be aware, if you remove tags that are not added by yourself, might break something else!
		
##### `- delmeta{meta="tag=lastdamagedentity"} @self ~onCombatDrop`
			
This remove the lastdamagedentity tag if the mob stop fighting.
		
### hasmeta condition:

With this condition you can check any parsed meta. In its main purpose its a compare condition. Mean its a TargetConditions because it needs 2 entities. But by setting the cs tag (compareself) in the condition, you can choose if the target or the caster metas are checked. Its also possible to use a list of tags. All mob variables that are useable at the moment the skill is executed can be used. Use hasmetasimple if you only need ot check a single entry.

##### `- hasmeta{meta="tag=tagname;value=metavalue;type=BOOLEAN/NUMERIC/STRING";cs=true/false;action=true/false/cast/castinstead}`

##### `- hasmetasimple{tag=tagname;value=metavalue;type=BOOLEAN/NUMERIC/STRING;cs=true/false;action=true/false/cast/castinstead}`

Example: `- hasmeta{meta="tag=lastdamagedentity;value=<target.uuid>;type=STRING";cs=true;action=true}`
			
This will check the caster entity if it has the tag "lastdamagedentity" and if it contains the uuid of the target. If cs=false it would check the target entity. The following condition use a list. ATM it will meet the condition if only 1 of the tags match. This will be changed in the future.
		
Example: `- hasmeta{meta="tag=<target.uuid>||tag=<trigger.uuid>";cs=true;action=true}`
			
Checks if the caster mob have the tag target uuid or trigger.uuid.
			
The following example shows how to make it, that every entity can hit the villager only once. After that the entity have to interact with the villager do get removed and can hit him again one time:

```yaml
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

Options:
+ `destination=` MythicMobs targeter or vanilla targeter. Use "" that the targeter can be parsed.
+ `noise=n=` number, random point around the target
+ `teleportdelay=tdelay=td=` number, delay in ticks between teleportation (if more than 1)
+ `infrontof=front=f=` true/false, teleport in front of target (if target is a entity)
+ `returntostart=return=r=` true/false, if the source entity should return to its start position
+ `betweenlastentitysignal=bls=` signalname to be send to caster mob between the teleportations, where the trigger is the last entity (if target was an entity)
+ `betweennextentitysignal=bns=` signalname to be send to caster mob 
+ `ignoreowner=io=` true/false, if the owner of the caster mob should be ignored.
+ `maxtargets=mt=` number, the maximium number of targets.
+ `targetinsight=insight=is=` true/false, only the targets insight of the current position are used.
				
```yaml
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

Options:
+ `mythicitem=item=itemtype=type=t=i=` Internal name of the mythic item or the mythic droptable.
+ `amount=a=` the amount of the that will be dropped if a mythic item, the amount of droptable creation if a droptable.
+ `shuffle=true/false` if true the droptable itemlist will be shuffled by random.
	
`- dropmythicitem{item=MythicItem;a=1} @pir{r=20} ~onAttack`
	
This drop one MythicItem at all players in radius of 20 if the mob targets a entity. Additional, whenever this mechanic is called there is a
`MythicMobsExtItemDropEvent`called with the mythicmobs the trigger and the drop as an arraylist.



## customparticleline skill (for MythicMobs 4.1.0 or higher):

The same like the original but added vDestOffset & hDestOffset to adjust the targetlocation.
Use vDestOffset to adjust the height and use hDestOffset to adjust the x/z position. Use idoy true/false(default) = ignoredestoffsetyaw to ignore the yaw of the targetlocation. With this its possible to target a specific part of the entity.
	
`- customparticleline{particle=reddust;amount=5;color=#feff90;ys=2.5;vd=1.0;hd=-1.0;idoy=true;distanceBetween=0.5;tyo=1.25} @pir{r=10} ~onTimer:5`



## setthreattarget & clearthreattarget skill:

Use setthreattarget to clear the activemobs threattable and add {a=double} targeter to the threat. Default amount is 65536.
	
##### `- setthreattarget{a=11111} @p`
	
To clear the threattable and trigger the dropCombat Event use:
	
##### `- clearthreattarget @self`



## SwapSkill:

Swap location of caster and target. Use keeptargetyaw=kty false/true or keepcasteryaw=kcy false/true to keep the original direction or not.
	
##### `- swap{kty=true;kcy=false} @target`



## StunSkill:
	
Use this mechanic to stun the target. 
	
##### `- stun{duration=Ticks;facing=true/false;gravity=true/false;stopai=true/false} @target`
	
Where `duration=d=`how many ticks the target will be stunned and `facing=true/false` if yaw pitch of entity shall remain. `gravity=true/false` false(default) turn off gravity while the entity is stunned. In addition there is the **isstunned condition**. Look at Conditions. If stopai is used. The ai of the mob will be disabled while playing the stun and reapplied afterwards.
	


## CustomSummonSkill:

Use this mechanic to add ax,ay,az to the targetlocation. Use ranged value in amount option. Use leashtocaster(leash/lc) to leash the summoned mob
to the caster.
	
##### `- customsummon{a=2to20;type=mobname} @self`
	
Summon 2 to 20 mobs of type mobname.
	
##### `- customsummon{type=WITHER_SKELETON;amount=5;radius=4;ax=5;ay=1;az=2} @self`
	
This example do not check for a safeplace it will spawn the mob no matter if its in block or such. If you want to use safeplace add noise. Example:
	
##### `- customsummon{type=WITHER_SKELETON;amount=5;radius=4;noise=1;ax=5;ay=1;az=2} @self`
	
This example shows how to spawn mobs relative to the direction of the targeted mob:
	
##### `- customsummon{t=mobname;ued=true;ifb=1} @self`
	
`ued=useEyeDirection=EyeDirection;ifb=inFrontBlocks=inFront`
	
Use setowner (true/false) to set the owner to the mob which casted the custumsummon skill.
	
##### `- customsummon{t=mobname;setowner=true} @selflocation`
	
This summon the mob mobname and set its owner to the mob which casted the skill.
	
Use `tag` option to add a parsed string to the scoreboard of the summoned mob. Variables are allowed and are parsed.
	
##### `- customsummon{t=mobname;tag=<target.uuid>} @self`
	
This summon the mob mobname and add the uuid of the target to the summoned mobs scoreboard.
	
Use `invisible=inv` option to summon the mob invisible.



## OxygenSkill:

Use this mechanic to add oxygen to the LivingEntity.
	
##### `- oxygen{a=20} @self`
	
`a = amount =` the amount of oxygen that will be added. A player has a usual max air amount of 300. A amount of 20 gives about 1 bubble air.
	
	
	
## FeedSkill:

Add specific amount of food to the players foodlevel.
	
##### `- feed{a=1} @PIR{r=20}`
	
`a = amount =` the food amount. The amount of 1 add 1/2 foodlevel.



## RemovePotionSkill or CureSkill:

Use this mechanic to remove all or a specific potion from the targeted entity. Use removepotion or cure.

##### `- cure{t=ALL} @self`
##### `- cure{t=INVISIBILITY} @PIR{r=30}`
	
##### `- removepotion{t=ALL} @self`
##### `- removepotion{t=INVISIBILITY} @PIR{r=30}`
	
`t = type = p = potion =`ALL by default. Use "ALL" to remove all effects or use a single effect or a list like: `p=INVISIBILITY,SPEED,HEAL`

	

## CustomDamageSkill:

Use this mechanic for a temporary mythicmobs damage mechanic fix. See http://www.mythicmobs.net/index.php?threads/skill-damage-bypassed-armour.3373/ this thread for details of the issue.
	
##### `- customdamage{a=1to2;ia=false;pk=false;pi=false;iabs=false}`
+ `a = amount =` 1 by default. The amount of damage applied. 1 = 1/2 heart. Ranged amount possbile: 1to2
+ `ia = i = ignorearmor =` false by default. If the armor of the target should be ignored or not.
+ `pk = pkb = preventknockback =` false by default. If knockback should be applied or not.
+ `pi = preventimmunity =` false by default. If immunity should be used or not.
+ `ignoreabs = iabs =` false by default. If absorbation should be ignored or not.
+ `percentage=p=` false by default. If true it uses the amount as percent.
+ `pcur=pc=` false by fault. Need that percentage=true. If pc is true is use the percent of current health if false it use percent dmg of maxhealth.
	

	

## EquipSkullSkill:

This is a fix for custom player heads for 1.8.8 as it appeard to not work with mm versions greater than 2.5.1

##### `- equipskull{skull=mythicitemname}` 
+ `skull=s=`  a valid MythicMobsItem to equip on head.
		
Example:

```yaml
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

## unEquipSkill:

##### `- unequip{armor=<all>||<list>||<single>;signal=<string>}`
+ `armor=` all / helmet / chest / leggings / boots / hand / offhand - can be single name or all or a list
+ `signal=` name of the signal which will be send to the target if set.

		
## DamageArmorSkill:

##### `- damagearmor{armor=<all>||<list>||<single>;damage=<int>||<ranged>;signal=<string>}`
+ `armor=` all / helmet / chest / leggings / boots / hand / offhand - can be single name or all or a list
+ `damage=` armor damage amount as integer or as ranged value for ranged value use "to".
+ `signal=` name of the signal which should be send to mob if an armor part is broken. dont set it if you dont want a signal to be send.
				
Example:

```yaml
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

##### `- grenade{size=3;amount=1;fuse=20;fire=false;breakblocks=true;utime=60;ueffect=true;undotnt=true;ued=boolean}`
+ `size=` radius of the explosion
+ `amount=` how many grenades the mob throw at once
+ `fuse=` how long the fuse tick
+ `fire=` should there be fire too? true / false
+ `breakblocks=` damage the blocks? true / false
+ `utime=` how many ticks until restore the blocks
+ `ueffect=` should there be a restore effect? true / false
+ `undotnt=` should the blocks be restored at all? true / false
+ `ued=` if true the grenade is thrown in eye direction. if false the grenade is thrown in target direction

Example:

```yaml
grenadezombie:
  Type: zombie
  Health: 20
  Skills:
  - grenade{size=2;amount=1;fuse=20;fire=false;breakblocks=true;utime=40;ueffect=true;undotnt=true} @target ~onTimer:200
  - grenade{size=2;amount=1;fuse=60;fire=false;breakblocks=false;utime=40;ueffect=false;undotnt=false} @pir{r=20} ~onTimer:60
```


## SetRandomLevelSkill:

##### `- setrandomlevel{min=1;max=10;self=true}`
+ `min=` lowest level
+ `max=` highest level
+ `self=` true = targetself / false = target any other targeted mob
		
Example:

```yaml
RndLvlMob:
  Type: zombie
  Skills:
  - setrandomlevel{min=1;max=10;self=true} @self ~onSpawn 1
```


## StealSkill:
##### `- steal{items=ITEM:AMOUNT,ITEM:AMOUNT,.....;failsignal=steal_fail;oksignal=steal_ok}`
+ `items=` Can be a list of valid spigot items. One of the items shuffled by random will be tried to steal from the targeted player. Use "ANY" for any item.
+ `failsignal=` name of the signal that should be send to the mob if the stealing failed. default signal = steal_fail
+ `oksignal=` name of the signal that should be send to the mob if the stealing was good. default signal = steal_ok
		
##### `- DropStolenItems`
Use this skill and the mob drop all its stolen items. Good idea to use it on death. Or all the items are gone with the mob to herobrine.
		
**There is a buildin cancel damage condition if the mob should make no damage while try to steal. It can be activated if the stance of the mob is "gostealing" So if the mob have the gostealing stance set, it will do no damage to its target.** 

Thief example:

mob yml:

```yaml
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

```yaml
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


##### `- hasvehicle{types=[array]||ANY}`
To use as **Conditions** condition. Check if the caster is riding or driving another entity. Use an array `zombie,skeleton,creeper` or a single type `boat` or `ANY` for any entity.
Ex: `- hasvehicle{types=ANY}` condition is true if the caster is riding, driving an entity.
Ex: `- hasvehicle{types=zombie,boat,skeleton}` condition is true if the caster is riding, driving one of the types.

##### `- movespeed{range=[ranged_value]}`
To use as **Conditions** && **TargetConditions** condition. Check the generic movementSpeed.
Ex: `- movespeed{rage=<0.51}}` condition is true if the speed is below 0.51.

##### `- getbowtension{range=[ranged_value];debug=[boolean];action=[boolean]||[CAST]||[CASTINSTEAD]}`
##### `- lastbowtension{range=[ranged_value];debug=[boolean];action=[boolean]||[CAST]||[CASTINSTEAD]}`
To use as **Conditions** && **TargetConditions**, **PLAYER ONLY** condition. Get the players bow tension.
Possible range is -1 for nothing at all, or 0.0 to 1.0. Where 1 is full strength. If unsure add `debug=true` and check the values in console.

While `getbowtension`check the strength while the bow is drawn, `lastbowtension` get the last strength of the entity's bowtension if there was any. To add skills ot the entity while draw the bow use getbowtension, if the entity shoot the bow use lastbowtension to get the strength the arrow is fired.

Ex: `- getbowtension{range=>0.9};debug=true}` condition is true if the tension of the bow is more than 90% of its full strength.

##### `- getlastindicator{value=[ranged_value];action=[boolean]||[CAST]||[CASTINSTEAD]}`
To use as **Conditions** && **TargetConditions**, **PLAYER ONLY** condition. Unlike getindicator this condition do not return a realtime value, instead it returns the indicator status of the last armswing. Range is 0.0f to 1.0f. Default value is >0
Ex: `- getlastindicator{value=1}}` condition is true if the indicator was fully loaded on last swing.

##### `- getindicator{value=[ranged_value];action=[boolean]||[CAST]||[CASTINSTEAD]}`
To use as **Conditions** && **TargetConditions**, **PLAYER ONLY** condition. Checks the status of the crosshair damageindicator.
Range is 0.0f to 1.0f. Default value is <1.1
Ex: `- getindicator{value=<0.51}}` condition is true if the indicator is not at more than 50%.

##### `- oncooldown{value=[ranged_value];action=[boolean]||[CAST]||[CASTINSTEAD]}`
To use as **Conditions** && **TargetConditions**, **PLAYER ONLY** condition. Checks if the selected item is on cooldown. value=0 if not.
Ex: `- oncooldown{v=1to10};action=CASTINSTEAD lowcooldownskill}`

##### `- testfor{vc="[valid testfor stuff]";action=[boolean]||[CAST]||[CASTINSTEAD]}`
To use as **Conditions** && **TargetConditions**, return true if caster or target match the testfor. See https://www.digminecraft.com/game_commands/testfor_command.php **PLEASE NOT THE "" for vc option!**

##### `- sameworld{action=[boolean]}`
To use as **TargetConditions**, return true if caster & target location in same world.

##### `- onsolidblock{action=[boolean]}`
To use as **Conditions**, return true if the location is *ON* a solidblock.

##### `- insolidblock{action=[boolean]}`
To use as **Conditions**, return true if the location is *INSIDE* a solidblock.

##### `- jumping{action=[boolean]}`
To use as **TaretConditions or Conditions**, return true if the entity is jumping.

##### `- crouching{action=[boolean]}`
To use as **TaretConditions or Conditions**, return true if the entity is crouching.

##### `- sleeping{action=[boolean]}`
To use as **TaretConditions or Conditions**, return true if the entity is sleeping.

##### `- running{action=[boolean]}`
To use as **TaretConditions or Conditions**, return true if the entity is running.

##### `- samespawner{action=[boolean]}`
To use as **TargetConditions**, return true if caster & target have same mythicspawner.

##### `- hasspawner{names=[arraylist]||[ANY]}`
Checks if the mythicmobs mob comes from a mythicspawner. Use a List or ANY.

##### `- isgoggling{action=[boolean]}`
Checks if the player is goggling at something. See playergoggle mechanic.

##### `- isspinning{action=[boolean]}`
Checks if the player is spinning around. See playerspin mechanic.

##### `- isvehicle{action=[boolean]}`
To use as **TargetConditions**, checks if the caster is riding its target.

##### `- ispresent{action=[boolean]}`
Checks if the the entity exists. Only makes sense as TargetCondtions though. Useful for triggers where its not always sure that there will be
an entity as target. Like ~onTimer and such stuff.

*Example*

```yaml
  TargetConditions:
  - ispresent{action=true}
  # true if the target is an entity and it exists.
```


##### `- isburning{range=[ranged_value];action=[boolean]}`
By default (without range option) checks if the entity is burning or not. If range is given, return true if the burning ticks match the range.

##### `- eir{isb=[BOOLEAN];types=[ALL]||[SINGLETEXT]||[ARRAY];amount=[RANGEDVALUE];radius=[VALUE];action=[boolean]}`
Check if amount entities of type "ALL" or "ENTITYTYPE" or "ENTITYTYPES" are in radius.

##### `- leir{isb=[BOOLEAN];types=[ALL]||[SINGLETEXT]||[ARRAY];amount=[RANGEDVALUE];radius=[VALUE];action=[boolean]}`
Check if amount living entities of type "ALL" or "ENTITYTYPE" or "ENTITYTYPES" are in radius.

##### `- pir{isb=[BOOLEAN];amount=[RANGEDVALUE];radius=[VALUE];action=[boolean]}`
Check if amount players are in radius.

*Example*

```yaml
  Conditions:
  - eir{types=ARROW,DIAMOND_SWORD;amount=1to10;radius=10}
  # true if there are 1 to 10 dropped items of type arrow and diamond_sword around.
  
  - leir{types=ZOMBIE,SKELETON,CREEPER;amount=>5;radius=20}
  # true if there are more than 5 entities of the type list around.
  
  - pir{amount=>0;radius=64}
  # true if there is atleast 1 player in radius of 64 blocks.
  
  - eir{types=all;isb=true;amount=>0;radius=10}
  # true if there is atleast 1 other entity in radius of 10 blocks & not on
   same block as source location.
```

##### `- playertime{time=[RANGEDVALUE];action=[boolean]}`
Check the player only time.

##### `- playerweather{weather=[CLEAR||DOWNFALL];action=[boolean]}`
Check the player only weather.

##### `- owneralive{action=[boolean]}`
Returns true if the mobs owner is online, alive or in same world.

##### `- infaction{faction=[STRING]or[ARRAY];action=[BOOLEAN]}`
Determines if the caster or target (if used in TargetConditions) is in the faction or in one of the factions if used as array. Any mythicmobs
variable avail at runtime can be used. Eg: `faction=<mob.stance>`

*Example*

```yaml
  Conditions:
  - infaction{faction=SomeFaction;action=true}
  - infaction{faction=aFaction,anotherFaction,yetAnotherFaction;action=true}
```

#####`- samefaction{faction=[STRING]or[ARRAY];action=[BOOLEAN]}`
Determines if the caster **AND** target are in the same faction or in one of the factions if used as array. Any mythicmobs variable avail at
runtime can be used. Eg: `faction=<trigger.name>`

*Example*

```yaml
  TargetConditions:
  - samefaction{faction=SomeFaction;action=true}
  - samefaction{faction=aFaction,anotherFaction,yetAnotherFaction;action=true}
```

##### `- lookatme{fov=[double];yo=[double];debug=[boolean]}`
##### `- looksatme{fov=[double];yo=[double];debug=[boolean]}`
  
This condition can determine if the target is looking at the caster. Therefor a **field-of-view-ratio** defined in fov as a double is used. For normal sized entities a fov-ratio of **1.999D** (used by default) is a good value to use. You can also adjust the Yoffset by define **yo**. For the standard sized entities a yo of **-0.4D** (used by default) is a good value. Usually the default offsets working very well on almost all entities. But if you need to adjust them, you can set **debug=true** and if the condition is used the fov and vecY offsets are written into the console. With that info you can easy adjust the values to your need.

*Example:*

```yaml
mobfile:

WeepingMonkey:
  Health: 500
  Type: zombie
  Display: "Weeping Monkey"
  Skills:
  - skill{s=freeze} @pir{r=32} ~onTimer:20
  
skillfile:

freeze:
  Conditions:
  - isstunned{action=false}
  TargetConditions:
  - infront{view=45}
  - lookatme
  Skills:
  - stun{d=60;facing=true} @self
```

##### `- relativedirection{angle=[RANGEDVALUE];action=[BOOLEAN]}`
Use this condition to determinate the relative direction to the target. Use this condition in **TargetConditions**. Where 180 is the right side. 90 degree is straight infront and 270 degrees are exact behind. If you use `angle=0to180` the it compares if the target comes from infront, where `angle=90to270` means the targeted entity is somewhere on the right side of the caster. etc...

*Example:*

```yaml
skillfile:

    targetsonleft:
      TargetConditions:
      - relativedirection{angle=>315}
      - relativedirection{angle=<45}
      Skills:
      - message{msg="Found <trigger.name> on my left!"} @world
      
mobfile:
    Monkey:
      Health: 500
      Type: zombie
      AITargetSelectors:
      - 0 clear
      AIGoalSelectors:
      - 0 clear
      - 1 randomstroll
      Display: "Me Monkey"
      Skills:
      - skill{s=targetsonleft} @pir{r=20} ~onTimer:20
```

##### `- ownsitem/iteminhand{list="where=[ANY||HAND||ARMOR||INVENTORY];material=[ANY||MATERIALTYPE];amount=[RANGEDVALUE];lore=[LORETEXT]";action=[BOOLEAN]}`
##### `- ownsitemsimple{where=[ANY||HAND||ARMOR||INVENTORY];material=[ANY||MATERIALTYPE];amount=[RANGEDVALUE];lore=[LORETEXT];action=[BOOLEAN]}`
Works as target or entitycondition. A single value or a boolean expression argument can be given (see below for some examples).
This condition works on all living entities, where the INVENTORY where type only works for players.

**NOTE:** The ownsitemsimple variant is used to only add 1 item to compare. There is no list and no "" but also no boolean expression.

```yaml
ownsitem{list="where=HAND;material=IRON_SWORD;amount=1"&&"where=ARMOR;material=DIAMOND_CHESTPLATE;amount=1"
```
Returns true if the entity holds an *iron sword* **AND** wears a *diamond chestplate*.

```yaml
ownsitem{list="where=HAND;material=IRON_SWORD;amount=1"||"where=ARMOR;material=DIAMOND_CHESTPLATE;amount=1"
```
True if the entity holds an *iron sword* **OR** wears a *diamond chestplate*.

```yaml
ownsitem{list="where=HAND;material=IRON_SWORD;amount=1"&&"where=ARMOR;material=DIAMOND_CHESTPLATE;amount=1"||"where=INVENTORY;material=DIRT;amount=1"
```
True if the player holds an *iron sword* **AND** wears a *diamond chestplate* **OR** has 1 piece of *dirt* in its inventory.

##### `- inmotion{action=[BOOLEAN]}`
Checks if the entity is in motion. Do not work for players or none living entities.

##### `- facingdirection{direction=dir=d=facing=face=d=[DIRECTION];action=[BOOLEAN]}`
Check the entities direction. Possible values: NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST

##### `- parsedstance{s="<target.uuid>";cs=true;action=true}`
If cs (compareself) = true the TargetCondition check if the target's uuid is in the casters stance. If cs = false the condition check if the stance is set in the targeted entity if its a mythicmobs mob. 


# Targeters
#### If you want to use the customtargeters, you need atleast MythicMobs 4.3 or if you only have the lastest release, you need a patched 4.2. Place this jar in your pluginsfolder and restart your server. It will patch all the necessary things in MythicMobs to use CustomTargeters. https://github.com/BerndiVader/MythicMobsPatcher



##### `@crosshair`
Returns the crosshair targeted entity or location if caster is a player

##### `@ownertarget`
If caster mob have an owner it returns the target of the owner or if owner is player the crosshair target.

##### `@lastdamager`
Returns the entity if the lastdamage was done by an entity.

##### `@targeterstarget`
Returns the target of the targeted entity. In case of target is a player the crosshair target.

##### `@triggerstarget`
Returns the target of the triggered entity. In case of target is a player the corsshair target.

##### `@eyedirection{length=[VALUE]`
Returns the location length blocks away from the direction the caster is looking. `@eyedirection{l=20}` returns the location 20 blocks infront of the direction the caster is looking at.

##### `@triggerdirection{length=[VALUE]`
Returns the location length blocks away from the direction the trigger is looking. `@triggerdirection{l=20}` returns the location 20 blocks infront of the direction the trigger is looking at.

##### `@targetdirection{length=[VALUE]`
Returns the location length blocks away from the direction the target is looking. `@targetdirection{l=20}` returns the location 20 blocks infront of the direction the target is looking at.

##### `@ownerdirection{length=[VALUE]`
Returns the location length blocks away from the direction the owner is looking. `@ownerdirection{l=20}` returns the location 20 blocks infront of the direction the owner is looking at.




#### ~onKill trigger with lastdamagecause condition example

```yaml
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

package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.botai.Bot;
import com.gmail.berndivader.mythicmobsext.botai.Session;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.mechanics.AuraMechanic;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "chatlistener", author = "BerndiVader")
public class ChatListenerMechanic extends AuraMechanic implements ITargetedEntitySkill {
	static String str, response;
	int period;
	boolean breakOnMatch, breakOnFalse, multi, cancelMatch, cancelFalse, removephrase, infinite, ignoreTrigger, sense,
			strict;
	String storage, botId;
	String[] phrases;
	RangedDouble radius;
	Optional<Skill> matchSkill = Optional.empty();
	Optional<Skill> falseSkill = Optional.empty();
	Optional<Skill> inuseSkill = Optional.empty();
	Optional<Skill> endSkill = Optional.empty();

	static {
		str = "MME_CHAT";
		response = "BOTRESPONSE";
	}

	public ChatListenerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.auraName = Optional.of(mlc.getString("chatname", "chatlistener"));
		String s1 = mlc.getString("phrases", "").toLowerCase();
		if (s1.startsWith("\"") && s1.endsWith("\"")) {
			s1 = s1.substring(1, s1.length() - 1);
		}
		phrases = SkillString.parseMessageSpecialChars(s1).split(",");
		period = mlc.getInteger("period", 60);
		radius = new RangedDouble(mlc.getString("radius", "<10"));
		breakOnMatch = mlc.getBoolean("breakonmatch", true);
		breakOnFalse = mlc.getBoolean("breakonfalse", false);
		cancelMatch = mlc.getBoolean("cancelmatch", false);
		cancelFalse = mlc.getBoolean("cancelfalse", false);
		removephrase = mlc.getBoolean("removephrase", false);
		ignoreTrigger = mlc.getBoolean("ignoretrigger", true);
		infinite = mlc.getBoolean("infinite", false);
		strict = mlc.getBoolean("strict", false);
		multi = mlc.getBoolean("multi", false);
		storage = mlc.getString("meta", null);
		sense = mlc.getBoolean("sensitive", true);
		botId = mlc.getString("bot", new String());
		if ((s1 = mlc.getString("matchskill")) != null)
			matchSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("falseskill")) != null)
			falseSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("inuseskill")) != null)
			inuseSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("endskill")) != null)
			endSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata arg0, AbstractEntity arg1) {
		if (!ignoreTrigger && !arg1.isPlayer())
			return false;
		if ((multi && !arg1.getBukkitEntity().hasMetadata(str + this.auraName))
				|| (!multi && !arg0.getCaster().hasAura(auraName.get()))) {
			new ChatListener(this, arg0, arg1);
			return true;
		}
		if (inuseSkill.isPresent()) {
			Skill sk = inuseSkill.get();
			SkillMetadata sd = arg0.deepClone();
			if (sk.isUsable(sd))
				sk.execute(sd);
		}
		return false;
	}

	class ChatListener extends ChatListenerMechanic.AuraTracker implements Runnable, IParentSkill, Listener {
		final ChatListenerMechanic buff;
		int ticksRemaining;
		boolean hasEnded = false, bot = false;
		AbstractEntity p;
		Bot steve;
		Session steveSession;

		public ChatListener(ChatListenerMechanic buff, SkillMetadata data, AbstractEntity p) {
			super(p, data);
			this.buff = buff;
			this.ticksRemaining = buff.period;
			this.skillMetadata.setCallingEvent(this);
			this.p = p;
			if (botId.length() > 0) {
				steve = new Bot(botId);
				steveSession = steve.createSession();
			}
			bot = steve != null;
			Main.pluginmanager.registerEvents(this, Main.getPlugin());
			p.getBukkitEntity().setMetadata(str + this.buff.auraName, new FixedMetadataValue(Main.getPlugin(), true));
			this.start();
		}

		@Override
		public void run() {
			if (!buff.infinite)
				this.ticksRemaining--;
			if (skillMetadata.getCaster().getEntity().isDead() || !this.hasEnded && this.ticksRemaining <= 0) {
				if (endSkill.isPresent() && endSkill.get().isUsable(skillMetadata))
					endSkill.get().execute(skillMetadata.deepClone());
				this.terminate();
			}
		}

		@EventHandler
		public void chatListener(AsyncPlayerChatEvent e) {
			if (!buff.ignoreTrigger && e.getPlayer().getUniqueId() != p.getUniqueId())
				return;
			boolean bl1 = phrases.length == 0;
			String s2, s22;
			final String s222 = s2 = s22 = new PlaceholderString(e.getMessage()).get(skillMetadata, p);
			if (!sense)
				s2 = s2.toLowerCase();
			Skill sk = null;
			if (ChatListenerMechanic.this.radius.equals((double) Math.sqrt(MathUtils.distance3D(
					this.skillMetadata.getCaster().getEntity().getBukkitEntity().getLocation().toVector(),
					e.getPlayer().getLocation().toVector())))) {
				if (!bot) {
					for (int i1 = 0; i1 < phrases.length; i1++) {
						String s4 = new PlaceholderString(phrases[i1]).get(skillMetadata, p);
						if (!sense)
							s4 = s4.toLowerCase();
						if (bl1 = buff.strict ? s2.equals(s4) : s2.contains(s4)) {
							if (removephrase)
								s22 = s22.replace(phrases[i1], "");
							break;
						}
					}
					if (bl1) {
						if (cancelMatch)
							e.setCancelled(true);
						if (storage != null) {
							String s3 = new PlaceholderString(storage).get(skillMetadata, p);
							skillMetadata.getCaster().getEntity().getBukkitEntity().setMetadata(s3,
									new FixedMetadataValue(Main.getPlugin(), s22));
						}
						if (matchSkill.isPresent()) {
							sk = matchSkill.get();
							if (sk.isUsable(skillMetadata))
								sk.execute(skillMetadata.deepClone());
						}
						if (breakOnMatch)
							this.terminate();
					} else {
						if (cancelFalse)
							e.setCancelled(true);
						if (falseSkill.isPresent()) {
							sk = falseSkill.get();
							if (sk.isUsable(skillMetadata))
								sk.execute(skillMetadata.deepClone());
						}
						if (breakOnFalse)
							this.terminate();
					}
				} else {
					new BukkitRunnable() {
						@Override
						public void run() {
							String thought;
							try {
								thought = steveSession.think(s222);
							} catch (Exception e1) {
								thought = new String();
								e1.printStackTrace();
							}
							if (thought.length() > 0) {
								ActiveMob am = (ActiveMob) skillMetadata.getCaster();
								if (am != null) {
									am.setStance(thought);
									am.signalMob(BukkitAdapter.adapt(e.getPlayer()), response);
								} else {
									e.getPlayer().sendMessage(thought);
								}
							}
						}
					}.runTaskAsynchronously(Main.getPlugin());
				}
			}
		}

		@Override
		public boolean getCancelled() {
			return this.hasTerminated();
		}

		@Override
		public void setCancelled() {
			this.terminate();
		}

		@Override
		public boolean terminate() {
			if (!this.hasEnded) {
				if (ChatListenerMechanic.this.auraName.isPresent()) {
					this.skillMetadata.getCaster().unregisterAura(ChatListenerMechanic.this.auraName.get(), this);
				}
				this.hasEnded = true;
			}
			HandlerList.unregisterAll(this);
			p.getBukkitEntity().removeMetadata(str + this.buff.auraName, Main.getPlugin());
			return true;
		}
	}

}
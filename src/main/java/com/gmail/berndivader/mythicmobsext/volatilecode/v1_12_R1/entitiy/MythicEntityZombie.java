package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.entitiy;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.AttributeModifier;
import net.minecraft.server.v1_12_R1.AttributeRanged;
import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.DataConverterManager;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityChicken;
import net.minecraft.server.v1_12_R1.EntityCreeper;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityIronGolem;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityMonster;
import net.minecraft.server.v1_12_R1.EntityPigZombie;
import net.minecraft.server.v1_12_R1.EntityVillager;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.EntityZombieVillager;
import net.minecraft.server.v1_12_R1.EnumDifficulty;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.EnumMonsterType;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.GroupDataEntity;
import net.minecraft.server.v1_12_R1.IAttribute;
import net.minecraft.server.v1_12_R1.IEntitySelector;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.LootTables;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.PathfinderGoalBreakDoor;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.SoundEffect;
import net.minecraft.server.v1_12_R1.SoundEffects;
import net.minecraft.server.v1_12_R1.World;

public
class
MythicEntityZombie
extends
EntityMonster
{
	
    protected static final IAttribute a = new AttributeRanged(null, "zombie.spawnReinforcements", 0.0, 0.0, 1.0).a("Spawn Reinforcements Chance");
    private static final UUID b = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier c = new AttributeModifier(b, "Baby speed boost", 0.5, 1);
    private static final DataWatcherObject<Boolean> bx = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.h);
    private static final DataWatcherObject<Integer> by = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean> bz = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.h);
    private final PathfinderGoalBreakDoor bA;
    private boolean bB;
    private float bC;
    private float bD;
	private boolean sunBurn;

    public MythicEntityZombie(World world,boolean sunBurn) {
        super(world);
        this.sunBurn=sunBurn;
        this.bA = new PathfinderGoalBreakDoor(this);
        this.bC = -1.0f;
        this.setSize(0.6f, 1.95f);
    }

    @Override
    protected void r() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalAttack(this, 1.0, false));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0));
        this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0f));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.do_();
    }

    protected void do_() {
        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0, false));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, EntityPigZombie.class));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
        if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
            this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false));
        }
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityIronGolem>(this, EntityIronGolem.class, true));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(35.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(3.0);
        this.getAttributeInstance(GenericAttributes.h).setValue(2.0);
        this.getAttributeMap().b(a).setValue(this.random.nextDouble() * 0.10000000149011612);
    }

    @Override
    protected void i() {
        super.i();
        this.getDataWatcher().register(bx, false);
        this.getDataWatcher().register(by, 0);
        this.getDataWatcher().register(bz, false);
    }

    public void a(boolean flag) {
        this.getDataWatcher().set(bz, flag);
    }

    public boolean dr() {
        return this.bB;
    }

    public void p(boolean flag) {
        if (this.bB != flag) {
            this.bB = flag;
            ((Navigation)this.getNavigation()).a(flag);
            if (flag) {
                this.goalSelector.a(1, this.bA);
            } else {
                this.goalSelector.a(this.bA);
            }
        }
    }

    @Override
    public boolean isBaby() {
        return this.getDataWatcher().get(bx);
    }

    @Override
    protected int getExpValue(EntityHuman entityhuman) {
        if (this.isBaby()) {
            this.b_ = (int)((float)this.b_ * 2.5f);
        }
        return super.getExpValue(entityhuman);
    }

    public void setBaby(boolean flag) {
        this.getDataWatcher().set(bx, flag);
        if (this.world != null && !this.world.isClientSide) {
            AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
            attributeinstance.c(c);
            if (flag) {
                attributeinstance.b(c);
            }
        }
        this.r(flag);
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (bx.equals(datawatcherobject)) {
            this.r(this.isBaby());
        }
        super.a(datawatcherobject);
    }

    @Override
    public void n() {
        float f2;
        if (this.world.D() && !this.world.isClientSide && !this.isBaby() && this.sunBurn && this.p() && (f2 = this.aw()) > 0.5f && this.random.nextFloat() * 30.0f < (f2 - 0.4f) * 2.0f && this.world.h(new BlockPosition(this.locX, this.locY + (double)this.getHeadHeight(), this.locZ))) {
            boolean flag = true;
            ItemStack itemstack = this.getEquipment(EnumItemSlot.HEAD);
            if (!itemstack.isEmpty()) {
                if (itemstack.f()) {
                    itemstack.setData(itemstack.i() + this.random.nextInt(2));
                    if (itemstack.i() >= itemstack.k()) {
                        this.b(itemstack);
                        this.setSlot(EnumItemSlot.HEAD, ItemStack.a);
                    }
                }
                flag = false;
            }
            if (flag) {
                EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                this.world.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.setOnFire(event.getDuration());
                }
            }
        }
        super.n();
    }

    protected boolean p() {
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f2) {
        if (super.damageEntity(damagesource, f2)) {
            EntityLiving entityliving = this.getGoalTarget();
            if (entityliving == null && damagesource.getEntity() instanceof EntityLiving) {
                entityliving = (EntityLiving)damagesource.getEntity();
            }
            if (entityliving != null && this.world.getDifficulty() == EnumDifficulty.HARD && (double)this.random.nextFloat() < this.getAttributeInstance(a).getValue() && this.world.getGameRules().getBoolean("doMobSpawning")) {
                int i2 = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.locY);
                int k = MathHelper.floor(this.locZ);
                EntityZombie entityzombie = new EntityZombie(this.world);
                int l = 0;
                while (l < 50) {
                    int j1;
                    int k1;
                    int i1 = i2 + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    if (this.world.getType(new BlockPosition(i1, (j1 = j + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1)) - 1, k1 = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1))).q() && this.world.getLightLevel(new BlockPosition(i1, j1, k1)) < 10) {
                        entityzombie.setPosition(i1, j1, k1);
                        if (!this.world.isPlayerNearby(i1, j1, k1, 7.0) && this.world.a(entityzombie.getBoundingBox(), entityzombie) && this.world.getCubes(entityzombie, entityzombie.getBoundingBox()).isEmpty() && !this.world.containsLiquid(entityzombie.getBoundingBox())) {
                            this.world.addEntity(entityzombie, CreatureSpawnEvent.SpawnReason.REINFORCEMENTS);
                            entityzombie.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.REINFORCEMENT_TARGET, true);
                            entityzombie.prepare(this.world.D(new BlockPosition(entityzombie)), null);
                            this.getAttributeInstance(a).b(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806, 0));
                            entityzombie.getAttributeInstance(a).b(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806, 0));
                            break;
                        }
                    }
                    ++l;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean B(Entity entity) {
        boolean flag = super.B(entity);
        if (flag) {
            float f2 = this.world.D(new BlockPosition(this)).b();
            if (this.getItemInMainHand().isEmpty() && this.isBurning() && this.random.nextFloat() < f2 * 0.3f) {
                EntityCombustByEntityEvent event = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 2 * (int)f2);
                this.world.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    entity.setOnFire(event.getDuration());
                }
            }
        }
        return flag;
    }

    @Override
    protected SoundEffect F() {
        return SoundEffects.ji;
    }

    @Override
    protected SoundEffect d(DamageSource damagesource) {
        return SoundEffects.jq;
    }

    @Override
    protected SoundEffect cf() {
        return SoundEffects.jm;
    }

    protected SoundEffect dm() {
        return SoundEffects.jw;
    }

    @Override
    protected void a(BlockPosition blockposition, Block block) {
        this.a(this.dm(), 0.15f, 1.0f);
    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    @Nullable
    @Override
    protected MinecraftKey J() {
        return LootTables.am;
    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        super.a(difficultydamagescaler);
        if (this.random.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.05f : 0.01f)) {
            int i2 = this.random.nextInt(3);
            if (i2 == 0) {
                this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }

    public static void c(DataConverterManager dataconvertermanager) {
        EntityInsentient.a(dataconvertermanager, EntityZombie.class);
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (this.isBaby()) {
            nbttagcompound.setBoolean("IsBaby", true);
        }
        nbttagcompound.setBoolean("CanBreakDoors", this.dr());
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.getBoolean("IsBaby")) {
            this.setBaby(true);
        }
        this.p(nbttagcompound.getBoolean("CanBreakDoors"));
    }

    @Override
    public void b(EntityLiving entityliving) {
        super.b(entityliving);
        if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && entityliving instanceof EntityVillager) {
            if (this.world.getDifficulty() != EnumDifficulty.HARD && this.random.nextBoolean()) {
                return;
            }
            EntityVillager entityvillager = (EntityVillager)entityliving;
            EntityZombieVillager entityzombievillager = new EntityZombieVillager(this.world);
            entityzombievillager.u(entityvillager);
            this.world.kill(entityvillager);
            entityzombievillager.prepare(this.world.D(new BlockPosition(entityzombievillager)), new GroupDataZombie(false, null));
            entityzombievillager.setProfession(entityvillager.getProfession());
            entityzombievillager.setBaby(entityvillager.isBaby());
            entityzombievillager.setNoAI(entityvillager.isNoAI());
            if (entityvillager.hasCustomName()) {
                entityzombievillager.setCustomName(entityvillager.getCustomName());
                entityzombievillager.setCustomNameVisible(entityvillager.getCustomNameVisible());
            }
            this.world.addEntity(entityzombievillager, CreatureSpawnEvent.SpawnReason.INFECTION);
            this.world.a(null, 1026, new BlockPosition(this), 0);
        }
    }

    @Override
    public float getHeadHeight() {
        float f2 = 1.74f;
        if (this.isBaby()) {
            f2 = (float)((double)f2 - 0.81);
        }
        return f2;
    }

    @Override
    protected boolean c(ItemStack itemstack) {
        return itemstack.getItem() == Items.EGG && this.isBaby() && this.isPassenger() ? false : super.c(itemstack);
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, @Nullable GroupDataEntity groupdataentity) {
        Calendar calendar;
        GroupDataEntity object = super.prepare(difficultydamagescaler, groupdataentity);
        float f2 = difficultydamagescaler.d();
        this.m(this.random.nextFloat() < 0.55f * f2);
        if (object == null) {
            object = new GroupDataZombie(this.world.random.nextFloat() < 0.05f, null);
        }
        if (object instanceof GroupDataZombie) {
            GroupDataZombie entityzombie_groupdatazombie = (GroupDataZombie)object;
            if (entityzombie_groupdatazombie.a) {
                this.setBaby(true);
                if ((double)this.world.random.nextFloat() < 0.05) {
                    List<EntityChicken> list = this.world.a(EntityChicken.class, this.getBoundingBox().grow(5.0, 3.0, 5.0), IEntitySelector.b);
                    if (!list.isEmpty()) {
                        EntityChicken entitychicken = (EntityChicken)list.get(0);
                        entitychicken.p(true);
                        this.startRiding(entitychicken);
                    }
                } else if ((double)this.world.random.nextFloat() < 0.05) {
                    EntityChicken entitychicken1 = new EntityChicken(this.world);
                    entitychicken1.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0f);
                    entitychicken1.prepare(difficultydamagescaler, null);
                    entitychicken1.p(true);
                    this.world.addEntity(entitychicken1, CreatureSpawnEvent.SpawnReason.MOUNT);
                    this.startRiding(entitychicken1);
                }
            }
        }
        this.p(this.random.nextFloat() < f2 * 0.1f);
        this.a(difficultydamagescaler);
        this.b(difficultydamagescaler);
        if (this.getEquipment(EnumItemSlot.HEAD).isEmpty() && (calendar = this.world.ae()).get(2) + 1 == 10 && calendar.get(5) == 31 && this.random.nextFloat() < 0.25f) {
            this.setSlot(EnumItemSlot.HEAD, new ItemStack(this.random.nextFloat() < 0.1f ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
            this.dropChanceArmor[EnumItemSlot.HEAD.b()] = 0.0f;
        }
        this.getAttributeInstance(GenericAttributes.c).b(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.05000000074505806, 0));
        double d0 = this.random.nextDouble() * 1.5 * (double)f2;
        if (d0 > 1.0) {
            this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).b(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }
        if (this.random.nextFloat() < f2 * 0.05f) {
            this.getAttributeInstance(a).b(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25 + 0.5, 0));
            this.getAttributeInstance(GenericAttributes.maxHealth).b(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0 + 1.0, 2));
            this.p(true);
        }
        return object;
    }

    public void r(boolean flag) {
        this.a(flag ? 0.5f : 1.0f);
    }

    @Override
    public final void setSize(float f2, float f1) {
        boolean flag = this.bC > 0.0f && this.bD > 0.0f;
        this.bC = f2;
        this.bD = f1;
        if (!flag) {
            this.a(1.0f);
        }
    }

    protected final void a(float f2) {
        super.setSize(this.bC * f2, this.bD * f2);
    }

    @Override
    public double aF() {
        return this.isBaby() ? 0.0 : -0.45;
    }

    @Override
    public void die(DamageSource damagesource) {
        EntityCreeper entitycreeper;
        if (damagesource.getEntity() instanceof EntityCreeper && (entitycreeper = (EntityCreeper)damagesource.getEntity()).isPowered() && entitycreeper.canCauseHeadDrop()) {
            entitycreeper.setCausedHeadDrop();
            ItemStack itemstack = this.dn();
            if (!itemstack.isEmpty()) {
                this.a(itemstack, 0.0f);
            }
        }
        super.die(damagesource);
    }

    protected ItemStack dn() {
        return new ItemStack(Items.SKULL, 1, 2);
    }

    class 
    GroupDataZombie
    implements
    GroupDataEntity {
        public boolean a;

        private GroupDataZombie(boolean flag) {
            this.a = flag;
        }

        GroupDataZombie(boolean flag, Object object) {
            this(flag);
        }
    }
    
    class 
    PathfinderGoalAttack
    extends
    PathfinderGoalMeleeAttack {

        private final MythicEntityZombie h;
        private int i;

        public PathfinderGoalAttack(MythicEntityZombie entityZombie, double d2, boolean bl) {
            super(entityZombie, d2, bl);
            this.h = entityZombie;
        }

        @Override
        public void c() {
            super.c();
            this.i = 0;
        }

        @Override
        public void d() {
            super.d();
            this.h.a(false);
        }

        @Override
        public void e() {
            super.e();
            ++this.i;
            if (this.i >= 5 && this.c < 10) {
                this.h.a(true);
            } else {
                this.h.a(false);
            }
        }    	
    	
    }
}

package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.utils.MotionDirectionType;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="motiondirection",author="BerndiVader")
public class MotionDirectionCondition
extends 
AbstractCustomCondition
implements
IEntityCondition {
	private static String str;
	HashSet<MotionDirectionType>dirs;
	boolean bl1,bl2;
	
	static {
		str="MMEMOTIONDIR";
	}

	public MotionDirectionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		dirs=new HashSet<>();
		bl1=false;
		String[]arr1=mlc.getString("directions","ANY").toUpperCase().split(",");
		if (!(bl1=arr1.length==1&&arr1[0]=="ANY")) {
			MotionDirectionType mdt;
			for(int i1=0;i1<arr1.length;i1++) {
				if ((mdt=MotionDirectionType.get(arr1[i1]))==null) continue;
				dirs.add(mdt);
			}
		}
		bl2=mlc.getBoolean("store",false);
	}
	
	@Override
	public boolean check(AbstractEntity var1) {
		Location s=var1.getBukkitEntity().getLocation().clone(),t;
		s.setPitch(0);
		s.setY(0);
		s.setYaw(0);
		t=s.clone();
		Vector Vvp,Vap,Vvd,Vd,VDD;
		Vec3D v3=var1.isPlayer()
				?Utils.pl.getOrDefault(var1.getUniqueId(),new Vec3D(0,0,0))
				:NMSUtils.getEntityLastMot(var1.getBukkitEntity());
		t.add(v3.getX(),0,v3.getZ());
		if (v3.getX()!=0||v3.getZ()!=0) {
			float f1=MathUtils.lookAtYaw(t,s);
			t.setYaw(f1);
	        Vvp=s.toVector();
	        Vap=t.toVector();
	        Vvd=s.getDirection();
	        Vd=Vap.subtract(Vvp).normalize();
	        VDD=Vd.clone();
	        float a=(float)Math.toDegrees(Math.acos(Vd.dot(Vvd)));
	        a=VDD.crossProduct(Vvd.multiply(2D).normalize()).getY()<0.0d?-a:a;
	        MotionDirectionType mdt=MotionDirectionType.getMotionDirection((a-NMSUtils.getLastYawFloat(var1.getBukkitEntity())+630)%360);
	        if (bl2) var1.getBukkitEntity().setMetadata(MotionDirectionCondition.str, new FixedMetadataValue(Main.getPlugin(),mdt.toString()));
	        return bl1||dirs.contains(mdt);
		}
		return false;
	}
}

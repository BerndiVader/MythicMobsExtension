package com.gmail.berndivader.mythicmobsext.targeters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob.ThreatTable;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

@ExternalAnnotation(name="ttt,threattabletargeter",author="BerndiVader")
public class ThreatTableTargeter 	
extends 
IEntitySelector {
	RangedDouble r1,r2;
	static Field f1;
	
	static {
		try {
			f1=ThreatTable.class.getDeclaredField("threatTable");
			f1.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			// Auto-generated catch block
		}
	}
	
	public ThreatTableTargeter(MythicLineConfig mlc) {
		super(mlc);
		r1=new RangedDouble(mlc.getString("range",">0"));
		r2=new RangedDouble(mlc.getString("threat",">0"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity>targets=new HashSet<>();
		ActiveMob am=(ActiveMob)data.getCaster();
		Map<AbstractEntity,Double>tt=null;
		if (am!=null&&am.hasThreatTable()) {
			try {
				if ((tt=sort((ConcurrentHashMap<AbstractEntity,Double>)f1.get(am.getThreatTable())))==null) return targets;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			Iterator<Map.Entry<AbstractEntity,Double>> it=tt.entrySet().iterator();
			int i1=1;
			while(it.hasNext()) {
				Map.Entry<AbstractEntity,Double>p1=it.next();
				if(r1.equals(i1)&&r2.equals(p1.getValue())) targets.add(p1.getKey());
				i1++;
			}
		}
		return targets;
	}
	
	static <K,V extends Comparable<? super Double>>Map<AbstractEntity,Double>sort(Map<AbstractEntity,Double>tt) {
		List<Entry<AbstractEntity,Double>>l1=new ArrayList<>(tt.entrySet());
		l1.sort(Entry.comparingByValue());
		Collections.reverse(l1);
		Map<AbstractEntity,Double>m1=new LinkedHashMap<>();
		for (Entry<AbstractEntity,Double>e1:l1) {
			m1.put(e1.getKey(),e1.getValue());
		}
		return m1;
	}
	
}

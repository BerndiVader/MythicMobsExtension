package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.EntityType;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="hasvehicle",author="BerndiVader")
public class HasVehicleCondition
extends
AbstractCustomCondition
implements
IEntityCondition {
	private String[]arr1;

	public HasVehicleCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		if ((arr1=mlc.getString(new String[] {"types","type","t"},"ANY").toUpperCase().split(",")).length>0&&!arr1[0].equals("ANY")) {
			for(int j1=0;j1<arr1.length;j1++) {
				String s1=arr1[j1];
				try {
					EntityType.valueOf(s1);
				} catch (Exception ex){
					String[]arr2=arr1;
					String[]arr3=new String[arr2.length-1];
					if(j1>=0&&arr2.length>0) {
						System.arraycopy(arr2,0,arr3,0,j1);
						System.arraycopy(arr2,j1+1,arr3,j1,arr3.length-j1);
						arr1=arr3;
						j1--;
					}
				}
			}
		}
	}

	@Override
	public boolean check(AbstractEntity var1) {
		if (var1.getBukkitEntity().getVehicle()==null) return false;
		if (arr1.length==1&&arr1[0].equals("ANY")) return true;
		EntityType e=var1.getBukkitEntity().getVehicle().getType();
		for(String s1:arr1) {
			if (EntityType.valueOf(s1).equals(e)) return true;
		}
		return false;
	}

}

package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.jboolexpr.BooleanExpression;
import com.gmail.berndivader.mythicmobsext.jboolexpr.MalformedBooleanException;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.items.HoldingItem;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="testitemfor,ownsitem,ownsitemsimple,iteminhand",author="BerndiVader")
public class HasItemCondition 
extends
AbstractCustomCondition
implements
IEntityCondition 
{
	
	private String conditionLine,meta_var;
	private boolean is,store_result;
	private List<HoldingItem>holdinglist;

	public HasItemCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.is=line.toLowerCase().startsWith("ownsitemsimple");
		this.holdinglist=new ArrayList<>();
		store_result=!(this.meta_var=mlc.getString("var","")).isEmpty();
		String tmp=null;
		if (!is) {
			tmp=mlc.getString(new String[]{"list","l"},null);
		} else {
			tmp="\"where="+mlc.getString("where","ANY");
			tmp+=";material="+mlc.getString("material","ANY");
			tmp+=";amount="+mlc.getString("amount",">0");
			tmp+=";slot="+mlc.getString("slot","0");
			tmp+=";name="+mlc.getString("name","ANY");
			tmp+=";enchant="+mlc.getString("enchant","ANY");
			tmp+=";lore="+mlc.getString("lore","ANY");
			tmp+=";bagname="+mlc.getString("bagname",BackBagHelper.str_name)+"\"";
			tmp=SkillString.unparseMessageSpecialChars(tmp);
		}
		this.conditionLine=SkillString.parseMessageSpecialChars(tmp);
		if (tmp!=null) {
			String[]list=tmp.split("&&|\\|\\|");
			for (int a=0;a<list.length;a++) {
				String parse=list[a];
				HoldingItem holding=new HoldingItem();
				parse=SkillString.parseMessageSpecialChars(parse);
				this.conditionLine=this.conditionLine.replaceFirst(parse,"\\$"+Integer.toString(a));
				if (parse.startsWith("\"")&&parse.endsWith("\"")) HoldingItem.parse(parse.substring(1,parse.length()-1),holding);
				this.holdinglist.add(holding);
			}
		}
	}

	@Override
	public boolean check(AbstractEntity t) {
		if (t.isLiving()) {
			String c=this.conditionLine;
			final LivingEntity target=(LivingEntity)t.getBukkitEntity();
			for(int i1=0;i1<holdinglist.size();i1++) {
				boolean bool=false;
				HoldingItem holding=holdinglist.get(i1);
				List<ItemStack>contents=HoldingItem.getContents(holding,target);
				for(int i2=0;i2<contents.size();i2++) {
					if(bool=holding.stackMatch(contents.get(i2),false)) {
						if(store_result) target.setMetadata(meta_var,new FixedMetadataValue(Main.getPlugin(),holding.getWhere().name()));
						break;
					}
				}
				c=c.replaceFirst("\\$"+Integer.toString(i1),Boolean.toString(bool));
			}
			BooleanExpression be;
			try {
				be=BooleanExpression.readLR(c);
			} catch (MalformedBooleanException e) {
				Main.logger.warning("Invalid bool expression: "+this.conditionLine);
				return false;
			}
			return be.booleanValue();
		}
		return false;
	}
	
}

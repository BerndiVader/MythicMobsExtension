package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.jboolexpr.BooleanExpression;
import com.gmail.berndivader.mythicmobsext.jboolexpr.MalformedBooleanException;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.items.HoldingItem;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="ownsitem,ownsitemsimple,iteminhand",author="BerndiVader")
public class HasItemCondition 
extends
AbstractCustomCondition
implements
IEntityCondition 
{
	
	private String conditionLine;
	private boolean is;
	private List<HoldingItem>holdinglist;

	public HasItemCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.is=line.toLowerCase().startsWith("ownsitemsimple");
		this.holdinglist=new ArrayList<>();
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
			tmp+=";lore="+mlc.getString("lore","ANY")+"\"";
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
				if (parse.startsWith("\"")&&parse.endsWith("\"")) {
					parse=parse.substring(1,parse.length()-1);
					String[]p=parse.split(";");
					for(String parse1:p) {
						if(parse1.startsWith("material=")) {
							parse1=parse1.substring(9, parse1.length());
							holding.setMaterial(parse1);
						} else if(parse1.startsWith("lore=")) {
							parse1=parse1.substring(5, parse1.length());
							holding.setLore(parse1);
						} else if(parse1.startsWith("name=")) {
							parse1=parse1.substring(5, parse1.length());
							holding.setName(parse1);
						} else if(parse1.startsWith("amount=")) {
							parse1=parse1.substring(7, parse1.length());
							holding.setAmount(parse1);
						} else if(parse1.startsWith("where=")) {
							parse1=parse1.substring(6,parse1.length());
							holding.setWhere(parse1);
						} else if(parse1.startsWith("slot=")) {
							parse1=parse1.substring(5,parse1.length());
							holding.setSlot(Integer.parseInt(parse1));
						} else if(parse1.startsWith("enchant=")) {
							parse1=parse1.substring(8,parse1.length());
							holding.setEnchantment(parse1);
						}
					}
				}
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
					if(bool=holding.stackMatch(contents.get(i2),false)) break;
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

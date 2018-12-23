package cn.AldebaRain.world.formations;

import cn.AldebaRain.world.util.*;

/**
 *	长蛇阵：row*col = 7 * 1.
 *	
 *	@author AldebaRain
 *
 *	@see Formation
 */
public final class ChangShe extends Formation {
	
	public ChangShe(GroupType ct) {	
		super(FormationType.CS, 8, 1, 3, 0); // 以阵型图所占行列等构建	
		if (ct == GroupType.Bro) {
			// 葫芦娃的位置
			formMap.put(new Point(0, 0), CreatureType.Bro1);
			formMap.put(new Point(1, 0), CreatureType.Bro2);
			formMap.put(new Point(2, 0), CreatureType.Bro3);
			formMap.put(new Point(3, 0), CreatureType.Bro4);
			formMap.put(new Point(4, 0), CreatureType.Bro5);
			formMap.put(new Point(5, 0), CreatureType.Bro6);
			formMap.put(new Point(6, 0), CreatureType.Bro7);
			formMap.put(new Point(7, 0), CreatureType.Eld); // 老爷爷
		}
		else {
			// 妖怪的位置
			formMap.put(new Point(0, 0), CreatureType.Mons);
			formMap.put(new Point(1, 0), CreatureType.Mons);
			formMap.put(new Point(2, 0), CreatureType.Mons);
			formMap.put(new Point(3, 0), CreatureType.Scorp); // 蝎子精
			formMap.put(new Point(7, 0), CreatureType.Snk); // 蛇精
			formMap.put(new Point(4, 0), CreatureType.Mons);
			formMap.put(new Point(5, 0), CreatureType.Mons);
			formMap.put(new Point(6, 0), CreatureType.Mons);
		}
	}
}

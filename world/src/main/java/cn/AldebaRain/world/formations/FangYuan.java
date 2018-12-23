package cn.AldebaRain.world.formations;

import cn.AldebaRain.world.util.*;

/**
 *	 方円阵：row*col = 5 * 5. <br>
 *	
 *	 		0	1	2	3	4		0	1	2	3	4 <br>
 *	 
 *	0		O	O	&	O	O		O	O	#	O	O <br>
 *	1		O	&	O	&	O		O	#	O	#	O <br>
 *	2		&	O	O	O	&		#	O	O	O	# <br>
 *	3		O	&	O	&	O		O	#	O	#	O <br>
 *	4		O	O	&	O	O		O	O	#	O	O <br>
 *	
 *	@author AldebaRain
 */
public final class FangYuan extends Formation {
	
	public FangYuan(GroupType ct) {		
		super(FormationType.FY, 5, 5, 2, 2); // 以阵型图所占行列构建
		if (ct == GroupType.Bro) {
			// 葫芦娃的位置
			formMap.put(new Point(0, 2), CreatureType.Bro1);
			formMap.put(new Point(1, 1), CreatureType.Bro2);
			formMap.put(new Point(1, 3), CreatureType.Bro3);
			formMap.put(new Point(2, 0), CreatureType.Eld); // 老爷爷
			formMap.put(new Point(2, 4), CreatureType.Bro4);
			formMap.put(new Point(3, 1), CreatureType.Bro5);
			formMap.put(new Point(3, 3), CreatureType.Bro6);
			formMap.put(new Point(4, 2), CreatureType.Bro7);
		}
		else {
			// 妖怪的位置
			formMap.put(new Point(0, 2), CreatureType.Mons);
			formMap.put(new Point(1, 1), CreatureType.Mons);
			formMap.put(new Point(1, 3), CreatureType.Mons);
			formMap.put(new Point(2, 0), CreatureType.Scorp); // 蝎子精
			formMap.put(new Point(2, 4), CreatureType.Snk); // 蛇精
			formMap.put(new Point(3, 1), CreatureType.Mons);
			formMap.put(new Point(3, 3), CreatureType.Mons);
			formMap.put(new Point(4, 2), CreatureType.Mons);
		}
	}
}

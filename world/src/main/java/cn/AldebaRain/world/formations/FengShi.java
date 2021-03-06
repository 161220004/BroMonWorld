package cn.AldebaRain.world.formations;

import cn.AldebaRain.world.util.*;

/**
 *	 锋矢阵：row*col = 7 * 6 / 5 * 4. <br>
 *	
 *	 		0	1	2	3	4	5		0	1	2	3 <br>
 *	 
 *	0		O	O	&	O	O	O		O	#	O	O <br>
 *	1		O	&	O	O	O	O		O	O	#	O <br>
 *	2		&	O	O	O	O	O		#	#	#	# <br>
 *	3		&	&	&	&	&	&		O	O	#	O <br>
 *	4		&	O	O	O	O	O		O	#	O	O <br>
 *	5		O	&	O	O	O	O <br>
 *	6		O	O	&	O	O	O <br>
 *	
 *	@author AldebaRain
 */
public final class FengShi extends Formation {
	
	public FengShi(GroupType ct) {		
		super(FormationType.FS); // 构建
		if (ct == GroupType.Bro) {
			init(5, 4, 2, 2); // 阵型图所占行列
			// 葫芦娃的位置
			formMap.put(new Point(2, 0), CreatureType.Eld); // 老爷爷
			formMap.put(new Point(0, 1), CreatureType.Bro1);
			formMap.put(new Point(2, 1), CreatureType.Bro2);
			formMap.put(new Point(4, 1), CreatureType.Bro3);
			formMap.put(new Point(1, 2), CreatureType.Bro4);
			formMap.put(new Point(2, 2), CreatureType.Bro5);
			formMap.put(new Point(3, 2), CreatureType.Bro6);
			formMap.put(new Point(2, 3), CreatureType.Bro7);
		}
		else {
			init(7, 6, 3, 2); // 阵型图所占行列
			// 妖怪的位置
			formMap.put(new Point(0, 2), CreatureType.Mons);
			formMap.put(new Point(1, 1), CreatureType.Mons);
			formMap.put(new Point(2, 0), CreatureType.Mons);
			formMap.put(new Point(3, 0), CreatureType.Scorp); // 蝎子精
			formMap.put(new Point(3, 1), CreatureType.Mons);
			formMap.put(new Point(3, 2), CreatureType.Snk); // 蛇精
			formMap.put(new Point(3, 3), CreatureType.Mons);
			formMap.put(new Point(3, 4), CreatureType.Mons);
			formMap.put(new Point(3, 5), CreatureType.Mons);
			formMap.put(new Point(4, 0), CreatureType.Mons);
			formMap.put(new Point(5, 1), CreatureType.Mons);
			formMap.put(new Point(6, 2), CreatureType.Mons);
		}
	}
}

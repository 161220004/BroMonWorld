package cn.AldebaRain.world.formations;

import cn.AldebaRain.world.util.*;

/**
 *	 冲轭阵：row*col = 8 * 2. <br>
 *	
 *	 		0	1		0	1 <br>
 *	 
 *	0		O	&		#	O <br>
 *	1		&	O		O	# <br>
 *	2		O	&		#	O <br>
 *	3		&	O		O	# <br>
 *	4		O	&		#	O <br>
 *	5		&	O		O	# <br>
 *	6		O	&		#	O <br>
 *	7		&	O		O	# <br>
 *	
 *	@author AldebaRain
 */
public final class ChongE extends Formation {
	
	public ChongE(GroupType ct) {		
		super(FormationType.CE, 8, 2, 3, 0); // 构建
		if (ct == GroupType.Bro) {
			// 葫芦娃的位置
			formMap.put(new Point(0, 0), CreatureType.Bro1);
			formMap.put(new Point(2, 0), CreatureType.Bro2);
			formMap.put(new Point(4, 0), CreatureType.Bro3);
			formMap.put(new Point(6, 0), CreatureType.Bro4);
			formMap.put(new Point(1, 1), CreatureType.Bro5);
			formMap.put(new Point(3, 1), CreatureType.Bro6);
			formMap.put(new Point(5, 1), CreatureType.Bro7);
			formMap.put(new Point(7, 1), CreatureType.Eld); // 老爷爷
		}
		else {
			// 妖怪的位置
			formMap.put(new Point(1, 0), CreatureType.Mons);
			formMap.put(new Point(3, 0), CreatureType.Mons);
			formMap.put(new Point(5, 0), CreatureType.Mons);
			formMap.put(new Point(7, 0), CreatureType.Mons);
			formMap.put(new Point(0, 1), CreatureType.Mons);
			formMap.put(new Point(2, 1), CreatureType.Scorp); // 蝎子精
			formMap.put(new Point(4, 1), CreatureType.Snk); // 蛇精
			formMap.put(new Point(6, 1), CreatureType.Mons);
		}
	}
}

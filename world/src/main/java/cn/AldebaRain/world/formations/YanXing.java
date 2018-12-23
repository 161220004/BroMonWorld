package cn.AldebaRain.world.formations;

import cn.AldebaRain.world.util.*;

/**
 *	 雁行阵：row*col = 8 * 8. <br>
 *	
 *	 		0	1	2	3	4	5	6	7		0	1	2	3	4	5	6	7 <br>
 *	 
 *	0		O	O	O	O	O	O	O	&		#	O	O	O	O	O	O	O <br>
 *	1		O	O	O	O	O	O	&	O		O	#	O	O	O	O	O	O <br>
 *	2		O	O	O	O	O	&	O	O		O	O	#	O	O	O	O	O <br>
 *	3		O	O	O	O	&	O	O	O		O	O	O	#	O	O	O	O <br>
 *	4		O	O	O	&	O	O	O	O		O	O	O	O	#	O	O	O <br>
 *	5		O	O	&	O	O	O	O	O		O	O	O	O	O	#	O	O <br>
 *	6		O	&	O	O	O	O	O	O		O	O	O	O	O	O	#	O <br>
 *	7		&	O	O	O	O	O	O	O		O	O	O	O	O	O	O	# <br>
 *	
 *	@author AldebaRain
 */
public final class YanXing extends Formation {
	
	public YanXing(GroupType ct) {		
		super(FormationType.YX); // 构建
		if (ct == GroupType.Bro) {
			init(8, 8, 3, 3); // 阵型图所占行列
			// 葫芦娃的位置
			formMap.put(new Point(0, 0), CreatureType.Bro1);
			formMap.put(new Point(1, 1), CreatureType.Bro2);
			formMap.put(new Point(2, 2), CreatureType.Bro3);
			formMap.put(new Point(3, 3), CreatureType.Bro4);
			formMap.put(new Point(4, 4), CreatureType.Bro5);
			formMap.put(new Point(5, 5), CreatureType.Bro6);
			formMap.put(new Point(6, 6), CreatureType.Bro7);
			formMap.put(new Point(7, 7), CreatureType.Eld); // 老爷爷
		}
		else {
			init(8, 8, 3, 4); // 阵型图所占行列
			// 妖怪的位置
			formMap.put(new Point(0, 7), CreatureType.Mons);
			formMap.put(new Point(1, 6), CreatureType.Mons);
			formMap.put(new Point(2, 5), CreatureType.Scorp); // 蝎子精
			formMap.put(new Point(3, 4), CreatureType.Mons);
			formMap.put(new Point(4, 3), CreatureType.Mons);
			formMap.put(new Point(5, 2), CreatureType.Snk); // 蛇精
			formMap.put(new Point(6, 1), CreatureType.Mons);
			formMap.put(new Point(7, 0), CreatureType.Mons);
		}
	}
}

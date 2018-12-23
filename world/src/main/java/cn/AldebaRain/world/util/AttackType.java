package cn.AldebaRain.world.util;

/**
 *	攻击种类.<br>
 *	用来决定攻击动画的效果；
 *	包括：
 *	近身攻击：敌方闪烁
 *	远程攻击：向敌方发射球或者射线
 *	范围攻击：以自己为中心产生“涟漪”
 *	@author AldebaRain
 */
public enum AttackType {
	
	Knock("近身攻击"),
	Radiate("发射射线"),
	Fire("发射能量球"),
	Aoe("范围攻击");
	
	public String name;
	
	private AttackType(String nam) {
		name = nam;
	}
}

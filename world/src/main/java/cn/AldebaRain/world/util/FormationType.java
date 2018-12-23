package cn.AldebaRain.world.util;

import cn.AldebaRain.world.formations.*;

/**	
 *	阵型类型（共8种）
 * 
 *	@author AldebaRain
 */
public enum FormationType {
	
	HY("鹤翼阵", 1.0, 1.0),
	YX("雁行阵", 1.0, 1.0),
	CE("冲轭阵", 1.0, 1.0),
	CS("长蛇阵", 1.0, 1.0),
	YL("鱼鳞阵", 1.0, 0.85),
	FY("方円阵", 1.0, 1.0),
	YY("偃月阵", 1.0, 0.4),
	FS("锋矢阵", 1.0, 0.7);
	
	public String name;
	// 阵型对攻击的加成
	public double broAtkBonus; 
	public double monAtkBonus; 
	private FormationType(String name, double brobon, double monbon) { // 构造方法
		this.name = name;
		this.broAtkBonus = brobon;
		this.monAtkBonus = monbon;
	}
	
	/**	@param type 阵营
	 *	@return 对应的阵型对象 */
	public Formation getFormation(GroupType type) {
		switch (this) {
		case HY: return new HeYi(type); 
		case YX: return new YanXing(type); 
		case CE: return new ChongE(type);
		case CS: return new ChangShe(type);
		case YL: return new YuLin(type);
		case FY: return new FangYuan(type);
		case YY: return new YanYue(type);
		case FS: return new FengShi(type);
		default: return new ChangShe(type); 
		}
	}
}

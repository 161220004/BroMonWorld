package cn.AldebaRain.world.entities;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.util.*;

public class EntityEld extends Entity implements EntityAction {

	/**	初始构造函数	*/
	public EntityEld(Point bia, Point p) {
		super(bia, CreatureType.Eld, p);
	}
	/**	构造函数，用于xml文件解析	*/
	public EntityEld(int i, int orgr, int orgc) {
		super(i, CreatureType.Eld, orgr, orgc);
	}

	/**	重写父类方法	*/
	@Override
	public void trymove() {
		super.trymove();
		if (Global.battleEnd) return;
		if (Global.keepFormationRound <= 0) // 不保持阵型
			setRandomDirection(true, 2);
	}
	
	/**	重写父类方法	*/
	@Override
	public void attack() {
		super.attack();
		startAttackSideEffect();
	}
	
}

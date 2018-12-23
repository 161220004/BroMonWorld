package cn.AldebaRain.world.entities;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.util.*;

public class EntityMons extends Entity implements EntityAction {

	/**	初始构造函数	*/
	public EntityMons(Point bia, Point p) {
		super(bia, CreatureType.Mons, p);
	}
	/**	构造函数，用于xml文件解析	*/
	public EntityMons(int i, int orgr, int orgc) {
		super(i, CreatureType.Mons, orgr, orgc);
	}

	/**	重写父类方法	*/
	@Override
	public void trymove() {
		super.trymove();
		if (Global.battleEnd) return;
		if (Global.keepFormationRound <= 0) // 不保持阵型
			setRandomDirection(false, 1);
	}
	
	/**	重写父类方法	*/
	@Override
	public void attack() {
		super.attack();
		startAttackSideEffect();
	}
	
}

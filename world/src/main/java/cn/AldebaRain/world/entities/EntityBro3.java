package cn.AldebaRain.world.entities;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.util.*;

public class EntityBro3 extends Entity implements EntityAction {

	/**	初始构造函数	*/
	public EntityBro3(Point bia, Point p) {
		super(bia, CreatureType.Bro3, p);
	}
	/**	构造函数，用于xml文件解析	*/
	public EntityBro3(int i, int orgr, int orgc) {
		super(i, CreatureType.Bro3, orgr, orgc);
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

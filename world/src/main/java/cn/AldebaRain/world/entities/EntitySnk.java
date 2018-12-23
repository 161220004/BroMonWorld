package cn.AldebaRain.world.entities;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.util.*;

public class EntitySnk extends Entity implements EntityAction {

	/**	初始构造函数	*/
	public EntitySnk(Point bia, Point p) {
		super(bia, CreatureType.Snk, p);
	}
	/**	构造函数，用于xml文件解析	*/
	public EntitySnk(int i, int orgr, int orgc) {
		super(i, CreatureType.Snk, orgr, orgc);
	}

	/**	重写父类方法	*/
	@Override
	public void trymove() {
		super.trymove();
		if (Global.battleEnd) return;
		if (Global.keepFormationRound <= 0) // 不保持阵型
			setRandomDirection(true, 3);
	}
	
	/**	重写父类方法	*/
	@Override
	public void attack() {
		super.attack();
		startAttackSideEffect();
	}
	
	/**	蛇精绝招的特殊效果，范围内治愈10点，包括自己	*/
	@Override
	public void startAttackSideEffect() {
		EntityAction.super.startAttackSideEffect();
		boolean hasTarget = false;
		boolean isFull = true;
		for (Entity en: CoreWorld.entities.values()) {
			if ((en.state == EntityState.LIVE) && (en.creatureType.group() == creatureType.group())
					&& inAttackRange(en.position)) {
				hasTarget = true;
				if (en.blood != en.creatureType.totalBlood)
					isFull = false;
				en.blood += 10;
				if (en.blood > en.creatureType.totalBlood)
					en.blood = en.creatureType.totalBlood;
			}
		}
		if (isFull) return;
		else if (hasTarget && (targetSet.size() == 0))
			targetSet.add(this); // 为了确保触发动画
	}
	
}

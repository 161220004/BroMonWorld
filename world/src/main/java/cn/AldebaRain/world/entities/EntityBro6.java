package cn.AldebaRain.world.entities;

import java.util.Iterator;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.util.*;

public class EntityBro6 extends Entity implements EntityAction {

	/**	初始构造函数	*/
	public EntityBro6(Point bia, Point p) {
		super(bia, CreatureType.Bro6, p);
	}
	/**	构造函数，用于xml文件解析	*/
	public EntityBro6(int i, int orgr, int orgc) {
		super(i, CreatureType.Bro6, orgr, orgc);
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
	
	/**	老六的绝招特效：吸血，恢复对方剩余血量 50% 的血量，附加伤害 20%	*/
	@Override
	public void startAttackSideEffect() {
		EntityAction.super.startAttackSideEffect();
		Iterator<Entity> it = targetSet.iterator();
		if (!it.hasNext()) return;
		Entity target = it.next();
		blood += target.blood / 5;
		target.blood = target.blood * 80 / 100;
		if (blood > creatureType.totalBlood)
			blood = creatureType.totalBlood;
	}
	
}

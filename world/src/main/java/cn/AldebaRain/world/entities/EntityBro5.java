package cn.AldebaRain.world.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.util.*;

public class EntityBro5 extends Entity implements EntityAction {

	/**	初始构造函数	*/
	public EntityBro5(Point bia, Point p) {
		super(bia, CreatureType.Bro5, p);
	}
	/**	构造函数，用于xml文件解析	*/
	public EntityBro5(int i, int orgr, int orgc) {
		super(i, CreatureType.Bro5, orgr, orgc);
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

	/**	老五绝招特效：血量分配，平均范围内友方血量，触发概率0.8	*/
	@Override
	public void startAttackSideEffect() {
		EntityAction.super.startAttackSideEffect();
		double rand = Math.random();
		if (rand < 0.8) // 触发概率0.8
			return;
		int sumBlood = 10;
		boolean isFull = true;
		List<Entity> aveArray = new ArrayList<>();
		for (Entity en: CoreWorld.entities.values()) {
			if ((en.state == EntityState.LIVE) && (en.creatureType.group() == creatureType.group())
					&& inAttackRange(en.position)) {
				if (en.blood != en.creatureType.totalBlood)
					isFull = false;
				sumBlood += en.blood;
				aveArray.add(en);
			}
		}
		int n = aveArray.size();
		if (n == 0) return;
		else if (isFull) return;
		else if (targetSet.size() == 0) targetSet.add(this); // 为了确保触发动画
		// 血量总值从大到小排序
		Comparator<Entity> comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity en1, Entity en2) {
				return (en2.creatureType.totalBlood - en1.creatureType.totalBlood);
			}};
		Collections.sort(aveArray, comparator);
		// 从小到大开始平均
		while (n > 0) {
			Entity en = aveArray.get(n - 1);
			if (en.creatureType.totalBlood < sumBlood / n) {
				en.blood = en.creatureType.totalBlood;
				sumBlood -= en.creatureType.totalBlood;
			} else {
				en.blood = sumBlood / n;
				sumBlood -= sumBlood / n;
			}
			n -= 1;
		}
	}
	
}

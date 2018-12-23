package cn.AldebaRain.world.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.AldebaRain.world.CoreWorld;
import cn.AldebaRain.world.Global;
import cn.AldebaRain.world.gui.GUIWindow;
import cn.AldebaRain.world.util.*;

/**
 *	生物实体类，世界的每个生物都有其对应的实体. <br>
 *	包括：
 *	(1) 固定属性：id + 生物类型（包括攻击类型攻击力等） + 线程（行为）
 * 	(2) 变化属性：状态（生死）+ 血量 + 位置 + 移动方向 + 攻击目标集
 * 
 *		@see #id
 *		@see #creatureType
 *		@see #thread
 *
 *		@see #state
 *		@see #blood
 *		@see #position
 *		@see #direction
 *		@see #targetSet
 *
 *	@see #attack()
 *	@see #trymove()
 *	@see #run()
 *	@see #start()
 *	@see #setRandomDirection(boolean, int)
 * 
 *	@author AldebaRain
 */
public class Entity implements Runnable {
	
	/**	编号，为阵营号*1000+阵型偏移	*/
	public int id;
	/**	生物类型	*/
	public CreatureType creatureType;

	/**	当前位置	*/
	public Point position;
	/**	当前状态	*/
	public EntityState state;
	/**	当前血量	*/
	public int blood;

	/**	移动方向	*/
	public Point direction = new Point(0, 0);
	/**	攻击对象的集合	*/
	public Set<Entity> targetSet = new HashSet<>();

	/**	线程	*/
	protected Thread thread; 
	
	/**	初始构造函数
	 *	@param bia 本实体阵型内位置，记作偏移
	 *	@param p 本实体在世界初始位置	*/
	public Entity(Point bia, CreatureType ctype, Point p) {
		creatureType = ctype;
		position = p.copy();
		int offset = bia.row() * Global.colNum + bia.col();
		id = (creatureType.group() == GroupType.Bro) ? (1000 + offset) : (2000 + offset);
		state = EntityState.LIVE;
		blood = creatureType.totalBlood;
	}
	/**	构造函数，用于xml文件解析	*/
	public Entity(int i, CreatureType ct, int orgr, int orgc) {
		id = i;
		creatureType = ct;
		position = new Point(orgr, orgc);
		state = EntityState.LIVE;
		blood = creatureType.totalBlood;
	}

	/**	重设属性值，用于xml文件解析	*/
	public void resetEntity(EntityState st, int bl, int pr, int pc, int movr, int movc) {
		state = st;
		blood = bl;
		position.reset(pr, pc);
		direction.reset(movr, movc);
	}

	/**	敌方最近对象的位置或null	*/
	private Point nearEnemy = null;
	
	/**	判断敌方是否在攻击范围内	*/
	protected boolean inAttackRange(Point pEnemy) {
		int dis = creatureType.attackDistance;
		if (pEnemy.inRange(position.mov(-dis, -dis), position.mov(dis, dis)))
			return true;
		else return false;
	}
	
	/**	为了制作动画，获得攻击的对象	*/
	private void getAttackTargets() {
		targetSet.removeAll(targetSet); // 先清空
		for (Entity en: CoreWorld.entities.values()) {
			if ((targetSet.size() < creatureType.attackNum) 
					&& (en.state == EntityState.LIVE)
					&& (en.creatureType.group() != creatureType.group())
					&& inAttackRange(en.position)) {
				targetSet.add(en);
			}
		}
	}

	/**	进行攻击
	 *	并通过判断是否在攻击范围以界定攻击是否有效
	 *	最后更新所有实体的血量和位置，包括副作用
	 *	等待子类对副作用进行重写	*/
	public void attack() { 
		getAttackTargets();
		for (Entity en: targetSet) {
			double bonus = 1; // 阵型修正指数
			if (creatureType.group() == GroupType.Bro)
				bonus = CoreWorld.broForm.getType().broAtkBonus;
			else bonus = CoreWorld.monForm.getType().monAtkBonus;
			// 敌方剩余血量
			int restBlood = (int)(en.blood - bonus * creatureType.attackPower);
			if (restBlood <= 0) {
				en.blood = 0;
				en.state = EntityState.DEAD;
			}
			else en.blood = restBlood;
		}
	}

	/**	设定direction，等待子类的重写	*/
	public void trymove() { 
		direction.reset(0, 0); 
		if (Global.keepFormationRound > 0) { // 若要求保持阵型
			if (creatureType.group() == GroupType.Bro) direction.reset(0, 1);
			else direction.reset(0, -1);
			if (!isValidDirection())
				direction.reset(0, 0); 
		}
	}

	/**	向右下移动Entity
	 *	限制Entity类的所有实例的访问	*/
	@Override
	public void run() {
		synchronized (CoreWorld.entities) {
			// 先判断战斗是否结束
			nearEnemy = getNearestEnemy();
			if (nearEnemy == null) // 无敌方对象
				Global.battleEnd = true;
			if (Global.battleEnd == true) {
				GUIWindow.threadCountDown.countDown();
				return;
			}
			if (state != EntityState.LIVE) { // 本实体对象死亡
				direction.reset(0, 0); 
				targetSet.removeAll(targetSet);
				GUIWindow.threadCountDown.countDown();
				return;
			}
			// 攻击
			attack();
			// 移动
			trymove(); 
			position.reset(position.mov(direction));
			GUIWindow.threadCountDown.countDown();
			return;
		}
	}

	/**	开启线程	*/
	public void start() {
    	thread = new Thread(this);
    	thread.start();
	}
	
	/**	获取最近的敌方实体的位置
	 *	@return 若敌方全部消灭，返回null；否则返回距离最近的敌方实体位置	*/
	private Point getNearestEnemy() {
		Entity nearEn = null; // 最近的敌方实体指针
		int nearD = Integer.MAX_VALUE; // 最近距离
		for (Entity en: CoreWorld.entities.values()) {
			if ((en.state == EntityState.LIVE) && (en.creatureType.group() != creatureType.group())) {
				int tmpD = en.position.distance(position);
				if (tmpD < nearD) {
					nearD = tmpD;
					nearEn = en;
				}
			}
		}
		return ((nearEn == null) ? (null) : (nearEn.position));
	}

	/**	获得一个与参数有关的随机位移向量
	 *	@param keepDistance 是否与敌方保持距离
	 *	@param minDistance 若保持距离，最小距离多少	*/
	protected void setRandomDirection(boolean keepDistance, int minDistance) {
		direction.reset(0, 0);
		// 自己到最近敌方的向量(eVr, eVc)
		int eVr = nearEnemy.row() - position.row();
		int eVc = nearEnemy.col() - position.col();
		int eDr = Math.abs(eVr);
		int eDc = Math.abs(eVc);
		int eSr = (eDr == 0) ? (0) : (eDr / eVr); // 靠近的方向
		int eSc = (eDc == 0) ? (0) : (eDc / eVc);
		int eDis = Integer.max(eDr, eDc);
		int aDis = creatureType.attackDistance;
		int mDis = creatureType.movDistance;
		// 如果移动再远也无法进入攻击范围
		if (mDis < eDis - aDis) { // 直接移到尽可能接近
			int dR = Integer.min(mDis, eDr);
			int dC = Integer.min(mDis, eDc);
			direction.reset(eSr * dR, eSc * dC);
			int tryNum = 3;
			while (!isValidDirection() && (tryNum > 0)) {
				if ((dR < 0) || (dC < 0)) { // 彻底不行了，就随机数吧
					double randR = Math.random() * (2 * mDis) - mDis;
					double randC = Math.random() * (2 * mDis) - mDis;
					direction.reset((int)Math.round(randR), (int)Math.round(randC));
					tryNum -= 1;
				} else {
					double rand = Math.random();
					if (rand < 0.5) dR -= 1;
					else dC -= 1;
					direction.reset(eSr * dR, eSc * dC);
				}
			}
			if (!isValidDirection())
				direction.reset(0, 0);
		} 
		else { 
			// 查找“以position为中心半径mDis的范围”和“以nearEnemy为中心半径aDis的范围”的交叉
			int tLUr = Integer.max(position.row() - mDis, nearEnemy.row() - aDis);
			int tLUc = Integer.max(position.col() - mDis, nearEnemy.col() - aDis);
			int tRDr = Integer.min(position.row() + mDis, nearEnemy.row() + aDis);
			int tRDc = Integer.min(position.col() + mDis, nearEnemy.col() + aDis);
			if (tLUr < 0) tLUr = 0;
			else if (tRDr >= Global.rowNum) tRDr = Global.rowNum - 1;
			if (tLUc < 0) tLUc = 0;
			else if (tRDc >= Global.colNum) tRDc = Global.colNum - 1;
			// 所有可能的落点数组
			ArrayList<Point> tPoints = new ArrayList<>();
			for (int ri = tLUr; ri <= tRDr; ri++) {
				for (int ci = tLUc; ci <= tRDc; ci++) {
					Point t = new Point(ri, ci);
					if (keepDistance) {
						if (t.distance(nearEnemy) >= minDistance)
							tPoints.add(t);
					} else
						tPoints.add(t);
				}
			}
			// 排除所有活着的对象的落点
			ArrayList<Point> livPoints = getLivePoints();
			tPoints.removeAll(livPoints);
			int tryNum = 4;
			do {
				int tNum = tPoints.size();
				if (tNum == 0) { // 随机数
					double randR = Math.random() * (1 + 2 * mDis) - mDis; // 取int得到 -mDis ~ mDis
					double randC = Math.random() * (1 + 2 * mDis) - mDis;
					if (keepDistance) // 远离
						direction.reset(-eSr * Math.abs((int)randR), -eSc * Math.abs((int)randC));
					else direction.reset((int)randR, (int)randC);
					tryNum -= 1;
				}
				else {
					double rand = Math.random() * tNum;
					Point t = tPoints.get((int)rand);
					direction.reset(t.row() - position.row(), t.col() - position.col());
					tryNum -= 1;
				}
			} while (!isValidDirection() && (tryNum >= 0));
			if (!isValidDirection())
				direction.reset(0, 0);
		}
    }
	
	/**	得到所有活着的对象的落点集合	*/
	protected ArrayList<Point> getLivePoints() {
		ArrayList<Point> livPoints = new ArrayList<>();
		for (Entity en: CoreWorld.entities.values()) {
			if (en.state == EntityState.LIVE)
				livPoints.add(en.position.copy());
		}
		return livPoints;
	}

	/**	查看即将前往的位置是否合法	*/
	private boolean isValidDirection() {
		// 首先保证不出界
		if (!position.mov(direction).inWorld())
			return false;
		// 然后保证不重叠
		for (Entity en: CoreWorld.entities.values()) {
			if (/*(en.state == EntityState.LIVE) && */(position.mov(direction).equals(en.position)))
				return false;
		}
		return true;
	}

}

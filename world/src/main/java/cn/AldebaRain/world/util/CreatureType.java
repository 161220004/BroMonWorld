package cn.AldebaRain.world.util;

import cn.AldebaRain.world.entities.*;
import javafx.scene.paint.Color;

/**	
 *	生物的最细类别划分. <br>
 *	只要存在不同即为不同的种类，具有不同的enum
 *	对应着不同的血量，攻击方式，攻击力，攻击范围等
 *
 *	@author AldebaRain
 */
public enum CreatureType { // 			  hp  atk  n  r  m
	Bro1 ("Bro1" , "老大"  , 'A', "bro1" , 180, 30, 2, 2, 3), // 近身攻击
	Bro2 ("Bro2" , "老二"  , 'B', "bro2" ,  80, 10, 4, 7, 1), // 射线攻击
	Bro3 ("Bro3" , "老三"  , 'C', "bro3" , 150, 40, 1, 1, 5), // 近身攻击
	Bro4 ("Bro4" , "老四"  , 'D', "bro4" , 120, 30, 2, 6, 1), // 能量球攻击
	Bro5 ("Bro5" , "老五"  , 'E', "bro5" , 120, 15, 3, 3, 2), // 范围攻击
	Bro6 ("Bro6" , "老六"  , 'F', "bro6" , 150, 60, 1, 2, 7), // 近身攻击
	Bro7 ("Bro7" , "老七"  , 'G', "bro7" , 120, 40, 1, 3, 2), // 射线攻击
	Eld  ("Eld"  , "老爷爷", 'Y', "eld"  , 100, 10, 4, 2, 1), // 范围攻击
	Mons ("Mons" , "小喽啰", 'o', "mons" , 120, 15, 2, 2, 3), // 能量球攻击
	Scorp("Scorp", "蝎子精", '&', "scorp", 180, 50, 1, 4, 2), // 射线攻击
	Snk  ("Snk"  , "蛇精"  , 'S', "snk"  , 150, 35, 4, 4, 2); // 范围攻击
	
	public String label; // xml标签
	public String detail; // 细致类别名
	public char symbol; // 对应控制台输出符号
	public String imgName; // 对应图片名
	public int totalBlood; // 总血量
	public int attackPower; // 攻击力
	public int attackNum; // 攻击目标数量
	public int attackDistance; // 攻击距离
	public int movDistance; // 最大移动距离
	
	/**	enum 的构造函数
	 *	@param rou 粗略类别名
	 *	@param det 细致类别名
	 *	@param nam 对应图片名 */
	private CreatureType(String lab, String det, char sym, String nam, int tbl, int atk, int an, int adis, int mdis) {
		this.label = lab;
		this.detail = det;
		this.symbol = sym;
		this.imgName = nam;
		this.totalBlood = tbl;
		this.attackPower = atk;
		this.attackNum = an;
		this.attackDistance = adis;
		this.movDistance = mdis;
	}
	
	/**	@return 对应的阵营 */
	public GroupType group() {
		switch (this) {
		case Mons: case Scorp: case Snk: return GroupType.Mon;
		default: return GroupType.Bro;
		}
	}
	
	/**	@return 攻击类型	*/
	public AttackType attackType() {
		switch (this) {
		case Bro1: case Bro3: case Bro6: return AttackType.Knock;
		case Bro2: case Scorp: case Bro7: return AttackType.Radiate;
		case Bro4: case Mons: return AttackType.Fire;
		default: return AttackType.Aoe;
		}
	}
	
	/**	@return 攻击颜色	*/
	public Color attackColor() {
		switch(this) {
		case Bro1: return Color.RED;
		case Bro2: return Color.WHEAT;
		case Bro3: return Color.GOLD;
		case Bro4: return Color.ORANGERED;
		case Bro5: return Color.CORNFLOWERBLUE;
		case Bro6: return Color.AQUAMARINE;
		case Bro7: return Color.PLUM;
		case Eld: return Color.LIGHTGREEN;
		case Snk: return Color.LIGHTGRAY;
		case Scorp: return Color.PURPLE;
		default: return Color.DEEPPINK;
		}
	}

	/**	@param ctype 生物种类
	 *	@param bia 该生物在阵型内的偏移
	 *	@param p 该生物在世界地图的位置
	 *	@return 实体对象 */
	public Entity getEntity(Point bia, Point p) {
		switch (this) {
		case Bro1: return new EntityBro1(bia, p);
		case Bro2: return new EntityBro2(bia, p);
		case Bro3: return new EntityBro3(bia, p);
		case Bro4: return new EntityBro4(bia, p);
		case Bro5: return new EntityBro5(bia, p);
		case Bro6: return new EntityBro6(bia, p);
		case Bro7: return new EntityBro7(bia, p);
		case Eld: return new EntityEld(bia, p);
		case Scorp: return new EntityScorp(bia, p);
		case Snk: return new EntitySnk(bia, p);
		default: return new EntityMons(bia, p);
		}
	}
}

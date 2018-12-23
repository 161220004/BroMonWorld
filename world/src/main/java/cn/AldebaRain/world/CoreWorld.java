package cn.AldebaRain.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.AldebaRain.world.entities.*;
import cn.AldebaRain.world.formations.*;
import cn.AldebaRain.world.util.*;
import cn.AldebaRain.world.xml.*;

/**
 *	核心世界，世界的基础. <br>
 *	用于终端显示整个世界
 *
 *	@author AldebaRain
 *
 *	@see #broForm
 *	@see #monForm
 *	@see #entities
 *	@see #initGlobal(boolean)
 *	@see #initEntities()
 *	@see #initFormation(FormationType, GroupType)
 *	@see #showWorld()
 *	@see GUIWindow
 */
public final class CoreWorld {

	/**	定义葫芦娃阵型对象，用于对实体的初始化	*/
	public static Formation broForm;
	/**	定义妖怪阵型对象，用于对实体的初始化	*/
	public static Formation monForm;

	/**	全部实体	*/
	public static Map<Integer, Entity> entities = new HashMap<>();

	/**	构造函数，初始化所有对象及其位置	*/
	public CoreWorld() {
		initAll();
	}
	
	/**	初始化全局变量	*/
	public void initGlobal(boolean playingBack) {
		Global.keepFormationRound = Global.keepFormationRoundNum;
		Global.roundNum = 0;
		// battleStart/battleEnd的变化历程为：
		// [准备]：battleStart = false, battleEnd = false
		// [进行中]：battleStart = true, battleEnd = false
		// [结束]：battleStart = true, battleEnd = true
		Global.battleStart = false;
		Global.battleEnd = false;
		Global.battlePlayingBack = playingBack;
		if (!Global.battlePlayingBack)
			Global.recordWriter = new XMLRecordWriter();
	}
	
	/**	根据当前阵型重新写入初始化的实体	*/
	private void initEntities() {
		// 先移除以前阵型的全部实体
		Iterator<Integer> it = entities.keySet().iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        // 加入新的实体对象
		for (Point bia : broForm.formMap.keySet()) { // 加入全部葫芦娃
			Point pos = Global.leftCenterP.mov(broForm.getFormCen().reverse()).mov(bia);
			Entity en = broForm.formMap.get(bia).getEntity(bia, pos);
			entities.put(en.id, en);
		}
		for (Point bia : monForm.formMap.keySet()) { // 加入全部妖怪
			Point pos = Global.rightCenterP.mov(monForm.getFormCen().reverse()).mov(bia);
			Entity en = monForm.formMap.get(bia).getEntity(bia, pos);
			entities.put(en.id, en);
		}
		// 临时存储
		if (!Global.battlePlayingBack)
			Global.recordWriter.addEntityElements();
	}
	
	/**	初始化	*/
	public void initAll() {
		broForm = new ChangShe(GroupType.Bro); // 葫芦娃阵型，初始化为长蛇阵
		monForm = new HeYi(GroupType.Mon); // 妖怪阵型，初始化为鹤翼阵
		// 根据阵型生成实体集
		initGlobal(false);
		initEntities();
		System.out.println("初始化中 · · · · · · 完成");
		System.out.println("准备战斗");
	}
	
	/**	改变阵型的同时重置，位置初始化为默认位置	*/
	public void initFormation(FormationType ftype, GroupType gtype) {
		if (gtype == GroupType.Bro) 
			broForm = ftype.getFormation(GroupType.Bro);
		else 
			monForm = ftype.getFormation(GroupType.Mon);
		initGlobal(false);
		initEntities();
		System.out.println("布阵中 · · · · · · " + gtype.label + "阵型：" + ftype.name);
		System.out.println("准备战斗");
	}
	
	/**	显示核心世界	*/
	public void showWorld() {
		 // 初始化显示的地图
		char[][] cMap = new char[Global.rowNum][Global.colNum];
		for (int i = 0; i < Global.rowNum; i++) { 
			for (int j = 0; j < Global.colNum; j++)
				cMap[i][j] = ' '; 
		}
		// 放置所有实体
		for (Entity en: entities.values()) {
			if (en.state == EntityState.LIVE) {
				cMap[en.position.row()][en.position.col()] = en.creatureType.symbol;
			}
		}
		// 开始打印地图
		char boundary = '#';
		// 第一行，地图边界
		System.out.print("    ");
		for (int j = 0; j < Global.colNum; j++)
			System.out.print((j % 10) + " ");
		System.out.println();
		System.out.print("  ");
		for (int j = 0; j < Global.colNum + 2; j++)
			System.out.print(boundary + " ");
		System.out.println();
		// 地图主体
		for (int i = 0; i < Global.rowNum; i++) {
			System.out.print((i % 10) + " " + boundary);
			for (int j = 0; j < Global.colNum; j++)
				System.out.print(" " + cMap[i][j]);
			System.out.println(" " + boundary);
		}
		// 最后一行，地图边界
		System.out.print("  ");
		for (int j = 0; j < Global.colNum + 2; j++)
			System.out.print(boundary + " ");
		System.out.println();
	}
}

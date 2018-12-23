package cn.AldebaRain.world.entities;

/**	一个实体的移动行为和攻击行为	*/
public interface EntityAction {

	/**	设置direction并检查战斗是否结束
	 *	获得移动方向上的位移向量，上下左右或四角
	 *	即(0,1), (1,0), (0,-1), (-1,0), (1,1), (-1,-1), (1,-1), (-1,1)
	 *	再乘以x和y方向位移	*/
	public void trymove();
	
	/**	攻击的副作用，默认没有，只有部分实体有	*/
	public default void startAttackSideEffect() {
		return;
	}
	
	/**	攻击并刷新所有实体对象血量	*/
	public void attack();

}

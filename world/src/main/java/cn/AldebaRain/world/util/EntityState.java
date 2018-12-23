package cn.AldebaRain.world.util;

/**
 *	实体状态
 *
 *	@author AldebaRain
 */
public enum EntityState {
	LIVE("LIVE"), // 活着
	DEAD("DEAD"); // 死了
	
	public String label; // xml标签
	private EntityState(String lab) { // 构造方法
		this.label = lab;
	}
}

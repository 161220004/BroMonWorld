package cn.AldebaRain.world.xml;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.entities.Entity;
import cn.AldebaRain.world.util.*;
import cn.AldebaRain.world.xml.XMLRecordReader;

public class XMLRecordReaderTest {

	private CoreWorld cWrd;
	
	private XMLRecordReader recordReader;
	
	private Map<Integer, Entity> expectedEntities;
	
	public XMLRecordReaderTest() {
		System.out.println("# 开始构造测试类 XMLRecordReaderTest");
		cWrd = new CoreWorld();
		// 测试文件的初始形态是 CS + HY
		cWrd.initFormation(FormationType.CS, GroupType.Bro);
		cWrd.initFormation(FormationType.HY, GroupType.Mon);
		// 正确的entities集合应该如下
		expectedEntities = new HashMap<Integer, Entity>();
		for (Point bia : CoreWorld.broForm.formMap.keySet()) { // 加入全部葫芦娃
			Point pos = Global.leftCenterP.mov(CoreWorld.broForm.getFormCen().reverse()).mov(bia);
			Entity en = CoreWorld.broForm.formMap.get(bia).getEntity(bia, pos);
			expectedEntities.put(en.id, en);
		}
		for (Point bia : CoreWorld.monForm.formMap.keySet()) { // 加入全部妖怪
			Point pos = Global.rightCenterP.mov(CoreWorld.monForm.getFormCen().reverse()).mov(bia);
			Entity en = CoreWorld.monForm.formMap.get(bia).getEntity(bia, pos);
			expectedEntities.put(en.id, en);
		}
		// 以下两个测试都需要这一步初始化
		// 此时的CoreWorld.entities已经是从文件读取的结果了
		recordReader = new XMLRecordReader(Global.defaultTestFile);
		System.out.println("# 测试类构造成功");
	}
	
	@Test
	public void testXMLRecordReader() {
		System.out.println("# 测试方法 XMLRecordReader");
		// 在这里测试XMLRecordReader(String)调用的(private)方法 initEntitiesByElement()
		// 即测试从文件读取的CoreWorld.entities与expectedEntities是否相等
		Iterator<Integer> it = CoreWorld.entities.keySet().iterator();
        while (it.hasNext()) {
            Integer actualId = it.next();
            Entity actualEntity = CoreWorld.entities.get(actualId);
            Entity expectedEntity = expectedEntities.get(actualId);
            // expectedEntity != null
            assertNotNull(expectedEntity); 
            // expectedEntity equals actualEntity
            assertTrue(expectedEntity.id == actualEntity.id);
            assertTrue(expectedEntity.blood == actualEntity.blood);
            assertTrue(expectedEntity.state == actualEntity.state);
            assertTrue(expectedEntity.creatureType == actualEntity.creatureType);
            assertTrue(expectedEntity.position.equals(actualEntity.position));
            assertTrue(expectedEntity.direction.equals(actualEntity.direction));
        }
		System.out.println("# 通过测试");
	}
	
	/**	将战斗复盘到 Round 8	*/
	private void setExpectedEntitiesToRound8() {
		expectedEntities.get(1090).resetEntity(EntityState.DEAD, 0, 11, 10, 0, 0);
		expectedEntities.get(1090).targetSet.removeAll(expectedEntities.get(1090).targetSet);
		
		expectedEntities.get(2054).resetEntity(EntityState.LIVE, 170, 8, 13, -1, 0);
		expectedEntities.get(2054).targetSet.removeAll(expectedEntities.get(2054).targetSet);
		expectedEntities.get(2054).targetSet.add(expectedEntities.get(1090));
		
		expectedEntities.get(2055).resetEntity(EntityState.LIVE, 200, 9, 13, 2, -2);
		expectedEntities.get(2055).targetSet.removeAll(expectedEntities.get(2055).targetSet);
		expectedEntities.get(2055).targetSet.add(expectedEntities.get(2055));
		
		expectedEntities.get(1036).resetEntity(EntityState.DEAD, 0, 6, 12, 0, 0);
		expectedEntities.get(1036).targetSet.removeAll(expectedEntities.get(1036).targetSet);
		
		expectedEntities.get(2003).resetEntity(EntityState.DEAD, 0, 8, 10, 0, 0);
		expectedEntities.get(2003).targetSet.removeAll(expectedEntities.get(2003).targetSet);
		
		expectedEntities.get(1108).resetEntity(EntityState.LIVE, 85, 8, 7, 0, 0);
		expectedEntities.get(1108).targetSet.removeAll(expectedEntities.get(1108).targetSet);
		
		expectedEntities.get(2073).resetEntity(EntityState.DEAD, 0, 6, 10, 0, 0);
		expectedEntities.get(2073).targetSet.removeAll(expectedEntities.get(2073).targetSet);
		
		expectedEntities.get(1054).resetEntity(EntityState.LIVE, 120, 8, 5, 0, 0);
		expectedEntities.get(1054).targetSet.removeAll(expectedEntities.get(1054).targetSet);
		
		expectedEntities.get(2020).resetEntity(EntityState.LIVE, 90, 9, 11, 3, -2);
		expectedEntities.get(2020).targetSet.removeAll(expectedEntities.get(2020).targetSet);
		
		expectedEntities.get(1126).resetEntity(EntityState.LIVE, 70, 10, 7, 0, 0);
		expectedEntities.get(1126).targetSet.removeAll(expectedEntities.get(1126).targetSet);
		
		expectedEntities.get(1000).resetEntity(EntityState.LIVE, 75, 7, 9, 0, 0);
		expectedEntities.get(1000).targetSet.removeAll(expectedEntities.get(1000).targetSet);
		
		expectedEntities.get(2092).resetEntity(EntityState.DEAD, 0, 9, 9, 0, 0);
		expectedEntities.get(2092).targetSet.removeAll(expectedEntities.get(2092).targetSet);
		
		expectedEntities.get(1072).resetEntity(EntityState.LIVE, 120, 7, 6, 0, 0);
		expectedEntities.get(1072).targetSet.removeAll(expectedEntities.get(1072).targetSet);
		
		expectedEntities.get(2037).resetEntity(EntityState.LIVE, 45, 12, 8, 0, -1);
		expectedEntities.get(2037).targetSet.removeAll(expectedEntities.get(2037).targetSet);
		expectedEntities.get(2037).targetSet.add(expectedEntities.get(1126));
		
		expectedEntities.get(1018).resetEntity(EntityState.LIVE, 80, 4, 4, 0, 0);
		expectedEntities.get(1018).targetSet.removeAll(expectedEntities.get(1018).targetSet);
		
		expectedEntities.get(2111).resetEntity(EntityState.LIVE, 55, 9, 8, 1, -1);
		expectedEntities.get(2111).targetSet.removeAll(expectedEntities.get(2111).targetSet);
		expectedEntities.get(2111).targetSet.add(expectedEntities.get(1108));
		expectedEntities.get(2111).targetSet.add(expectedEntities.get(1126));
	}

	@Test
	public void testSetEntityByRound() {
		System.out.println("# 测试方法 SetEntityByRound");
		// 在这里测试读取 Round 4 的正确性
		setExpectedEntitiesToRound8(); // 计算正确值
		// 期望值
		Iterator<Integer> it = CoreWorld.entities.keySet().iterator();
        while (it.hasNext()) {
            Integer actualId = it.next();
            Entity actualEntity = CoreWorld.entities.get(actualId);
            recordReader.setEntityByRound(actualEntity, 8);
            // 现在CoreWorld.entities已经是从文件读取的结果了
            Entity expectedEntity = expectedEntities.get(actualId);
            assertNotNull(expectedEntity); 
            // expectedEntity equals actualEntity
            assertTrue(expectedEntity.id == actualEntity.id);
            assertTrue(expectedEntity.blood == actualEntity.blood);
            assertTrue(expectedEntity.state == actualEntity.state);
            assertTrue(expectedEntity.creatureType == actualEntity.creatureType);
            assertTrue(expectedEntity.position.equals(actualEntity.position));
            assertTrue(expectedEntity.direction.equals(actualEntity.direction));
            assertTrue(expectedEntity.targetSet.size() == actualEntity.targetSet.size());
        }
		System.out.println("# 通过测试");
	}

}

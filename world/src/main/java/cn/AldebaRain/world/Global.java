package cn.AldebaRain.world;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;

import cn.AldebaRain.world.entities.Entity;
import cn.AldebaRain.world.util.*;
import cn.AldebaRain.world.xml.*;

/**	
 *	全局常量变量和全局函数.
 *	包括：定值，特殊位置，当前状态，文件存取，是否播放动画等
 *	
 *	@author AldebaRain
 */
public class Global {

	/**	行数 {@value}, 0 ~ {@value}-1	*/
	public static final int rowNum = 17; // 0 ~ 16
	
	/**	列数 {@value}, 0 ~ {@value}-1	*/
	public static final int colNum = 18; // 0 ~ 17

	/**	网格宽度 {@value}	*/
	public static final double span = 50;

	/**	背景图片宽 {@value}	*/
	public static final double groundWidth = colNum * span; // 900
	
	/**	背景图片高  {@value}	*/
	public static final double groundHeight = rowNum * span; // 850

	/**	窗口位置 {@value}	*/
	public static final double winLocationXY = 0;
	
	/**	窗口宽 {@value}	*/
	public static final double winWidth = 906;
	
	/**	窗口高 {@value}	*/
	public static final double winHeight = 917;

	/**	实体图片宽 {@value}	*/
	public static final double entityWidth = 100;
	
	/**	实体图片高 {@value}	*/
	public static final double entityHeight = 100;
	
	/**	说明标签中每个字的宽 {@value}	*/
	public static final double labelFontWidth = 24;

	/**	说明标签宽 {@value}	*/
	public static final double labelWidth(int n) { return (labelFontWidth * n); }
	
	/**	说明标签高 {@value}	*/
	public static final double labelHeight = 48;
	
	/**	血量形状宽	*/
	public static final double bloodWidth = 40;
	
	/**	血量形状高	*/
	public static final double bloodHeight = 4;
	
	/**	GridPane的偏移x {@value}	*/
	public static final double marginX = -span / 2; // -25
	
	/**	GridPane的偏移y {@value}	*/
	public static final double marginY = (span - groundHeight) / 2; // -400

	/**	形状的偏移x	*/
	public static final double marginShapeX(double width) { return (span - width) / 2; }
	
	/**	形状的偏移y	*/
	public static final double marginShapeY(double height) { return (marginY + (span - height) / 2); }
	
	/**	Label的偏移x {@value}	*/
	public static final double marginLabelX(int n) { return (labelWidth(n) - groundWidth) / 2; }
	
	/**	Label的偏移y {@value}	*/
	public static final double marginLabelY = (labelHeight - groundHeight) / 2;
	
	/**	左侧战场中心点	*/
	public static final Point leftCenterP = new Point(7, 4);

	/**	右侧战场中心点	*/
	public static final Point rightCenterP = new Point(7, 13);	

	/**	双方保持原阵型的半回合数	*/
	public static final int keepFormationRoundNum = 2;
	
	/**	双方保持原阵型的半回合数	*/
	public static int keepFormationRound = keepFormationRoundNum;

	/**	相遇时葫芦娃的胜率	*/
	public static final int BroWinPercent = 50;
	
	/**	相遇时妖怪的胜率	*/
	public static final int MonWinPercent = 100 - BroWinPercent;

	/**	是否播放动画	*/
	public static boolean hasAnimation = true;
	
	/**	默认攻击动画持续时间(ms)	*/
	public static final int atkDurTime = 1000;
	/**	默认移动动画持续时间(ms)	*/
	public static final int movDurTime = 500;

	/**	战斗回合数	*/
	public static int roundNum = 0;

	/**	判断是否是本阵营的行动回合	*/
	public static boolean isActionRound(GroupType type) {
		if (roundNum % 2 == 1)
			return (type == GroupType.Bro);
		else return (type == GroupType.Mon);
	}
	
	/**	战斗是否开始	*/
	public static boolean battleStart = false;
	
	/**	战斗是否结束	*/
	public static boolean battleEnd = false;

	/**	战斗胜利方	*/
	public static GroupType battleWin;
	
	/**	判断战斗是否结束	*/
	public static boolean isEnded() {
		boolean hasBro = false;
		boolean hasMon = false;
		for (Entity en: CoreWorld.entities.values()) {
			if ((en.state == EntityState.LIVE) && (en.creatureType.group() == GroupType.Bro))
				hasBro = true;
			if ((en.state == EntityState.LIVE) && (en.creatureType.group() == GroupType.Mon))
				hasMon = true;
		}
		if (hasBro && hasMon)
			return false;
		else {
			if (hasBro) battleWin = GroupType.Bro;
			else battleWin = GroupType.Mon;
			return true;
		}
	}
	
	/**	是否正在回放战斗过程	*/
	public static boolean battlePlayingBack = false;
	
	/**	保存战斗	*/
	public static XMLRecordWriter recordWriter = null;
	
	/**	读取战斗	*/
	public static XMLRecordReader recordReader = null;
	
	/**	辅助获取默认文件位置	*/
	public static final File getFile(String root, String alt) {
		File myFile = new File(root);
		if (!myFile.exists()) { // 说明是maven路径
			String myPath = (new File("")).getAbsolutePath();
			myPath = myPath.substring(0, myPath.length() - 6);
			System.out.println(myPath);
			myFile = new File(myPath + root);
		}
		if (!myFile.exists()) { // 说明jar被换地方了
			myFile = new File(alt);
			myFile.mkdirs();
		}
		return myFile;
	}

	/**	默认保存文件	*/
	public static final File defaultFile = getFile("src/main/resources/records/defaultRecordFile.xml", "tmp");

	/**	测试用文件（不可更改）	*/
	public static final File defaultTestFile = getFile("src/main/resources/records/JUnitTestRecordFile.xml", "tmp");
	
	/**	默认文件选择器初始路径	*/
	public static final File defaultPath = getFile("myRecords", "myRecords");

	/**	战斗结束后（非回放模式）	*/
	public static boolean isAfterBattleNotPlayback() {
		return (!battlePlayingBack && battleStart && battleEnd);
	}
	
	/**	战斗开始前或战斗结束后或回放时	*/
	public static boolean isBeforeOrAfterOrPlayback() {
		if (battlePlayingBack)
			return true;
		if ((!battleStart && !battleEnd) || (battleStart && battleEnd))
			return true;
		return false;
	}

    /**	弹出提示框
     *	@param head 提示框标题内容
     *	@param content 详细说明	*/
	public static void showAlertDialog(String head, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("提示");
		alert.initOwner(Main.worldStage);
		alert.setHeaderText(head);
		alert.setContentText(content);
		alert.showAndWait();
	}

}

package cn.AldebaRain.world.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import cn.AldebaRain.world.*;
import cn.AldebaRain.world.entities.Entity;
import cn.AldebaRain.world.util.*;
import cn.AldebaRain.world.xml.XMLRecordReader;

/**
 *	GUI 相关属性和行为（如键盘监听和动画显示），与WorldWindow.fxml相连.<br>
 *	GUI分为5层：
 *	（1）最底层只有一个Label，用来显示背景图片
 *	（2）次底层是战斗动画的显示层，绘制各种攻击图形和动画
 *	（3）中间层是人物的显示层，用来绘制人物Label以及显示移动动画
 *	（4）次顶层是血量条的显示层，在每个人物上方绘制血量条并伴随人物移动
 *	（5）最顶层是说明标签层，用来显示当前状态（战斗/回放、哪一方胜利）以及接受键盘输入
 *
 *	@see #animLayer
 *	@see #bloodLayer
 *	@see #attackLayer
 *
 *	@see #animItems
 *	@see #bloodBarItems
 *	@see #bloodFillItems
 *
 *	@see #initLabels(boolean)
 *	@see #initAnimItems()
 *	@see #initBloodItems()
 *	@see #initialize()
 *
 *	@see #setAttackAnimation()
 *	@see #setMoveAnimation()
 *	@see #setFadeAnimation()
 *
 *	@see #setBlood()
 *	@see #setMove()
 *	@see #setInvisible()
 *
 *	@see #pressKeySpace()
 *	@see #pressKeyL()
 *	@see #pressKeyS()
 *	@see #pressKeyR()
 *	@see #pressKeyP()
 *
 *	@see #start()
 *
 *	@author AldebaRain
 */
public class GUIWindow {

	/**	CoreWorld指针	*/
	public CoreWorld cWrd = null;
	
	@FXML
	private Label background; // 背景图片标签
	
	@FXML
	public GridPane attackLayer; // 战斗动画层指针
	
	@FXML
	public GridPane animLayer; // 动画层指针
	
	@FXML
	public GridPane bloodLayer; // 血量层指针
	
	@FXML
	private Label modeLabel; // 模式：[战斗]或[回放]；状态：[准备]或[进行中]或[结束]

	@FXML
	private Label groupLabel; // 行动方：[葫芦娃]或[妖怪]
	
	@FXML
	private Label roundLabel; // 显示回合数
	
	@FXML
	private Label observer; // 用于打印战斗结果
	
	@FXML
	private TextField receiver; // 用于设置键盘监听
	
	/**	动画Label集	*/
	public static Map<Integer, Label> animItems = new HashMap<>();

	/**	血量框Shape集	*/
	public static Map<Integer, Shape> bloodBarItems = new HashMap<>();
	/**	血量填充Shape集	*/
	public static Map<Integer, Shape> bloodFillItems = new HashMap<>();

	/**	线程倒计时器	*/
	public static CountDownLatch threadCountDown;
	
	/**	动画时间轴	*/
	private Timeline timeline;
	
	/**	初始化CoreWorld	*/
    public void setWin(CoreWorld cw) {
    	cWrd = cw;
    }

	/**	初始化模式状态/回合数标签	*/
    public void initLabels(boolean onlyText) {
    	if (Global.battlePlayingBack)
    		modeLabel.setText("回放准备");
    	else modeLabel.setText("战斗准备");
		roundLabel.setText("回合数：0");
		groupLabel.setText("行动方：---");
		observer.setVisible(false);
    	if (onlyText) return;
		modeLabel.setPrefWidth(Global.labelWidth(6));
		modeLabel.setPrefHeight(Global.labelHeight);
		modeLabel.setTranslateX(Global.marginLabelX(6));
		modeLabel.setTranslateY(Global.marginLabelY);
		modeLabel.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.5), null, null)));
		roundLabel.setPrefWidth(Global.labelWidth(6));
		roundLabel.setPrefHeight(Global.labelHeight);
		roundLabel.setTranslateX(-Global.marginLabelX(6) - Global.labelWidth(8));
		roundLabel.setTranslateY(Global.marginLabelY);
		roundLabel.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.5), null, null)));
		groupLabel.setPrefWidth(Global.labelWidth(8));
		groupLabel.setPrefHeight(Global.labelHeight);
		groupLabel.setTranslateX(-Global.marginLabelX(8));
		groupLabel.setTranslateY(Global.marginLabelY);
		groupLabel.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.5), null, null)));
		observer.setPrefHeight(2 * Global.labelHeight);
		observer.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.5), null, null)));
	}

    private double getAnimItemX(int c) {
    	return (Global.marginX + c * Global.span);
    }
    private double getAnimItemY(int r) {
    	return (Global.marginY + r * Global.span);
    }
    private double getShapeItemX(int c, double width) {
    	return (Global.marginShapeX(width) + c * Global.span);
    }
    private double getShapeItemY(int r, double height) {
    	return (Global.marginShapeY(height) + r * Global.span);
    }
    
	/**	根据entities重新写入初始化的animItems	*/
	public void initAnimItems() {
		// 先移除以前的全部Label
		Iterator<Integer> it = animItems.keySet().iterator();
        while (it.hasNext()) {
            animLayer.getChildren().remove(animItems.get(it.next()));
            it.remove();
        }
		// 根据entities重新写入
		for (Entity en: CoreWorld.entities.values()) {
			Label tmpLab = new Label();
			setEntityImage(tmpLab, en.creatureType, EntityState.LIVE); // 添加图片
			tmpLab.setScaleX(Global.span / Global.entityWidth); // 标签整体缩放
			tmpLab.setScaleY(Global.span / Global.entityHeight);
			tmpLab.setTranslateX(getAnimItemX(en.position.col())); // 标签位置
			tmpLab.setTranslateY(getAnimItemY(en.position.row()));
			animLayer.getChildren().add(tmpLab); // 放置到动画层
			animItems.put(en.id, tmpLab);
		}
	}

	/**	得到一个指定位置的空血槽	*/
	private Shape getBloodBar(int r, int c) {
		Shape spaceRect = new Rectangle(Global.bloodWidth, Global.bloodHeight);
		spaceRect.setFill(Color.WHITE); // 白色
		spaceRect.setStrokeType(StrokeType.OUTSIDE); // 边框在形状外
		spaceRect.setStroke(Color.WHITE); // 边框白色
		spaceRect.setTranslateX(getShapeItemX(c, Global.bloodWidth)); // 形状位移
		spaceRect.setTranslateY(getShapeItemY(r, Global.bloodHeight));
		return spaceRect;
	}
	
	/**	得到一个指定位置的满的血量填充形状	*/
	private Shape getBloodFill(int r, int c) {
		Shape fillRect = new Rectangle(Global.bloodWidth, Global.bloodHeight);
		fillRect.setFill(Color.LIGHTGREEN); // 淡绿
		fillRect.setStrokeWidth(0); // 无边框
		fillRect.setTranslateX(getShapeItemX(c, Global.bloodWidth) + 1); // 形状位移
		fillRect.setTranslateY(getShapeItemY(r, Global.bloodHeight));
		return fillRect;
	}	
	
	/**	根据entities重新写入初始化的bloodItems	*/
	public void initBloodItems() {
		// 先移除以前的全部Shape
		Iterator<Integer> it = bloodBarItems.keySet().iterator();
        while (it.hasNext()) {
            bloodLayer.getChildren().remove(bloodBarItems.get(it.next()));
            it.remove();
        }
		it = bloodFillItems.keySet().iterator();
        while (it.hasNext()) {
            bloodLayer.getChildren().remove(bloodFillItems.get(it.next()));
            it.remove();
        }
		// 根据entities重新写入
		for (Entity en: CoreWorld.entities.values()) {
			Shape tmpBar = getBloodBar(en.position.row(), en.position.col());
			Shape tmpFill = getBloodFill(en.position.row(), en.position.col());
			bloodLayer.getChildren().add(tmpBar); // 放置到血量层
			bloodLayer.getChildren().add(tmpFill); 
			bloodBarItems.put(en.id, tmpBar);
			bloodFillItems.put(en.id, tmpFill);
		}
	}
	/**	初始化GUIWindow.
     *	在加载WorldWindow.fxml完成后自动调用	*/
    @FXML
    private void initialize() {
    	// 初始化背景
    	Image backImg = new Image(getClass().getClassLoader().getResourceAsStream("img/BackGround.png"));
    	background.setGraphic(new ImageView(backImg));
    	// 初始化说明标签
    	initLabels(false);
    	// 初始化animItems（动画标签）
		initAnimItems();
		initBloodItems();
		animLayer.setVisible(true);
		Global.hasAnimation = true; // 有动画
		// 设置键盘事件
		receiver.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE && cWrd != null) {
                	pressKeySpace();
                }
                if (event.getCode() == KeyCode.L && cWrd != null) {
                	pressKeyL();
                }
                if (event.getCode() == KeyCode.S && cWrd != null) {
                	pressKeyS();
                }
                if (event.getCode() == KeyCode.R && cWrd != null) {
                	pressKeyR();
                }
                if (event.getCode() == KeyCode.P && cWrd != null) {
                	pressKeyP();
                }
            }
        });
    }

    /**	每单击键盘R调用	*/
    public void pressKeyR() {
		System.out.println("重置");
		cWrd.initAll();
		initLabels(true);
		initAnimItems();
		initBloodItems();
		cWrd.showWorld();
    }

    /**	每单击键盘P调用	*/
    private void pressKeyP() {
    	if (Global.isAfterBattleNotPlayback()) // 战斗结束时（非回放模式）
    		loadRecord(true);
    	else {
    		String head = "本局回放失败";
    		String content = "请在[战斗]模式下，本局[结束]后键入“P”以回放本局";
    		Global.showAlertDialog(head, content);
    	}
    }

    /**	每单击键盘L调用	*/
    private void pressKeyL() {
    	if (Global.isBeforeOrAfterOrPlayback()) // 回放中或战斗开始前或战斗结束时
    		loadRecord(false);
    	else {
    		String head = "读取记录失败";
    		String content = "请在：(1)[回放]模式下，或 (2)[战斗]模式下的[准备]时和[结束]后，键入“L”以读取记录";
    		Global.showAlertDialog(head, content);
    	}
    }

    /**	加载数据	*/
    public void loadRecord(boolean isDefault) {
    	if (isDefault)
    		Global.recordReader = new XMLRecordReader(Global.defaultFile); // 读取默认文件
    	else {
        	File xmlFile = Main.showOpenXMLChooser();
        	if (xmlFile == null) {
        		System.out.println("取消读取对战记录");
        		return;
        	}
    		Global.recordReader = new XMLRecordReader(xmlFile); // 读取文件
    	}
		// 初始化窗口，准备回放
		cWrd.initGlobal(true); // 初始化全局变量
		initLabels(true);
		initAnimItems(); // 初始化动画
		initBloodItems();
		cWrd.showWorld();
    }
    
    /**	每单击键盘S调用	*/
    public void pressKeyS() {
    	if (Global.isAfterBattleNotPlayback()) { // 战斗结束时（非回放模式）
        	File xmlFile = Main.showSaveXMLChooser();
        	if (xmlFile == null) {
        		System.out.println("取消保存对战记录");
        		return;
        	}
    		Global.recordWriter.saveRecord(xmlFile); // 保存xml战斗记录
    	}
    	else {
    		String head = "保存记录失败";
    		String content = "请在[战斗]模式下，本局[结束]后键入“S”以保存记录";
    		Global.showAlertDialog(head, content);
    	}
    }
    
    /**	设置回合数等描述并输出到控制台以及显示到标签	*/
    private void showRoundDescription() {
		if (Global.battlePlayingBack) {
			System.out.print("[回放中] ");
			modeLabel.setText("回放中...");
		}
		else {
			System.out.print("[战斗中] ");
			modeLabel.setText("战斗中...");
		}
		System.out.print("第" + (Global.roundNum + 1) / 2 + "回合：[");
		roundLabel.setText("回合数：" + (Global.roundNum + 1) / 2);
		if (Global.roundNum % 2 == 1) {
			System.out.print("葫芦娃");
	    	groupLabel.setText("行动方：葫芦娃");
		}
		else {
			System.out.print("妖怪");
	    	groupLabel.setText("行动方：妖怪");
		}
		System.out.println("]行动回合");
    }
    
    /**	每单击键盘空格调用	*/
	private void pressKeySpace() {
		Global.battleStart = true;
		if (!Global.battleEnd) { // 战斗未结束
	    	// 回合数加1（从“第1回合”开始），显示描述信息
			Global.roundNum += 1; 
			showRoundDescription();
			// 新建动画时间轴
			timeline = new Timeline();
	        timeline.setCycleCount(1);
	        // 子线程个数
	        int threadNum = CoreWorld.entities.size();
	        // 给子线程一个倒计时
	        threadCountDown = new CountDownLatch(threadNum);
			// 行动（一群子进程开启，或读文件不开启子进程）
	        start(); 
			// 只有所有子进程执行结束后才执行动画和打印操作
			try {
				// 等待所有子线程
				threadCountDown.await(5, TimeUnit.SECONDS); // 5秒超时
				if (Global.hasAnimation) {
					// 先控制输入，防止在动画期间键入空格
	    			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(0),
	    					new KeyValue(receiver.disableProperty(), true)
	    					));
	    			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime + Global.movDurTime),
	    					new KeyValue(receiver.disableProperty(), false)
	    					));
					// 设置动画
					setAttackAnimation();
					setMoveAnimation(); 
					setFadeAnimation();
					// 开始播放
			        timeline.play(); 
				}
				else {
					setBlood();
					setMove();
					setDead();
				}
		        // 开始打印
				cWrd.showWorld();
				Global.keepFormationRound -= 1;
				// 临时存储
				if (!Global.battlePlayingBack) // 回放模式不存储
					Global.recordWriter.addRoundElements(Global.roundNum); 
				Global.battleEnd = Global.isEnded();
				// 如果战斗/回放[结束]
				if (Global.battleEnd) {
					observer.setText("  胜利方：" + Global.battleWin.label + "  ");
					observer.setVisible(true);
					if (Global.battlePlayingBack) { // 回放模式不存储
						System.out.println("回放结束，键入“R”重置");
						modeLabel.setText("回放结束");
					}
					else { // 导出到默认文件
						System.out.println("战斗结束，键入“R”重置，键入“P”回放本局");
						modeLabel.setText("战斗结束");
						Global.recordWriter.saveRecord(Global.defaultFile);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**	战斗模式开启所有实体的线程或回放模式读取文件不开启线程
	 *	线程倒计时	*/
	public void start() {
        // 通过回放或线程改变所有实体对象的运动属性
		for (Entity en: CoreWorld.entities.values()) {
			if (Global.battlePlayingBack) { // 回放模式
				Global.recordReader.setEntityByRound(en, Global.roundNum);
				threadCountDown.countDown(); 
			}
			else if (Global.isActionRound(en.creatureType.group()))
				en.start(); // 开始移动
			else { // 静止
				en.direction.reset(0, 0); 
				en.targetSet.removeAll(en.targetSet);
				threadCountDown.countDown(); // 由于初始化的线程个数包括了非行动回合实体，因此需要减一下
			}
		}
	}
	
	/**	设置攻击动画	*/
	private void setAttackAnimation() {
		attackLayer.getChildren().removeAll(attackLayer.getChildren());
		for (Entity en: CoreWorld.entities.values()) {
	        if (en.state == EntityState.LIVE) {
	        	if (Global.isActionRound(en.creatureType.group())) { // 是行动回合
					// 设置自己位置和敌人位置的动画
        			Iterator<Entity> it = en.targetSet.iterator();
        			while (it.hasNext()) {
        				Entity target = it.next();
		        		int originCol = en.position.col() - en.direction.col();
		        		int originRow = en.position.row() - en.direction.row();
    	        		switch (en.creatureType.attackType()) {
    	        		case Knock:
	        				Shape selfRect = new Rectangle(Global.span, Global.span, en.creatureType.attackColor());
	        				selfRect.setStrokeWidth(0);
	        				selfRect.setTranslateX(getShapeItemX(originCol, Global.span));
	        				selfRect.setTranslateY(getShapeItemY(originRow, Global.span));
	        				selfRect.setRotate(90);
	        				selfRect.setOpacity(0);
	        				Shape knockRect = new Rectangle(Global.span, Global.span, en.creatureType.attackColor());
	        				knockRect.setStrokeWidth(0);
	        				knockRect.setTranslateX(getShapeItemX(target.position.col(), Global.span));
	        				knockRect.setTranslateY(getShapeItemY(target.position.row(), Global.span));
	        				knockRect.setRotate(90);
	        				knockRect.setOpacity(0);
	        				attackLayer.getChildren().add(selfRect);
	        				attackLayer.getChildren().add(knockRect);
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime / 4),
		        					new KeyValue(selfRect.opacityProperty(), 1), 
		        					new KeyValue(knockRect.opacityProperty(), 1)
		        					));
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime / 2), 
		        					new KeyValue(selfRect.opacityProperty(), 0), 
		        					new KeyValue(knockRect.opacityProperty(), 0)
		        					));
    	        			break;
    	        		case Radiate:
			        		Shape radShape = new Rectangle(1, 10, en.creatureType.attackColor());
			        		radShape.setStrokeWidth(0);
			        		radShape.setTranslateX(getShapeItemX(originCol, 1));
			        		radShape.setTranslateY(getShapeItemY(originRow, 10));
		        			double angle = 0; // 旋转角
		        			int dc = target.position.col() - originCol;
		        			int dr = target.position.row() - originRow;
		        			if (dc == 0) angle = 90;
		        			else if (dr == 0) angle = 0;
		        			else angle = Math.atan((double)dr / (double)dc) * 180.0 / 3.14;
		        			radShape.setRotate(angle);
	        				attackLayer.getChildren().add(radShape);
	        				double finalWidth = Global.span * Math.sqrt(dc * dc + dr * dr);
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(0),
		    						(ActionEvent t) -> { 
		    							TranslateTransition radTrans = new TranslateTransition(Duration.millis(Global.atkDurTime * 3 / 4), radShape);
		    							radTrans.setByX(Global.span * dc / 2);
		    							radTrans.setByY(Global.span * dr / 2);
		    							ScaleTransition radScale = new ScaleTransition(Duration.millis(Global.atkDurTime * 3 / 4), radShape);
		    							radScale.setToX(finalWidth);
		    							radTrans.play();
		    							radScale.play();
		    						}));
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime * 3 / 4),
		        					new KeyValue(radShape.opacityProperty(), 1)
		        					));
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime),
		        					new KeyValue(radShape.opacityProperty(), 0)
		        					));
    	        			break;
    	        		case Fire:
    	        			double radiusFire = 5; // 火球半径
    	        			Shape fireShape = new Circle(radiusFire, en.creatureType.attackColor());
    	        			fireShape.setStrokeWidth(1);
    	        			fireShape.setStroke(Color.WHITE);
    	        			fireShape.setTranslateX(getShapeItemX(originCol, radiusFire));
    	        			fireShape.setTranslateY(getShapeItemY(originRow, radiusFire));
	        				attackLayer.getChildren().add(fireShape);
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime - 1),
		        					new KeyValue(fireShape.translateXProperty(), getShapeItemX(target.position.col(), radiusFire)), 
		        					new KeyValue(fireShape.translateYProperty(), getShapeItemY(target.position.row(), radiusFire)), 
		        					new KeyValue(fireShape.opacityProperty(), 1)
		        					));
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime),
		        					new KeyValue(fireShape.opacityProperty(), 0)
		        					));
    	        			break;
    	        		default: // AOE
    	        			double radiusAOE = Global.span * en.creatureType.attackDistance + 30;
    	        			Shape aoeShape = new Circle(1, en.creatureType.attackColor());
    	        			aoeShape.setStrokeWidth(0);
    	        			aoeShape.setOpacity(0);
    	        			aoeShape.setTranslateX(getShapeItemX(originCol, 1));
    	        			aoeShape.setTranslateY(getShapeItemY(originRow, 1));
	        				attackLayer.getChildren().add(aoeShape);
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime / 2),
		        					new KeyValue(aoeShape.scaleXProperty(), radiusAOE), 
		        					new KeyValue(aoeShape.scaleYProperty(), radiusAOE), 
		        					new KeyValue(aoeShape.opacityProperty(), 0.3)
		        					));
		        			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime),
		        					new KeyValue(aoeShape.opacityProperty(), 0)
		        					));
    	        		}
    	        	}
	        	}
				// 设置血量动画：颜色变化+血量变化
	        	if (en.blood < en.creatureType.totalBlood / 3)
	        		bloodFillItems.get(en.id).setFill(Color.SALMON);
	        	else if (en.blood < en.creatureType.totalBlood * 2 / 3)
	        		bloodFillItems.get(en.id).setFill(Color.YELLOW);
	        	else bloodFillItems.get(en.id).setFill(Color.LIGHTGREEN);
	        	// 血量减小
				double bloodBia = (1 - (double)en.blood / (double)en.creatureType.totalBlood) * Global.bloodWidth / 2;
		        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime), 
		        		new KeyValue(bloodFillItems.get(en.id).scaleXProperty(), (double)en.blood / (double)en.creatureType.totalBlood), 
		        		new KeyValue(bloodFillItems.get(en.id).translateXProperty(), getShapeItemX(en.position.col() - en.direction.col(), Global.bloodWidth) + 1 - bloodBia)
		        		));
	        }
		}
	}

	/**	设置血量	*/
	private void setBlood() {
		attackLayer.getChildren().removeAll(attackLayer.getChildren());
		for (Entity en: CoreWorld.entities.values()) {
	        if (en.state == EntityState.LIVE) {
	        	if (en.blood < en.creatureType.totalBlood / 3)
	        		bloodFillItems.get(en.id).setFill(Color.SALMON);
	        	else if (en.blood < en.creatureType.totalBlood * 2 / 3)
	        		bloodFillItems.get(en.id).setFill(Color.YELLOW);
	        	else bloodFillItems.get(en.id).setFill(Color.LIGHTGREEN);
				bloodFillItems.get(en.id).setScaleX((double)en.blood / (double)en.creatureType.totalBlood);
	        }
        }
	}
	
	/**	设置移动动画	*/
	private void setMoveAnimation() {
		for (Entity en: CoreWorld.entities.values()) {
	        if (en.state == EntityState.LIVE) {
				double bloodBia = (1 - (double)en.blood / (double)en.creatureType.totalBlood) * Global.bloodWidth / 2;
				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime), 
						(ActionEvent t) -> { 
			                TranslateTransition animTrans = new TranslateTransition(Duration.millis(Global.movDurTime), animItems.get(en.id));
			                animTrans.setToX(getAnimItemX(en.position.col()));
			                animTrans.setToY(getAnimItemY(en.position.row()));
			                TranslateTransition bloodBarTrans = new TranslateTransition(Duration.millis(Global.movDurTime), bloodBarItems.get(en.id));
			                bloodBarTrans.setToX(getShapeItemX(en.position.col(), Global.bloodWidth));
			                bloodBarTrans.setToY(getShapeItemY(en.position.row(), Global.bloodHeight));
			                TranslateTransition bloodFillTrans = new TranslateTransition(Duration.millis(Global.movDurTime), bloodFillItems.get(en.id));
			                bloodFillTrans.setToX(getShapeItemX(en.position.col(), Global.bloodWidth) + 1 - bloodBia);
			                bloodFillTrans.setToY(getShapeItemY(en.position.row(), Global.bloodHeight));
			                animTrans.play();
			                bloodBarTrans.play();
			                bloodFillTrans.play();
	        		}));  
	        }
		}
	}

	/**	设置移动	*/
	private void setMove() {
		for (Entity en: CoreWorld.entities.values()) {
	        if (en.state == EntityState.LIVE) {
				double bloodBia = (1 - (double)en.blood / (double)en.creatureType.totalBlood) * Global.bloodWidth / 2;
				animItems.get(en.id).setTranslateX(getAnimItemX(en.position.col()));
				animItems.get(en.id).setTranslateY(getAnimItemY(en.position.row()));
				bloodBarItems.get(en.id).setTranslateX(getShapeItemX(en.position.col(), Global.bloodWidth));
				bloodBarItems.get(en.id).setTranslateY(getShapeItemY(en.position.row(), Global.bloodHeight));
				bloodFillItems.get(en.id).setTranslateX(getShapeItemX(en.position.col(), Global.bloodWidth) + 1 - bloodBia);
				bloodFillItems.get(en.id).setTranslateY(getShapeItemY(en.position.row(), Global.bloodHeight));
	        }
		}
	}

	/**	设置消失动画	*/
	private void setFadeAnimation() {
		for (Entity en: CoreWorld.entities.values()) {
	        // 如果死亡则设置消失
	        if (en.state != EntityState.LIVE) {
	        	if (animItems.get(en.id).getOpacity() > 0.7) { // 若原来是活着的，Fade处理一下
	    			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime + Global.movDurTime / 2),
	    					new KeyValue(animItems.get(en.id).opacityProperty(), 0),
	    					new KeyValue(animItems.get(en.id).graphicProperty(), new ImageView(getEntityImage(null, EntityState.DEAD)))
    					));
	    			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Global.atkDurTime + Global.movDurTime),
	    					new KeyValue(animItems.get(en.id).opacityProperty(), 0.5),
	    					new KeyValue(bloodBarItems.get(en.id).opacityProperty(), 0),
	    					new KeyValue(bloodFillItems.get(en.id).opacityProperty(), 0)
	    					));
    			}
	        }
		}
	}
	
	/**	设置消失	*/
	private void setDead() {
		for (Entity en: CoreWorld.entities.values()) {
	        // 如果死亡则设置消失
	        if (en.state != EntityState.LIVE) {
	        	setEntityImage(animItems.get(en.id), null, EntityState.DEAD);
	        	animItems.get(en.id).setOpacity(0.5);
	        	bloodBarItems.get(en.id).setOpacity(0);
	        	bloodFillItems.get(en.id).setOpacity(0);
	        }
		}
    }
	
	/**	根据生物类型获取对应的图像	*/
	private Image getEntityImage(CreatureType ctype, EntityState st) {
    	Image creatureImg = null;
    	if (st == EntityState.DEAD)
    		creatureImg = new Image(getClass().getClassLoader().getResourceAsStream("img/dead.png"));
    	else if (ctype == null)
    		creatureImg = new Image(getClass().getClassLoader().getResourceAsStream("img/none.png"));
			//creatureImg = new Image(getClass().getClassLoader().getResourceAsStream("img/debug.png"));
    	else
    		creatureImg = new Image(getClass().getClassLoader().getResourceAsStream("img/" + ctype.imgName + ".png"));
    	return creatureImg;
	}
	
	/**	设置一个格子的图像	*/
    private void setEntityImage(Label cellLabel, CreatureType ctype, EntityState st) {
    	cellLabel.setGraphic(new ImageView(getEntityImage(ctype, st)));
    }
}

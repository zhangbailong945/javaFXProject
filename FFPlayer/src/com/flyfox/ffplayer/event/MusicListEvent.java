package com.flyfox.ffplayer.event;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import com.flyfox.ffplayer.FFPlayerManager;
import com.flyfox.ffplayer.control.FuncBottom;
import com.flyfox.ffplayer.control.MusicList;
import com.flyfox.ffplayer.model.CacheUtils;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.ffplayer.util.FFPlayerUtils;
import com.flyfox.ffplayer.util.ImageUtils;

/**
 * 播放列表 事件
 * 
 * @author flyfox
 * @date 2014年12月8日
 */
public class MusicListEvent {

	FFPlayerManager manager;

	public MusicListEvent(FFPlayerManager manager) {
		this.manager = manager;
	}

	/**
	 * 列表事件初始化
	 */
	public void bind() {
		MusicList musicList = manager.getMusicList();
		// node添加拖入文件事件
		musicList.getTable().setOnDragOver(event -> {
			Dragboard dragboard = event.getDragboard();
			if (dragboard.hasFiles()) {
				File file = dragboard.getFiles().get(0);
				// 暂时只支持mp3
				if (file.getAbsolutePath().endsWith(".mp3")) { // 用来过滤拖入类型
					event.acceptTransferModes(TransferMode.COPY);// 接受拖入文件
				}
			}
		});

		// 拖入后松开鼠标触发的事件
		musicList.getTable().setOnDragDropped(event -> {
			Dragboard dragboard = event.getDragboard();
			if (event.isAccepted()) {
				// 获取拖入的文件
				dragboard.getFiles().forEach(file -> {
					addMusic(musicList, file);
				});
			}
		});

		// 每列的事件绑定，双击事件，右键菜单
		musicList.getTable().setRowFactory(tv -> {
			return new TableRowControl();
		});

		musicList.getTable().setTableMenuButtonVisible(true);

	}

	/**
	 * 播放列表控制
	 * 
	 * @author flyfox
	 * @date 2014年11月12日
	 */
	private class TableRowControl extends TableRow<FFPlayer> {

		MusicList musicList = manager.getMusicList();
		FuncBottom funcBottom = manager.getFuncBottom();

		public TableRowControl() {
			super();

			// 初始化右键菜单
			initContextMenu();

			// 双击
			this.setOnMouseClicked(event -> {
				if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2
						&& TableRowControl.this.getIndex() < musicList.getTable().getItems().size()) {
					manager.stop();
					// 获取当前音乐，播放
					CacheUtils.setIndex(TableRowControl.this.getIndex());
					manager.setModel(musicList.get(TableRowControl.this.getIndex()));
					manager.bindMusic();

					manager.play();
				}
			});
		}

		// 右键菜单
		private void initContextMenu() {
			final ContextMenu contextMenu = new ContextMenu();
			// 加载中
			contextMenu.setOnShowing(e -> {
				// System.out.println("showing");
				});
			// 显示完成
			contextMenu.setOnShown(e -> {
				// System.out.println("shown");
				});

			MenuItem addFileItem = new MenuItem("添加文件");
			addFileItem.setOnAction(e -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("请选择文件");

				// TODO 下面代码没有效果，不知道为什么
				// FileChooser.ExtensionFilter extFilter = new
				// FileChooser.ExtensionFilter("music files (mp3)", "*.mp3");
				// fileChooser.setSelectedExtensionFilter(extFilter);

					// Show open file dialog
					List<File> fileList = fileChooser.showOpenMultipleDialog(null);
					if (fileList != null) {
						fileList.forEach(file -> {
							addMusic(musicList, file);
						});
					}

				});

			MenuItem addFolderItem = new MenuItem("添加文件夹");
			addFolderItem.setOnAction(e -> {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				directoryChooser.setTitle("请选择文件夹");

				// Show open file dialog
					File dir = directoryChooser.showDialog(null);
					if (dir != null) {
						Arrays.asList(dir.listFiles()).forEach(file -> {
							addMusic(musicList, file);
						});
					}
				});

			MenuItem deleteItem = new MenuItem("删除");
			deleteItem.setOnAction(e -> {
				if (TableRowControl.this.getIndex() < musicList.getTable().getItems().size()) {
					// 如果是当前播放，播放下一首
					if (TableRowControl.this.getIndex() == CacheUtils.getIndex()) {
						manager.playPre();
					}
					musicList.remove(TableRowControl.this.getIndex());
				}
			});

			MenuItem clearItem = new MenuItem("清空列表");
			clearItem.setOnAction(event -> {
				manager.stop(); // 停止播放
					// 初始化时间
					funcBottom.getTimeLabel().setText("00:00/00:00");
					// 初始化名称
					funcBottom.getName().setText("FFPlayer音乐播放器");
					// 初始化并仅用时间轴
					funcBottom.getTimeSlider().setValue(0);
					funcBottom.getTimeSlider().setDisable(true);
					// 初始化播放按钮
					Label play = funcBottom.getPlay();
					Tooltip playTooltip = new Tooltip("播放");
					((ImageView) play.getGraphic()).setImage(ImageUtils.getImage("play.png"));
					play.setTooltip(playTooltip);
					// 清空数据
					musicList.clear();
					manager.setModel(null);
				});

			contextMenu.getItems().addAll(addFileItem, addFolderItem, deleteItem, clearItem);
			this.setContextMenu(contextMenu);
		}
	}

	/**
	 * 添加文件
	 * 
	 * @param musicList
	 * @param file
	 */
	private void addMusic(MusicList musicList, File file) {
		if (!file.getAbsolutePath().endsWith(".mp3")) {
			return;
		}
		FFPlayer model = FFPlayerUtils.getFFPlayer(file.getAbsolutePath());
		musicList.add(model);
	}

}

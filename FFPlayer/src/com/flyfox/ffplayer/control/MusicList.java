package com.flyfox.ffplayer.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.flyfox.ffplayer.model.CacheUtils;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.ffplayer.util.FFPlayerUtils;

/**
 * 播放列表
 * 
 * @author flyfox
 * @date 2014年11月7日
 */
public class MusicList {

	private TableView<FFPlayer> playTable = new TableView<FFPlayer>();
	// 数据列表
	private final ObservableList<FFPlayer> tableData = FXCollections.observableArrayList();

	public MusicList() {
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		playTable.getStyleClass().addAll("ffplayer-musicList");
		
		// 名称
		TableColumn<FFPlayer, String> title = new TableColumn<FFPlayer, String>("歌曲");
		title.setCellValueFactory(new PropertyValueFactory<FFPlayer, String>("title"));
		title.setPrefWidth(150);
		// 艺术家
		TableColumn<FFPlayer, String> artist = new TableColumn<FFPlayer, String>("歌手");
		artist.setCellValueFactory(new PropertyValueFactory<FFPlayer, String>("artist"));
		artist.setPrefWidth(90);
		// 专辑
		TableColumn<FFPlayer, String> album = new TableColumn<FFPlayer, String>("专辑");
		album.setCellValueFactory(new PropertyValueFactory<FFPlayer, String>("album"));
		album.setPrefWidth(150);
		// 总时间
		TableColumn<FFPlayer, String> totleTime = new TableColumn<FFPlayer, String>("时间");
		totleTime.setCellValueFactory(new PropertyValueFactory<FFPlayer, String>("duration"));
		totleTime.setPrefWidth(100);

		playTable.getColumns().addAll(title, artist, album, totleTime);

		// 初始化列表
		if (CacheUtils.getTableData().size() > 0) {
			CacheUtils.getTableData().forEach(path -> {
				FFPlayer ffplayer = FFPlayerUtils.getFFPlayer(path);
				tableData.add(ffplayer);
			});
		}

		// 数据绑定
		playTable.setItems(tableData);

	}

	public TableView<FFPlayer> getTable() {
		return playTable;
	}

	public TableView<FFPlayer> getNode() {
		return playTable;
	}

	/**
	 * 添加音乐
	 * 
	 * @param music
	 */
	public void add(FFPlayer music) {
		// 不添加相同数据
		if (!CacheUtils.contains(music.getPath())) {
			tableData.add(music);
			// 缓存同步
			CacheUtils.add(music.getPath());
		}
	}

	/**
	 * 删除音乐
	 * 
	 * @param index
	 */
	public void remove(int index) {
		tableData.remove(index);
		// 缓存同步
		CacheUtils.remove(index);
	}

	/**
	 * 删除音乐
	 * 
	 * @param music
	 */
	public void remove(FFPlayer music) {
		tableData.remove(music);
		// 缓存同步
		CacheUtils.remove(music.getPath());
	}

	/**
	 * 清空列表
	 * 
	 * @param index
	 */
	public void clear() {
		tableData.clear();
		// 缓存同步
		CacheUtils.clear();
	}

	public FFPlayer getFirst() {
		return get(0);
	}

	public FFPlayer getLast() {
		return get(tableData.size() - 1);
	}

	public FFPlayer get(int index) {
		return tableData.get(index);
	}

	/**
	 * 是否可以播放
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return tableData.size() <= 0;
	}

	public int size() {
		return tableData.size();
	}

	public ObservableList<FFPlayer> getTableData() {
		return tableData;
	}

}

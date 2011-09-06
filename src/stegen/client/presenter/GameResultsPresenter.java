package stegen.client.presenter;

import java.util.*;

import stegen.client.event.*;
import stegen.client.event.callback.*;
import stegen.client.gui.playeraction.*;
import stegen.shared.*;

public class GameResultsPresenter implements Presenter {
	private final Display view;
	private final EventBus eventBus;
	final UpdateGameResultListCallback eventUpdateGameResultListCallback = creatUpdateGameResultListCallback();
	final RefreshCallback eventRefreshCallback = createRefreshCallback();
	final UndoCallback eventUndoCallback = createUndoCallback();
	final PlayerWonCallback eventPlayerWonCallback = createPlayerWonCallback();
	final ClearScoresCallback eventClearScoresCallback = createClearScoresCallback();
	final ChangeNicknameCallback eventChangeNicknameCallback = createChangeNicknameCallback();

	public interface Display {
		void changeGameResultList(List<GameResultsRow> content);
	}

	public GameResultsPresenter(Display scoreView, EventBus eventBus) {
		this.view = scoreView;
		this.eventBus = eventBus;
	}

	@Override
	public void go() {
		initEvents();
		loadGameResults();
	}

	private void initEvents() {
		eventBus.addHandler(eventUpdateGameResultListCallback);
		eventBus.addHandler(eventRefreshCallback);
		eventBus.addHandler(eventUndoCallback);
		eventBus.addHandler(eventPlayerWonCallback);
		eventBus.addHandler(eventClearScoresCallback);
		eventBus.addHandler(eventChangeNicknameCallback);
	}

	private void loadGameResults() {
		eventBus.updateGameResultList();
	}

	private UpdateGameResultListCallback creatUpdateGameResultListCallback() {
		return new UpdateGameResultListCallback() {

			@Override
			public void onSuccessImpl(List<PlayerCommandDto> gameResults) {
				List<GameResultsRow> content = new ArrayList<GameResultsRow>();
				for (PlayerCommandDto playerCommandDto : gameResults) {
					content.add(new GameResultsRow(playerCommandDto.player.nickname,
							playerCommandDto.performedDateTime, playerCommandDto.description));
				}
				view.changeGameResultList(content);
			}
		};
	}

	private RefreshCallback createRefreshCallback() {
		return new RefreshCallback() {

			@Override
			public void onSuccessImpl(Void result) {
				loadGameResults();
			}
		};
	}

	private UndoCallback createUndoCallback() {
		return new UndoCallback() {

			@Override
			public void onSuccessImpl(UndoPlayerCommandResult result) {
				loadGameResults();
			}
		};
	}

	private PlayerWonCallback createPlayerWonCallback() {
		return new PlayerWonCallback() {

			@Override
			public void onSuccessImpl(Void result) {
				loadGameResults();
			}
		};
	}

	private ClearScoresCallback createClearScoresCallback() {
		return new ClearScoresCallback() {

			@Override
			public void onSuccessImpl(Void result) {
				loadGameResults();
			}
		};
	}

	private ChangeNicknameCallback createChangeNicknameCallback() {
		return new ChangeNicknameCallback() {

			@Override
			public void onSuccessImpl(PlayerDto result) {
				loadGameResults();
			}

		};
	}

}

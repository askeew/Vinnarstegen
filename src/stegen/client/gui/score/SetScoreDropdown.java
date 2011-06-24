package stegen.client.gui.score;

import stegen.client.dto.*;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

public class SetScoreDropdown extends VerticalPanel {
	private final PlayerDto winner;
	private final PlayerDto loser;
	private final GameResultDto gameResult;
	private final UpdateScoreCallback scoreCallback;
	private final ListBox listBox = new ListBox(false);

	public SetScoreDropdown(PlayerDto winner, PlayerDto loser, GameResultDto gameResult,
			UpdateScoreCallback scoreCallback) {
		this.winner = winner;
		this.loser = loser;
		this.gameResult = gameResult;
		this.scoreCallback = scoreCallback;
		addItems();
		addListener();
	}

	private void addListener() {
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				SetResult setResult = SetResult.values()[listBox.getSelectedIndex()];
				int setIndex = 0;
				for (int i = 0; i < setResult.winnerSets; i++) {
					gameResult.setScores[setIndex++] = new SetScoreDto(11, 1);
				}
				for (int i = 0; i < setResult.loserSets; i++) {
					gameResult.setScores[setIndex++] = new SetScoreDto(1, 11);
				}
				for (int i = setIndex; i < gameResult.setScores.length; i++) {
					gameResult.setScores[setIndex++] = new SetScoreDto(0, 0);
				}
				scoreCallback.onScoreChange();
			}
		});

	}

	private void addItems() {
		Label playersLabel = new Label(winner.nickname + " - " + loser.nickname);
		playersLabel.setStylePrimaryName("players_label");
		add(playersLabel);

		setWidth("100%");
		for (SetResult setResult : SetResult.values()) {
			listBox.addItem(setResult.description, setResult.name());
		}
		listBox.setStylePrimaryName("center");
		add(listBox);
		setCellHorizontalAlignment(listBox, ALIGN_CENTER);
	}

	enum SetResult {
		NOLL_NOLL(0, 0, "0 - 0"),
		TRE_NOLL(3, 0, "3 - 0"),
		TRE_ETT(3, 1, "3 - 1"),
		TRE_TVA(3, 2, "3 - 2");

		public final int winnerSets;
		public final int loserSets;
		public final String description;

		private SetResult(int winnerScore, int loserScore, String description) {
			this.winnerSets = winnerScore;
			this.loserSets = loserScore;
			this.description = description;
		}
	}

}
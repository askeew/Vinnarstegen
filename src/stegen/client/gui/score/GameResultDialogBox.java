package stegen.client.gui.score;

import stegen.client.dto.*;
import stegen.shared.*;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

public abstract class GameResultDialogBox extends DialogBox {
	private final Button closeButton = new Button("Avbryt");
	private final Button okButton = new Button("Ok");
	private final PlayerDto winner;
	private final PlayerDto loser;
	private final GameResultDto gameResult = GameResultDto.createEmptyGameResult();
	private final Label scoreLabel = new Label();

	public GameResultDialogBox(PlayerDto winner, PlayerDto loser) {
		this.winner = winner;
		this.loser = loser;
		init();
		setupButtonHandler();
	}

	private void init() {
		closeButton.setStylePrimaryName("button");
		okButton.setStylePrimaryName("button");
		setText("Matchresultat");
		setAnimationEnabled(true);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(new HTML("<p>Skriv in matchresultatet. Vinnaren står först</p>"));
		// Widget gameSetResultTable = new GameSetResultTable(new
		// UpdateScoreCallback() {
		//
		// @Override
		// public void onScoreChange() {
		// updateScoreLabel();
		// }
		// }, winnerEmail, loserEmail, gameResult);
		// verticalPanel.add(gameSetResultTable);

		Widget setScoreDropdown = new SetScoreDropdown(winner, loser, gameResult, new UpdateScoreCallback() {

			@Override
			public void onScoreChange() {
				updateScoreLabel();
			}
		});
		verticalPanel.add(setScoreDropdown);

		updateScoreLabel();
		verticalPanel.add(scoreLabel);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.add(okButton);
		buttonPanel.add(closeButton);
		buttonPanel.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_LEFT);
		buttonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		verticalPanel.add(buttonPanel);
		setWidget(verticalPanel);
	}

	private void updateScoreLabel() {
		GameResultCalculator calculator = new GameResultCalculator(gameResult);
		int win = calculator.getWonSets();
		int lose = calculator.getLostSets();
		scoreLabel.setText(win + " - " + lose);
		if (lose >= win) {
			okButton.setEnabled(false);
			scoreLabel.setStylePrimaryName("score_big_error");
		} else {
			okButton.setEnabled(true);
			scoreLabel.setStylePrimaryName("score_big");
		}
	}

	private void setupButtonHandler() {
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				sendGameResult(gameResult);
			}

		});
	}

	protected abstract void sendGameResult(GameResultDto gameResult);
}
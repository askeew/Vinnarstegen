package stegen.client.presenter;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.*;

import org.easymock.*;
import org.junit.*;

import stegen.client.event.*;
import stegen.client.gui.score.*;
import stegen.client.presenter.ScorePresenter.Display;
import stegen.shared.*;

public class ScorePresenterTest {

	private Display view;
	private LoginDataDto result;
	private EventBus eventBus;
	private ScorePresenter presenter;

	@Test
	public void testShowView() {
		setupPresenter();

		setupInitializationExpects();

		presenter.go();
	}

	@Test
	public void testPressCleanAllScores() {
		setupPresenter();
		presenter.go();

		setupCleanAllScoresExpects();

		simulateCleanAllScores();
	}

	@Test
	public void testEventPlayerWon() {
		setupPresenter();

		eventBus.updatePlayerScoreList();
		replay(eventBus);

		presenter.eventClearScoresCallback.onSuccess(null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testEventScoresUpdated() {
		setupPresenter();
		presenter.go();

		reset(view, eventBus);
		view.changeScoreList(anyObject(List.class));
		verifyListContentForPreviousMethod();
		replay(view, eventBus);

		List<PlayerScoreDto> scores = new ArrayList<PlayerScoreDto>();
		PlayerScoreDto playerScoreDto = new PlayerScoreDto(result.player, 42, result.player, "date");
		playerScoreDto.ranking = 10;
		scores.add(playerScoreDto);
		presenter.eventChangedScoresCallback.onSuccess(scores);
	}

	private void setupPresenter() {
		result = LoginDataDtoFactory.createLoginData();
		view = createStrictMock(Display.class);
		eventBus = createStrictMock(EventBus.class);
		presenter = new ScorePresenter(view, result, eventBus);
	}

	private void setupInitializationExpects() {
		view.addCleanScoresHandler(presenter.clickCleanScoresHandler);
		eventBus.addHandler(presenter.eventClearScoresCallback);
		eventBus.addHandler(presenter.eventChangedScoresCallback);
		eventBus.updatePlayerScoreList();
		replay(view, eventBus);
	}

	private void setupCleanAllScoresExpects() {
		reset(view, eventBus);
		eventBus.clearAllScores(result.player);
		replay(view, eventBus);
	}

	private void simulateCleanAllScores() {
		presenter.clickCleanScoresHandler.onClick(null);
	}

	private void verifyListContentForPreviousMethod() {
		IAnswer<? extends Object> answer = new IAnswer<Object>() {

			@Override
			public Object answer() throws Throwable {
				@SuppressWarnings("unchecked")
				List<ScoreTableRow> content = (List<ScoreTableRow>) getCurrentArguments()[0];
				assertEquals(1, content.size());
				assertEquals(result.player.nickname, content.get(0).changedBy);
				assertEquals("date", content.get(0).changedDateTime);
				assertEquals(true, content.get(0).currentUser);
				assertEquals(result.player.nickname, content.get(0).name);
				assertEquals("10", content.get(0).ranking);
				assertEquals("42", content.get(0).score);
				return null;
			}
		};
		expectLastCall().andAnswer(answer);
	}

}

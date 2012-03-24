package stegen.client.presenter;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import stegen.client.gui.*;
import stegen.client.presenter.CompositeMainPresenter.Display;
import stegen.client.service.*;
import stegen.shared.*;
@RunWith(MockitoJUnitRunner.class)
public class CompositeMainPresenterTest {
	@Mock
	private Display view;
	private LoginDataDto loginData = LoginDataDtoFactory.createLoginData();
	private CompositeMainPresenter presenter;
	@Mock
	private DateTimeFormats dateTimeFormats;
	@Mock
	private Shell shell;
	@Mock
	com.google.gwt.event.shared.EventBus gwtEventBus;
	@Mock
	private  PlayerCommandServiceAsync playerCommandService;
	@Mock
	private ScoreServiceAsync scoreService;
	@Mock
	private  PlayerServiceAsync playerService;

	@Test
	public void testShowView() {
		setupPresenter();
		
		setupInitializationExpects();
		
		presenter.go();
	}

	private void setupPresenter() {
		presenter = new CompositeMainPresenter(view, loginData,gwtEventBus,playerCommandService, scoreService,playerService, dateTimeFormats,shell);
	}

	private void setupInitializationExpects() {
		when(view.getScoreView()).thenReturn(mock(stegen.client.presenter.ScorePresenter.Display.class));
		when(view.getMessageView()).thenReturn(
				mock(stegen.client.presenter.MessagesPresenter.Display.class));
		when(view.getChallengeInputView()).thenReturn(
				mock(stegen.client.presenter.ChallengePresenter.Display.class));
		when(view.getWinGameInputView()).thenReturn(
				mock(stegen.client.presenter.WinGameInputPresenter.Display.class));
		when(view.getGameResultsView()).thenReturn(
				mock(stegen.client.presenter.GameResultsPresenter.Display.class));
		when(view.getUndoView()).thenReturn(mock(stegen.client.presenter.UndoPresenter.Display.class));
		when(view.getLoginStatusesView()).thenReturn(
				mock(stegen.client.presenter.LoginStatusesPresenter.Display.class));
		when(view.getPlayerMiscCommandView()).thenReturn(
				mock(stegen.client.presenter.PlayerMiscCommandsPresenter.Display.class));
		view.setShell(shell);
	}

}

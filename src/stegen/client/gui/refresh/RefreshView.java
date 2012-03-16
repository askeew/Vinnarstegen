package stegen.client.gui.refresh;
import stegen.client.gui.*;
import stegen.client.presenter.RefreshPresenter.Display;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;

public class RefreshView implements Display {
	private static final int ONE_MINUTE = 60 * 1000;
	private final RefreshButton refreshButton = new RefreshButton();
	private Timer timer;

	public RefreshView() {
	}

	@Override
	public void addClickRefreshHandler(ClickHandler clickHandler) {
		refreshButton.addClickHandler(clickHandler);
	}

	@Override
	public void startTimer(final Runnable commandToRun) {
		timer = new Timer() {

			@Override
			public void run() {
				commandToRun.run();
			}
		};
		timer.scheduleRepeating(ONE_MINUTE);
	}
	
	@Override
	public void setShell(Shell shell) {
		shell.showInRefreshArea(refreshButton);
	}

}

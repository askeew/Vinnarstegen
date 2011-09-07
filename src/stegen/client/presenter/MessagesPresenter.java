package stegen.client.presenter;

import java.util.*;

import stegen.client.event.*;
import stegen.client.event.callback.*;
import stegen.client.gui.message.*;
import stegen.client.service.*;
import stegen.client.service.messageprefix.*;
import stegen.shared.*;

import com.google.gwt.event.dom.client.*;

public class MessagesPresenter implements Presenter {

	private final Display view;
	private final LoginDataDto loginData;
	private final EventBus eventBus;
	private final MessagePrefixGenerator messagePrefixGenerator;
	final ClickHandler clickOpenMessageInputHandler = createClickOpenMessageInputHandler();
	final ClickHandler clickSendMessageHandler = createClickSendMessageHandler();
	final UpdateSendMessageListCallback eventUpdateSendMessageListCallback = createUpdateSendMessageListCallback();
	final CommandSendMessageCallback eventCommandSendMessageCallback = createCommandSendMessageCallback();
	final CommandRefreshCallback eventCommandRefreshCallback = createCommandRefreshMessagesCallback();
	final CommandChangeNicknameCallback eventCommandChangeNicknameCallback = createCommandChangeNicknameCallback();
	private MessagePrefix currentMessagePrefix;

	public interface Display {

		void setMessageButtonTitle(String buttonText);

		void addClickOpenMessageInputHandler(ClickHandler clickHandler);

		void setMessageInputTitle(String string);

		void addClickSendMessageHandler(ClickHandler clickHandler);

		String getMessageInputContent();

		void changeMessageList(List<MessageTableRow> content);

	}

	public MessagesPresenter(Display messagesView, LoginDataDto loginData,
			MessagePrefixGenerator messagePrefixGenerator, EventBus eventBus) {
		this.view = messagesView;
		this.loginData = loginData;
		this.messagePrefixGenerator = messagePrefixGenerator;
		this.eventBus = eventBus;
	}

	@Override
	public void go() {
		changeMessagePrefixOnButton();
		initView();
		initEvents();
		loadMessages();
	}

	private void changeMessagePrefixOnButton() {
		this.currentMessagePrefix = messagePrefixGenerator.getRandomizedPrefix();
		view.setMessageButtonTitle(currentMessagePrefix.buttonText);
	}

	private void initView() {
		view.addClickOpenMessageInputHandler(clickOpenMessageInputHandler);
		view.addClickSendMessageHandler(clickSendMessageHandler);
	}

	private void initEvents() {
		eventBus.addHandler(eventCommandSendMessageCallback);
		eventBus.addHandler(eventUpdateSendMessageListCallback);
		eventBus.addHandler(eventCommandRefreshCallback);
		eventBus.addHandler(eventCommandChangeNicknameCallback);
	}

	private void loadMessages() {
		eventBus.updateSendMessageList();
	}

	private ClickHandler createClickOpenMessageInputHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				view.setMessageInputTitle(loginData.player.nickname + " " + currentMessagePrefix.actionText);
			}
		};
	}

	private ClickHandler createClickSendMessageHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String messageContent = view.getMessageInputContent();
				boolean emptyMessageContent = messageContent.trim().isEmpty();
				if (!emptyMessageContent) {
					String completeMessage = loginData.player.nickname + " " + currentMessagePrefix.actionText + " "
							+ messageContent;
					eventBus.sendMessage(loginData.player, completeMessage);
				}

			}
		};
	}

	private CommandSendMessageCallback createCommandSendMessageCallback() {
		return new CommandSendMessageCallback() {

			@Override
			public void onSuccessImpl(Void result) {
				eventBus.updateSendMessageList();
				changeMessagePrefixOnButton();
			}
		};
	}

	private UpdateSendMessageListCallback createUpdateSendMessageListCallback() {
		return new UpdateSendMessageListCallback() {

			@Override
			public void onSuccessImpl(List<PlayerCommandDto> result) {
				List<MessageTableRow> content = new ArrayList<MessageTableRow>();
				for (PlayerCommandDto playerCommandDto : result) {
					content.add(new MessageTableRow(playerCommandDto.player.nickname,
							playerCommandDto.performedDateTime, playerCommandDto.description));
				}
				view.changeMessageList(content);
			}
		};
	}

	private CommandRefreshCallback createCommandRefreshMessagesCallback() {
		return new CommandRefreshCallback() {

			@Override
			public void onSuccessImpl(Void result) {
				loadMessages();
				changeMessagePrefixOnButton();
			}
		};
	}

	private CommandChangeNicknameCallback createCommandChangeNicknameCallback() {
		return new CommandChangeNicknameCallback() {

			@Override
			public void onSuccessImpl(PlayerDto result) {
				loadMessages();
			}

		};
	}
}

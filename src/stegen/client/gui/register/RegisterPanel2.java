package stegen.client.gui.register;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

public class RegisterPanel2 implements IsWidget {

	private final VerticalPanel baseWidget = new VerticalPanel();
	private final Button registeraButton = new Button("Registrera");
	private final Label registerLabel = new Label("Du måste skriva in den magiska koden för att registrera dig.");
	private final TextBox kodField = new TextBox();;

	public RegisterPanel2() {
		initLayout();
	}

	private void initLayout() {
		registeraButton.setStylePrimaryName("button");

		kodField.setText("<kod>");

		// Focus the cursor on the name field when the app loads
		kodField.setFocus(true);
		kodField.selectAll();
		baseWidget.add(registerLabel);
		baseWidget.add(kodField);
		baseWidget.add(registeraButton);
	}

	@Override
	public Widget asWidget() {
		return baseWidget;
	}

	public String getRegistrationText() {
		return kodField.getText();
	}

	public void showRegistrationFail() {
		registerLabel.setText("Nej, det där gick inte bra. Skriv in den magiska koden för att registrera dig.");
	}

	public void addClickRegistrationHandler(ClickHandler clickHandler) {
		registeraButton.addClickHandler(clickHandler);
	}

}
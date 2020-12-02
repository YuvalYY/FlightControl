package view;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Model;
import model.Update;
import viewModel.ViewModel;

public class MainWindowController implements Observer, Initializable {
	@FXML
	private MapDisplayer mMapDisplayer;
	@FXML
	private JoystickDisplayer mJoystickDisplayer;
	@FXML
	private ToggleGroup tGroup;
	@FXML
	private Slider mThrottleSlider;
	@FXML
	private Slider mRudderSlider;
	int[][] arr;

	private double pressX, pressY;
	private ViewModel mViewModel;
	private String mPathServerIP;
	private int mPathServerPort=0;
	private boolean mHandControl = false; 
	private long mLastJoystickSent=0;

	
	private static final long JOYSTICK_DELAY=100;

	
	//C'tors and related functions ----------------------------------------------------------------------
	
	public MainWindowController() {
		mViewModel = new ViewModel(new Model());
		mViewModel.addObserver(this);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mJoystickDisplayer.redraw();
		registerEventHandlers();
	}
			
	//Event methods ----------------------------------------------------------------

	public void onCalculatePathPress() {
		Optional<Pair<String, String>> result = showConnectDialog();
		if (result.isPresent()) {
			mPathServerIP = result.get().getKey();
			mPathServerPort = Integer.parseInt(result.get().getValue());
			calculatePath();
		}

	}

	public void onConnectPress() {
		Optional<Pair<String, String>> result = showConnectDialog();
		if (result.isPresent())
			mViewModel.vmConnectToSimulator(result.get().getKey(), Integer.parseInt(result.get().getValue()));
	}

	public void onJoystickChanged(int x, int y) {
		mViewModel.vmJoystickChanged(x, y);
	}

	@FXML
	public void onLoadDataPress() {
		testAPI();
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Open CSV File");
	    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
	    File file = chooser.showOpenDialog(new Stage());
	    if(file!=null) {
	    	handleCSVFile(file);
			mMapDisplayer.resetRoute();
			mMapDisplayer.setMapData(arr);
	    }
	}

	public void onMapPress(double x, double y) {
		if(mPathServerIP!=null&&mPathServerPort!=0) {
			mMapDisplayer.setRoute(null);
			mMapDisplayer.onMouseClick(x, y);
			calculatePath();
		} else {
			mMapDisplayer.onMouseClick(x, y);
		}
	}
	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		Update update = (Update) arg1;
		switch (update.getId()) {
		case "Path":
			mMapDisplayer.setRoute((String[]) (update.getArg()));
			break;
		case "ProgressAsExpected":
			mMapDisplayer.continueOneTile();
			break;
		case "ProgressUnexpected":
			break;
			
		}
	}
	
	public void autoToManual() {
		mViewModel.vmAutoToManual(mThrottleSlider.getValue(),mRudderSlider.getValue());
	}
	
	public void manualToAuto() {
		//TODO check if the textbox is not empty and run it, send its values to the function
		String[] lines = null;
		mViewModel.vmManualToAuto(lines);
	}

	//Utility methods ----------------------------------------------------------------
	
	public void handleCSVFile(File file) {
		
	}
	
	private void sendJoystickPosition(boolean dragged, double x, double y) {
		
		if (mHandControl) {
			if (dragged)
				if (System.currentTimeMillis() - mLastJoystickSent < JOYSTICK_DELAY)
					return;
			mLastJoystickSent = System.currentTimeMillis();
			mViewModel.vmJoystickChanged(x, y);
		}
	}
	
	public void calculatePath() {
		mViewModel.vmCalculatePath(mPathServerIP, mPathServerPort, mMapDisplayer.getMapData(),
				mMapDisplayer.getCurrentPosition(), mMapDisplayer.getEnd());
	}
	
	public Optional<Pair<String, String>> showConnectDialog() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Enter Port and IP");
		ButtonType loginButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		TextField ip = new TextField();
		ip.setPromptText("IP");
		TextField port = new TextField();
		port.setPromptText("Port");
		grid.add(new Label("IP:"), 0, 0);
		grid.add(ip, 1, 0);
		grid.add(new Label("Port:"), 0, 1);
		grid.add(port, 1, 1);

		Node connect = dialog.getDialogPane().lookupButton(loginButtonType);
		connect.setDisable(true);

		boolean[] disable = { true, true };

		ip.textProperty().addListener((observable, oldValue, newValue) -> {
			disable[0] = newValue.trim().isEmpty();
			connect.setDisable(disable[0] || disable[1]);
		});
		port.textProperty().addListener((observable, oldValue, newValue) -> {
			disable[1] = newValue.trim().isEmpty();
			connect.setDisable(disable[0] || disable[1]);
		});

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> ip.requestFocus());

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(ip.getText(), port.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		return result;
	}
	
	public void registerEventHandlers() {
		mJoystickDisplayer.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				mJoystickDisplayer.redraw();
				sendJoystickPosition(false,0,0);
			}
		});
		mJoystickDisplayer.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				mJoystickDisplayer.redraw(event.getX() - pressX, event.getY() - pressY);
				sendJoystickPosition(true,event.getX() - pressX,event.getY() - pressY); //TODO make sure that this is sending correct values and you dont need to delete the minus radius
				
			}

		});
		mJoystickDisplayer.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				pressX = event.getX();
				pressY = event.getY();
			}

		});
		mMapDisplayer.setOnMouseClicked(new EventHandler<MouseEvent>() {


			@Override
			public void handle(MouseEvent event) {
				mMapDisplayer.onMouseClick(event.getX(), event.getY());
			}
		});

		mThrottleSlider.setOnMouseReleased(event -> {
			if (mHandControl)
				mViewModel.vmThrottleChanged(mThrottleSlider.getValue());
		});

		mRudderSlider.setOnMouseReleased(event -> {
			if (mHandControl)
				mViewModel.vmRudderChanged(mRudderSlider.getValue());
		});

		tGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (tGroup.getSelectedToggle() != null) {
					mHandControl=!mHandControl;
					if (mHandControl) autoToManual();
					else manualToAuto();
				}
			}
		});
	}
	
	
	//test methods -----------------------------------------------------------
	public void testAPI() {

		arr = new int[20][20];
		Random r = new Random();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = r.nextInt(16);
			}
		}
	}
}

package form.login.obho;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	private static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 400;

	private Scene scene;
	private StackPane loginPane, registerPane, mainPane, profilePane;
	private AnimatedTextField emailField, rEmailField, rNameField;
	private AnimatedPasswordField passwordField, rPasswordField, rPasswordField0;
	private AnimatedButton loginButton, logoutButton, registerButton, okButton, cancelButton, profileButton, backButton;
	private Label nameLabel;
	private Label emailLabel, passwordLabel, dateLabel;
	private ImageView profileImage;
	private Timeline t;

	private ArrayList<Account> accounts;
	private boolean signInFailed;

	public Main() {
		loginPane = new StackPane();
		registerPane = new StackPane();
		mainPane = new StackPane();
		profilePane = new StackPane();

		scene = new Scene(loginPane, SCREEN_WIDTH, SCREEN_HEIGHT);

		emailField = new AnimatedTextField();
		passwordField = new AnimatedPasswordField();
		rEmailField = new AnimatedTextField();
		rPasswordField = new AnimatedPasswordField();
		rPasswordField0 = new AnimatedPasswordField();
		rNameField = new AnimatedTextField();

		loginButton = new AnimatedButton("로그인");
		logoutButton = new AnimatedButton("로그아웃");
		registerButton = new AnimatedButton("회원가입");
		okButton = new AnimatedButton("확인");
		cancelButton = new AnimatedButton("취소");
		profileButton = new AnimatedButton("내 프로필");
		backButton = new AnimatedButton("뒤로");

		nameLabel = new Label();
		emailLabel = new Label();
		passwordLabel = new Label();
		dateLabel = new Label();

		profileImage = new ImageView(new Image("file:C:/javaWork/workspace/LoginFormV2/src/form/login/obho/images/profileImage.png"));

		t = new Timeline();

		accounts = new ArrayList<Account>();

		loadAccounts();
	}

	@Override
	public void start(Stage stage) {
		// sign in pane
		HBox[] siHBoxes = new HBox[] { new HBox(), new HBox(), new HBox() };
		VBox siVBox = new VBox();

		Label[] signInLabels = new Label[] { new Label("로그인"), new Label("이메일"), new Label("비밀번호") };

		signInLabels[0].setStyle("-fx-font-size: 24pt;");

		siHBoxes[0].getChildren().addAll(loginButton, registerButton);
		siHBoxes[1].getChildren().addAll(signInLabels[1], emailField);
		siHBoxes[2].getChildren().addAll(signInLabels[2], passwordField);

		siVBox.getChildren().addAll(signInLabels[0], siHBoxes[1], siHBoxes[2], siHBoxes[0]);

		for (HBox hbox : siHBoxes) {
			hbox.setAlignment(Pos.CENTER);
			hbox.setSpacing(10);
		}

		siVBox.setAlignment(Pos.CENTER);
		siVBox.setSpacing(20);

		loginButton.setOnAction(e -> {
			if (login()) {
				loginPane.getChildren().add(mainPane);
				mainPane.translateXProperty().set(SCREEN_WIDTH);

				KeyValue kv = new KeyValue(mainPane.translateXProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
				KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

				t.getKeyFrames().clear();
				t.getKeyFrames().add(kf);
				t.setOnFinished(e1 -> {
					loginPane.getChildren().remove(mainPane);
					scene.setRoot(mainPane);
					profileImage.requestFocus();
				});
				t.play();
			} else {
				if (signInFailed) return;

				showDialog("ERROR", "존재하지 않는 계정입니다.", "다시 입력해 주세요.", AlertType.ERROR);

				return;
			}

			signInFailed = false;
		});
		registerButton.setOnAction(e -> {
			loginPane.getChildren().add(registerPane);
			registerPane.translateXProperty().set(SCREEN_WIDTH);

			KeyValue kv = new KeyValue(registerPane.translateXProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
			KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

			t.getKeyFrames().clear();
			t.getKeyFrames().add(kf);
			t.setOnFinished(e1 -> {
				loginPane.getChildren().remove(registerPane);
				scene.setRoot(registerPane);
			});
			t.play();
		});

		loginPane.getChildren().add(siVBox);
		loginPane.setStyle("-fx-background-color: rgb(100, 200, 30);");

		// sign up pane
		HBox[] suHBoxes = new HBox[] { new HBox(), new HBox(), new HBox(), new HBox(), new HBox() };
		VBox suVBox = new VBox();

		Label[] signUpLabels = new Label[] { new Label("회원가입"), new Label("이메일"), new Label("이름"), new Label("비밀번호"), new Label("비밀번호 확인") };

		suHBoxes[0].getChildren().addAll(signUpLabels[1], rEmailField);
		suHBoxes[1].getChildren().addAll(signUpLabels[2], rNameField);
		suHBoxes[2].getChildren().addAll(signUpLabels[3], rPasswordField);
		suHBoxes[3].getChildren().addAll(signUpLabels[4], rPasswordField0);
		suHBoxes[4].getChildren().addAll(okButton, cancelButton);

		suVBox.getChildren().add(signUpLabels[0]);
		suVBox.getChildren().addAll(suHBoxes);

		for (HBox hbox : suHBoxes) {
			hbox.setAlignment(Pos.CENTER);
			hbox.setSpacing(10);
		}

		suVBox.setAlignment(Pos.CENTER);
		suVBox.setSpacing(20);

		okButton.setOnAction(e -> {
			if (register()) {
				registerPane.getChildren().add(loginPane);
				loginPane.translateYProperty().set(SCREEN_HEIGHT);

				KeyValue kv = new KeyValue(loginPane.translateYProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
				KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

				t.getKeyFrames().clear();
				t.getKeyFrames().add(kf);
				t.setOnFinished(e1 -> {
					registerPane.getChildren().remove(loginPane);
					scene.setRoot(loginPane);
				});
				t.play();

				rEmailField.clear();
				rPasswordField.clear();
				rPasswordField0.clear();
				rNameField.clear();

				showDialog("INFORMATION", "회원가입 되었습니다.", "", AlertType.INFORMATION);
			}
		});
		cancelButton.setOnAction(e -> {
			registerPane.getChildren().add(loginPane);
			loginPane.translateXProperty().set(-SCREEN_WIDTH);

			KeyValue kv = new KeyValue(loginPane.translateXProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
			KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

			t.getKeyFrames().clear();
			t.getKeyFrames().add(kf);
			t.setOnFinished(e1 -> {
				registerPane.getChildren().remove(loginPane);
				scene.setRoot(loginPane);
			});
			t.play();

			rEmailField.clear();
			rPasswordField.clear();
			rPasswordField0.clear();
			rNameField.clear();
		});

		registerPane.getChildren().add(suVBox);
		registerPane.setStyle("-fx-background-color: rgb(30, 200, 100);");

		// main pane
		VBox mVBox = new VBox();
		StackPane mPane = new StackPane();
		HBox mHBox = new HBox();

		Label notImplLabel = new Label("다른 기능이 구현되지 않았습니다.");
		Label nameLabel = new Label();

		mVBox.getChildren().addAll(profileImage, nameLabel, profileButton);
		mPane.getChildren().add(mVBox);

		mHBox.getChildren().addAll(notImplLabel, mPane);

		mPane.setPrefSize(200, SCREEN_HEIGHT);
		notImplLabel.setPrefWidth(SCREEN_WIDTH - mPane.getPrefWidth());
		profileImage.setFitWidth(100);
		profileImage.setFitHeight(100);

		profileButton.setOnAction(e -> {
			mainPane.getChildren().add(profilePane);
			profilePane.translateYProperty().set(-SCREEN_HEIGHT);

			KeyValue kv = new KeyValue(profilePane.translateYProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
			KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

			t.getKeyFrames().clear();
			t.getKeyFrames().add(kf);
			t.setOnFinished(e1 -> {
				mainPane.getChildren().remove(profilePane);
				scene.setRoot(profilePane);
				emailLabel.requestFocus();
			});
			t.play();
		});

		mainPane.getChildren().add(mHBox);

		mVBox.setAlignment(Pos.CENTER);
		mHBox.setAlignment(Pos.CENTER);
		mVBox.setSpacing(10);

		notImplLabel.setTextAlignment(TextAlignment.CENTER);
		nameLabel.setTextAlignment(TextAlignment.CENTER);

		nameLabel.textProperty().bind(this.nameLabel.textProperty());
		nameLabel.setStyle("-fx-font-size: 18pt;");

		mPane.setStyle("-fx-background-color: rgb(50, 180, 10); -fx-border-color: black;");
		mainPane.setStyle("-fx-background-color: rgb(10, 200, 50);");

		// profile pane
		VBox pVBox = new VBox();
		HBox pHBox = new HBox(), pHBox0 = new HBox();

		Line line = new Line();
		ImageView pImage = new ImageView(profileImage.getImage());

		pHBox.getChildren().addAll(pImage, this.nameLabel);
		pHBox0.getChildren().addAll(backButton, logoutButton);
		pVBox.getChildren().addAll(pHBox, line, emailLabel, passwordLabel, dateLabel, pHBox0);

		backButton.setOnAction(e -> {
			profilePane.getChildren().add(mainPane);
			mainPane.translateYProperty().set(SCREEN_HEIGHT);

			KeyValue kv = new KeyValue(mainPane.translateYProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
			KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

			t.getKeyFrames().clear();
			t.getKeyFrames().add(kf);
			t.setOnFinished(e1 -> {
				profilePane.getChildren().remove(mainPane);
				scene.setRoot(mainPane);
				profileImage.requestFocus();
			});
			t.play();
		});
		logoutButton.setOnAction(e -> {
			logout();

			profilePane.getChildren().add(loginPane);
			loginPane.translateYProperty().set(-SCREEN_HEIGHT);
			loginPane.maxHeightProperty().set(0);

			KeyValue kv = new KeyValue(loginPane.maxHeightProperty(), SCREEN_HEIGHT, Interpolator.SPLINE(0, 0, 1, 1));
			KeyValue kv0 = new KeyValue(loginPane.translateYProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
			KeyFrame kf = new KeyFrame(Duration.seconds(1), kv, kv0);

			t.getKeyFrames().clear();
			t.getKeyFrames().add(kf);
			t.setOnFinished(e1 -> {
				profilePane.getChildren().remove(loginPane);
				scene.setRoot(loginPane);
			});
			t.play();
		});

		line.setStartX(0);
		line.setStartY(50);
		line.setEndX(SCREEN_WIDTH);
		line.setEndY(50);

		pHBox.setAlignment(Pos.CENTER);
		pHBox.setSpacing(10);
		pHBox0.setAlignment(Pos.CENTER);
		pHBox0.setSpacing(10);
		pVBox.setAlignment(Pos.CENTER);
		pVBox.setSpacing(30);

		pImage.setFitWidth(100);
		pImage.setFitHeight(100);

		emailLabel.setTextAlignment(TextAlignment.CENTER);
		passwordLabel.setTextAlignment(TextAlignment.CENTER);
		dateLabel.setTextAlignment(TextAlignment.CENTER);

		this.nameLabel.setStyle("-fx-font-size: 24pt;");

		profilePane.getChildren().add(pVBox);
		profilePane.setStyle("-fx-background-color: lightgray;");

		stage.setScene(scene);
		stage.setTitle("Login Form Version 2");
		stage.show();
	}

	private void loadAccounts() {
		try (BufferedReader reader = new BufferedReader(new FileReader(Account.ACCOUNTS_FILE))) {
			String line = "";
			String name = "", email = "", password = "", registerDate = "";
			String[] datas = new String[4];
			int count = 0;

			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "|");

				while (st.hasMoreTokens()) {
					datas[count] = st.nextToken();
					count++;
				}

				count = 0;

				name = datas[0];
				email = datas[1];
				password = datas[2];
				registerDate = datas[3];

				Account account = new Account(name, email, password, registerDate);
				accounts.add(account);
			}
		} catch (IOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("ERROR");
			error.setHeaderText("계정을 가져오는 중 오류가 발생했습니다.");
			error.setContentText("프로그램을 재시작 해주세요.\n오류 메시지: " + e.getMessage());
			error.show();
			e.printStackTrace();
		}
	}

	private boolean login() {
		String email = emailField.getText(), password = passwordField.getText();

		if (email.equals("") || password.equals("")) {
			showDialog("ERROR", "빈 곳을 모두 채워주세요.", "모두 입력해야 로그인 할 수 있습니다.", AlertType.ERROR);
			signInFailed = true;
			return false;
		}

		for (Account a : accounts) {
			if (a.getEmail().equals(email)) {
				if (a.getPassword().equals(password)) {
					nameLabel.setText(a.getName());
					emailLabel.setText("이메일\n" + a.getEmail());

					passwordLabel.setText("비밀번호\n");
					for (int i = 0; i < a.getPassword().length(); i++) {
						passwordLabel.setText(passwordLabel.getText() + "●");
					}

					String date = "";
					StringTokenizer st = new StringTokenizer(a.getRegisterDate(), "!");
					date += st.nextToken() + "\n";
					date += st.nextToken();

					dateLabel.setText("가입한 날짜\n" + date);

					return true;
				} else {
					showDialog("ERROR", "비밀번호가 일치하지 않습니다.", "다시 입력해 주세요.", AlertType.ERROR);
					signInFailed = true;
					break;
				}
			}
		}

		return false;
	}

	private boolean register() {
		String name = rNameField.getText(), email = rEmailField.getText(), password = rPasswordField.getText(), password0 = rPasswordField0.getText();

		if (name.equals("") || email.equals("") || password.equals("") || password0.equals("")) {
			showDialog("ERROR", "빈 곳을 모두 채워주세요.", "모두 입력해야 회원가입이 가능합니다.", AlertType.ERROR);
			return false;
		}

		Account account = new Account(name, email, password);

		if (!checkAccount(account, password0)) return false;

		account.saveAccount();
		accounts.add(account);

		return true;
	}

	private boolean checkAccount(Account account, String password0) {
		if (!account.getPassword().equals(password0)) {
			showDialog("ERROR", "비밀번호가 일치하지 않습니다.", "다시 입력해 주세요.", AlertType.ERROR);
			return false;
		}

		for (Account a : accounts) {
			if (a.getEmail().equals(account.getEmail())) {
				showDialog("ERROR", "이미 존재하는 이메일 입니다.", "다른 이메일을 입력하세요.", AlertType.ERROR);
				return false;
			}
		}

		if (!Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", account.getEmail())) {
			showDialog("ERROR", "이메일 형식이 아닙니다.", "올바른 이메일 형식으로 입력해 주세요.", AlertType.ERROR);
			return false;
		}

		return true;
	}

	private void logout() {
		emailLabel.setText("");
		passwordLabel.setText("");
		dateLabel.setText("");
		emailField.clear();
		passwordField.clear();
	}

	private void showDialog(String title, String header, String content, AlertType type) {
		Alert dialog = new Alert(type);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);
		dialog.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

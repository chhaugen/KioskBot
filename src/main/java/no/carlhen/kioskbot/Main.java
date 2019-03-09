package no.carlhen.kioskbot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import no.carlhen.kioskbot.view.MainScene;
import no.carlhen.kioskbot.discord.Controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

	public static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {

		//List<Item> testitem = new ArrayList<>();
		//testitem.add(new Item("ts", "testhd", "Iskrem", 34.0));
		//Menu.AddNewMenu(new Menu("testmeny", "jallaballa", testitem));

		logger.info("Starting Javacord instance");
		Controller.init("NTUwNzYxMTk1NTU3MDkzMzg2.D2BIfw.EK8g__VvfXgSweTIPDnrHiKlmYc");

		logger.info("Starting Application Window");
		launch(args);
    }

	@Override
    public void start(Stage primaryStage) {

		primaryStage.setTitle("KioskBot");
		primaryStage.setOnCloseRequest((e) -> {
			Platform.exit();
			System.exit(0);
		});

		primaryStage.setScene(MainScene.scene());
		primaryStage.setMinWidth(1000);
		primaryStage.setMinHeight(300);

		primaryStage.show();
	}
}

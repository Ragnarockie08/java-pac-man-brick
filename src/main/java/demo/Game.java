package demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Game extends Application {

    private static final int BLOCK_SIZE = 40;
    private static final int WIDTH = 15 * BLOCK_SIZE;
    private static final int HEIGHT = 15 * BLOCK_SIZE;

    private static Pane root;

    private Rectangle player = new Rectangle(20, 20);

    public void start(Stage primaryStage) throws IOException {

        root = FXMLLoader.load(getClass().getResource("/test.fxml"));

        Scene scene = new Scene(root);
        Pane pane = (Pane) root.lookup("#scene");
        pane.getChildren().add(player);
        scene.setOnKeyPressed(event -> {


            switch(event.getCode()){

                case W:
                    if (!(player.getTranslateY() <= 0)){
                        player.setTranslateY(player.getTranslateY() - 40);
                    }
                    break;
                case S:
                    if (!(player.getTranslateY() >= WIDTH)) {
                        player.setTranslateY(player.getTranslateY() + 40);
                    }
                    break;
                case A:
                    if (!(player.getTranslateX() <= 0)) {
                        player.setTranslateX(player.getTranslateX() - 40);
                    }
                    break;
                case D:
                    if (!(player.getTranslateX() >= HEIGHT)) {
                        player.setTranslateX(player.getTranslateX() + 40);
                    }
                    break;
            }
        });
        primaryStage.setTitle("Tanks");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);

    }
}

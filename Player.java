import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.net.Socket;

public class Player extends Application {
    Stage stage;
    static Stage window;
    private Pane root;

    private Socket socket;
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    static TextField tf;
    static int point;
    static Scene Button;
    Boolean flag = false;
    int current = 0;

    public static Button kahootButton(String bntColor){
        Button btn = new Button();
        btn.setMinWidth(295);
        btn.setMinHeight(295);
        btn.setStyle("-fx-background-color: " + bntColor );

        btn.setTextFill(Color.WHITE);
        btn.setWrapText(true);
        btn.setPadding(new Insets(10));

        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15);
        btn.setFont(font);
        return btn;
    }
    public StackPane nicknamePane() throws IOException {
        StackPane stackPane = new StackPane();
        TextField tf = new TextField();
        tf.setPromptText("Enter username");
        tf.setMaxWidth(200);
        tf.setMinHeight(40);
        tf.setAlignment(Pos.CENTER);
        Button btn = new Button("Enter");
        btn.setMaxWidth(200);
        btn.setMinHeight(40);
        btn.setStyle("-fx-background-color:#333333");
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));
        VBox vBox = new VBox(10);
        vBox.setMaxWidth(300);
        vBox.setMaxHeight(300);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(tf, btn);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #fc9403");

        btn.setOnAction(e -> {
            try {
                toServer.writeUTF(tf.getText());
                if(fromServer.readUTF().equalsIgnoreCase("start")) {
                    window.setScene(Button);
                    window.setTitle(tf.getText());
                    flag = false;
                    window.show();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return stackPane;
    }
    public StackPane pinPane() {
        StackPane stackPane = new StackPane();
        TextField tf = new TextField();
        tf.setPromptText("Game PIN");
        tf.setMaxWidth(200);
        tf.setMinHeight(40);
        tf.setAlignment(Pos.CENTER);
        Button btn = new Button("Enter");
        btn.setMaxWidth(200);
        btn.setMinHeight(40);
        btn.setStyle("-fx-background-color:#333333");
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));
        VBox vBox = new VBox(10);
        vBox.setMaxWidth(300);
        vBox.setMaxHeight(300);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(tf, btn);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #fc9403");

        btn.setOnAction(e -> {
            try {
                toServer.writeInt(Integer.parseInt(tf.getText()));
                String status = fromServer.readUTF();
                if (status.equals("Success!")) {
                    window.setScene(new Scene(nicknamePane(), 600,600));
                    window.setTitle("Enter Nickname");
                }
                System.out.println(status);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        return stackPane;
    }
    private void connectToServer() throws IOException {
        socket = new Socket("localhost", 2022);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;

        connectToServer();
        VBox vBox1 = new VBox(10);
        Button btnRed = kahootButton("red");
        Button btnBlue = kahootButton("blue");
        vBox1.getChildren().addAll(btnRed, btnBlue);
        VBox vBox2 = new VBox(10);
        Button btnOrange = kahootButton("orange");
        Button btnGreen = kahootButton("green");
        vBox2.getChildren().addAll(btnOrange, btnGreen);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vBox1, vBox2);
        StackPane pane  = new StackPane();
        pane.getChildren().add(hBox);

        btnRed.setOnAction(event -> {
            current++;
            try {
                toServer.writeUTF( "red");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                Correct();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        btnBlue.setOnAction(event -> {
            try {
                toServer.writeUTF( "green");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                InCorrect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        btnOrange.setOnAction(event -> {
            try {
                toServer.writeUTF("green");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                InCorrect();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        btnGreen.setOnAction(event -> {
            try {
                toServer.writeUTF("green");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                InCorrect();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        Button = new Scene(pane,600,600);

        root = pinPane();
        window.setScene(new Scene(pinPane(),600,600));
        window.setTitle("Enter Pin");
        window.show();
        root.requestFocus();

    }
    public void  Correct() throws IOException {
        stage = new Stage();
        BorderPane stackPane = new BorderPane();
        Label label = new Label("CORRECT ANSWER");
        label.setFont(Font.font("Times New Roman",FontWeight.BLACK,FontPosture.REGULAR,22));
        stackPane.setStyle("-fx-background-color: #0ffc03");
        stackPane.setCenter(label);

        stage.setScene(new Scene(stackPane,600,600));
        stage.setTitle("Correct");
        window.close();
        stage.showAndWait();
        show();



    }
    public void  InCorrect() throws IOException {
        stage = new Stage();
        StackPane stackPane = new StackPane();
        Label label = new Label("WRONG ANSWER");
        label.setFont(Font.font("Times New Roman",FontWeight.BLACK,FontPosture.REGULAR,22));
        stackPane.setStyle("-fx-background-color: #fc2c03");
        stackPane.getChildren().addAll(label);
        stage.setScene(new Scene(stackPane,600,600));
        window.close();
        stage.showAndWait();
        show();

    }
    public StackPane LAST(){
        StackPane pane  = new StackPane();
        Label label = new Label("POINT");
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.REGULAR,22));
        pane.getChildren().add(label);
        return pane;
    }
    public void show(){
            current++;
            System.out.println("show");

            window.setScene(Button);
            window.setTitle("SHOW");
            window.show();
    }
}

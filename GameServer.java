import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameServer extends Application {
    static DataInputStream fromClient;
    static DataOutputStream toClient;
    static Stage StageMain = new Stage();
    static int sec = 30;
    static int point = 0;
    static int minute = 0;
    static int current = 0;
    static Scene[] SceneOfQuestions;
    static ArrayList<Question> questions;
    static String[] UsersAnswer;
    static int numberOfQuestions;
    static int secondPassed = 60;
    static Timer timer;
    static TimerTask task;
    static Stage stageForKahoot;
    File mediaFile;
    Media music;
    static MediaPlayer player;


    public static String genPin(){
        Random rnd = new Random();
        int number = rnd. nextInt(999999);
        return String.format("%06d", number);
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage = FileChoose();
        primaryStage.show();
    }

    public Stage FileChoose() {
        Stage Mainstage = new Stage();
        StackPane stackPane = new StackPane();
        Button buttonChoose = new Button("Choose File");
        stackPane.getChildren().add(buttonChoose);
        Image img = new Image("C:\\Users\\user\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo3\\background.jpg");
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        stackPane.setBackground(bGround);

        buttonChoose.setOnAction(actionEvent -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(StageMain);
            Quiz quiz = null;
            try {
                quiz = Quiz.LoadFromFile(file.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Mainstage.close();
            try {
                begin(quiz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Scene scene = new Scene(stackPane, 450, 250);
        Mainstage.setScene(scene);
        return Mainstage;
    }


    public void begin(Quiz quiz){
        questions = quiz.getQuestions();
        numberOfQuestions = questions.size();
        UsersAnswer = new String[numberOfQuestions];
        SceneOfQuestions = new Scene[numberOfQuestions];

        stageForKahoot = new Stage();
        StackPane firstPane = new StackPane();
        firstPane.setStyle("-fx-background-color: #fc9403");

        BorderPane borderPane = new BorderPane();
        Label labelForPin = new Label("Join at www.kahoot.it or with the kahoot app!" + "\n" + "Game Pin" + "\n" + genPin());
        labelForPin.setTextFill(Color.BLACK);
        labelForPin.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.REGULAR,18));
        labelForPin.setAlignment(Pos.CENTER);
        labelForPin.setMinWidth(600);
        labelForPin.setStyle("-fx-background-color: white");

        Button start = new Button("start");
        start.setTextFill(Color.BLACK);
        start.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.REGULAR,18));
        borderPane.setTop(labelForPin);
        StackPane paneForStart = new StackPane();
        paneForStart.getChildren().add(start);
        borderPane.setRight(paneForStart);
        firstPane.getChildren().addAll(borderPane);

        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(8888);
                int clientNo = 1;
                while (true) {
                    try {
                        System.out.println("Waiting for incomes");
                        Socket socket = server.accept();
                        System.out.println(clientNo + " Client is Connected!");
                        new Thread(() -> {
                            try {
                                fromClient = new DataInputStream(socket.getInputStream());
                                toClient = new DataOutputStream(socket.getOutputStream());
                                while (true) {
                                    int clientPin = fromClient.readInt();
                                    if (String.valueOf(clientPin).equalsIgnoreCase(genPin())) {
                                        toClient.writeUTF("Wrong PIN!");
                                    } else {
                                        toClient.writeUTF("Success!");
                                    }
                                    String nickname = fromClient.readUTF();
                                    Label nLbl = new Label(nickname);
                                    nLbl.setTextFill(Color.WHITE);
                                    nLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));
                                    nLbl.setWrapText(true);

                                    Platform.runLater(() -> {
                                                borderPane.setCenter(nLbl);
                                            }
                                    );

                                
                                }

                            } catch (IOException e) {

                            }

                        }).start();
                        clientNo++;
//        }
                    } catch (IOException e) {

                    }
                }
            } catch (IOException e) {

            }

        }).start();


        stageForKahoot.setScene(new Scene(firstPane,600,600));
        stageForKahoot.show();

        start.setOnAction(event -> {
            stageForKahoot.close();
            try {
                toClient.writeUTF("start");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < numberOfQuestions;i++){
                Question question = questions.get(i);
                if(question instanceof FillIn){
                    try {
                        SceneOfQuestions[i] = fillInQuestion((FillIn) question);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (question instanceof Test) {
                    try {
                        SceneOfQuestions[i] = testQuestion((Test) question);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            mediaFile = new File("C:\\Users\\user\\IdeaProjects\\demo3\\src\\resources\\kahoot_music.mp3");
            music = new Media(mediaFile.toURI().toString() );
            player = new MediaPlayer(music);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            StageMain.setScene(SceneOfQuestions[current]);
            player.play();
            StageMain.show();
        });


    }

    public static Scene testQuestion(Test question) throws IOException {
        BorderPane mainPane = new BorderPane();
        Label tLabel = new Label(question.getDescription());
        tLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        tLabel.setMinWidth(400);
        tLabel.setAlignment(Pos.CENTER);
        tLabel.setWrapText(true);

        Button btNext = new Button(">>");


        Image image = new Image("C:\\Users\\user\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo3\\logok.png");
        ImageView iv = new ImageView(image);
        iv.setFitHeight(150);
        iv.setFitWidth(200);
        VBox holder = new VBox(10);
        holder.setAlignment(Pos.CENTER);
        holder.setMaxHeight(500);
        holder.setMaxWidth(500);
        holder.getChildren().addAll(iv);

        Font font = Font.font("Times New Roman", FontWeight.BOLD,
                FontPosture.REGULAR, 15);

        HBox Options = new HBox(3);
        Options.setMaxWidth(500);
        Options.setMaxHeight(100);
        Options.setAlignment(Pos.CENTER);
        Button redButton = new Button(question.getOptionsAt(0));
        redButton.setStyle("-fx-background-color: red");
        redButton.setMinWidth(200);
        redButton.setMinHeight(100);
        redButton.setTextFill(Color.WHITE);
        redButton.setFont(font);
        Button blueButton = new Button(question.getOptionsAt(1));
        blueButton.setStyle("-fx-background-color: blue");
        blueButton.setMinWidth(200);
        blueButton.setMinHeight(100);
        blueButton.setTextFill(Color.WHITE);
        blueButton.setFont(font);
        Button orangeButton = new Button(question.getOptionsAt(2));
        orangeButton.setStyle("-fx-background-color: orange");
        orangeButton.setMinWidth(200);
        orangeButton.setMinHeight(100);
        orangeButton.setTextFill(Color.WHITE);
        orangeButton.setFont(font);
        Button greenButton = new Button(question.getOptionsAt(3));
        greenButton.setStyle("-fx-background-color: green");
        greenButton.setMinWidth(200);
        greenButton.setMinHeight(100);
        greenButton.setTextFill(Color.WHITE);
        greenButton.setFont(font);
        VBox vBox1 = new VBox(3);
        vBox1.getChildren().addAll(redButton,blueButton);
        VBox vBox2 = new VBox(3);
        vBox2.getChildren().addAll(orangeButton,greenButton);
        Options.getChildren().addAll(vBox1,vBox2);


        btNext.setOnAction(event -> {
            current++;

            if(current < questions.size()) {
                try {
                    toClient.writeUTF("next");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StageMain.setScene(SceneOfQuestions[current]);
                StageMain.show();
            }
            else if(current <= questions.size() ) {
                try {
                    StageMain.setScene(ResultScene());
                    StageMain.show();
                    toClient.writeUTF("last");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mainPane.setBottom(Options);
        mainPane.setLeft(Timer());
        mainPane.setTop(tLabel);
        mainPane.setCenter(holder);

        mainPane.setRight(new StackPane(btNext));
        return new Scene(mainPane, 450, 400);
    }

    public Scene fillInQuestion(FillIn question) throws IOException {
        BorderPane FillInPane = new BorderPane();
        Label fLabel = new Label(question.toString());
        fLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        fLabel.setAlignment(Pos.CENTER);
        fLabel.setWrapText(true);
        fLabel.setMinWidth(400);

        Button btNext = new Button(">>");


        TextField Answer = new TextField();


        Image image = new Image("C:\\Users\\user\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo3\\fillin.png");
        ImageView iv = new ImageView(image);
        iv.setFitHeight(200);
        iv.setFitWidth(350);
        VBox holder = new VBox(10);
        holder.setAlignment(Pos.CENTER);
        holder.setMaxHeight(500);
        holder.setMaxWidth(500);
        holder.getChildren().addAll(iv,Answer);


        FillInPane.setCenter(holder);
        FillInPane.setTop(fLabel);
        FillInPane.setLeft(Timer());

        FillInPane.setRight(new StackPane(btNext));


        return new Scene(FillInPane, 450, 400);
    }


    public static Text ret(){
        Text text = new Text("00");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            try {
                change(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.play();
        return text;
    }
    public static void change(Text text) throws IOException {
        sec--;
        if(sec == 0) {
            ret();
        }
        text.setText((((minute /10) == 0) ? "0" : "") + minute + ":"
                + (((sec /10) == 0) ? "0" : "") + sec);
    }
    public static Scene ResultScene() throws IOException {

        player.stop();

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #fc9403");

        Label label = new Label("Result Pane ");
        label.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.REGULAR,25));
        label.setMinWidth(50);
        label.setMinWidth(50);
        label.setTextFill(Color.BLACK);
        label.setAlignment(Pos.CENTER);

        Label labelForResult = new Label();

        labelForResult.setText("nicnkame" + " ------------> " + point);
        labelForResult.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.REGULAR,20));
        label.setTextFill(Color.BLACK);
        labelForResult.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(labelForResult);

        Scene ResultScene = new Scene(borderPane,500,500);

        return ResultScene;
    }
    public static StackPane Timer() throws IOException {
        StackPane clock = new StackPane();
        Circle circle = new Circle();
        circle.setRadius(50);
        circle.setFill(Color.AQUAMARINE);
        Text timer = new Text("30");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            try {
                change(timer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.play();
        timer.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));

        clock.getChildren().addAll(circle,timer);
        clock.setPadding(new Insets(10));

        return clock;
    }
    


}



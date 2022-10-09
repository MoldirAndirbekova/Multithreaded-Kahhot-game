import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class QuizMaker extends Application {
    static Stage StageMain = new Stage();
    static int sec = 0;
    static int point = 0;
    static int minute = 0;
    static int current = 0;
    static Scene[] SceneOfQuestions;
    static ArrayList<Question> questions;
    static String[] UsersAnswer;
    static int numberOfQuestions;


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
            Stage stage = new Stage();
            Pane pane = new Pane();
            stage.setScene(new Scene(pane, 500, 500));
            File file = fc.showOpenDialog(stage);
            Quiz quiz = null;
            try {
                quiz = Quiz.LoadFromFile(file.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Mainstage.close();
            begin(quiz);
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

        for(int i = 0; i < numberOfQuestions;i++){
            Question question = questions.get(i);
            if(question instanceof FillIn){
                SceneOfQuestions[i] = fillInQuestion((FillIn) question, i);
            }
            else if (question instanceof Test) {
                SceneOfQuestions[i] = testQuestion((Test) question,i);
            }
        }
        File mediaFile = new File("C:\\Users\\user\\IdeaProjects\\demo3\\src\\resources\\kahoot_music.mp3");
        Media music = new Media(mediaFile.toURI().toString() );
        MediaPlayer player = new MediaPlayer(music);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        StageMain.setScene(SceneOfQuestions[current]);
        player.play();
        StageMain.show();
    }

    public static Scene testQuestion(Test question, int number){
        BorderPane mainPane = new BorderPane();
        Label tLabel = new Label(question.getDescription());
        tLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        tLabel.setMinWidth(400);
        tLabel.setAlignment(Pos.CENTER);
        tLabel.setWrapText(true);

        Button btNext = new Button(">>");
        Button btPrevious = new Button("<<");

        Image image = new Image("C:\\Users\\user\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo3\\logok.png");
        ImageView iv = new ImageView(image);
        iv.setFitHeight(150);
        iv.setFitWidth(200);
        VBox holder = new VBox(10);
        holder.setAlignment(Pos.CENTER);
        holder.setMaxHeight(500);
        holder.setMaxWidth(500);
        holder.getChildren().addAll(iv,ret());

        Font font = Font.font("Times New Roman", FontWeight.BOLD,
                FontPosture.REGULAR, 15);

        HBox Options = new HBox(3);
        Options.setMaxWidth(500);
        Options.setMaxHeight(100);
        Options.setAlignment(Pos.CENTER);
        RadioButton redButton = new RadioButton(question.getOptionsAt(0));
        redButton.setStyle("-fx-background-color: red");
        redButton.setMinWidth(200);
        redButton.setMinHeight(100);
        redButton.setTextFill(Color.WHITE);
        redButton.setFont(font);
        RadioButton blueButton = new RadioButton(question.getOptionsAt(1));
        blueButton.setStyle("-fx-background-color: blue");
        blueButton.setMinWidth(200);
        blueButton.setMinHeight(100);
        blueButton.setTextFill(Color.WHITE);
        blueButton.setFont(font);
        RadioButton orangeButton = new RadioButton(question.getOptionsAt(2));
        orangeButton.setStyle("-fx-background-color: orange");
        orangeButton.setMinWidth(200);
        orangeButton.setMinHeight(100);
        orangeButton.setTextFill(Color.WHITE);
        orangeButton.setFont(font);
        RadioButton greenButton = new RadioButton(question.getOptionsAt(3));
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


        redButton.setOnAction(e -> {
            if (redButton.isSelected() == true){
                UsersAnswer[current] = redButton.getText();
            }
        });

        blueButton.setOnAction(e -> {
            if (blueButton.isSelected()== true){
                UsersAnswer[current] = blueButton.getText();
            }
        });

        orangeButton.setOnAction(e -> {
            if (orangeButton.isSelected()== true){
                UsersAnswer[current] = orangeButton.getText();
            }
        });

        greenButton.setOnAction(e -> {
            if (greenButton.isSelected()== true){
                UsersAnswer[current] = greenButton.getText();
            }
        });

        ToggleGroup group = new ToggleGroup();
        redButton.setToggleGroup(group);
        greenButton.setToggleGroup(group);
        blueButton.setToggleGroup(group);
        orangeButton.setToggleGroup(group);

        btNext.setOnAction(event -> {
            current++;
            StageMain.setScene(SceneOfQuestions[current]);

        });

        btPrevious.setOnAction(event -> {
            current--;
            StageMain.setScene(SceneOfQuestions[current]);

        });
        mainPane.setBottom(Options);
        mainPane.setTop(tLabel);
        mainPane.setCenter(holder);
        mainPane.setLeft(new StackPane(btPrevious));
        mainPane.setRight(new StackPane(btNext));
        Scene scene =  new Scene(mainPane, 450, 400);
        return scene;
    }
    public static void check(){
        for (int i = 0; i < numberOfQuestions; i++) {
            Question Currentquestion = questions.get(i);
            if (Currentquestion instanceof FillIn) {
                if (UsersAnswer[i] != null) {
                    if (UsersAnswer[i].equalsIgnoreCase(Currentquestion.getAnswer())) {
                        point++;
                    }
                }
            } else if (Currentquestion instanceof Test) {
                if (UsersAnswer[i] != null){
                    if (UsersAnswer[i].equals(Currentquestion.getAnswer())){
                        point++;
                    }
                }
            }
        }
    }
    public Scene fillInQuestion(FillIn question, int number){
        BorderPane FillInPane = new BorderPane();
        Label fLabel = new Label(question.toString());
        fLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        fLabel.setAlignment(Pos.CENTER);
        fLabel.setWrapText(true);
        fLabel.setMinWidth(400);

        Button btNext = new Button(">>");
        Button btPrevious = new Button("<<");

        TextField Answer = new TextField();

        Button button = new Button("Enter");
        button.setMinWidth(40);
        button.setMinHeight(40);
        button.setAlignment(Pos.BOTTOM_CENTER);

        Image image = new Image("C:\\Users\\user\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo3\\fillin.png");
        ImageView iv = new ImageView(image);
        iv.setFitHeight(200);
        iv.setFitWidth(350);
        VBox holder = new VBox(10);
        holder.setAlignment(Pos.CENTER);
        holder.setMaxHeight(500);
        holder.setMaxWidth(500);
        holder.getChildren().addAll(iv,Answer,ret());

        button.setOnAction(e -> {
            UsersAnswer[current] = Answer.getText();
        });

        btNext.setOnAction(actionEvent -> {
            current++;
            result();
        });

        btPrevious.setOnAction(actionEvent -> {
            current--;
            StageMain.setScene(SceneOfQuestions[current]);
        });

        FillInPane.setCenter(holder);
        FillInPane.setTop(fLabel);
        FillInPane.setLeft(new StackPane(btPrevious));
        FillInPane.setRight(new StackPane(btNext));
        FillInPane.setBottom(button);

        Scene scene = new Scene(FillInPane, 450, 400);
        return scene;
    }
    public static void result(){
        Stage Result = new Stage();
        BorderPane mainPane = new BorderPane();
        check();
        Font font = Font.font("Times New Roman", FontWeight.BOLD,
                FontPosture.REGULAR, 20);
        Font fontL = Font.font("Times New Roman", FontWeight.BOLD,
                FontPosture.REGULAR, 20);
        int percent = (point*100)/numberOfQuestions;
        Label labelOfResult = new Label("                            Your Result is: " + point + "/"
                + numberOfQuestions  + "   "
                + "\n" + "                                           " + percent + "%");
        labelOfResult.setAlignment(Pos.CENTER);
        labelOfResult.setFont(fontL);
        labelOfResult.setWrapText(true);
        Button show = new Button("Show answers");
        show.setMinWidth(200);
        show.setMinHeight(40);
        Button finish = new Button("Close Test");
        finish.setMinWidth(200);
        finish.setMinHeight(40);

        show.setTextFill(Color.WHITE);
        finish.setTextFill(Color.WHITE);
        show.setFont(font);
        finish.setFont(font);
        show.setStyle("-fx-background-color: red");
        finish.setStyle("-fx-background-color: blue");
        finish.setOnMouseEntered(mouseEvent -> Result.getScene().setCursor(Cursor.HAND));
        finish.setOnMouseExited(mouseEvent -> Result.getScene().setCursor(Cursor.DEFAULT));


        finish.setOnAction(e ->{
            StageMain.close();
            Result.close();
        });

        VBox buttons = new VBox(3);
        buttons.setAlignment(Pos.CENTER);
        buttons.setMaxHeight(50);
        buttons.setMaxWidth(250);

        buttons.getChildren().addAll(show,finish);

        Image image = new Image("C:\\Users\\user\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo3\\result.png");
        ImageView iv = new ImageView(image);
        iv.setFitHeight(250);
        iv.setFitWidth(350);
        VBox holder = new VBox(10);
        holder.setAlignment(Pos.CENTER);
        holder.setMaxHeight(500);
        holder.setMaxWidth(350);
        holder.getChildren().add(iv);

        mainPane.setCenter(buttons);
        mainPane.setBottom(holder);
        mainPane.setTop(labelOfResult);

        Result.setScene(new Scene(mainPane,450,400));
        Result.show();
    }
    public static Text ret(){
        Text text = new Text("00:00");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                change(text);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.play();
        return text;
    }
    public static void change(Text text) {
        sec++;
        if(sec == 60) {
            minute++;
            sec = 0;
        }
        text.setText((((minute /10) == 0) ? "0" : "") + minute + ":"
                + (((sec /10) == 0) ? "0" : "") + sec);
    }

    }






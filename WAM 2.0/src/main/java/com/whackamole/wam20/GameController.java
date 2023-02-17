package com.whackamole.wam20;

import com.whackamole.wam20.models.Game;
import com.whackamole.wam20.models.Player;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameController implements Initializable {

    @FXML
    Text streakText;

    @FXML
    Text pointsText;

    @FXML
    Label doublePoints;

    @FXML
    Text pointsLabel;

    @FXML
    Text streakLabel;

    @FXML
    ImageView scoreboard;

    @FXML
    ImageView endScoreboard;

    @FXML
    Text playerName;

    @FXML
    Text playerPoints;

    @FXML
    Text playerStreak;

    @FXML
    Button movesBtn;

    @FXML
    Button documentationBtn;

    @FXML
    Button saveGameBtn;

    @FXML
    Button loadGameBtn;

    @FXML
    Rectangle mole1;

    @FXML
    Rectangle mole2;

    @FXML
    Rectangle mole3;

    @FXML
    Rectangle mole4;

    @FXML
    Rectangle mole5;

    @FXML
    Rectangle mole6;

    @FXML
    Rectangle mole7;

    @FXML
    Rectangle mole8;

    @FXML
    Rectangle mole9;

    @FXML
    Rectangle mole10;

    @FXML
    Rectangle mole11;

    List<Rectangle> moles;

    Player player;

    Game game;

    private int highestStreak = 0;

    private int activeMole;

    private int points;

    private int streak;

    private final int REGULAR_POINTS = 100;

    private final int STREAK_POINTS = 200;

    Timeline gameLoop;

    Timeline countdown;

    List<String> moves;


    private static final String CLASS_EXTENSION = ".class";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideEndScoreboard();

        initList();

        moves = new ArrayList<>();

        InputStream moleRes = getClass().getResourceAsStream("images/mole.png");
        Image moleImage = new Image(moleRes);

        initBoard(moleImage);

        initPlayer();

        initGame();

        initCountdown();


    }

    public void printMoves(){

        System.out.print("\n");

        for (String move : moves){

            System.out.print(move + "\n");

        }
        System.out.print("------------------");

    }

    private void hideEndScoreboard() {

        endScoreboard.setVisible(false);
        playerName.setVisible(false);
        playerPoints.setVisible(false);
        playerStreak.setVisible(false);
        movesBtn.setVisible(false);
        documentationBtn.setVisible(false);
        saveGameBtn.setVisible(false);
        loadGameBtn.setVisible(false);

    }

    private void initCountdown() {

        int gameDurationInMs = 30000;
        countdown = new Timeline(
                new KeyFrame(Duration.millis(gameDurationInMs))
        );

        countdown.playFromStart();

        countdown.setOnFinished(actionEvent -> gameOver());

    }

    private void gameOver() {

        gameLoop.stop();
        countdown.stop();

        player.setPoints(points);
        player.setHighestStreak(highestStreak);
        player.setMoves(moves);


        removeScoreboard();

        initGameStats();

    }

    private void removeScoreboard() {

        FadeTransition fadeOutSc = new FadeTransition(Duration.millis(600), scoreboard);
        fadeOutSc.setToValue(0);
        fadeOutSc.play();

        FadeTransition fadeOutSl = new FadeTransition(Duration.millis(600), streakLabel);
        fadeOutSl.setToValue(0);
        fadeOutSl.play();

        FadeTransition fadeOutPl = new FadeTransition(Duration.millis(600), pointsLabel);
        fadeOutPl.setToValue(0);
        fadeOutPl.play();

        FadeTransition fadeOutPt = new FadeTransition(Duration.millis(600), pointsText);
        fadeOutPt.setToValue(0);
        fadeOutPt.play();

        FadeTransition fadeOutSt = new FadeTransition(Duration.millis(600), streakText);
        fadeOutSt.setToValue(0);
        fadeOutSt.play();


    }

    private void initGameStats() {

        endScoreboard.setVisible(true);
        endScoreboard.setOpacity(0);
        FadeTransition fadeInEs = new FadeTransition(Duration.millis(600), endScoreboard);
        fadeInEs.setToValue(1.0);
        fadeInEs.play();


        playerName.setVisible(true);
        playerName.setText(player.name + "'s stats");

        playerPoints.setVisible(true);
        playerPoints.setText("Points: " + player.points);

        playerStreak.setVisible(true);
        playerStreak.setText("Highest streak: " + player.highestStreak);

        movesBtn.setVisible(true);

        documentationBtn.setVisible(true);

        saveGameBtn.setVisible(true);

        loadGameBtn.setVisible(true);

    }

    private void initBoard(Image moleImage) {

        for (Rectangle mole : moles){

            mole.setFill(new ImagePattern(moleImage));
            mole.setStroke(Color.TRANSPARENT);
            mole.setVisible(false);

        }


    }


    private void initGame() {

        game = new Game(player);

        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(600), e -> initMoleJump()),
                new KeyFrame(Duration.millis(1200), e -> removeMole(activeMole))
        );

        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.playFromStart();

    }

    private void removeMole(int activeMole) {

        if (moles.get(activeMole).isVisible()){

            moves.add("Mole " + activeMole + " miss");

            moles.get(activeMole).setVisible(false);
            streak = 0;
            streakText.setText(String.valueOf(streak));

            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), doublePoints);
            fadeIn.setToValue(0);
            fadeIn.play();

        }

    }

    private void initMoleJump() {

        Random random = new Random();
        activeMole = random.nextInt(moles.size());

        if (streak == 4){
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), doublePoints);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }

        if (streak > highestStreak){

            highestStreak = streak;

        }

        moles.get(activeMole).setVisible(true);
        moles.get(activeMole).setOnMouseClicked(mouseEvent -> {

            moves.add("Mole " + activeMole + " hit");

            streak++;
            streakText.setText(String.valueOf(streak));

            moles.get(activeMole).setVisible(false);

            if (streak > 4){

                points += STREAK_POINTS;
                pointsText.setText(String.valueOf(points));

            }
            else {

                points += REGULAR_POINTS;
                pointsText.setText(String.valueOf(points));
            }

        });

    }

    private void initPlayer() {

        player = new Player(HelloController.getPlayerName());

    }

    private void initList() {

        moles = new ArrayList<>();
        moles.add(mole1);
        moles.add(mole2);
        moles.add(mole3);
        moles.add(mole4);
        moles.add(mole5);
        moles.add(mole6);
        moles.add(mole7);
        moles.add(mole8);
        moles.add(mole9);
        moles.add(mole10);
        moles.add(mole11);

    }

    public void generateDocumentation() {

        File documentationFile = new File("documentation.html");

        try {

            FileWriter writer = new FileWriter(documentationFile);

            writer.write("<!DOCTYPE html>");
            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Project documentation</title>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1>Project documentation</h1>");
            writer.write("<p>Class list:</p>");

            List<Path> paths = Files.walk(Paths.get("."))
                    .filter(path -> path.getFileName().toString().endsWith(CLASS_EXTENSION))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                //System.out.println("Path: " + path);
                String[] tokens = path.toString().split(Pattern.quote(System.getProperty("file.separator")));

                Boolean startBuildingPath = false;

                StringBuilder sb = new StringBuilder();

                for (String token : tokens) {
                    if ("classes".equals(token)) {
                        startBuildingPath = true;
                        continue;
                    }

                    if (startBuildingPath) {

                        if (token.endsWith(CLASS_EXTENSION)) {
                            sb.append(token, 0, token.indexOf("."));
                        } else {
                            sb.append(token);
                            sb.append(".");
                        }
                    } else {
                        continue;
                    }
                }

                if ("module-info".equals(sb.toString())) {
                    continue;
                }

                System.out.println("Fully qualified name: " + sb);

                try {
                    Class<?> clazz = Class.forName(sb.toString());

                    writer.write("<h2>" + Modifier.toString(clazz.getModifiers()) + " " + clazz.getName() + "</h2>");

                    StringBuilder classFieldString = new StringBuilder();

                    for (Field classField : clazz.getDeclaredFields()) {
                        Annotation[] annotations = classField.getAnnotations();
                        if (annotations.length != 0) {
                            for (Annotation a : annotations) {
                                classFieldString.append(a.toString());
                                classFieldString.append("<br />");
                            }
                        }
                        classFieldString.append(Modifier.toString(classField.getModifiers()));
                        classFieldString.append(" ");
                        classFieldString.append(classField.getType().getSimpleName());
                        classFieldString.append(" ");
                        classFieldString.append(classField.getName());
                        classFieldString.append(" ");
                        classFieldString.append("<br /><br />");
                    }

                    writer.write("<h3>Fields</h3>");

                    writer.write("<h4>" + classFieldString + "</h4>");

                    Constructor[] constructors = clazz.getConstructors();

                    writer.write("<h3>Constructors:</h3>");

                    for (Constructor c : constructors) {

                        String constructorParams = generateDocumentation(c);

                        writer.write("<h4>Constructor:" + Modifier.toString(c.getModifiers()) + " " + c.getName()
                                + "(" + constructorParams + ")" + "</h4>");
                    }

                    Method[] methods = clazz.getMethods();

                    writer.write("<h3>Methods:</h3>");

                    for (Method m : methods) {

                        String methodsParams = generateDocumentation(m);

                        StringBuilder exceptionsBuilder = new StringBuilder();

                        for (int i = 0; i < m.getExceptionTypes().length; i++) {
                            if (exceptionsBuilder.isEmpty()) {
                                exceptionsBuilder.append(" throws ");
                            }

                            Class exceptionClass = m.getExceptionTypes()[i];
                            exceptionsBuilder.append(exceptionClass.getSimpleName());

                            if (i < m.getExceptionTypes().length - 1) {
                                exceptionsBuilder.append(", ");
                            }
                        }

                        writer.write("<h4>Method:" + Modifier.toString(m.getModifiers())
                                + " " + m.getReturnType().getSimpleName()
                                + " " + m.getName() + "(" + methodsParams + ")"
                                + " " + exceptionsBuilder
                                + "</h4>");
                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            writer.write("</body>");
            writer.write("</html>");
            writer.close();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while generating documentation!");
            alert.setHeaderText("Cannot find the files");
            alert.setContentText("The class files cannot be accessed.");

            alert.showAndWait();
        }
    }

    private <T extends Executable> String generateDocumentation(T executable) {
        Parameter[] params = executable.getParameters();

        StringBuilder methodsParams = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            String modifierString = Modifier.toString(params[i].getModifiers());

            if (!modifierString.isEmpty()) {
                methodsParams.append(modifierString);
                methodsParams.append(" ");
            }
            methodsParams.append(params[i].getType().getSimpleName());
            methodsParams.append(" ");
            methodsParams.append(params[i].getName());

            if (i < (params.length - 1)) {
                methodsParams.append(", ");
            }
        }

        return methodsParams.toString();
    }

    public void saveGame() throws IOException{

        Player savePlayer = new Player(player.getName());
        savePlayer.setPoints(player.getPoints());
        savePlayer.setHighestStreak(player.getHighestStreak());
        savePlayer.setMoves(player.getMoves());

        try (ObjectOutputStream serializer = new ObjectOutputStream(new FileOutputStream("savedGame.ser"))){
            serializer.writeObject(savePlayer);
        }

    }

    public void loadGame() throws IOException, ClassNotFoundException{

        try (ObjectInputStream deserializer = new ObjectInputStream(new FileInputStream("savedGame.ser"))){

            Player loadedPlayer = (Player) deserializer.readObject();

            playerName.setText(loadedPlayer.getName());
            playerPoints.setText("Points: " + loadedPlayer.getPoints());
            playerStreak.setText("Streak: " + loadedPlayer.getHighestStreak());
            moves = loadedPlayer.getMoves();


        }



    }
}

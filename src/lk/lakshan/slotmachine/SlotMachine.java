package lk.lakshan.slotmachine;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

/**
 * Created by Lakshan on 10/31/17.
 */
public class SlotMachine extends Application {
    /*
    * Initially 10 credits are given to the player
    * Max Bet is set to 3
    * */
    public static final int INITIAL_CREDITS = 10;
    public static final int MAX_BET = 3;

    private int credits = INITIAL_CREDITS;//No of credits
    private int betCoins;//No of bet coins
    private int addedCoins;//No of coins added by user
    private int noOfWins;//No of wins
    private int noOfLosts;//No of losts
    private boolean areSpinning;//if all 3 reels are spinning

    //StackPane
    private StackPane stackPane;//Stack Pane

    //Title
    private Label lblTitle;//Label of the Title

    //ImageViews to hold images for slots
    private ImageView imgView1;//Image View 1
    private ImageView imgView2;//Image View 2
    private ImageView imgView3;//Image View 3

    //Three symbols on slots
    private Symbol symbol1;//Symbol1
    private Symbol symbol2;//Symbol2
    private Symbol symbol3;//Symbol3

    //Three slots
    private Label lblSlot1;//Label of slot1
    private Label lblSlot2;//Label of slot2
    private Label lblSlot3;//Label of slot3

    //MessageBox & SpinButton
    private Label messageBox;//Information Area to display messages
    private Button btnSpin;//Spin button

    //Credit Area
    private Label lblCredits;//label credits
    private Label displayCredits;//textfield to display no of credit
    private Button btnAddCoin;//button to add coins

    //Bet Area
    private Label lblBet;//label bet
    private Label displayBet;//textfield to display bet
    private Button btnBetOne;//button to bet one
    private Button btnBetMax;//button to bet max
    private Button btnReset;//button to reset credits and bet

    //Menu Area
    private Button btnNewGame;//NewGame
    private Button btnStatistics;//Statistics
    private Button btnPayout;//Payout


    //HBoxes
    private HBox hbSlots;
    private HBox hbMessageAndSpin;
    private HBox hbAreas;
    private HBox hbBetAndReset;
    private HBox hbBetAreaBtns;

    //VBoxes
    private VBox vbCreditArea;
    private VBox vbBetArea;
    private VBox vbContainer;
    private VBox vbMenuArea;

    //Threads to set Images
    private SpinThread reelThread1;//Thread to change imageView of Reel1
    private SpinThread reelThread2;//Thread to change imageView of Reel2
    private SpinThread reelThread3;//Thread to change imageView of Reel3


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Title
        lblTitle = new Label("SLOT MACHINE");
        lblTitle.setId("lblTitle");

        //Setting images to the image views
        imgView1 = new ImageView();
        imgView2 = new ImageView();
        imgView3 = new ImageView();
        try {
            imgView1.setImage(new Image("images/watermelon.png"));
            imgView2.setImage(new Image("images/cherry.png"));
            imgView3.setImage(new Image("images/redseven.png"));
        }catch (IllegalArgumentException e){
            System.err.println("Images not found in the specified URL");
        }

        //Styling of the Image Views
        imgView1.setFitWidth(200);
        imgView2.setFitWidth(200);
        imgView3.setFitWidth(200);
        imgView1.setFitHeight(200);
        imgView2.setFitHeight(200);
        imgView3.setFitHeight(200);
        imgView1.setPreserveRatio(true);
        imgView1.setSmooth(true);
        imgView1.setCache(true);
        imgView2.setPreserveRatio(true);
        imgView2.setSmooth(true);
        imgView2.setCache(true);
        imgView3.setPreserveRatio(true);
        imgView3.setSmooth(true);
        imgView3.setCache(true);



        //Reels
        lblSlot1 = new Label("", imgView1);
        lblSlot2 = new Label("", imgView2);
        lblSlot3 = new Label("", imgView3);

        lblSlot1.getStyleClass().add("reel");
        lblSlot2.getStyleClass().add("reel");
        lblSlot3.getStyleClass().add("reel");
        hbSlots = new HBox();
        hbSlots.setId("hbSlots");
        hbSlots.getChildren().addAll(lblSlot1, lblSlot2, lblSlot3);



        lblSlot1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (areSpinning) {
                    reelThread1.stopSpinning();
                    symbol1 = reelThread1.getSymbol();
                    updateResult();
                }
            }
        });

        lblSlot2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (areSpinning) {
                    reelThread2.stopSpinning();
                    symbol2 = reelThread2.getSymbol();
                    updateResult();
                }
            }
        });

        lblSlot3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (areSpinning) {
                    reelThread3.stopSpinning();
                    symbol3 = reelThread3.getSymbol();
                    updateResult();
                }
            }
        });



        //Message Box
        messageBox = new Label("New Game! Bet and Spin!");
        messageBox.setId("messageBox");

        //Spin Button
        btnSpin = new Button("SPIN");
        btnSpin.setId("btnSpin");
        btnSpin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!areSpinning) {
                    if (betCoins < 1) {
                        messageBox.setText("Please bet before spinning!");
                        messageBox.setStyle("-fx-text-fill: #dc143c;");//Crimson
                    } else {
                        playSound("spin");//Play Sound Effect
                        areSpinning = true;
                        reelThread1 = new SpinThread(imgView1);
                        reelThread2 = new SpinThread(imgView2);
                        reelThread3 = new SpinThread(imgView3);
                        Thread t1 = new Thread(reelThread1,"Reel1");
                        Thread t2 = new Thread(reelThread2,"Reel2");
                        Thread t3 = new Thread(reelThread3,"Reel3");
                        t1.setDaemon(true);
                        t2.setDaemon(true);
                        t3.setDaemon(true);
                        t1.start();
                        t2.start();
                        t3.start();

                        messageBox.setText("Click a reel to stop!");
                        messageBox.setStyle("-fx-text-fill: #e6e6fa;");//Color-Lavender
                    }

                } else {
                    messageBox.setText("Click a reel to stop!");
                }
            }
        });

        hbMessageAndSpin = new HBox();//HBox to hold MessageBox and SpinButton
        hbMessageAndSpin.setId("hbMessageAndSpin");
        hbMessageAndSpin.getChildren().addAll(messageBox, btnSpin);



        //Credit Area
        lblCredits = new Label("CREDITS");
        lblCredits.getStyleClass().add("areaLabel");
        displayCredits = new Label(String.valueOf(credits));
        displayCredits.getStyleClass().add("displayValue");
        btnAddCoin = new Button("ADD COIN");
        btnAddCoin.getStyleClass().add("btn");

        vbCreditArea = new VBox();//VBox to hold Credit Area Items
        vbCreditArea.getChildren().addAll(lblCredits, displayCredits, btnAddCoin);
        vbCreditArea.getStyleClass().add("area");
        vbCreditArea.setId("vbCreditArea");

        //Event Handler for AddCoin Button
        btnAddCoin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!areSpinning) {
                    if (credits < 1000) {
                        credits++;
                        addedCoins++;
                        displayCredits.setText(String.valueOf(credits));
                        messageBox.setText("");
                        playSound("addcoin");//Play sound effect
                    } else {
                        //Can't add more than 1000 coins
                        messageBox.setText("Can't add more than 1000 coins!");
                        messageBox.setStyle("-fx-text-fill: #dc143c;");//Color-Crimson
                    }
                } else {
                    //Can't add coins while spinning the reels
                    messageBox.setText("Can't add coins while spinning!");
                }

            }
        });



        //Bet Area
        lblBet = new Label("BET");
        lblBet.getStyleClass().add("areaLabel");
        displayBet = new Label(String.valueOf(betCoins));
        displayBet.getStyleClass().add("displayValue");
        btnBetOne = new Button("BET ONE");
        btnBetMax = new Button("BET MAX");
        btnReset = new Button("RESET");
        btnReset.setId("btnReset");
        btnBetOne.getStyleClass().add("btn");
        btnBetMax.getStyleClass().add("btn");
        btnReset.getStyleClass().add("btn");

        hbBetAndReset = new HBox();//HBox to hold displayBet and Reset Button
        hbBetAndReset.getChildren().addAll(displayBet, btnReset);
        hbBetAndReset.setSpacing(10);

        hbBetAreaBtns = new HBox();//HBox to hold betOne button and betMax button
        hbBetAreaBtns.getChildren().addAll(btnBetOne, btnBetMax);
        hbBetAreaBtns.setSpacing(5);

        vbBetArea = new VBox();//VBox to hold Bet Area Items
        vbBetArea.getChildren().addAll(lblBet, hbBetAndReset, hbBetAreaBtns);
        vbBetArea.getStyleClass().add("area");
        vbBetArea.setSpacing(5);

        //Event Handler for Reset Button
        btnReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!areSpinning) {
                    if(betCoins!=0) {
                        resetValues();
                        playSound("reset");//Play Sound Effect
                    }
                } else {
                    messageBox.setText("Can't reset while spinning!");
                }
            }
        });

        //Event Handler for Bet One Button
        btnBetOne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!areSpinning) {
                    if (credits > 0) {
                        betCoins++;
                        credits--;
                        updateValues();
                        messageBox.setText("");
                        playSound("betone");//Play Sound Effect
                    } else {
                        messageBox.setText("You don't have coins!");
                        messageBox.setStyle("-fx-text-fill: #dc143c;");//Color-Crimson
                    }
                } else {
                    messageBox.setText("Can't bet while spinning!");
                }
            }
        });

        //Event Handler for Bet Max Button
        btnBetMax.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!areSpinning) {
                    if (credits + betCoins >= MAX_BET) {
                        resetValues();
                        betCoins += MAX_BET;
                        credits -= MAX_BET;
                        updateValues();
                        playSound("betmax");//Play Sound Effect
                        messageBox.setText("You have bet Max!");
                        messageBox.setStyle("-fx-text-fill: #e6e6fa;");//Color-Lavender
                    } else {
                        messageBox.setText("You don't have enough coins!");
                        messageBox.setStyle("-fx-text-fill: #dc143c;");//Color-Crimson
                    }
                } else {
                    messageBox.setText("Can't bet while spinning!");
                }
            }
        });



        //MenuArea
        btnNewGame = new Button("NEW GAME");
        btnNewGame.getStyleClass().add("menuBtn");
        btnStatistics = new Button("STATISTICS");
        btnStatistics.getStyleClass().add("menuBtn");
        btnPayout = new Button("PAYOUT");
        btnPayout.getStyleClass().add("menuBtn");

        //VBox to hold Menu Items(NewGame,Statistics,Quit buttons)
        vbMenuArea = new VBox();
        vbMenuArea.setId("vbMenuArea");
        vbMenuArea.getChildren().addAll(btnNewGame, btnStatistics, btnPayout);
        vbMenuArea.getStyleClass().add("area");

        //Event Handler for Payout Button
        btnPayout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Alert payout = new Alert(Alert.AlertType.INFORMATION);
                payout.setTitle("Payout - Slot Machine");
                payout.setHeaderText("(P)of a winning combination = (1/6)*(1/6)= 0.027\n");
                payout.setContentText("Payout for 2xSeven = 0.027*10$ = 0.270$\n" +
                        "Payout for 2xBell = 0.027*8$ = 0.216$\n" +
                        "Payout for 2xWatermelon = 0.027*6$ = 0.162$\n" +
                        "Payout for 2xPlum = 0.027*4$ = 0.108$\n" +
                        "Payout for 2xLemon = 0.027*3$ = 0.081$\n" +
                        "Payout for 2xCherry = 0.027*2$ = 0.054$\n" +
                        "\nTotal Payout for 1$ = 0.891$ (89.1%)");
                payout.showAndWait();
            }
        });

        //Event Handler for statistics Button
        btnStatistics.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!areSpinning) {
                    Statistics.viewStatistics(
                            INITIAL_CREDITS,addedCoins,credits,noOfWins,noOfLosts);
                }
            }
        });

        //Event Handler for NewGame Button
        btnNewGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!areSpinning) {
                    newGame();
                } else {
                    messageBox.setText("Click a reel to stop!");
                }
            }
        });


        //Adding creditArea, betArea and MenuArea to the HBox for Area
        hbAreas = new HBox();
        hbAreas.setId("hbAreas");
        hbAreas.getChildren().addAll(vbCreditArea, vbBetArea, vbMenuArea);

        //container VBox
        vbContainer = new VBox();
        vbContainer.setId("vbContainer");
        vbContainer.getChildren().addAll(lblTitle, hbSlots, hbMessageAndSpin, hbAreas);


        //Setting margin for elements of the StackPane
        stackPane = new StackPane();
        stackPane.setId("stackPane");

        stackPane.getChildren().add(vbContainer);

        //Creating scene
        Scene scene = new Scene(stackPane,800,700);
        scene.getStylesheets().add("style_slotmachine.css");//Adding the stylesheet
        primaryStage.setTitle("Slot Machine");//Set Title
        primaryStage.setScene(scene);

        /*Setting the minimum window size*/
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(800);
        primaryStage.show();
        playSound("newgame");

    }

    //Method to update the values of credits and bet in the UI
    private void updateValues() {
        displayCredits.setText(String.valueOf(credits));
        displayBet.setText(String.valueOf(betCoins));
    }

    //Method to update the result after three reels have been stopped
    private void updateResult() {
        if ((!reelThread1.isSpinning() && !reelThread2.isSpinning()) &&
                !reelThread3.isSpinning()) {
            if (Symbol.compareSymbols(symbol1, symbol2) ||
                    Symbol.compareSymbols(symbol1, symbol3)) {
                credits += betCoins * symbol1.getValue();
                noOfWins++;
                messageBox.setText("You Won!  (+" +
                        betCoins *symbol1.getValue()+ " Credits)");
                messageBox.setStyle("-fx-text-fill: #00fa9a;");//Color-MediumSpringGreen
                playSound("win");//Play Sound Effect

            } else if (Symbol.compareSymbols(symbol2, symbol3)) {
                credits += betCoins * symbol2.getValue();
                noOfWins++;
                messageBox.setText("You Won!  (+"
                        + betCoins * symbol2.getValue() + " Credits)");
                messageBox.setStyle("-fx-text-fill: #00fa9a;");//Color-MediumSpringGreen
                playSound("win");//Play Sound Effect
            } else {
                noOfLosts++;
                messageBox.setText("You Lost!");
                messageBox.setStyle("-fx-text-fill: #dc143c;");//Color-Crimson
                playSound("lost");//Play Sound Effect
            }
            betCoins = 0;
            areSpinning = false;
            updateValues();
        }
    }

    //Methods to reset the values of bet and credits
    private void resetValues() {
        if (betCoins != 0) {
            credits += betCoins;
            betCoins -= betCoins;
            updateValues();
            messageBox.setText("");
        }
    }

    //Method to play a new game
    private void newGame() {
        playSound("newgame");//Play Sound Effect
        try {
            imgView1.setImage(new Image("images/watermelon.png"));
            imgView2.setImage(new Image("images/cherry.png"));
            imgView3.setImage(new Image("images/redseven.png"));
        }catch(IllegalArgumentException e) {
            //If the images are not found in the specified URL
            System.err.println("Images not found in the specified URL");
        }
        messageBox.setText("New Game! Bet and Spin!");
        messageBox.setStyle("-fx-text-fill: #00fa9a;");//Color-MediumSpringGreen
        noOfWins = 0;
        noOfLosts = 0;
        betCoins = 0;
        addedCoins=0;
        credits = INITIAL_CREDITS;
        updateValues();
    }

    //Method to create Sound Thread and play Sound
    private synchronized void playSound(String soundName) {
        SoundThread soundThread = new SoundThread(soundName);
        Thread t = new Thread(soundThread);
        t.setDaemon(true);
        t.start();
    }

    //Class to play SoundEffects
    private class SoundThread implements Runnable{
        private String soundName;
        private  SoundThread(String soundName){
            this.soundName=soundName;
        }

        @Override
        public void run() {
            try {
                String soundFileURL = "soundfx/" + soundName + ".wav";
                Media soundFile = new Media(new File(soundFileURL).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(soundFile);
                mediaPlayer.play();
            }catch (MediaException e){
                System.err.println("Sound files not found in the provided URL");
            }
        }
    }


    //Class to spin the threads
    private class SpinThread implements Runnable {

        private ImageView imgView;//ImageView on the Reel
        private List<Symbol> symbolList;//List of 6 symbols
        private Symbol symbol;//displayed Symbol on the reel
        private volatile boolean spinning = true;//If the reel is spinning

        //Constructor of the SpinThread Class
        private SpinThread(ImageView imgView) {
            Reel r = new Reel();
            this.imgView = imgView;
            this.symbolList = r.spin();
        }

        //Getter of symbol
        private Symbol getSymbol() {
            return symbol;
        }

        //Getter of boolean isSpinning
        private boolean isSpinning() {
            return spinning;
        }

        //Method to set the isSpiining to false
        private void stopSpinning() {
            this.spinning = false;
        }

        @Override
        public void run() {
            //Change the imageView of the reel every 100 ms
            for (int x = 0; x < symbolList.size(); x++) {
                if (spinning) {
                    this.symbol = symbolList.get(x);

                    try {
                        /*
                        * Put a synchronized lock to avoid more than one thread to
                        * access the same image at the same time
                        * */
                        synchronized (SlotMachine.class) {
                            imgView.setImage(new Image(this.symbol.getImage()));
                        }
                        //Putting the thread to sleep
                        Thread.sleep(100);

                    } catch(IllegalArgumentException e){
                        //If the image is not found in the specified URL
                        e.printStackTrace();

                    }catch (InterruptedException e){
                        //Interrupted Exception
                        e.printStackTrace();
                    }

                    if (x == symbolList.size()- 1) {
                        x = -1;//To keep the for loop running again and again
                    }
                } else {
                    //If boolean Spinning is false, it will break the loop
                    break;
                }
            }
        }
    }
}
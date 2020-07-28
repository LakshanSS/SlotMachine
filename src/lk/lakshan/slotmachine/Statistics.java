package lk.lakshan.slotmachine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class Statistics {

    private static Statistics stats = null;
    private Stage stage;//Stage
    private boolean fileSaved;//If it has been saved

    private int initialCredits;
    private int addedCoins;
    private int grossCredits;
    private int noOfWins;
    private int noOfLosses;
    private int totalSpins;
    private int investment;
    private int gain;
    private double averagePerGame;
    private double gainPercentage;


    //Private Constructor for Statistics
    private Statistics(int initialCredits,int addedCoins,int grossCredits, int noOfWins, int noOfLosses){

        this.initialCredits = initialCredits;
        this.addedCoins = addedCoins;
        this.grossCredits = grossCredits;
        this.noOfWins = noOfWins;
        this.noOfLosses = noOfLosses;

        //Calculations
        this.totalSpins = noOfWins+noOfLosses;
        this.investment = initialCredits+addedCoins;
        this.gain = grossCredits-investment;
        this.averagePerGame = Math.round((double) gain/totalSpins*100.0)/100.0;
        this.gainPercentage = Math.round((double)gain/investment*10000.0)/100.0;

        //Observable Arraylist to create a pie chart
        ObservableList<PieChart.Data> gameDetails = FXCollections.observableArrayList();
        gameDetails.addAll(new PieChart.Data("Wins",noOfWins),new PieChart.Data("Losts",noOfLosses));


        //Labels
        Label title = new Label("Statistics - Slot Machine");
        title.setId("lblTitle");
        Label lblInitialCredits = new Label("Initial Credits : ");
        Label lblDisplayInitialCredits = new Label(SlotMachine.INITIAL_CREDITS+"");
        Label lblAddedCoins = new Label("Added Coins : ");
        Label lblDisplayAddedCoins = new Label(addedCoins+"");
        Label lblInvestedCoins = new Label("Total Investment : ");
        Label lblDisplayInvestedCoins = new Label(investment+"");
        Label lblGrossCoins = new Label("Gross Coins : ");
        Label lblDisplayGrossCoins = new Label(grossCredits+"");
        Label lblGain = new Label("Gain : ");
        lblGain.getStyleClass().add("lblGainStats");
        Label lblDisplayGain = new Label(gain+"");
        lblDisplayGain.getStyleClass().add("lblGainStats");
        Label lblGainPercentage= new Label("Gain Percentage : ");
        lblGainPercentage.getStyleClass().add("lblGainStats");
        Label lblDisplayGainPercentage = new Label(gainPercentage+"%");
        lblDisplayGainPercentage.getStyleClass().add("lblGainStats");
        Label lblAvgGainPerSpin= new Label("Average Gain/Spin: ");
        lblAvgGainPerSpin.getStyleClass().add("lblGainStats");
        Label lblDisplayAvgGainPerSpin= new Label(averagePerGame+"");
        lblDisplayAvgGainPerSpin.getStyleClass().add("lblGainStats");

        //Set red color if gain<0
        if(gain<0){
            lblGain.setText("Loss : ");
            lblGain.setStyle("-fx-text-fill: #dc143c;");//Color-Crimson
            lblDisplayGain.setText(-gain+"");
            lblDisplayGain.setStyle("-fx-text-fill: #dc143c;");
            lblGainPercentage.setText("Loss Percentage : ");
            lblGainPercentage.setStyle("-fx-text-fill: #dc143c;");
            lblDisplayGainPercentage.setText(-gainPercentage+"%");
            lblDisplayGainPercentage.setStyle("-fx-text-fill: #dc143c;");
            lblAvgGainPerSpin.setText("Average Loss/Spin: ");
            lblAvgGainPerSpin.setStyle("-fx-text-fill: #dc143c;");
            lblDisplayAvgGainPerSpin.setText(-averagePerGame+"");
            lblDisplayAvgGainPerSpin.setStyle("-fx-text-fill: #dc143c;");

        }

        Label lblTotalSpins = new Label(" Total Spins : ");
        Label lblDisplayTotalSpins = new Label(totalSpins+"");
        Label lblNoOfWins = new Label(" Wins : ");
        Label lblDisplayWins = new Label(noOfWins+"");
        Label lblNoOfLosses = new Label(" Losses : ");
        Label lblDisplayLosses = new Label(noOfLosses+"");

        //Button to save details
        Button btnSave = new Button("Save");
        btnSave.setId("btnSave");

        btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if(!fileSaved){
                    saveStatistics();
                    fileSaved=true;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Statistics - Slot Machine");
                    alert.setHeaderText(null);
                    alert.setContentText("Statistics has been saved !");
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Statistics - Slot Machine");
                    alert.setHeaderText(null);
                    alert.setContentText("Statistics has been saved already." +
                            " Are you sure want to save it again?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        saveStatistics();
                    }
                }
            }
        });

        //GridPane to hold Labels
        GridPane grid = new GridPane();
        grid.setId("gpGrid");
        grid.add(lblInitialCredits,0,0);
        grid.add(lblDisplayInitialCredits,1,0);
        grid.add(lblAddedCoins,0,1);
        grid.add(lblDisplayAddedCoins,1,1);
        grid.add(lblInvestedCoins,0,2);
        grid.add(lblDisplayInvestedCoins,1,2);
        grid.add(lblGrossCoins,0,3);
        grid.add(lblDisplayGrossCoins,1,3);
        grid.add(lblGain,0,4);
        grid.add(lblDisplayGain,1,4);
        grid.add(lblGainPercentage,0,5);
        grid.add(lblDisplayGainPercentage,1,5);
        grid.add(lblAvgGainPerSpin,0,6);
        grid.add(lblDisplayAvgGainPerSpin,1,6);
        grid.add(lblTotalSpins,2,0);
        grid.add(lblDisplayTotalSpins,3,0);
        grid.add(lblNoOfWins,2,1);
        grid.add(lblDisplayWins,3,1);
        grid.add(lblNoOfLosses,2,2);
        grid.add(lblDisplayLosses,3,2);
        grid.add(btnSave,3,6);

        //PieChart
        PieChart pieChart = new PieChart(gameDetails);
        pieChart.setTitle("Wins & Losses");
        pieChart.setLegendSide(Side.RIGHT);
        pieChart.setPrefSize(400,400);
        pieChart.setLabelsVisible(true);
        pieChart.setId("pieChart");

        //BarChart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setPrefSize(200,200);
        bc.setCategoryGap(0);
        bc.setBarGap(5);
        xAxis.setLabel("Result");
        yAxis.setLabel("Number of times");
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(Math.max(noOfWins,noOfLosses));

        XYChart.Series bar = new XYChart.Series<>();
        bar.getData().add(new XYChart.Data("Wins",noOfWins));
        bar.getData().add(new XYChart.Data("Losts",noOfLosses));

        bc.getData().add(bar);
        bc.setLegendVisible(false);
        bc.setId("barChart");


        //HBox to hold graphs
        HBox hbGraphs = new HBox(pieChart,bc);
        hbGraphs.setId("hbGraphs");

        //VBox to hold two GridPane and HBox
        VBox vbox = new VBox(title,grid,hbGraphs);
        vbox.setId("vbContainer");

        //StackPane to hold the VBox
        StackPane stackPane = new StackPane(vbox);
        stackPane.setId("stackPane");

        //Creating Scene
        Scene scene = new Scene(stackPane,700,700);
        scene.getStylesheets().add("style_statistics.css");
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Statistics - Slot Machine");
        stage.show();

    }

    //Method to Save Staticstics
    private void saveStatistics(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try(FileWriter fileWriter = new FileWriter(new File(date+".txt"),true)){
            try(PrintWriter printWriter = new PrintWriter(fileWriter,true)){
                printWriter.println("Date and Time: "+dateFormat.format(date)+"\n");
                printWriter.println("Initial Credits: "+SlotMachine.INITIAL_CREDITS);
                printWriter.println("Added Coins: "+addedCoins);
                printWriter.println("Total Investment: "+investment);
                printWriter.println("Gross Coins: "+grossCredits+"\n");
                if(gain<0) {
                    printWriter.println("Loss: "+ -gain+"\nLoss Percentage: "+ -gainPercentage+
                            "%\nAverage Loss/Spin: "+ -averagePerGame+"\n");

                }else{
                    printWriter.println("Gain: "+ gain+"\nGain Percentage: "+ gainPercentage+
                            "%\nAverage Gain/Spin: "+averagePerGame+"\n");
                }

                printWriter.println("Total Spins: "+totalSpins+"\nNo Of Wins: "+noOfWins+
                        "\nNo Of Losses: "+noOfLosses);
            }

        }catch (IOException e1){
            e1.printStackTrace();
        }
    }


    //View Statistics method to create new Statistics Window
    public static Statistics viewStatistics(
            int initialCredits,int addedCoins,int grossCredits, int noOfWins, int noOfLosts){

        //if a statistics window was opened previously, it will be closed
        if(stats != null){
            stats.stage.close();
        }
        //Create a new Statistics window with updated values
        stats = new Statistics(
                initialCredits,addedCoins,grossCredits,noOfWins,noOfLosts);
        return stats;
    }
}

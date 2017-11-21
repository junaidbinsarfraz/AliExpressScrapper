package com.aliexperssscrapper.gui;

import java.io.File;
import java.util.List;

import com.aliexperssscrapper.controller.ScrapperController;
import com.aliexperssscrapper.model.Input;
import com.aliexperssscrapper.model.Product;
import com.aliexperssscrapper.util.Constants;
import com.aliexperssscrapper.util.FileUtil;
import com.aliexperssscrapper.util.Util;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The class GUI is use to make javafx gui. After the file selection user can be
 * able to start the crawling
 * 
 * @author Junaid
 */
public class GUI extends Application {
	
	ScrapperController scrapperController = new ScrapperController();
	File inputFile = null;
	public static File proxyFile = null;
	public static File outputDirectory = null;
	public static Integer delay = 2;
	
	private Thread thread = null;

	public static void main(String[] args) {

		GUI gui = new GUI();

		gui.launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		try {

			primaryStage.setTitle("Ali Express Crawler");

			// Grid
			GridPane grid = new GridPane();

			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));
			
			
			
			final Label status = new Label();
			final Label proxyLabel = new Label("Select Proxies File:");
			final Label proxyFileNameLabel = new Label();
			final Label inputFileLabel = new Label("Select Input File:");
			final Label inputFileNameLabel = new Label();
			final Label timeIntervalLabel = new Label("Delay Between Requests:");
			final Label outputDirectoryLabel = new Label("Select Output Directory:");
			final Label outputDirectoryPathLabel = new Label();
			
			final Button proxyFileButton = new Button("Proxy File");
			final Button inputFileButton = new Button("Input File");
			final Button outputDirectoryButton = new Button("Output Directory");
			final Button startButton = new Button("Start");
			
			final TextField timeIntervalTextField = new TextField();
			
			final FileChooser fileChooser = new FileChooser();
			final DirectoryChooser directoryChooser = new DirectoryChooser();
			
			final ProgressIndicator progressIndicator = new ProgressIndicator();
			
			progressIndicator.setVisible(Boolean.FALSE);
			
			grid.add(status, 0, 0, 1, 1);
			grid.add(progressIndicator, 1, 0, 2, 1);
			
			// CSV file

			proxyFileButton.setPrefWidth(120);
			proxyFileButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					configureFileChooser(fileChooser);
					proxyFile = fileChooser.showOpenDialog(primaryStage);
					String fileName = "";
					
					if (proxyFile != null) {
						status.setText("");
						fileName = proxyFile.getName();
					}
					
					startButton.setDisable(Util.isNull(proxyFile) || Util.isNull(outputDirectory) || Util.isNull(inputFile));
					
					proxyFileNameLabel.setText(fileName);
				}
			});
			
			grid.add(proxyLabel, 0, 1, 1, 1);
			grid.add(proxyFileButton, 1, 1, 1, 1);
			grid.add(proxyFileNameLabel, 2, 1, 1, 1);
			
			// time interval
			
			timeIntervalTextField.setPrefWidth(120);
			
			// force the field to be numeric only
			timeIntervalTextField.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, 
			        String newValue) {
			        if (!newValue.matches("\\d*")) {
			        	timeIntervalTextField.setText(newValue.replaceAll("[^\\d]", ""));
			        }
			    }
			});
			
			grid.add(timeIntervalLabel, 0, 2, 1, 1);
			grid.add(timeIntervalTextField, 1, 2, 1, 1);
			
			// output directory file
			
			outputDirectoryButton.setPrefWidth(120);
			outputDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					outputDirectory = directoryChooser.showDialog(primaryStage);
					String dirctoryPath = "";
					
					if (outputDirectory != null) {
						status.setText("");
						dirctoryPath = outputDirectory.getPath();
					}
					
					startButton.setDisable(Util.isNull(proxyFile) || Util.isNull(outputDirectory) || Util.isNull(inputFile));
					
					outputDirectoryPathLabel.setText(dirctoryPath);
				}
			});
			
			grid.add(outputDirectoryLabel, 0, 3, 1, 1);
			grid.add(outputDirectoryButton, 1, 3, 1, 1);
			grid.add(outputDirectoryPathLabel, 2, 3, 1, 1);

			// Buttons

			inputFileButton.setPrefWidth(120);
			inputFileButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					configureFileChooser(fileChooser);
					inputFile = fileChooser.showOpenDialog(primaryStage);
					String fileName = "";
					
					if (inputFile != null) {
						status.setText("");
						fileName = inputFile.getName();
					}
					
					startButton.setDisable(Util.isNull(proxyFile) || Util.isNull(outputDirectory) || Util.isNull(inputFile));
					
					inputFileNameLabel.setText(fileName);
				}
			});
			
			grid.add(inputFileLabel, 0, 4, 1, 1);
			grid.add(inputFileButton, 1, 4, 1, 1);
			grid.add(inputFileNameLabel, 2, 4, 1, 1);
			
			startButton.setDisable(Boolean.TRUE);
			startButton.setPrefWidth(120);
			startButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					
					status.setText("");
					progressIndicator.setVisible(Boolean.FALSE);
					
					updateStatus(null); // To avoid UI redundancy
					
					if (inputFile != null && proxyFile != null && outputDirectory != null) {
						
						startButton.setDisable(Boolean.TRUE);
						inputFileButton.setDisable(Boolean.TRUE);
						outputDirectoryButton.setDisable(Boolean.TRUE);
						proxyFileButton.setDisable(Boolean.TRUE);
						timeIntervalTextField.setDisable(Boolean.TRUE);
						
						if(Util.isNotNullAndEmpty(timeIntervalTextField.getText())) {
							delay = Integer.parseInt(timeIntervalTextField.getText());
						}
						
						status.setTextFill(Color.DARKBLUE);
						status.setText("Processing ...");
						
						progressIndicator.setVisible(Boolean.TRUE);
						progressIndicator.setProgress(Constants.PROCESSING);
						
						updateStatus(null); // To avoid UI redundancy
						
						thread = new Thread() {
							public void run() {
								// Start Crawling
								List<Input> inputs = FileUtil.readInputFile(inputFile);
								
								if(Util.isNullOrEmpty(inputs)) {
									status.setText("No data extracted from file");
									return;
								}
								
								for(Input input : inputs) {
									List<Product> products = scrapperController.extractProductsFromCategory(input);
									
									FileUtil.writeProductsOfCategoryInExcel(GUI.outputDirectory.getAbsolutePath() + "\\" + input.getCategoryName(), products);
								}
								
								status.setTextFill(Color.GREEN);
								status.setText("Successfully scrapped");

								updateStatus(null); // To avoid UI redundancy
								
								progressIndicator.setVisible(Boolean.FALSE);
								
								updateStatus(null);
								
								startButton.setDisable(Boolean.FALSE);
								inputFileButton.setDisable(Boolean.FALSE);
								outputDirectoryButton.setDisable(Boolean.FALSE);
								proxyFileButton.setDisable(Boolean.FALSE);
								timeIntervalTextField.setDisable(Boolean.FALSE);
								
								updateStatus(null);
							}
						};
						
						thread.start();
						
					} else {
						status.setTextFill(Color.FIREBRICK);
						status.setText("Kindly select all files and directories");
					}
				}
			});
			
			grid.add(startButton, 1, 5, 1, 1);
			
			Scene scene = new Scene(grid);
			
			primaryStage.setHeight(400);
			primaryStage.setWidth(600);
			
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(e -> {
				if(thread != null) {
					thread.stop();
				}
			});
			
			primaryStage.show();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * The method configureFileChooser() is use to configure the file chooser so
	 * that it can only select csv file
	 * 
	 * @param fileChooser
	 */
	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Select Input File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
	}
	
	/**
	 * The method updateStatus() is use to update status on GUI
	 * 
	 * @param t
	 */
	public void updateStatus(final Throwable t) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
			}
		});
	}
	
}

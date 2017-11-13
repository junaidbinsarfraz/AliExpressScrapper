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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
	File file = null;

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
			
			Label message = new Label();
			
			grid.add(message, 0, 0, 2, 1);
			
			Label keywordLabel = new Label("Select File:");

			grid.add(keywordLabel, 0, 1, 1, 1);

			// Buttons
			final FileChooser fileChooser = new FileChooser();
			final Button inputFileButton = new Button("Input File");
			final Button startButton = new Button("Start");

			inputFileButton.setPrefWidth(80);
			inputFileButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					configureFileChooser(fileChooser);
					file = fileChooser.showOpenDialog(primaryStage);
					if (file != null) {
						startButton.setDisable(Boolean.FALSE);
						message.setText("");
					}
				}
			});
			
			grid.add(inputFileButton, 1, 1, 1, 1);
			
			startButton.setDisable(Boolean.TRUE);
			startButton.setPrefWidth(80);
			startButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					
					startButton.setDisable(Boolean.TRUE);
					
					if (file != null) {
						
						// Start Crawling
						List<Input> inputs = FileUtil.readInputFile(file);
						
						if(Util.isNullOrEmpty(inputs)) {
							message.setText("No data extracted from file");
							return;
						}
						
						for(Input input : inputs) {
							List<Product> products = scrapperController.extractProductsFromCategory(input);
							
							FileUtil.writeProductsOfCategoryInExcel(Constants.OUTPUT_DIRECTORY + input.getCategoryName(), products);
						}
						
						message.setText("Successfully scrapped");
						
					}
				}
			});
			
			grid.add(startButton, 1, 2, 1, 1);
			
			Scene scene = new Scene(grid);
			
			primaryStage.setHeight(400);
			primaryStage.setWidth(400);
			
			primaryStage.setScene(scene);
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

}

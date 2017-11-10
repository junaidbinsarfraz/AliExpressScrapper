package com.aliexperssscrapper.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection.Response;

import com.aliexperssscrapper.model.Input;
import com.aliexperssscrapper.model.Product;

public class FileUtil {
	
	public static List<Input> readInputFile(String filePath) {
		
		List<Input> inputs = new LinkedList<>();
		
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
		
		try {
			
			br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] columns = line.split(cvsSplitBy);
                
                Input input = new Input();
                
                input.setCategoryUrl(columns[0]);
                input.setCategoryName(columns[1]);
                input.setMaxPageNumber(columns.length > 2 ? Integer.parseInt(columns[2]) : null);
                
                inputs.add(input);
            }
			
		} catch (Exception e) {
			
		} finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		return inputs;
	}
	
	public static void writeProductsOfCategory(String categoryName, List<Product> products) {
		
		// TODO: Change it w.r.t. logic 
		
		// Create folder of category name
		
		// Within folder create one csv file for all products to store product information
		for(Product product : products) {
			// Create new folder for each product where we will save images
		}
		
	}
	
	public static void saveImage(Response response, String pathToFolder, String imgUrl) {
		
		// TODO: Extract image name with ext from image Url
		String name = getImageNameWithExtFromImageUrl(imgUrl);
		
		// output here
		FileOutputStream out = (new FileOutputStream(new java.io.File(pathToFolder + "\\" + name)));
		out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
		out.close();
		
		/*try (InputStream in = new URL(imgUrl).openStream()) {
			Files.copy(in, Paths.get(Constants.OUTPUT_DIRECTORY + categoryName + "\\" + imageNameWithExtension));
		} catch (Exception e) {

		}*/
	}
	
	private static String getImageNameWithExtFromImageUrl(String imageUrl) {
		String imageName = "";
		
		imageName = imageUrl.substring(0, imageUrl.indexOf("?"));
		imageName = imageName.substring(imageName.lastIndexOf("/"), imageName.length() - 1);
		
		return imageName;
	}
	
}

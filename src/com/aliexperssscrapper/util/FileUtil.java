package com.aliexperssscrapper.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection.Response;

import com.aliexperssscrapper.model.Input;
import com.aliexperssscrapper.model.Product;

public class FileUtil {
	
	public static List<Input> readInputFile(File file) {
		
		List<Input> inputs = new LinkedList<>();
		
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
		
		try {
			
			br = new BufferedReader(new FileReader(file));
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
	
	public static void writeProductsOfCategoryInExcel(String pathToCategoryFolder, List<Product> products) {
		try {
			
			Workbook wb = new HSSFWorkbook();
		    //Workbook wb = new XSSFWorkbook();
		    CreationHelper createHelper = wb.getCreationHelper();
		    Sheet sheet = wb.createSheet("Products");

		    // Create a row and put some cells in it. Rows are 0 based.
		    Row row = sheet.createRow((short)0);
		    // Create a cell and put a value in it.
		    row.createCell(0).setCellValue(createHelper.createRichTextString("Title"));
		    row.createCell(1).setCellValue(createHelper.createRichTextString("Product page url"));
		    row.createCell(2).setCellValue(createHelper.createRichTextString("Product ID"));
		    row.createCell(3).setCellValue(createHelper.createRichTextString("Price"));
		    row.createCell(4).setCellValue(createHelper.createRichTextString("Highest Price"));
		    row.createCell(5).setCellValue(createHelper.createRichTextString("Lowest Price"));
		    row.createCell(6).setCellValue(createHelper.createRichTextString("Order Numbers"));
		    row.createCell(7).setCellValue(createHelper.createRichTextString("Colors"));
		    row.createCell(8).setCellValue(createHelper.createRichTextString("Sizes"));
		    row.createCell(9).setCellValue(createHelper.createRichTextString("Item specifics"));
		    
		    for(Integer i = 0; i < products.size(); i++) {
		    	
		    	Product product = products.get(i);
		    	
		    	Row productRow = sheet.createRow((short)(i + 1));
			    // Create a cell and put a value in it.
			    productRow.createCell(0).setCellValue(createHelper.createRichTextString(product.getTitle()));
			    productRow.createCell(1).setCellValue(createHelper.createRichTextString(product.getUrl()));
			    productRow.createCell(2).setCellValue(createHelper.createRichTextString(product.getId()));
			    productRow.createCell(3).setCellValue(createHelper.createRichTextString(product.getPrice()));
			    productRow.createCell(4).setCellValue(createHelper.createRichTextString(product.getHightPrice()));
			    productRow.createCell(5).setCellValue(createHelper.createRichTextString(product.getLowPrice()));
			    productRow.createCell(6).setCellValue(createHelper.createRichTextString(product.getOrderNumber()));
			    productRow.createCell(7).setCellValue(createHelper.createRichTextString(getString(product.getColors())));
			    productRow.createCell(8).setCellValue(createHelper.createRichTextString(getString(product.getSizes())));
			    productRow.createCell(9).setCellValue(createHelper.createRichTextString(getString(product.getItemSpecs())));
		    }

		    // Write the output to a file
		    FileOutputStream fileOut = new FileOutputStream(pathToCategoryFolder + "\\output.xls");
		    wb.write(fileOut);
		    fileOut.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void writeProductsOfCategory(String pathToCategoryFolder, List<Product> products) {
		
		String csvFile = pathToCategoryFolder + "\\output.csv";
        try {
			FileWriter writer = new FileWriter(csvFile);
			
			// Headers
			CSVUtil.writeLine(writer, Arrays.asList("Title", "Product page url", "Product ID", "Price", "Highest Price", "Lowest Price", "Order Numbers", "Colors", "Sizes", "Item specifics"), '|');
			
			// Within folder create one csv file for all products to store product information
			for(Product product : products) {
				
				List<String> list = new LinkedList<>();
				
				list.add(product.getTitle());
				list.add(product.getUrl());
				list.add(product.getId());
				list.add(product.getPrice());
				list.add(product.getHightPrice());
				list.add(product.getLowPrice());
				list.add(product.getOrderNumber());
				list.add(getString(product.getColors()));
				list.add(getString(product.getSizes()));
				list.add(getString(product.getItemSpecs()));
				
				CSVUtil.writeLine(writer, list, '|');
			}
			
			writer.flush();
	        writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getString(List<String> list) {
		String result = "";
		
		if(Util.isNotNullAndEmpty(list)) {
			result = list.toString();
			result = result.replaceFirst("\\[", "");
			result = result.substring(0, result.length() - 1);
		}
		
		return result;
	}
	
	private static String getString(Map<String, String> map) {
		String result = "";
		
		if(Util.isNotNullAndEmpty(map)) {
			result = map.toString();
			result = result.replaceFirst("\\{", "");
			result = result.substring(0, result.length() - 1);
		}
		
		return result;
	}
	
	public static void saveImage(Response response, String pathToImage, String imgUrl) {
		
		try {
//			String name = getImageNameWithExtFromImageUrl(imgUrl);
			
			// output here
			FileOutputStream out;
			out = (new FileOutputStream(new java.io.File(pathToImage)));
			out.write(response.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getImageNameWithExtFromImageUrl(String imageUrl) {
		String imageName = "";
		
		imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
		
		return imageName;
	}
	
}

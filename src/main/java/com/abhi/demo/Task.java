package com.abhi.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Task implements ApplicationRunner {
	String INPUT_FILE_LOCATION = "/home/abhishek/Downloads/2m_Sales_Records.csv";
	// String INPUT_FILE_LOCATION = "/2m_Sales_Records.csv";

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("***************************************************");
		System.out.println("start time:" + LocalDateTime.now());

		CompletableFuture<List<SalesModel>> fileReadingFuture = CompletableFuture.supplyAsync(() -> {
			try {
				return processFile(INPUT_FILE_LOCATION);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		});

		fileReadingFuture
				.thenAccept(sales -> sales.stream().map(SalesModel::getTotalRevenue).forEach(System.out::println));

		fileReadingFuture.join();
		System.out.println("stop time:" + LocalDateTime.now());

	}

	private List<SalesModel> processFile(String inputFileLocation) throws IOException {

		List<SalesModel> inputData = new ArrayList<>();

		File file = new File(inputFileLocation);
		InputStream is = new FileInputStream(file);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(is));

		inputData = bfr.lines().skip(1).map(mapToItem).collect(Collectors.toList());
		bfr.close();

		// System.out.println("No of records read:::" + inputData.stream().count());

		// inputData.stream().map(SalesModel::getTotalRevenue).forEach(System.out::println);

		return inputData;
	}

	Function<String, SalesModel> mapToItem = (line) -> {

		SalesModel salesModel = new SalesModel();
		String[] p = line.split(",");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

		salesModel.setRegion(p[0]);
		salesModel.setCountry(p[1]);
		salesModel.setItemType(p[2]);

		salesModel.setSalesChannel(p[3]);
		salesModel.setOrderPriority(p[4]);
		salesModel.setOrderDate(LocalDate.parse(p[5], formatter));
		// salesModel.setOrderDate(p[5]);

		salesModel.setOrderId(p[6]);
		salesModel.setShipDate(LocalDate.parse(p[7], formatter));
		// salesModel.setShipDate(p[7]);
		salesModel.setUnitSold(Integer.parseInt(p[8]));
		salesModel.setUnitPrice(Double.parseDouble(p[9]));
		salesModel.setUnitCost(Double.parseDouble(p[10]));

		salesModel.setTotalRevenue(Double.parseDouble(p[11]));
		salesModel.setTotalCost(Double.parseDouble(p[12]));
		salesModel.setTotalProfit(Double.parseDouble(p[13]));

		return salesModel;
	};

}

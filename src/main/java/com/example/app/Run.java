package main.java.com.example.app;

import main.java.com.example.utils.message.Message;
import main.java.com.example.task01.Task01;
import main.java.com.example.utils.math.Add;

public class Run {

	public static final String FILE_PATH = "data/periods/period_";
	public static final String FILE_EXTENSION = ".txt";

	public static String fileName(String fileName) {
		return new StringBuilder()
				.append(FILE_PATH)
				.append(fileName)
				.append(FILE_EXTENSION)
				.toString();
	}

	public static void run() {
		System.out.println("Hello World from Run!");
		Message.message();
		System.out.println("1 + 2 = " + Add.add(1, 2) + "/n");
	}

	public static void runTask(String[] args) {
		if (args[0].equals("1")) { runFirst(); }
		if (args[0].equals("2")) { runSecond(); }
	}

	public static void runFirst() {
		Task01.runFirstImplementation(fileName("01"), fileName("02"));
		Task01.runFirstImplementation(fileName("03"), fileName("04"));
		Task01.runFirstImplementation(fileName("05"), fileName("06"));
		Task01.runFirstImplementation(fileName("07"), fileName("08"));
		Task01.runFirstImplementation(fileName("09"), fileName("10"));
	}

	public static void runSecond() {
		Task01.runSecondImplementation(fileName("01"), fileName("02"));
	}

}

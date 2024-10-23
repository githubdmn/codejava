package main.java.com.example.app02masterclass;

import main.java.com.example.utils.message.Message;
import main.java.com.example.utils.math.Add;

public class RunApp02 {
	public static void run() {
		System.out.println("Hello World from masterclass!");
		Message.message();
		System.out.println("1 + 2 = " + Add.add(1, 2));
	}
}

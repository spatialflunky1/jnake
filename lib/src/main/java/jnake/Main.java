package jnake;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import jline.ConsoleReader;

public class Main {
	
	static List<String>[] createScreen(int[] position, int columns, int rows) {
		List<String>[] screen = new List[rows];
		for (int i = 0; i < rows; i++) {
			List<String> row = new ArrayList<String>();
			for (int j = 0; j < columns; j++) {
				if (j == position[0] && i == position[1]+1) {
					row.add(" ");
				}
				else {
					row.add("█");
				}
			}
			screen[i] = row;
		}
		return screen;
	}
	
	static void printScreen(List<String>[] layout) {
		for (int i = 0; i < layout.length; i++) {
			List<String> row = layout[i];
			for (int j = 0; j < row.size(); j++) {
				System.out.print(row.get(j));
			}
			System.out.print("\n");
		}
		System.out.println("Score: 0");
	}
	
	static void clearScreen() {
	    System.out.print("\033[H\033[2J");  
	    System.out.flush(); 
	}
	
	static String getTermSize() throws IOException, InterruptedException {
		ProcessBuilder sizeCommand = new ProcessBuilder("/bin/sh", "-c", "stty size </dev/tty");
		final Process p = sizeCommand.start();
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
		return out.readLine();
	}
	
	static int[] changePos(String move, int[] position) {
		int [] pos = position;
		if (move == "up") {pos[1]++;}
		if (move == "down") {pos[1]--;}
		return pos;
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Random rand = new Random();
		String[] dimensions = getTermSize().split(" ");
		int height = Integer.valueOf(dimensions[0])-2;
		int width = Integer.valueOf(dimensions[1]);
		if (width > 115) width = 115;
		if (height > 60) height = 60;
		int[] position = {rand.nextInt(width-1),rand.nextInt(height-1)};
		ConsoleReader reader = new ConsoleReader();
		int key;
		String move = "none";
		while ((key = reader.readVirtualKey()) != 1) {
			switch (key) {
			case 65539:
				move = "up";
				break;
			case 65540:
				move = "down";
				break;
			}
			position = changePos(move, position);
			List<String>[] screen = createScreen(position, width, height);
			clearScreen();
			printScreen(screen);
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}
}

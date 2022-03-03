package jnake;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Thread;
import org.jline.terminal.TerminalBuilder;

public class Main {
	public static int key;
	public static String move = "none";
	
	static List<List<String>> createScreen(int[] position, int columns, int rows) {
		List<List<String>> screen = new ArrayList<List<String>>();
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
			screen.add(row);
		}
		return screen;
	}
	
	static void printScreen(List<List<String>> layout, int score) {
		for (int i = 0; i < layout.size(); i++) {
			List<String> row = layout.get(i);
			for (int j = 0; j < row.size(); j++) {
				System.out.print(row.get(j));
			}
			System.out.print("\n");
		}
		System.out.println("Score: "+score+" (key value for testing) (press 'q' to quit)");
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
		if (move == "up") {pos[1]--;}
		if (move == "down") {pos[1]++;}
		if (move == "left") {pos[0]--;}
		if (move == "right") {pos[0]++;}
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
		var term = TerminalBuilder.terminal();
		term.enterRawMode();
		var reader = term.reader();
		
		new Thread(() -> {
		    while (true) {
		    	try {
					key = reader.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				switch (key) {
				case 113:
					System.exit(0);
					break;
				case 66:
					move = "down";
					break;
				case 67:
					move = "right";
					break;
				case 68:
					move = "left";
					break;
				case 65:
					move = "up";
					break;
				default:
					break;
				}
		    }
		}).start();
		
		while (true) {
			position = changePos(move, position);
			List<List<String>> screen = createScreen(position, width, height);
			clearScreen();
			printScreen(screen, key);
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}
}

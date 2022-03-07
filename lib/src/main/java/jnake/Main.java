package jnake;

import java.util.ArrayList;
import java.util.List;
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
	public static int score = 0;
	public static int[][] snakeList;
	
	static String[][] createScreen(List<List<Integer>> positions, int columns, int rows) {
		String[][] screen = new String[rows][columns];
		for (int i = 0; i < rows; i++) {
			String[] row = new String[columns];
			for (int j = 0; j < columns; j++) {
				if (i == 0 || i == rows-1 || j == 0 || j == columns-1) {
					row[j] = "█";
				}
				else {
					row[j] = " ";
				}
			}
			screen[i] = row;
		}
		for (int i=0; i < positions.size(); i++) {
				screen[positions.get(i).get(1)][positions.get(i).get(0)] = "█";
		}
		return screen;
	}
	
	static void printScreen(String[][] layout, int score) {
		for (int i = 0; i < layout.length; i++) {
			String[] row = layout[i];
			for (int j = 0; j < row.length; j++) {
				System.out.print(row[j]);
			}
			System.out.print("\n");
		}
		System.out.println("Score: "+score+" (press 'q' to quit)");
	}
	
	static void clearScreen() {
	    System.out.print("\033[H\033[2J");  
	    System.out.flush(); 
	}
	
	static int[] getTermSize() throws IOException, InterruptedException {
		int[] dimensions = new int[2];
		String os = System.getProperty("os.name");
		if (os.contains("Linux")) {
			ProcessBuilder sizeCommand = new ProcessBuilder("/bin/sh", "-c", "stty size </dev/tty");
			final Process p = sizeCommand.start();
			BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String[] temp = out.readLine().split(" ");
			dimensions[0] = Integer.valueOf(temp[0]);
			dimensions[1] = Integer.valueOf(temp[1]);
		}
		else {
			dimensions[1] = 80;
			dimensions[0] = 24;
		}
		return dimensions;
	}
	
	static List<List<Integer>> changePos(String move, List<List<Integer>> positions, int[] maxSize) throws InterruptedException {
		if (move == "quit") {
			clearScreen();
			System.exit(0);
		}
		List<List<Integer>> pos = positions;
		for (int i=0; i<1; i++) {
			List<Integer> position = pos.get(i);
			int y = position.get(1);
			int x = position.get(0);
			List<Integer> newpos = new ArrayList<Integer>();
			if ((y == 1 && move == "up") || (x == 1 && move == "left") || (y+4 == maxSize[0] && move == "down") || (x+2 == maxSize[1] && move == "right")) gameOver();
			if (move == "up" && y > 0) y--; //index 1
			if (move == "down" && y+4 <= maxSize[0]) y++; //index 1
			if (move == "left" && x > 0) x--; //index 0
			if (move == "right" && x+2 <= maxSize[1]) x++; //index 0
			newpos.add(x);
			newpos.add(y);
			pos.add(0, newpos);
			if (pos.size() > score+1) pos.remove(pos.size()-1);
		}
		return pos;
	}
	
	public static void gameOver() throws InterruptedException {
		clearScreen();
		System.out.println("Game Over!");
		TimeUnit.SECONDS.sleep(2);
		System.exit(0);
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Random rand = new Random();
		int[] dimensions = getTermSize();
		int height = dimensions[0]-2;
		int width = dimensions[1];
		if (width > 120) {
			width = 120;
			dimensions[1] = width;
		}
		if (height > 50) {
			height = 48;
			dimensions[0] = height+2;
		}
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		List<Integer> initialPos = new ArrayList<Integer>();
		initialPos.add(rand.nextInt(width-1));
		initialPos.add(rand.nextInt(height-1));
		positions.add(initialPos);
		var term = TerminalBuilder.terminal();
		term.enterRawMode();
		var reader = term.reader();
		// New thread for keyboard input
		new Thread(() -> {
		    while (true) {
		    	try {
					key = reader.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				switch (key) {
				case 113:
					move = "quit";
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
				case 32:
					score++;
					break;
				default:
					break;
				}
		    }
		}).start();;
		while (true) {
			positions = changePos(move, positions, dimensions);
			String[][] screen = createScreen(positions, width, height);
			clearScreen();
			printScreen(screen, score);
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}
}
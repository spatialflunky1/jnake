package jnake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Thread;
import org.jline.terminal.TerminalBuilder;

public class Main {
	public static String move = "none";
	public static int score = 0;
	public static int[][] snakeList;
	public static int[] applePos;
	
	public static List<List<Integer>> changePos(String move, List<List<Integer>> positions, int[] maxSize) throws InterruptedException {
		if (move == "quit") {
			Screen.clearScreen();
			System.exit(0);
		}
		List<List<Integer>> pos = positions;
		if (pos.get(0).get(1) == applePos[1] && pos.get(0).get(0) == applePos[0]) {
			applePos = updateApplePos(maxSize[1], maxSize[0]);
			score++;
		}
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
			if (pos.size() > (score+1)*2) pos.remove(pos.size()-1);
		}
		return pos;
	}
	
	public static void gameOver() throws InterruptedException {
		Screen.clearScreen();
		System.out.println("Game Over!");
		TimeUnit.SECONDS.sleep(2);
		System.exit(0);
	}
	
	public static int[] updateApplePos(int width, int height) {
		Random rand = new Random();
		int[] pos = {rand.nextInt(width-10)+5, rand.nextInt(height-10)+5};
		return pos;
	}
	
	public static void runGame() throws InterruptedException, IOException {
		// Get the dimensions for the game window (height,width)
		int[] dimensions = Screen.getTermSize();
		int height = dimensions[0]-2;
		int width = dimensions[1];
		
		// Generate random numbers for the initial position and apple position
		Random rand = new Random();
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		List<Integer> initialPos = new ArrayList<Integer>();
		applePos = updateApplePos(width, height);
		initialPos.add(rand.nextInt(width-10)+5);
		initialPos.add(rand.nextInt(height-10)+5);
		positions.add(initialPos);
		
		// Enter terminal raw mode for keyboard input
		var term = TerminalBuilder.terminal();
		term.enterRawMode();
		var reader = term.reader();
		
		// New thread for keyboard input
		new Thread(() -> {
			int key = 0;
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
					if (move != "up") move = "down";
					break;
				case 67:
					if (move != "left") move = "right";
					break;
				case 68:
					if (move != "right") move = "left";
					break;
				case 65:
					if (move != "down") move = "up";
					break;
				default:
					break;
				}
		    }
		}).start();
		
		// Running loop of the program
		int rate = 100;
		String bgd_color="";
		String snk_color="";
		String apl_color="";
		String bdr_color="";
		try {
			List<Integer> configuration = Config.readConfig();
			bgd_color = Colors.background_colors[configuration.get(0)];
			snk_color = Colors.colors[configuration.get(1)];
			apl_color = Colors.colors[configuration.get(2)];
			bdr_color = Colors.colors[configuration.get(3)];
		}
		catch (FileNotFoundException e) {
			System.out.println("Config file not found, run with 'config' parameter before before first time run");
			System.exit(0);
		}
		while (true) {
			positions = changePos(move, positions, dimensions);
			String[][] screen = Screen.createScreen(positions, width, height, applePos, snk_color, apl_color, bgd_color, bdr_color);
			Screen.clearScreen();
			Screen.printScreen(screen, score, false);
			TimeUnit.MILLISECONDS.sleep(rate);
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		if (args.length > 0) {
			if (args[0].toLowerCase().equals("config")) {
				Config.startConfig();
			}
			if (args[0].toLowerCase().equals("help")) {
				System.out.println("Jnake v1.0");
				System.out.println("---------------------------------");
				System.out.println("java -jar {filepath} [arguments]");
				System.out.println("\narguments:");
				System.out.println("help   - displays this list");
				System.out.println("config - runs the configuration wizard");
			}
		}
		else {
			runGame();
		}
	}
}

package jnake;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
	
	static int[] setNewLocation(int[] location, KeyEvent e) {
		int[] newpos = location;
		int keycode = e.getKeyCode();
		System.out.println();
		// Left
		if (keycode == KeyEvent.VK_LEFT) {newpos[0] -= 1;}
		// Up
		if (keycode == KeyEvent.VK_UP) {newpos[1] -= 1;}
		// Down
		if (keycode == KeyEvent.VK_DOWN) {newpos[0] -= 1;}
		// Right
		if (keycode == KeyEvent.VK_RIGHT) {newpos[0] += 1;}
		return newpos;
	}
	
	static String getTermSize() throws IOException, InterruptedException {
		ProcessBuilder sizeCommand = new ProcessBuilder("/bin/sh", "-c", "stty size </dev/tty");
		final Process p = sizeCommand.start();
		BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
		return out.readLine();
		
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Random rand = new Random();
		String[] dimensions = getTermSize().split(" ");
		int height = Integer.valueOf(dimensions[0])-2;
		int width = Integer.valueOf(dimensions[1]);
		if (width > 100) width = 100;
		if (height > 50) height = 50;
		System.out.println(width);
		for (int i = 0; i < 10; i++) {
			int[] position = {rand.nextInt(width-1),rand.nextInt(23)};
			List<String>[] screen = createScreen(position, width, height);
			clearScreen();
			printScreen(screen);
			TimeUnit.MILLISECONDS.sleep(500);
		}
	}
}

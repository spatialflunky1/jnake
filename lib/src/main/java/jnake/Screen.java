package jnake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Screen {
	public static String[][] createScreen(List<List<Integer>> positions, int columns, int rows, int[] applePos) {
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
				screen[positions.get(i).get(1)][positions.get(i).get(0)] = Colors.GREEN+"█"+Colors.ANSI_RESET;
		}
		screen[applePos[1]][applePos[0]] = Colors.RED+"█"+Colors.ANSI_RESET;
		return screen;
	}
	
	public static void printScreen(String[][] layout, int score) {
		for (int i = 0; i < layout.length; i++) {
			String[] row = layout[i];
			for (int j = 0; j < row.length; j++) {
				System.out.print(row[j]);
			}
			System.out.print("\n");
		}
		System.out.println("Score: "+score+" (press 'q' to quit)");
	}
	
	public final static void clearScreen() {
	    System.out.println("\033[H\033[2J");
        }
	
	public static int[] getTermSize() throws IOException, InterruptedException {
		int[] dimensions = new int[2];
		String os = System.getProperty("os.name");
		if (os.contains("Linux")) {
			ProcessBuilder sizeCommand = new ProcessBuilder("/bin/sh", "-c", "stty size </dev/tty");
			final Process p = sizeCommand.start();
			BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String[] temp = out.readLine().split(" ");
			dimensions[0] = Integer.valueOf(temp[0]);
			dimensions[1] = Integer.valueOf(temp[1]);
			if (dimensions[1] > 120) {
				dimensions[1] = 120;
				dimensions[1] = dimensions[1];
			}
			if (dimensions[0] > 50) {
				dimensions[0] = 48;
				dimensions[0] = dimensions[0]+2;
			}
		}
		else {
			dimensions[1] = 80;
			dimensions[0] = 24;
		}
		return dimensions;
	}
}

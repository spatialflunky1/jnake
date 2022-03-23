package jnake;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import java.lang.Thread;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Config {
	public static int current = 0;
	public static String mode = "radio";
	public static int num = 0;
	public static String select = "ok";
	public static int optLen = 0;
	public static String[] selections = {"Ok", "Cancel"};
	public static String[] opts = new String[5];

	public static String[][] createScreen(List<String> texts, int columns, int rows) {
		String[][] screen = new String[rows][columns];
		for (int i = 0; i < rows; i++) {
			String[] row = new String[columns];
			for (int j = 0; j < columns; j++) {
				row[j] = Colors.BLUE+"█";
			}
			screen[i] = row;
		}
		if (texts.isEmpty()) return new String[0][0];
		else {
			for (int i=0; i < 2; i++) {
				String[] firstRows = screen[i];
				for (int j=0; j < texts.get(i).length(); j++) {
					firstRows[(j+columns/2)-(texts.get(i).length()/2)] = Colors.WHITE_BACKGROUND+Colors.RED+String.valueOf(texts.get(i).charAt(j));
				}
			}
			
			for (int i=2; i < texts.size(); i++) {
				String[] row = screen[((rows/2)+(i*2))-texts.size()];
				if ((i-2) == num) row[(columns/2)-4] = Colors.WHITE_BACKGROUND+" "+Colors.BLUE_BACKGROUND;
				else row[(columns/2)-4] = Colors.ANSI_RESET+" "+Colors.BLUE_BACKGROUND;
				for (int j=0; j < texts.get(i).length(); j++) {
					row[(j+(columns/2))] = Colors.BLUE_BACKGROUND+Colors.BLACK+String.valueOf(texts.get(i).charAt(j));
				}
			}
			
			
			int multiplier = 1;
			for (int i=0; i < selections.length; i++) {
				String[] lastRow = screen[screen.length-1];
				for (int j=0; j < selections[i].length(); j++) {
					if ((select == "ok" && i==0) || (select == "cancel" && i==1)) lastRow[(j+((columns/4)*multiplier))] = Colors.WHITE_BACKGROUND+Colors.BLACK+String.valueOf(selections[i].charAt(j))+Colors.BLUE_BACKGROUND;
					else lastRow[(j+((columns/4)*multiplier))] = Colors.ANSI_RESET+String.valueOf(selections[i].charAt(j))+Colors.BLUE_BACKGROUND;
				}
				multiplier+=2;
			}
		}
		
		return screen;
	}
	
	public static List<String> getOptions() {
		List<String> options = new ArrayList<String>();
		switch (current) {
		case 0:
			Collections.addAll(options, "Colors:", "Background", "Black", "Red", "Green", "Yellow", "Blue", "Magenta", "Cyan", "White");
			break;
		case 1:
			Collections.addAll(options, "Colors:", "Snake", "Black", "Red", "Green", "Yellow", "Blue", "Magenta", "Cyan", "White");
			break;
		case 2:
			Collections.addAll(options, "Colors:", "Apple", "Black", "Red", "Green", "Yellow", "Blue", "Magenta", "Cyan", "White");
			break;
		case 3:
			Collections.addAll(options, "Colors:", "Border", "Black", "Red", "Green", "Yellow", "Blue", "Magenta", "Cyan", "White");
			break;
		default:
			break;
		}
		return options;
	}
	
	public static void writeConfig() throws IOException {
		File config = new File("Config.conf");
		config.delete();
		BufferedWriter writer = new BufferedWriter(new FileWriter("config.conf"));
		for (int  i=0; i < opts.length; i++) {
		    writer.write(opts[i]+"\n");  
		}
		writer.close();
	}
	
	public static void refreshScreen() throws IOException, InterruptedException {
		int[] dimensions = Screen.getTermSize();
		List<String> options = getOptions();
		optLen = options.size();
		String[][] screen = createScreen(options, dimensions[1], dimensions[0]-3);
		Screen.clearScreen();
		Screen.printScreen(screen, 0, true);
	}
	
	public static void startConfig() throws IOException, InterruptedException {
		Terminal term = TerminalBuilder.terminal();
		term.enterRawMode();
		NonBlockingReader reader = term.reader();
		Thread keyInput = new Thread(new Runnable() {
			public void run() {
				boolean running = true;
				int key=0;
			    while (running) {
			    	try {
						key = reader.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
					switch (key) {
					case 66:
						if (num < optLen-3) num++;
						if (mode.equals("radio")) {
							try {refreshScreen();}
							catch (IOException | InterruptedException e) {e.printStackTrace();}
						}
						break;
					case 67:
						select = "cancel";
						if (mode.equals("radio")) {
							try {refreshScreen();}
							catch (IOException | InterruptedException e) {e.printStackTrace();}
						}
						break;
					case 68:
						select = "ok";
						if (mode.equals("radio")) {
							try {refreshScreen();}
							catch (IOException | InterruptedException e) {e.printStackTrace();}
						}
						break;
					case 65:
						if (num > 0) num--;
						if (mode.equals("radio")) {
							try {refreshScreen();}
							catch (IOException | InterruptedException e) {e.printStackTrace();}
						}
						break;
					case 13:
						if (select == "cancel") {
							System.out.println(Colors.ANSI_RESET);
							Screen.clearScreen();
							System.exit(0);
						}
						if (select == "ok") {
							if (current == 3) {
								running = false;
								System.out.println(Colors.ANSI_RESET);
								Screen.clearScreen();
								
							}
							opts[current] = String.valueOf(num);
							num = 0;
							current++;
							if (current != 4) {
								if (mode.equals("radio")) {
									try {refreshScreen();}
									catch (IOException | InterruptedException e) {e.printStackTrace();}
								}
							}
						}
						break;
					default:
						break;
					}
			    }
			    try {
					term.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			    mode = "text";
			}
		});
		keyInput.start();
		// Must be run twice for background to fill
		refreshScreen();
		refreshScreen();
		// Must wait or scanner doesn't work
		while (mode.equals("radio")) TimeUnit.MILLISECONDS.sleep(1000);
		keyInput.interrupt();
        // getInput gives warning that it is never closed but if you
        @SuppressWarnings("resource")
        Scanner getInput = new Scanner(System.in);
        System.out.print("Enter Game Speed (lower is faster) (100): ");
        String input = getInput.nextLine();
        if (input.isEmpty()) input = "100";
        opts[4] = input;
        writeConfig();
	}
	
	public static List<Integer> readConfig() throws IOException {
		List<Integer> config_options = new ArrayList<Integer>();
		BufferedReader reader = new BufferedReader(new FileReader("config.conf"));
		String line = reader.readLine();
		while (line != null) {
			config_options.add(Integer.valueOf(line));
			line = reader.readLine();
		}
		reader.close();
		return config_options;
	}
}
		

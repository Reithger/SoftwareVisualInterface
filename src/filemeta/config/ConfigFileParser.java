package filemeta.config;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class ConfigFileParser {

	public final static String ENTRY_EQUAL_SYMBOL = " = ";
	public final static String ENTRY_VALUE_END_SYMBOL = "</;>";
	
	public static String getContents(File f, String entryName) throws IOException{
		Scanner sc = new Scanner(f);
		String line = "";
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			if(!line.contains("#")) {
				String[] use = line.split(ENTRY_EQUAL_SYMBOL);
				String name = use[0];
				if(name.equals(entryName)) {
					String out = use[1];
					while(!out.contains(ENTRY_VALUE_END_SYMBOL)) {
						line = sc.nextLine();
						out += line;
					}
					sc.close();
					return out.replaceAll(ENTRY_VALUE_END_SYMBOL + ".*", "");
				}
			}
		}
		sc.close();
		return null;
	}
	
	public static boolean setContents(File f, String entryName, String newValue) throws IOException{
		Scanner sc = new Scanner(f);
		String line = "";
		String out = "";
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			if(!line.contains("#")) {
				String[] use = line.split(ENTRY_EQUAL_SYMBOL);
				String name = use[0];
				if(name.equals(entryName)) {
					out += entryName + ENTRY_EQUAL_SYMBOL + newValue + ENTRY_VALUE_END_SYMBOL;
				}
				else {
					out += line;
				}
			}
			else {
				out += line;
			}
			out += "\n";
		}
		sc.close();
		f.delete();
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.writeBytes(out);
			raf.close();
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("Failure to write config file: " + f.getAbsolutePath() + " with contents:\n" + out);
			return false;
		}
	}
	
}
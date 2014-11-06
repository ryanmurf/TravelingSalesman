package algorithms.designProblem3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Input {
	private Path filePath;
	private String fileName;
	private int nCities;
	private int[][] adjMatrix;
	private String[] names;
	
	public Input(String filePath) {
		this.filePath = Paths.get(filePath);
		if(Files.notExists(this.filePath)) {
			System.out.println("File does not exist. File: "+filePath);
			System.exit(1);
		}
		fileName = this.filePath.getFileName().toString();
		read();
	}
	
	private void read() {
		List<String> lines = null;
		try {
			 lines = Files.readAllLines(this.filePath);
		} catch (IOException e) {
			System.out.println("File: "+this.fileName+" could not be opened.");
			System.exit(1);
			e.printStackTrace();
		}
		
		nCities = Integer.valueOf(lines.get(0).split("[ \t]+")[0]);
		lines.remove(0);
		names = lines.get(0).trim().split("[ \t]+");
		lines.remove(0);
		adjMatrix = new int[nCities][nCities];
		int i=0;
		for (String line : lines) {
			line = line.trim();
			line = line.replaceFirst(names[i]+" ", "");
			String[] values = line.split("[ \t]+");
			for(int j=0; j<values.length; j++) {
				if(values[j].compareTo("-")==0)
					adjMatrix[i][j] = -1;
				else
					adjMatrix[i][j] = Integer.valueOf(values[j]);
			}
			i++;
		}
	}
	
	public int[][] getAdjMatrix() {
		return this.adjMatrix;
	}
	public String[] getNames() {
		return this.names;
	}
}

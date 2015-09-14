package selcpkg;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {

	public static void main(String[] args) {
		int chunkSize = 10;
		String myText = "Three swiss witches,\n"
				+ "which wished to be switched swiss witches,\n"
				+ "Watch three swiss Swatch watch switches.\n"
				+ "Which swiss witch,\n"
				+ "Which wishes to be a switched swiss witch,\n"
				+ "Wishes to watch which swiss Swatch switch?";

		try {
			FileOutputStream fo = new FileOutputStream("mychunk.txt");
			ChunkEncodeStream ce = new ChunkEncodeStream(chunkSize, fo);
			
			ce.write(myText.getBytes());
			ce.close();

			FileInputStream fi = new FileInputStream("mychunk.txt");
			ChunkDecodeStream cd = new ChunkDecodeStream(fi);
			byte[] b = new byte[50];
			
			// Read and print first 50 bytes
			cd.read(b);
			for (byte c : b) {
				System.out.print((char) c);
			}
			
			// Read and print the rest
			int c;
			while ((c = cd.read()) > 0) {
				System.out.print((char) c);
			}
			
			cd.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

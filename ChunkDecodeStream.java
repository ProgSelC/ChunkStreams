import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkDecodeStream extends InputStream {
	private ByteArrayInputStream buf;
	private InputStream is;

	public ChunkDecodeStream(InputStream is) {
		this.is = is;
	}

	private int readChunk() throws IOException {
		StringBuilder hexSize = new StringBuilder();
		int size;
		boolean prevCR = false;

		while (true) {
			char c = (char) is.read();
			if (c == '\r') {
				prevCR = true;
			} else if (c == '\n' && prevCR) {
				if (hexSize.length() >= 0) {
					size = Integer.parseInt(hexSize.toString(), 16);
					break;
				} else {
					throw new IOException("Incorrect input data");
				}
			} else {
				hexSize.append(c);
			}
		}

		if (size == 0) {
			return -1;
		} else {
			byte[] b = new byte[size];

			if (is.read(b) >= 0) {

				is.read(new byte[2]);

				buf = new ByteArrayInputStream(b);

				return size;
			} else {
				throw new IOException("Incorrect input data");
			}
		}
	}

	@Override
	public int read() throws IOException {
		int result;
		if (buf == null) {
			if (readChunk() == -1)
				return -1;
		}
		if ((result = buf.read()) == -1) {
			if (readChunk() == -1){
				return -1;
			} else {
				result = buf.read();
			}
		}

		return result;
	}
	
	@Override
	public void close() throws IOException {
		is.close();
	}
}

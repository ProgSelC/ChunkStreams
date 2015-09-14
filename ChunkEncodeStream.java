import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ChunkEncodeStream extends OutputStream {
	private int chunkSize;
	private OutputStream os;
	private ByteArrayOutputStream buf;

	public ChunkEncodeStream(int chunkSize, OutputStream os) {
		super();
		this.chunkSize = chunkSize;
		this.os = os;
		this.buf = new ByteArrayOutputStream();
	}

	private void checkBuf() throws IOException {
		if (buf.size() == chunkSize) {
			outFilledChunk();
		}
	}

	private void outFilledChunk() throws IOException {
		os.write(Integer.toHexString(buf.size()).getBytes());
		os.write("\r\n".getBytes());
		buf.writeTo(os);
		os.write("\r\n".getBytes());
		buf.reset();
	}

	@Override
	public void write(int b) throws IOException {
		buf.write(b);
		checkBuf();
	}

	@Override
	public void close() throws IOException {
		outFilledChunk();
		os.write("0\r\n\r\n".getBytes());
		os.close();
	}
}

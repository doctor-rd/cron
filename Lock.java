import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Lock {
	private final FileChannel fc;
	private FileLock l = null;

	public Lock( String filename ) throws IOException {
		File file = new File( filename );
		file.createNewFile();
		FileOutputStream is = new FileOutputStream( file );
		fc = is.getChannel();
	}

	public synchronized boolean lock() throws IOException {
		if( l != null )
			if( l.isValid() )
				return true;
		l = fc.tryLock();
		return l != null;
	}

	public synchronized void release() throws IOException {
		if( l != null )
			l.release();
	}
}

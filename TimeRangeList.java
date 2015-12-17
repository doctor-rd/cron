import java.util.LinkedList;

public class TimeRangeList {
	private LinkedList<TimeRange> timeRanges = new LinkedList<TimeRange>();
	public final TimeRange boundary;

	public TimeRangeList( TimeRange b ) {
		boundary = b;
	}

	public void add( TimeRange t ) throws Exception {
		if( !boundary.contains( t ) )
			throw new Exception( "range exceeding boundary" );
		timeRanges.add( t );
	}

	public boolean contains( int t ) {
		for( TimeRange i : timeRanges )
			if( i.contains( t ) )
				return true;
		return false;
	}

	public int next( int now ) {
		int result = -1;

		for( TimeRange i : timeRanges ) {
			int next = i.next( now );
			if( next != -1 && ( next < result || result == -1 ) )
				result = next;
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		boolean sep = false;

		for( TimeRange i : timeRanges ) {
			if( sep )
				res.append( ',' );
			res.append( i.toString() );
			sep = true;
		}
		return res.toString();
	}
}

public class TimeRange {
	public final int start;
	public final int end;

	public TimeRange( int s, int e ) throws Exception {
		start = s;
		end = e;
		if( start < 0 || end < 0 || start > end )
			throw new Exception( "range not valid" );
	}

	public boolean contains( int t ) {
		return ( t >= start ) && ( t <= end );
	}

	public boolean contains( TimeRange t ) {
		return contains( t.start ) && contains( t.end );
	}

	public int next( int now ) {
		if( contains( now ) )
			return now;
		if( now < start )
			return start;
		return -1;
	}

	@Override
	public String toString() {
		if ( start == end )
			return String.valueOf( start );
		else
			return start + "-" + end;
	}
}

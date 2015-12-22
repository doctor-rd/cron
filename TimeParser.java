import java.text.ParseException;

public class TimeParser {
	public final TimeRange boundary;

	public TimeParser( TimeRange boundary ) {
		this.boundary = boundary;
	}

	public TimeRangeList parse( String in ) throws Exception {
		TimeRangeList res = new TimeRangeList( boundary );
		String[] s = in.split( "," );
		for( String i : s )
			res.add( parseRange( i ) );
		return res;
	}

	public TimeRange parseRange( String in ) throws Exception {
		if( in.equals( "*" ) )
			return boundary;
		String[] s = in.split( "-", 2 );
		if( s.length > 1 )
			return new TimeRange( parseValue( s[0] ), parseValue( s[1] ) );
		else
			return new TimeRange( parseValue( s[0] ), parseValue( s[0] ) );
	}

	public int parseValue( String in ) throws ParseException {
		try {
			return Integer.valueOf( in );
		}
		catch( NumberFormatException e ) {
			throw new ParseException( e.toString(), 0 );
		}
	}
}

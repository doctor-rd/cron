public class Boundaries {
	public static final TimeRange MON = create( 1, 12 );
	public static final TimeRange DOW = create( 1, 7 );
	public static final TimeRange DOM = create( 1, 31 );
	public static final TimeRange MID = create( 0, 1439 );
	public static final TimeRange HOURS = create( 0, 23 );
	public static final TimeRange MINUTES = create( 0, 59 );

	private static TimeRange create( int a, int b ) {
		try {
			return new TimeRange( a, b );
		} catch( Exception e ) {
			throw new RuntimeException( e.getMessage() );
		}
	}
}

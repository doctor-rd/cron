import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MinuteInDayParser extends TimeParser {
	private static final SimpleDateFormat MINTE_IN_DAY = new SimpleDateFormat( "HH:mm" );

	private static TimeRange getBoundary() {
		try {
			return new TimeRange( 0, 1439 );
		} catch( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	public MinuteInDayParser() {
		super( getBoundary() );
	}

	@Override
	public int parseValue( String in ) throws ParseException {
		try {
			return super.parseValue( in );
		}
		catch( ParseException e ) {
			Calendar tmp = Calendar.getInstance();
			tmp.setTime( MINTE_IN_DAY.parse( in ) );
			return tmp.get( Calendar.HOUR_OF_DAY ) * 60 + tmp.get( Calendar.MINUTE );
		}
	}
}

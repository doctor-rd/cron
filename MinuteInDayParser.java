import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MinuteInDayParser extends TimeParser {
	private static final SimpleDateFormat MINUTE_IN_DAY = new SimpleDateFormat( "HH:mm" );

	public MinuteInDayParser() {
		super( Boundaries.MID );
	}

	@Override
	public int parseValue( String in ) throws ParseException {
		try {
			return super.parseValue( in );
		}
		catch( ParseException e ) {
			Calendar tmp = new GregorianCalendar( MINUTE_IN_DAY.getTimeZone() );
			tmp.setTime( MINUTE_IN_DAY.parse( in ) );
			return tmp.get( Calendar.HOUR_OF_DAY ) * 60 + tmp.get( Calendar.MINUTE );
		}
	}
}

import java.util.Calendar;

public class TabEntry {
	public TimeRangeList mon = null;
	public TimeRangeList dow = null;
	public TimeRangeList dom = null;
	public TimeRangeList mid = null;
	public TimeRangeList hours = null;
	public TimeRangeList minutes = null;

	public static int getDow( Calendar c ) {
		int res = c.get( Calendar.DAY_OF_WEEK ) - 1;
		if( res == 0 )
			return 7;
		else
			return res;
	}

	public boolean check( Calendar c ) {
		if( mon != null )
			if( !mon.contains( c.get( Calendar.MONTH ) + 1 ) )
				return false;
		if( dow != null )
			if( !dow.contains( getDow( c ) ) )
				return false;
		if( dom != null )
			if( !dom.contains( c.get( Calendar.DAY_OF_MONTH ) ) )
				return false;
		if( mid != null )
			if( !mid.contains( c.get( Calendar.HOUR_OF_DAY ) * 60 + c.get( Calendar.MINUTE ) ) )
				return false;
		if( hours != null )
			if( !hours.contains( c.get( Calendar.HOUR_OF_DAY ) ) )
				return false;
		if( minutes != null )
			if( !minutes.contains( c.get( Calendar.MINUTE ) ) )
				return false;
		return true;
	}
}

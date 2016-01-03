import java.util.Calendar;

public class TabEntry {
	public TimeRangeList mon = null;
	public TimeRangeList dow = null;
	public TimeRangeList dom = null;
	public TimeRangeList mid = null;
	public TimeRangeList hours = null;
	public TimeRangeList minutes = null;
	private static final RuntimeException boundex = new RuntimeException( "Invalid boundary. You must choose one from the class Boundaries." );

	private static int getDow( Calendar c ) {
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

	private static void resetTimeOfDay( Calendar c ) {
		c.set( Calendar.HOUR_OF_DAY, 0 );
		c.set( Calendar.MINUTE, 0 );
		c.set( Calendar.SECOND, 0 );
	}

	private static boolean nextMon( TimeRangeList t, Calendar c ) {
		if( t.boundary != Boundaries.MON )
			throw boundex;
		int now = c.get( Calendar.MONTH ) + 1;
		int next = t.next( now );
		if( next == now )
			return true;
		if( next == -1 ) {
			next = t.next( 1 );
			if( next == -1 )
				return false;
			c.add( Calendar.MONTH, 12 + next - now );
		}
		else
			c.add( Calendar.MONTH, next - now );
		c.set( Calendar.DAY_OF_MONTH, 1 );
		resetTimeOfDay( c );
		return true;
	}

	private static boolean nextDow( TimeRangeList t, Calendar c ) {
		if( t.boundary != Boundaries.DOW )
			throw boundex;
		int now = getDow( c );
		int next = t.next( now );
		if( next == now )
			return true;
		if( next == -1 ) {
			next = t.next( 1 );
			if( next == -1 )
				return false;
			c.add( Calendar.DAY_OF_YEAR, 7 + next - now );
		}
		else
			c.add( Calendar.DAY_OF_YEAR, next - now );
		resetTimeOfDay( c );
		return true;
	}

	private static boolean nextDom( TimeRangeList t, Calendar c ) {
		if( t.boundary != Boundaries.DOM )
			throw boundex;
		int now = c.get( Calendar.DAY_OF_MONTH );
		int next = t.next( now );
		if( next == now )
			return true;
		if( next == -1 || next > c.getActualMaximum( Calendar.DAY_OF_MONTH ) ) {
			next = t.next( 1 );
			if( next == -1 )
				return false;
			c.add( Calendar.MONTH, 1 );
			while( next > c.getActualMaximum( Calendar.DAY_OF_MONTH ) )
				c.add( Calendar.MONTH, 1 );
			c.set( Calendar.DAY_OF_MONTH, next );
		}
		else
			c.set( Calendar.DAY_OF_MONTH, next );
		resetTimeOfDay( c );
		return true;
	}

	private static boolean nextMid( TimeRangeList t, Calendar c ) {
		if( t.boundary != Boundaries.MID )
			throw boundex;
		int now = c.get( Calendar.HOUR_OF_DAY ) * 60 + c.get( Calendar.MINUTE );
		int next = t.next( now );
		if( next == now )
			return true;
		if( next == -1 ) {
			next = t.next( 0 );
			if( next == -1 )
				return false;
			c.add( Calendar.DAY_OF_YEAR, 1 );
			c.set( Calendar.HOUR_OF_DAY, 0 );
			c.set( Calendar.MINUTE, next );
		}
		else {
			c.set( Calendar.HOUR_OF_DAY, 0 );
			c.set( Calendar.MINUTE, next );
		}
		c.set( Calendar.SECOND, 0 );
		return true;
	}

	private static boolean nextHour( TimeRangeList t, Calendar c ) {
		if( t.boundary != Boundaries.HOURS )
			throw boundex;
		int now = c.get( Calendar.HOUR_OF_DAY );
		int next = t.next( now );
		if( next == now )
			return true;
		if( next == -1 ) {
			next = t.next( 0 );
			if( next == -1 )
				return false;
			c.add( Calendar.DAY_OF_YEAR, 1 );
			c.set( Calendar.HOUR_OF_DAY, next );
		}
		else
			c.set( Calendar.HOUR_OF_DAY, next );
		c.set( Calendar.MINUTE, 0 );
		c.set( Calendar.SECOND, 0 );
		return true;
	}

	private static boolean nextMinute( TimeRangeList t, Calendar c ) {
		if( t.boundary != Boundaries.MINUTES )
			throw boundex;
		int now = c.get( Calendar.MINUTE );
		int next = t.next( now );
		if( next == now )
			return true;
		if( next == -1 ) {
			next = t.next( 0 );
			if( next == -1 )
				return false;
			c.add( Calendar.HOUR_OF_DAY, 1 );
			c.set( Calendar.MINUTE, next );
		}
		else
			c.set( Calendar.MINUTE, next );
		c.set( Calendar.SECOND, 0 );
		return true;
	}

	public Calendar findNext( Calendar now, Calendar limit ) {
		Calendar res = (Calendar) now.clone();
		while( res.before( limit ) ) {
			if( check( res ) )
				return res;
			if( mon != null )
				if( !nextMon( mon, res ) )
					return null;
			if( dow != null )
				if( !nextDow( dow, res ) )
					return null;
			if( dom != null )
				if( !nextDom( dom, res ) )
					return null;
			if( mid != null )
				if( !nextMid( mid, res ) )
					return null;
			if( hours != null )
				if( !nextHour( hours, res ) )
					return null;
			if( minutes != null )
				if( !nextMinute( minutes, res ) )
					return null;
		}
		return null;
	}
}

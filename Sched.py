import java.util.Calendar as Calendar
import java.util.Locale as Locale
import java.util.TimeZone as TimeZone
import TimeParser
import Boundaries
import TabEntry
import MinuteInDayParser

class Sched( TabEntry ):
	def __init__( self, desc ):
		self.wait = desc.optInt( "wait", 1 )
		if desc.has( "mon" ):
			self.mon = TimeParser( Boundaries.MON ).parse( desc.getString( "mon" ) )
		if desc.has( "dow" ):
			self.dow = TimeParser( Boundaries.DOW ).parse( desc.getString( "dow" ) )
		if desc.has( "dom" ):
			self.dom = TimeParser( Boundaries.DOM ).parse( desc.getString( "dom" ) )
		if desc.has( "mid" ):
			self.mid = MinuteInDayParser().parse( desc.getString( "mid" ) )
		if desc.has( "hrs" ):
			self.hours = TimeParser( Boundaries.HOURS ).parse( desc.getString( "hrs" ) )
		if desc.has( "mins" ):
			self.minutes = TimeParser( Boundaries.MINUTES ).parse( desc.getString( "mins" ) )

	def dump( self, prefix = "" ):
		if self.mon != None:
			print prefix + "mon: \t" + self.mon.toString()
		if self.dow != None:
			print prefix + "dow: \t" + self.dow.toString()
		if self.dom != None:
			print prefix + "dom: \t" + self.dom.toString()
		if self.mid != None:
			print prefix + "mid: \t" + self.mid.toString()
		if self.hours != None:
			print prefix + "hrs: \t" + self.hours.toString()
		if self.minutes != None:
			print prefix + "mins:\t" + self.minutes.toString()
		print prefix + "wait:\t" + str( self.wait )

	def next( self, last, tz = TimeZone.getDefault() ):
		cal = Calendar.getInstance( tz, Locale.US )
		limit = cal.clone()
		limit.add( Calendar.YEAR, 1 )
		cal.setTime( last )
		cal.add( Calendar.MINUTE, self.wait )
		n = self.findNext( cal, limit )
		if n == None:
			return None
		return n.getTime()

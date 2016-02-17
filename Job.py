import java.util.Date as Date
import java.util.TimeZone as TimeZone
import org.json.JSONObject as JSONObject
from Sched import Sched

class Job:
	def __init__( self, desc, tz = TimeZone.getDefault() ):
		self.id = desc.getString( "id" )
		self.tz = tz
		self.last = None
		self.next = None
		self.scheds = []
		a = desc.getJSONArray( "scheds" )
		for i in range( a.length() ):
			self.scheds.append( Sched( a.getJSONObject( i ) ) )
		self.cmd = desc.getString( "cmd" )

	def dump( self, prefix = "", headprefix = "" ):
		print headprefix + "id:   \t" + self.id
		print headprefix + "tz:   \t" + self.tz.getID()
		if self.last != None:
			print headprefix + "last: \t" + self.last.toString()
		if self.next != None:
			print headprefix + "next: \t" + self.next.toString()
		for i in self.scheds:
			print headprefix + "sched:\t+"
			i.dump( prefix + "      \t|- " )
		print headprefix + "cmd:  \t" + self.cmd

	def setState( self, data ):
		self.last = None
		self.next = None
		if data.has( "last" ):
			self.last = Date( data.getInt( "last" ) * 1000 )
		if data.has( "next" ):
			self.next = Date( data.getInt( "next" ) * 1000 )

	def getState( self ):
		res = JSONObject()
		if self.last != None:
			res.put( "last", self.last.getTime() / 1000 )
		if self.next != None:
			res.put( "next", self.next.getTime() / 1000 )
		return res

	def pending( self ):
		if self.next == None:
			return False
		else:
			return not Date().before( self.next )

	def start( self ):
		self.next = Date()

	def stop( self ):
		self.next = None

	def resume( self ):
		if self.last == None:
			return
		next = None
		for i in self.scheds:
			tmp = i.next( self.last, self.tz )
			if tmp != None:
				if next == None:
					next = tmp
				elif tmp.before( next ):
					next = tmp
		self.next = next

	def done( self ):
		self.last = Date()
		self.resume()

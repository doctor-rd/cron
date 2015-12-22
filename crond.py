#!/usr/bin/env jython

import java.util.Calendar as Calendar
import subprocess, getopt, sys, time
import TimeParser
import TimeRange

class Cronjob:
	def __init__( self, desc ):
		s = desc.split( None, 5 )
		self.mm = TimeParser( TimeRange( 0, 59 ) ).parse( s[0] )
		self.hh = TimeParser( TimeRange( 0, 23 ) ).parse( s[1] )
		self.dom = TimeParser( TimeRange( 1, 31 ) ).parse( s[2] )
		self.mon = TimeParser( TimeRange( 1, 12 ) ).parse( s[3] )
		self.dow = TimeParser( TimeRange( 1, 7 ) ).parse( s[4] )
		self.cmd = s[5]

	def dump( self ):
		print "%s\t%s\t%s\t%s\t%s\t\t%s" % ( self.mm, self.hh, self.dom, self.mon, self.dow, self.cmd )

	def check( self, cal ):
		res = True
		res &= self.mm.contains( cal.get( cal.MINUTE ) )
		res &= self.hh.contains( cal.get( cal.HOUR_OF_DAY ) )
		res &= self.dom.contains( cal.get( cal.DAY_OF_MONTH ) )
		res &= self.mon.contains( cal.get( cal.MONTH ) + 1 )
		g = cal.get( cal.DAY_OF_WEEK ) - 1;
		if g == 0:
			g = 7
		res &= self.dow.contains( g )
		return res

opts, args = getopt.getopt( sys.argv[1:], "hlf:" )

filename = "crontab"
list = False

for o, a in opts:
	if o == '-l':
		list = True
	elif o == '-f':
		filename = a
	elif o == '-h':
		print sys.argv[0] + " [-options]"
		print "\t-h\n\t\tThis"
		print "\t-l\n\t\tDump parsed crontab and exit."
		print "\t-f FILENAME\n\t\tDefault: FILENAME=crontab"
		quit()

tab = []
f = open( filename )
for line in f:
	s = line.strip()
	if len( s ) > 0:
		if s[0] != "#":
			tab.append( Cronjob( s ) )
f.close()

if list:
	for i in tab:
		i.dump()
	quit()

while True:
	cal = Calendar.getInstance()
	print cal.getTime()
	for i in tab:
		if i.check( cal ):
			subprocess.call( [ "/bin/sh", "-c", i.cmd ] )
	wait = 60 - Calendar.getInstance().get( cal.SECOND )
	time.sleep( wait )

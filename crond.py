#!/usr/bin/env jython

import java.util.Calendar as Calendar
import subprocess, getopt, sys, time
import TimeParser
import Boundaries
import TabEntry

class Cronjob( TabEntry ):
	def __init__( self, desc ):
		s = desc.split( None, 5 )
		self.minutes = TimeParser( Boundaries.MINUTES ).parse( s[0] )
		self.hours = TimeParser( Boundaries.HOURS ).parse( s[1] )
		self.dom = TimeParser( Boundaries.DOM ).parse( s[2] )
		self.mon = TimeParser( Boundaries.MON ).parse( s[3] )
		self.dow = TimeParser( Boundaries.DOW ).parse( s[4] )
		self.cmd = s[5]

	def dump( self ):
		print "%s\t%s\t%s\t%s\t%s\t\t%s" % ( self.minutes, self.hours, self.dom, self.mon, self.dow, self.cmd )

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

with open( filename ) as f:
	for line in f:
		s = line.strip()
		if len( s ) > 0:
			if s[0] != "#":
				tab.append( Cronjob( s ) )

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
	wait = 60 - Calendar.getInstance().get( Calendar.SECOND )
	time.sleep( wait )

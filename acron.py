#!/usr/bin/env jython

import java.util.TimeZone as TimeZone
import org.json.JSONObject as JSONObject
import Lock
import subprocess, getopt, sys, os, tempfile
from Job import Job

def loadjson( filename ):
	with open( filename ) as f:
		return JSONObject( f.read() )

def savejson( filename, json ):
	( h, tmpname ) = tempfile.mkstemp( prefix = filename + ".tmp", dir = "." )
	with os.fdopen( h, "w" ) as f:
		f.write( json.toString() )
	os.rename( tmpname, filename )

if len( sys.argv ) < 2:
	print "Type\n# " + sys.argv[0] + " -h\nfor help."
	quit()

opts, args = getopt.getopt( sys.argv[1:], "hrlf:s:k:" )

configfn = "acroncfg.json"
lst = False
run = False
startlist = []
stoplist = []

for o, a in opts:
	if o == '-l':
		lst = True
	elif o == '-r':
		run = True
	elif o == '-f':
		configfn = a
	elif o == '-s':
		startlist.append( a )
	elif o == '-k':
		stoplist.append( a )
	elif o == '-h':
		print sys.argv[0] + " [-options]"
		print "\t-h\n\t\tThis"
		print "\t-r\n\t\tRun all pending jobs."
		print "\t-l\n\t\tDump all jobs."
		print "\t-f FILENAME\n\t\tDefault: FILENAME=acroncfg.json"
		print "\t-s ID\n\t\tStart a job."
		print "\t-k ID\n\t\tStop a job."
		quit()

config = loadjson( configfn )
tz = TimeZone.getDefault()
if config.has( "tz" ):
	tz = TimeZone.getTimeZone( config.getString( "tz" ) )
statefn = config.getString( "statefile" )
if os.path.isfile( statefn ):
	states = loadjson( statefn )
else:
	states = JSONObject()
jobsdesc = config.getJSONArray( "jobs" )
jobs = {}
for i in range( jobsdesc.length() ):
	j = Job( jobsdesc.getJSONObject( i ), tz )
	if states.has( j.id ):
		j.setState( states.getJSONObject( j.id ) )
	jobs[j.id] = j

for i in startlist:
	jobs[i].start()

for i in stoplist:
	jobs[i].stop()

if lst:
	for i in jobs:
		print "Job:\t+"
		jobs[i].dump( "    \t|  ", "    \t|- " )

if run:
	l = Lock( config.getString( "lockfile" ) )
	if not l.lock():
		print "busy"
		quit()
	for i in jobs:
		j = jobs[i]
		if j.pending():
			subprocess.call( [ "/bin/sh", "-c", j.cmd ] )
			j.done()

outstate = JSONObject()
for i in jobs:
	j = jobs[i]
	outstate.put( j.id, j.getState() )
savejson( statefn, outstate )

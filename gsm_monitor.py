#!/usr/bin/python
import serial,subprocess
import threading,time,urllib
import mpl3115
from SimpleXMLRPCServer import SimpleXMLRPCServer
from SimpleXMLRPCServer import SimpleXMLRPCRequestHandler

def getConnection():
    #return open("/dev/ttyUSB1","rw+")
    return serial.Serial("/dev/ttyUSB1", baudrate=115200, timeout=1.0)

def getRSSI():
    conn.write("AT+ZRSSI\r\n")
    for x in xrange(4):
        resp=repr(conn.readline())
        print resp.strip()
        if resp.find("+ZRSSI:") >= 0:
            return resp[9:-5].strip().split(',')
    return []
dial_process=None
out=""
conn = getConnection()
conn.write('\r\nAT\r\n')
for x in xrange(5):
    print repr(conn.readline())
pressure_sensor=None
pressure_sensor = mpl3115.MPL3115()
#for x in range(10):
#while True:
#    getRSSI()
#    time.sleep(5)

def disconnect():
    global dial_process
    
    if dial_process != None:
        dial_process.terminate()
        dial_process.wait()
        print "Disconnected!"
        returncode = dial_process.returncode
        dial_process = None
        return returncode
    return -1
def connect():
    global dial_process
    if dial_process == None:
        dial_process = subprocess.Popen('wvdial', stderr=subprocess.PIPE)
        
        output_thread = threading.Thread(target=getOutput)
        output_thread.start()
        return 1
    return -1
def getOutput():
    global out,dial_process
    while dial_process.poll() == None:
        ret = dial_process.stderr.readline()
        out=out+ret
        print "#"+str(len(ret))+ret
        print dial_process.poll()
        
def clearLog():
    global out
    out = ""
    return 0
def getLog():
  return urllib.quote(out)

def configureSensor(altitude,barometric_pressure):
    global calibration_pressure
    calibration_pressure = barometric_pressure
    pressure_sensor.config(altitude,barometric_pressure)
    return 1
def readSensor():
    return pressure_sensor.read_data()
#connect()
#getOutput(connect())
configureSensor(False,1020)
# Create server
server = SimpleXMLRPCServer(("0.0.0.0", 8000))
server.register_introspection_functions()

server.register_function(getRSSI, 'getRSSI')
server.register_function(getLog, 'getLog')
server.register_function(clearLog, 'clearLog')

server.register_function(connect, 'connect')
server.register_function(disconnect, 'disconnect')

server.register_function(configureSensor,'configureSensor')
server.register_function(readSensor, 'readSensor')
server.serve_forever()
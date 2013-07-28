import xmlrpclib,time
proxy = xmlrpclib.ServerProxy("http://192.168.7.2:8000/")
proxy.configureSensor(False,103000)
log_file = open("log.out","a")

while True:
    out=proxy.readSensor()
    log_file.write(str(out['time'])+","+str(out['pressure'])+","+str(out['temp'])+"\n")
    print out
    time.sleep(10)

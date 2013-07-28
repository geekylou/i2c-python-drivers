import smbus
import time
class HMC5883():
    ADDRESS = 0x1e
    
    def __init__(self):
        self.i2c = smbus.SMBus(1)
    def IIC_Read(self,reg):
        return self.i2c.read_byte_data(self.ADDRESS, reg)
    
    def IIC_Write(self,reg,data):
        self.i2c.write_byte_data(self.ADDRESS, reg, data)
            
    def config(self):
        self.IIC_Write(0x02, 0x00)
    def read(self):
        x = (self.IIC_Read(0x03) << 8) + self.IIC_Read(0x04)
        y = (self.IIC_Read(0x05) << 8) + self.IIC_Read(0x06)
        z = (self.IIC_Read(0x07) << 8) + self.IIC_Read(0x08)
        
        return (x,y,z)
if __name__ == "__main__":
    #log_file = open("log.out","a")
    gpio = HMC5883()
    gpio.config()

    while True:
        time.sleep(1.0)
        print gpio.read() 
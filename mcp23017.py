import smbus
import time
class MCP23017():
    ADDRESS = 0x27
    
    def __init__(self):
        self.i2c = smbus.SMBus(1)
    def IIC_Read(self,reg):
        return self.i2c.read_byte_data(self.ADDRESS, reg)
    
    def IIC_Write(self,reg,data):
        self.i2c.write_byte_data(self.ADDRESS, reg, data)
            
    def config(self):
        self.IIC_Write(0x00, 0x08)
        self.IIC_Write(0x01, 0x0)
        self.IIC_Write(0x12, 0xff)
    def write(self,data):
        self.IIC_Write(0x13,data)
    def read(self):
        return self.IIC_Read(0x12)

    def oled_write(self,reg,data):
        self.IIC_Write(0x12, data);
        self.IIC_Write(0x13, reg << 2);
        self.IIC_Write(0x13, 1 | reg << 2);
        self.IIC_Write(0x13, reg << 2);
    
    def oled_init(self):
        // Write magical init sequence for the display.
        delay(100);
        self.OLED_Write(0,8);
        
        delay(100);
        
        self.OLED_Write(0,0x1);// 61
        
        delay(100);
        self.OLED_Write(0,6);
        delay(100);
        self.OLED_Write(0,2);
        delay(100);
        self.OLED_Write(0,0xc);
        delay(100);
        self.OLED_Write(0,12);// 15 for curser
        delay(100);
        
        self.OLED_Write(0,57);
        delay(100);
        
        #End of magical init sequence for display
if __name__ == "__main__":
    log_file = open("log.out","a")
    gpio = MCP23017()
    gpio.config()

    while True:
        gpio.write(0x1)
        time.sleep(0.1)
        gpio.write(0x2)
        time.sleep(0.1)
        gpio.write(0x4)
        time.sleep(0.1)
        gpio.write(0x8)
        time.sleep(0.1)
        print ~gpio.read() & 0x7
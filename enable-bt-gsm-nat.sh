echo ttyO1_armhf.com >/sys/devices/bone_capemgr.9/slots
brctl addbr br0
sysctl net.ipv4.ip_forward=1
iptables -t nat -A POSTROUTING -o ppp0 -j MASQUERADE

#ifconfig eth0 down
ifconfig br0 192.168.77.1

udhcpd -f /home/ubuntu/udhcp.wlan0.conf &
python /home/ubuntu/gsm_monitor.py &
#echo nameserver 192.168.0.1 >/etc/resolv.conf
#route add default gw 192.168.7.1
service dnsmasq restart
bt-network -s nap br0
#wvdial
brctl addbr br0
sysctl net.ipv4.ip_forward=1
iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE

ifconfig br0 192.168.77.1

udhcpd -f udhcp.wlan0.conf &
#echo nameserver 192.168.0.1 >/etc/resolv.conf
#route add default gw 192.168.7.1
service dnsmasq restart
bt-network -s nap br0
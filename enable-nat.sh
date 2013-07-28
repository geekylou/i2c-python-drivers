sysctl net.ipv4.ip_forward=1
iptables -t nat -A POSTROUTING -o ppp0 -j MASQUERADE

hostapd hostapd-test.conf &
ifconfig wlan0 192.168.77.1
ifconfig eth0 down
udhcpd -f dhcp-wlan0.conf &
#echo nameserver 192.168.0.1 >/etc/resolv.conf
#route add default gw 192.168.7.1
service dnsmasq restart
sleep 10000000
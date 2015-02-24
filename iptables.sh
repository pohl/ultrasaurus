#!/bin/bash

#
# http://stackoverflow.com/questions/12464926/linux-in-ec2amazon-cannot-use-port-80-for-tomcat
#

/sbin/iptables -A INPUT -i eth0 -p tcp --dport 80 -j ACCEPT
/sbin/iptables -A INPUT -i eth0 -p tcp --dport 8080 -j ACCEPT
/sbin/iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080
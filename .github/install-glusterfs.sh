#!/usr/bin/env bash

# Add gluster repository
apt-get install software-properties-common -y
wget -O- https://download.gluster.org/pub/gluster/glusterfs/7/rsa.pub | apt-key add -
add-apt-repository ppa:gluster/glusterfs-7
apt-get update

# Install gluster
apt-get install glusterfs-server=7.4 -y

# Start gluster daemon
systemctl start glusterd
systemctl enable glusterd

gluster pool list

# The brick directory must exist 
mkdir /tmp/vol01

# Create the volume
gluster volume create vol01 $HOSTNAME:/tmp/vol01 force
gluster volume start vol1

# Mount the volume locally
mkdir -p /mnt/glusterfs
mount -t glusterfs $HOSTNAME:vol01 /mnt/glusterfs

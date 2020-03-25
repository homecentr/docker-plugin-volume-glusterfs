#!/usr/bin/env bash

if [[ $EUID -ne 0 ]]; then
    echo "This script should be run using sudo or as the root user"
    exit 1
fi

TAG=${1:-local}
PLUGIN_NAME="homecentr/glusterfs-volume-plugin:$TAG"

echo "Building plugin as $PLUGIN_NAME"

docker plugin rm -f $PLUGIN_NAME || true
docker rmi -f rootfsimage || true

set -e

docker build . -t rootfsimage
id=$(docker create rootfsimage true)
rm -rf ./build/rootfs
mkdir -p ./build/rootfs
docker export "$id" | tar -x -C ./build/rootfs
docker rm -vf "$id"
cp ./config.json ./build/config.json
docker plugin create $PLUGIN_NAME ./build
docker plugin enable $PLUGIN_NAME
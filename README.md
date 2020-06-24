[![Project status](https://badgen.net/badge/project%20status/stable%20%26%20actively%20maintaned?color=green)](https://github.com/homecentr/docker-plugin-volume-glusterfs/graphs/commit-activity) [![](https://badgen.net/github/label-issues/homecentr/docker-plugin-volume-glusterfs/bug?label=open%20bugs&color=green)](https://github.com/homecentr/docker-plugin-volume-glusterfs/labels/bug) [![](https://badgen.net/github/release/homecentr/docker-plugin-volume-glusterfs)](https://hub.docker.com/repository/docker/homecentr/glusterfs-volume-plugin)
[![](https://badgen.net/docker/pulls/homecentr/glusterfs-volume-plugin)](https://hub.docker.com/repository/docker/homecentr/glusterfs-volume-plugin) 
[![](https://badgen.net/docker/size/homecentr/glusterfs-volume-plugin)](https://hub.docker.com/repository/docker/homecentr/glusterfs-volume-plugin)

![CI/CD on master](https://github.com/homecentr/docker-plugin-volume-glusterfs/workflows/CI/CD%20on%20master/badge.svg)

# HomeCentr - glusterfs-volume-plugin

This plugin is based on the [original plugin by trajano](https://github.com/trajano/docker-volume-plugins/tree/master/glusterfs-volume-plugin) which was not maintained for a while. This version is ready to support the latest version of Gluster (currently v7).

## Usage

```yml
version: "3.7"
services:
  client1:
    image: some-image
    volumes:
      - "my_volume:/my-gluster-volume-mountpoint:rw"

volumes:
  my_volume:
    driver: homecentr/glusterfs-volume-plugin:latest
    driver_opts:
      servers: node1,node2,node3
    name: "vol01/tst" # First part (vol01) is the name of the volume, rest is path to the subdirectory inside of the volume which will be mounted
```

## Environment variables

| Name | Default value | Description |
|------|---------------|-------------|
| SERVERS |  | Comma delimited list of GlusterFS servers (i.e. the nodes of your cluster) |

## Exposed ports

The plugin does not expose any ports.

## Security
The container is regularly scanned for vulnerabilities and updated. Further info can be found in the [Security tab](https://github.com/homecentr/docker-plugin-volume-glusterfs/security).
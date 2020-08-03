FROM golang:1.14.4 as build

# Download the plugin source
RUN go get github.com/trajano/docker-volume-plugins/glusterfs-volume-plugin

# Build the plugin
WORKDIR /go/src/github.com/trajano/docker-volume-plugins/glusterfs-volume-plugin

RUN go get ./... && go build

FROM archlinux:20200705

# Copy the plugin binary from the build stage
COPY --from=build /go/src/github.com/trajano/docker-volume-plugins/glusterfs-volume-plugin/glusterfs-volume-plugin /usr/bin/glusterfs-volume-plugin

# Install glusterfs dependencies
RUN pacman -Sy -y --noconfirm glusterfs
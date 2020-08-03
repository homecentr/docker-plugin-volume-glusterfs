FROM golang:1.14.4 as build

# Download the plugin source
RUN go get github.com/trajano/docker-volume-plugins/glusterfs-volume-plugin

# Build the plugin
WORKDIR /go/src/github.com/trajano/docker-volume-plugins/glusterfs-volume-plugin

RUN go get ./... && go build

FROM centos:8.2.2004

# Copy the plugin binary from the build stage
COPY --from=build /go/src/github.com/trajano/docker-volume-plugins/glusterfs-volume-plugin/glusterfs-volume-plugin /usr/bin/glusterfs-volume-plugin

# Install glusterfs dependencies
RUN yum install -q -y \
      glusterfs-6.0-20.el8 \
      glusterfs-fuse-6.0-20.el8 \
      attr-2.4.48-3.el8 && \
    yum autoremove -q -y && \
    yum clean all && \
    rm -rf /var/cache/yum /var/log/anaconda /var/cache/yum /etc/mtab && \
    rm /var/log/lastlog
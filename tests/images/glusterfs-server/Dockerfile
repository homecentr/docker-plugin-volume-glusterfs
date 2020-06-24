FROM centos:8.2.2004

RUN yum install -y "wget-1.19.5-8.el8_1.1" && \
    yum install -y dnf-plugins-core && \
    yum config-manager --set-enabled PowerTools && \
    yum install -y centos-release-gluster7-1.0-2.el8 && \
    yum install -y attr-2.4.48-3.el8 python3-pyxattr-0.5.3-18.el8 && \
    yum install -y glusterfs-server-7.5-1.el8

ENTRYPOINT [ "glusterd", "--no-daemon", "--debug" ]
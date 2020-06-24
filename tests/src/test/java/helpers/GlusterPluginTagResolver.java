package helpers;

import io.homecentr.testcontainers.images.EnvironmentImageTagResolver;

public class GlusterPluginTagResolver extends EnvironmentImageTagResolver {
  public GlusterPluginTagResolver() {
    super("homecentr/glusterfs-volume-plugin:local");
  }
}
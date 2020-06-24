package helpers;

import com.github.dockerjava.api.DockerClient;
import org.testcontainers.DockerClientFactory;

import java.util.HashMap;

public class GlusterfsVolume {
    private final GlusterPluginTagResolver _tagResolver;
    private final GlusterfsServerContainer _serverContainer;
    private final DockerClient _dockerClient;
    private final String _volumeName;

    public GlusterfsVolume(GlusterfsServerContainer serverContainer, String subdirectory){
        _serverContainer = serverContainer;
        _tagResolver = new GlusterPluginTagResolver();
        _dockerClient = DockerClientFactory.instance().client();
        _volumeName = String.format("%s/%s", _serverContainer.getVolumeName(), subdirectory);
    }

    public void create() {
        HashMap<String, String> driverOpts = new HashMap<>();
        driverOpts.put("servers", "localhost");

        _dockerClient.createVolumeCmd()
                .withName(_volumeName)
                .withDriver(_tagResolver.getImageTag())
                .withDriverOpts(driverOpts)
                .exec();
    }

    public void close() {
        _dockerClient.removeVolumeCmd(_volumeName).exec();
    }

    public String getVolumeName() {
        return _volumeName;
    }
}

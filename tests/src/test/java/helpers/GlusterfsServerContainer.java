package helpers;

import io.homecentr.testcontainers.containers.GenericContainerEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GlusterfsServerContainer {
    private final String brickPath = "/tmp/vol01";
    private final String volumeName = "vol01";
    private final Logger logger = LoggerFactory.getLogger(GlusterfsServerContainer.class);

    private final GenericContainerEx _container;
    private final String _subdirectory;

    public GlusterfsServerContainer(String subdirectory) {
        _subdirectory = subdirectory;

        _container = new GenericContainerEx<>(createImage())
                .withPrivilegedMode(true)
                .withNetworkMode("host");
    }

    public void start() throws Exception {
        _container.start();
        _container.followOutput(new Slf4jLogConsumer(logger));
        _container.waitingFor(Wait.forLogMessage("TODO", 1));

        executeShellCommand(String.format("mkdir %s", brickPath));
        executeShellCommand(String.format("mkdir -p %s/%s", brickPath, _subdirectory));
        executeShellCommand(String.format("gluster volume create %s $(hostname):%s force", volumeName, brickPath));
        executeShellCommand(String.format("gluster volume start %s", volumeName));
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void close() {
        _container.close();
    }

    private void executeShellCommand(String command) throws Exception {
        logger.info("Executing command: {}", command);

        Container.ExecResult result = _container.executeShellCommand(command);

        if(result.getExitCode() != 0) {
            String message = String.format(
                    "Command execution ended with unexpected exit code: %d.\n  Output: %s\n Error output: %s",
                    result.getExitCode(),
                    result.getStdout(),
                    result.getStderr());

            throw new Exception(message);
        }
    }

    private ImageFromDockerfile createImage() {
        Path dockerFileDir = Paths.get(System.getProperty("user.dir"), "images", "glusterfs-server", "Dockerfile");

        return new ImageFromDockerfile()
                .withFileFromPath("Dockerfile", dockerFileDir);
    }
}

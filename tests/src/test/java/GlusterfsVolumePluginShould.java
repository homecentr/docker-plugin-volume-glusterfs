import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Volume;
import helpers.GlusterfsServerContainer;
import helpers.GlusterfsVolume;
import io.homecentr.testcontainers.containers.GenericContainerEx;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Container;

import static org.junit.Assert.assertEquals;

public class GlusterfsVolumePluginShould {
    private static final Logger logger = LoggerFactory.getLogger(GlusterfsVolumePluginShould.class);
    private static final String subdirectory = "test-dir";

    private static GlusterfsServerContainer _glusterfsServer;
    private static GlusterfsVolume _glusterfsVolume;
    private static GenericContainerEx _clientContainer1;
    private static GenericContainerEx _clientContainer2;

    @BeforeClass
    public static void setUp() throws Exception {
        _glusterfsServer = new GlusterfsServerContainer(subdirectory);
        _glusterfsServer.start();

        _glusterfsVolume = new GlusterfsVolume(_glusterfsServer, subdirectory);
        _glusterfsVolume.create();

        Volume volume = new Volume("/gfs");
        Bind volumeBind = new Bind(_glusterfsVolume.getVolumeName(), volume);

        _clientContainer1 = new GenericContainerEx<>("alpine")
                .withCommand("ash", "-c", "sleep 10h")
                .withCreateContainerCmdModifier(c -> c.withVolumes(volume).withBinds(volumeBind));

        _clientContainer2 = new GenericContainerEx<>("alpine")
                .withCommand("ash", "-c", "sleep 10h")
                .withCreateContainerCmdModifier(c -> c.withVolumes(volume).withBinds(volumeBind));

        _clientContainer1.start();
        _clientContainer2.start();
    }

    @AfterClass
    public static void cleanUp() {
        if(_clientContainer1 != null) {
            _clientContainer1.close();
        }

        if(_clientContainer2 != null) {
            _clientContainer2.close();
        }

        if(_glusterfsServer != null) {
            _glusterfsServer.close();
        }

        if(_glusterfsVolume != null) {
            _glusterfsVolume.close();
        }
    }

    @Test
    public void allowWritingDataInOneContainerAndReadingInOther() throws Exception {
        _clientContainer1.executeShellCommand("echo Hello > /gfs/test.txt");

        Container.ExecResult result = _clientContainer2.executeShellCommand("cat /gfs/test.txt");

        assertEquals("Hello", result.getStdout().trim());
    }
}
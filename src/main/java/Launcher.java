import org.apache.logging.log4j.core.config.yaml.YamlConfigurationFactory;
import org.yetiz.serv.HTTPCacheServer;

/**
 * Created by yeti on 16/2/4.
 */
public class Launcher {
    static {
        System.setProperty(YamlConfigurationFactory.CONFIGURATION_FILE_PROPERTY, "logger.yaml");

    }

    public static void main(String... args) {
        if (args.length != 1) {
            System.out.println("java -jar HTTPCacheService-1.0.jar <port>");
            System.exit(1);
        }
        new HTTPCacheServer().start(Integer.parseInt(args[0]));
    }
}

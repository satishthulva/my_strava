package com.satish.config;

import org.apache.commons.text.StringSubstitutor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Utility to substitute a variable with environment property or JVM argument
 *
 * @author satish.thulva.
 */
public class EnvironmentVariableResolver {
    private static final String CONFIG_FILE = "runner_log.yaml";
    private final StringSubstitutor resolver = new StringSubstitutor(new EnvironmentVariableLookup());

    public Config getConfig() throws IOException {
        return new Yaml().loadAs(resolve(), Config.class);
    }

    private String resolve() throws IOException {
        Map<String, Object> data = readAsMap();
        resolveInternal(data);
        return writeAsYaml(data);
    }

    private Map<String, Object> readAsMap() throws IOException {
        try (InputStream in = EnvironmentVariableResolver.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            return new Yaml().load(in);
        }
    }

    private void resolveInternal(Map<String, Object> data) {
        if (data == null) {
            return;
        }
        for (Map.Entry<String, Object> node : data.entrySet()) {
            if (node.getValue() instanceof Map) {
                resolveInternal((Map<String, Object>) node.getValue());
            } else if (node.getValue() instanceof String) {
                String value = resolver.replace(node.getValue());
                node.setValue(value);
            }
        }
    }

    private String writeAsYaml(Object data) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        return new Yaml(options).dump(data);
    }

}

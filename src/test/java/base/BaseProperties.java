package base;

import x_client.helpers.AuthHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseProperties {

    private final Properties properties;

    public BaseProperties() throws IOException {
        String appConfigPath = "src/test/resources/env.properties";
        properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
    }

    public Properties getProperties() {
        return properties;
    }

    public String getURL() {
        return properties.getProperty("URL");
    }

    public String getHeader() {return properties.getProperty("header");}

    public String getSwaggerURL() {
        return properties.getProperty("swaggerUrl");
    }

    public String getName() {
        return properties.getProperty("name");
    }

    public String getDescription() {
        return properties.getProperty("description");
    }

    public String getUserAdmin() {
        return properties.getProperty("userAdmin");
    }

    public String getPasswordAdmin() {
        return properties.getProperty("passwordAdmin");
    }

    public String getTokenAdmin(){
        AuthHelper authHelper = new AuthHelper(getUserAdmin(), getPasswordAdmin());

        return authHelper.authorization().orElse("");
    }

    public String getUserClient() {
        return properties.getProperty("userClient");
    }

    public String getPasswordClient() {
        return properties.getProperty("passwordClient");
    }

    public String getTokenClient(){
        AuthHelper authHelper = new AuthHelper(getUserClient(), getPasswordClient());

        return authHelper.authorization().orElse("");
    }
}

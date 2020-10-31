package de.flamestro.AgileIsTheNewOrange.util;

import org.testcontainers.containers.GenericContainer;

// https://www.patrick-gotthard.de/integrationstests-mit-spring-data-mongodb-und-testcontainers
public class MongoDBContainer extends GenericContainer<MongoDBContainer> {
    private static final int PORT = 27017;

    public MongoDBContainer(final String image) {
        super(image);
        this.addExposedPort(PORT);
    }

    public String getUri() {
        final String ip = this.getContainerIpAddress();
        final Integer port = this.getMappedPort(PORT);
        return String.format("mongodb://%s:%s/agile_is_the_new_orage", ip, port);
    }

    @Override
    public void stop() {
        // let the JVM handle the shutdown
    }

}

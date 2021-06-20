package de.flamestro.AgileIsTheNewOrange;

import java.util.UUID;

public class DataProvider {
    public static String generateRandomString() {
        return "test_" + UUID.randomUUID().toString();
    }
}

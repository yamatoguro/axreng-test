package com.axreng.backend.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    void testGenerateID() {
        ArrayList<String> ids = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            ids.add(Utils.generateID());
        }
        for (String id : ids) {
            if (id.length() == 8) {
                List<String> ids_repeated = new ArrayList<String>();
                ids.forEach(e -> {
                    if (e.equals(id))
                    ids_repeated.add(e);
                });
        assertTrue(ids_repeated.size() <= 1);
        }
        }
    }
}
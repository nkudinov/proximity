package com.proximity.proximity.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuadTreeNodeTest {


    @Test
    void customerShouldBeAddedToPointsList() {
        QuadTreeNode root = new QuadTreeNode();
        root.add(new Customer(0, "1", 1, 1));
        assertEquals(1, root.getPointsListSize());
    }

    @Test
    void only4customerShouldBeInAnode() {
        QuadTreeNode root = new QuadTreeNode();
        root.add(new Customer(1, "1", 1, 1));
        root.add(new Customer(2, "2", 1, 1));
        root.add(new Customer(3, "3", 1, 1));
        root.add(new Customer(4, "4", 1, 1));
        root.add(new Customer(5, "5", 1, 1));
        assertEquals(4, root.getPointsListSize());
        assertEquals(1, root.getTopRight().getPointsListSize());
    }

    @Test
    void searchShouldReturnAllPoints() {
        QuadTreeNode root = new QuadTreeNode();
        int count = 100;
        for (int i = 1; i <= count; i++) {
            root.add(new Customer(i, "" + i, 1, 1));
        }
        assertEquals(count, root.search(1, 1, 1).size());
    }

    @Test
    void searchNeedToFind2objects() {
        QuadTreeNode root = new QuadTreeNode();
        int count = 100;
        Random rand = new Random();

        // Generate random latitude in the range -90 to 90

        for (int i = 1; i <= count; i++) {
            // Generate random longitude in the range -90 to 90
            double latitude = rand.nextDouble() * 180 - 90;
            // Generate random longitude in the range -180 to 180
            double longitude = rand.nextDouble() * 360 - 180;
            root.add(new Customer(i, "" + i, latitude, longitude));
        }
        Customer first = new Customer(count + 1, "first", 52.515609864238364, 13.349090091794015);
        Customer second = new Customer(count + 2, "second", 52.5157468665224, 13.377064748077222);
        root.add(first);
        root.add(second);
        List<Customer> result = root.search(52.51480668595452, 13.361958546372763, 10.0);
        assertThat(result).containsAll(List.of(first));
    }
}
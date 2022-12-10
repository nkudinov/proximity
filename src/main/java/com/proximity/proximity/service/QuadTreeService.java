package com.proximity.proximity.service;

import com.proximity.proximity.domain.Customer;
import com.proximity.proximity.domain.QuadTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuadTreeService {
    private volatile QuadTreeNode root;

    private final CustomerService customerService;

    @Autowired
    public QuadTreeService(CustomerService customerService) {
        this.root = new QuadTreeNode();
        this.customerService = customerService;
    }

    @Scheduled(fixedDelay = 1000)
    void buildTree() {
        QuadTreeNode newRoot = new QuadTreeNode();
        for (Customer customer : customerService.findAll()) {
            newRoot.add(customer);
        }
        root = newRoot;
    }

    public List<Customer> search(double lat, double lon, double r) {
        return root.search(lat, lon, r);
    }
}

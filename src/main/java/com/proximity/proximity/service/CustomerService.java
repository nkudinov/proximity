package com.proximity.proximity.service;

import com.proximity.proximity.domain.Customer;
import com.proximity.proximity.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final GeoHashService geoHashService;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(GeoHashService geoHashService, CustomerRepository customerRepository) {
        this.geoHashService = geoHashService;
        this.customerRepository = customerRepository;
    }

    public void create(Customer customer) {
        customer.geoHash = geoHashService.encode(customer.lat, customer.lon, 12);
        customerRepository.create(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> find(final String geoHash) {
        return customerRepository.find(geoHash);
    }
}

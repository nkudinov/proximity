package com.proximity.proximity.web;

import com.proximity.proximity.domain.Customer;
import com.proximity.proximity.repository.CustomerRepository;
import com.proximity.proximity.service.CustomerService;
import com.proximity.proximity.web.dto.CustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    CustomerService customerService;

    @PostMapping("/{customer-id}")
    public void createCustomer(@PathVariable("customer-id") int customerId, @RequestBody CustomerRequest customerRequest) {
        Customer customer = new Customer(customerId, customerRequest.customerName, customerRequest.lat, customerRequest.lon);
        customerService.create(customer);
    }

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }
}

package com.proximity.proximity.repository;

import com.proximity.proximity.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepository {
    @Autowired
    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;

    public void create(Customer customer) {
        System.out.println("creating customer: " + customer);
        final String sql = "INSERT INTO customers(customer_id, customer_name, lat, lon, geohash) values(?,?,?,?,?)";
        int ret = jdbcTemplate.update(sql, customer.customerId, customer.customerName, customer.lat, customer.lon, customer.geoHash);
        System.out.println("inserted" + ret);
    }

    private Customer mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(rs.getInt("customer_id"), rs.getString("customer_name"), rs.getDouble("lat"), rs.getDouble("lon"));
    }

    public List<Customer> findAll() {
        final String sql = "select * from customers";
        return jdbcTemplate.query(sql, this::mapRowToCustomer);
    }

    public List<Customer> find(String geohash) {
        System.out.println("search by:"+geohash);
        final String sql = "select * from customers where geohash like ?";
        return jdbcTemplate.query(sql, this::mapRowToCustomer, geohash + "%");
    }
}

package com.proximity.proximity.web;

import com.proximity.proximity.domain.Customer;
import com.proximity.proximity.service.CustomerService;
import com.proximity.proximity.service.GeoHashService;
import com.proximity.proximity.service.QuadTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final GeoHashService geoHashService;
    private final QuadTreeService quadTreeService;
    CustomerService customerService;

    @Autowired
    public SearchController(GeoHashService geoHashService, QuadTreeService quadTreeService, CustomerService customerService) {
        this.geoHashService = geoHashService;
        this.quadTreeService = quadTreeService;
        this.customerService = customerService;
    }
    @GetMapping("/v1/")
    public List<Customer> search1(@RequestParam("lat") double lat, @RequestParam("lon") double lon) {
        return customerService.find(geoHashService.encode(lat, lon, 4));
    }
    @GetMapping("/v2/")
    public List<Customer> search2(@RequestParam("lat") double lat, @RequestParam("lon") double lon) {
        return quadTreeService.search(lat,lon,10);
    }
}

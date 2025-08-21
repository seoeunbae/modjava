package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Demand;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.DemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DemandService {

    private final DemandRepository demandRepository;

    @Autowired
    public DemandService(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    @Transactional
    public void createDemand(User user, Product product) {
        Demand demand = new Demand();
        demand.setUser(user);
        demand.setProduct(product);
        demandRepository.save(demand);
    }

    @Transactional(readOnly = true)
    public List<Demand> getDemandsByProduct(Product product) {
        return demandRepository.findByProduct(product);
    }

    @Transactional
    public void deleteDemand(User user, Product product) {
        demandRepository.deleteByUserAndProduct(user, product);
    }
}

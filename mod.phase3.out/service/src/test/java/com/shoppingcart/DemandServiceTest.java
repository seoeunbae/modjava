
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DemandServiceTest {

    @Mock
    private DemandRepository demandRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DemandService demandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddDemand_newDemand() {
        // Given
        String username = "testuser";
        String prodid = "prod123";
        int demandQty = 1;
        DemandPK demandPK = new DemandPK(username, prodid);
        Demand newDemand = new Demand(username, prodid, demandQty);

        when(demandRepository.findById(demandPK)).thenReturn(Optional.empty());
        when(userRepository.findById(username)).thenReturn(Optional.of(new User()));
        when(productRepository.findById(prodid)).thenReturn(Optional.of(new Product()));
        when(demandRepository.save(any(Demand.class))).thenReturn(newDemand);

        // When
        demandService.addDemand(username, prodid, demandQty);

        // Then
        verify(demandRepository).findById(demandPK);
        verify(userRepository).findById(username);
        verify(productRepository).findById(prodid);
        verify(demandRepository).save(any(Demand.class));
    }

    @Test
    public void testAddDemand_existingDemand() {
        // Given
        String username = "testuser";
        String prodid = "prod123";
        int demandQty = 1;
        DemandPK demandPK = new DemandPK(username, prodid);
        Demand existingDemand = new Demand(username, prodid, 1);

        when(demandRepository.findById(demandPK)).thenReturn(Optional.of(existingDemand));
        when(demandRepository.save(any(Demand.class))).thenReturn(existingDemand);

        // When
        demandService.addDemand(username, prodid, demandQty);

        // Then
        verify(demandRepository).findById(demandPK);
        verify(demandRepository).save(any(Demand.class));
    }

    @Test
    public void testRemoveDemand() {
        // Given
        String username = "testuser";
        String prodid = "prod123";
        DemandPK demandPK = new DemandPK(username, prodid);

        // When
        demandService.removeDemand(username, prodid);

        // Then
        verify(demandRepository).deleteById(demandPK);
    }

    @Test
    public void testGetDemandsByProdid() {
        // Given
        String prodid = "prod123";

        // When
        demandService.getDemandsByProdid(prodid);

        // Then
        verify(demandRepository).findByProdid(prodid);
    }

    @Test
    public void testDeleteDemands() {
        // Given
        List<Demand> demands = new ArrayList<>();

        // When
        demandService.deleteDemands(demands);

        // Then
        verify(demandRepository).deleteAll(demands);
    }
}

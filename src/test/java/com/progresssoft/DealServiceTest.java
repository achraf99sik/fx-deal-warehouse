package com.progresssoft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class DealServiceTest {

    @Mock
    private DealRepository dealRepository;

    @InjectMocks
    private DealService dealService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessValidNewDeal() throws SQLException {
        Deal validDeal = new Deal(
            UUID.randomUUID(),
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("100.00")
        );

        when(dealRepository.dealExists(validDeal.getDealId())).thenReturn(false);

        dealService.processDeal(validDeal);

        verify(dealRepository, times(1)).dealExists(validDeal.getDealId());
        verify(dealRepository, times(1)).saveDeal(validDeal);
    }

    @Test
    void testProcessDuplicateDeal() throws SQLException {
        Deal duplicateDeal = new Deal(
            UUID.randomUUID(),
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("100.00")
        );

        when(dealRepository.dealExists(duplicateDeal.getDealId())).thenReturn(true);

        dealService.processDeal(duplicateDeal);

        verify(dealRepository, times(1)).dealExists(duplicateDeal.getDealId());
        verify(dealRepository, never()).saveDeal(any(Deal.class));
    }

    @Test
    void testProcessInvalidDeal() throws SQLException {
        Deal invalidDeal = new Deal(
            UUID.randomUUID(),
            "US",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("100.00")
        );

        dealService.processDeal(invalidDeal);

        verify(dealRepository, never()).dealExists(any(UUID.class));
        verify(dealRepository, never()).saveDeal(any(Deal.class));
    }

    @Test
    void testProcessDealWithRepositorySaveError() throws SQLException {
        Deal dealWithError = new Deal(
            UUID.randomUUID(),
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("100.00")
        );

        when(dealRepository.dealExists(dealWithError.getDealId())).thenReturn(false);
        doThrow(new SQLException("Database connection lost")).when(dealRepository).saveDeal(dealWithError);

        dealService.processDeal(dealWithError);

        verify(dealRepository, times(1)).dealExists(dealWithError.getDealId());
        verify(dealRepository, times(1)).saveDeal(dealWithError);
    }

    @Test
    void testProcessDealWithRepositoryExistsError() throws SQLException {
        Deal dealWithError = new Deal(
            UUID.randomUUID(),
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("100.00")
        );

        doThrow(new SQLException("Database connection lost")).when(dealRepository).dealExists(dealWithError.getDealId());

        dealService.processDeal(dealWithError);

        verify(dealRepository, times(1)).dealExists(dealWithError.getDealId());
        verify(dealRepository, never()).saveDeal(any(Deal.class));
    }
}

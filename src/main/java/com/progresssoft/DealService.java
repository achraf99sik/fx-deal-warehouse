package com.progresssoft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DealService {

    private static final Logger logger = LoggerFactory.getLogger(DealService.class);
    private final DealRepository dealRepository;

    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public void processDeal(Deal deal) {
        try {
            DealValidator.validate(deal);
            logger.info("Deal {} validated successfully.", deal.getDealId());

            if (dealRepository.dealExists(deal.getDealId())) {
                logger.warn("Attempted to import duplicate deal with ID: {}", deal.getDealId());
                return;
            }

            dealRepository.saveDeal(deal);
            logger.info("Deal {} processed and saved successfully.", deal.getDealId());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error for deal {}: {}", deal != null ? deal.getDealId() : "null", e.getMessage());
        } catch (SQLException e) {
            logger.error("Database error processing deal {}: {}", deal != null ? deal.getDealId() : "null", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing deal {}: {}", deal != null ? deal.getDealId() : "null", e.getMessage(), e);
        }
    }
}

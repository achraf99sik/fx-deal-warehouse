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
            // 1. Validate the deal
            DealValidator.validate(deal);
            logger.info("Deal {} validated successfully.", deal.getDealId());

            // 2. Check for duplicates
            if (dealRepository.dealExists(deal.getDealId())) {
                logger.warn("Attempted to import duplicate deal with ID: {}", deal.getDealId());
                // As per requirement: "System should not import same request twice."
                // And "No rollback allowed, what every rows imported should be saved in DB."
                // So, we just log and skip if it's a duplicate.
                return;
            }

            // 3. Persist the deal
            dealRepository.saveDeal(deal);
            logger.info("Deal {} processed and saved successfully.", deal.getDealId());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error for deal {}: {}", deal != null ? deal.getDealId() : "null", e.getMessage());
            // Depending on requirements, you might re-throw a custom business exception
            // or return an error status. For now, just logging.
        } catch (SQLException e) {
            logger.error("Database error processing deal {}: {}", deal != null ? deal.getDealId() : "null", e.getMessage());
            // Handle database-specific errors.
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing deal {}: {}", deal != null ? deal.getDealId() : "null", e.getMessage(), e);
            // Catch any other unexpected exceptions.
        }
    }
}

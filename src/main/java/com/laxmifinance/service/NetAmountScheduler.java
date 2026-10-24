package com.laxmifinance.service;

import com.laxmifinance.repository.NetAmountRepository;
import com.laxmifinance.util.Dates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Replaces the legacy thread that was started on every login. Recalculates
 * the monthly net amount for every stored month plus the current month.
 */
@Slf4j
@Service
public class NetAmountScheduler {

    private final NetAmountRepository netAmountRepository;

    public NetAmountScheduler(NetAmountRepository netAmountRepository) {
        this.netAmountRepository = netAmountRepository;
    }

    @Scheduled(fixedDelay = 300_000, initialDelay = 60_000)
    public void recalculate() {
        try {
            Set<String> dates = new LinkedHashSet<>(netAmountRepository.findAllDates());
            dates.add(Dates.formatDb(LocalDate.now()));
            for (String date : dates) {
                if (date != null) {
                    netAmountRepository.upsert(date, netAmountRepository.netAmountForDate(date));
                }
            }
        } catch (Exception e) {
            log.warn("Monthly net amount recalculation failed: {}", e.getMessage());
        }
    }
}

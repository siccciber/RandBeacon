package com.example.beacon.interfac.domain.repository;

import java.time.ZonedDateTime;

import com.example.beacon.interfac.infra.PulseEntity;

public interface PulsesQueries {
    PulseEntity last(Long chain);
    PulseEntity first(Long chain);
    PulseEntity findByChainAndPulseId(Long chainIndex, Long pulseIndex);
    PulseEntity findByTimestamp(ZonedDateTime timeStamp);
}

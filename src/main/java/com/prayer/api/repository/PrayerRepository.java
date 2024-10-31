package com.prayer.api.repository;

import com.prayer.api.entity.Prayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrayerRepository extends JpaRepository<Prayer, Long> {}

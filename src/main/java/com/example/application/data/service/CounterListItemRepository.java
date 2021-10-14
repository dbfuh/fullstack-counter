package com.example.application.data.service;

import com.example.application.data.entity.CounterListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterListItemRepository extends JpaRepository<CounterListItem, Integer> {

}

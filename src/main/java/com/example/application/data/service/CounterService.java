package com.example.application.data.service;

import java.util.List;

import com.example.application.data.entity.CounterListItem;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;

@Service
public class CounterService {
  private CounterListItemRepository shoppingListItemRepo;

  public CounterService(
      CounterListItemRepository shoppingListItemRepo) {
    this.shoppingListItemRepo = shoppingListItemRepo;
  }

  @Lock(LockModeType.OPTIMISTIC)
  public CounterListItem saveItem(CounterListItem counterListItem) {
    return shoppingListItemRepo.save(counterListItem);
  }

  public List<CounterListItem> getShoppingList(){
    return shoppingListItemRepo.findAll();
  }

  public void deleteItem(CounterListItem item) {
    shoppingListItemRepo.delete(item);
  }
}

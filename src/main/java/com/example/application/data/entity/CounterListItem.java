package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Version;

import com.example.application.data.AbstractEntity;

@Entity
public class CounterListItem extends AbstractEntity {

  @Version
  private Long version;
  private int counter;

  public Long getVersion() {
    return version;
  }

  public int getCounter() {
    return counter;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }

}

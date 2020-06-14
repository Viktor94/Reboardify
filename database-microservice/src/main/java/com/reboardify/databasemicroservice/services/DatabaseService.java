package com.reboardify.databasemicroservice.services;

import com.reboardify.databasemicroservice.models.Employee;
import com.reboardify.databasemicroservice.models.Position;
import java.util.LinkedList;

public interface DatabaseService {

  LinkedList<String> getAuthorizedList();

  LinkedList<String> getQueueList();

  Boolean isInAuthorizedList(Employee employee);

  Boolean isInQueueList(Employee employee);

  void addToAuthorizedList(Employee employee);

  void addToQueueList(Employee employee);

  void removeFromAuthorizedList(Employee employee);

  void moveFirstFromQueueToAuthorized();

  Position getPosition(Employee employee);

  void dailyReset();
}

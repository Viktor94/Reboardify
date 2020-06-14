package com.reboardify.databasemicroservice.services;

import com.reboardify.databasemicroservice.models.Employee;
import java.util.LinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseServiceImplTest {

  private LinkedList<String> authorized;
  private LinkedList<String> queue;
  private final DatabaseService databaseService = new DatabaseServiceImpl();
  private Employee employee;

  @BeforeEach
  void setUp() {
    authorized = databaseService.getAuthorizedList();
    queue = databaseService.getQueueList();
    employee = new Employee("123");
  }

  @Test
  void addToAuthorizedList_OK() {
    Assertions.assertEquals(0, authorized.size());

    databaseService.addToAuthorizedList(employee);
    Assertions.assertEquals(1, authorized.size());
  }

  @Test
  void addToAuthorizedList_NULL_Employee_Not_Added() {
    Assertions.assertEquals(0, authorized.size());

    databaseService.addToAuthorizedList(null);
    Assertions.assertEquals(0, authorized.size());
  }

  @Test
  void addToQueueList_OK() {
    Assertions.assertEquals(0, queue.size());

    databaseService.addToQueueList(employee);
    Assertions.assertEquals(1, queue.size());
  }

  @Test
  void addToQueueList_NULL_Employee_Not_Added() {
    Assertions.assertEquals(0, queue.size());

    databaseService.addToQueueList(null);
    Assertions.assertEquals(0, queue.size());
  }

  @Test
  void isInAuthorizedList_True() {
    databaseService.addToAuthorizedList(employee);

    Assertions.assertTrue(databaseService.isInAuthorizedList(employee));
  }

  @Test
  void isInAuthorizedList_False() {
    Assertions.assertFalse(databaseService.isInAuthorizedList(employee));
  }

  @Test
  void isInQueueList_True() {
    databaseService.addToQueueList(employee);

    Assertions.assertTrue(databaseService.isInQueueList(employee));
  }

  @Test
  void isInQueueList_False() {
    Assertions.assertFalse(databaseService.isInQueueList(employee));
  }

  @Test
  void removeFromAuthorizedList_OK() {
    databaseService.addToAuthorizedList(employee);
    Assertions.assertTrue(databaseService.isInAuthorizedList(employee));

    databaseService.removeFromAuthorizedList(employee);
    Assertions.assertFalse(databaseService.isInAuthorizedList(employee));
  }

  @Test
  void removeFromAuthorizedList_NULL_Employee() {
    databaseService.addToAuthorizedList(employee);
    int listSize = authorized.size();
    databaseService.removeFromAuthorizedList(null);
    int listSizeAfterRemove = authorized.size();
    Assertions.assertEquals(listSize, listSizeAfterRemove);
  }

  @Test
  void moveFirstFromQueueToAuthorized_OK() {
    databaseService.addToAuthorizedList(employee);
    Employee employee2 = new Employee("456");
    databaseService.addToQueueList(employee2);
    databaseService.moveFirstFromQueueToAuthorized();

    Assertions.assertEquals(0, queue.size());
    Assertions.assertEquals(2, authorized.size());
    Assertions.assertEquals("456", authorized.getLast());
  }

  @Test
  void moveFirstFromQueueToAuthorized_QueueList_Empty() {
    Assertions.assertDoesNotThrow(databaseService::moveFirstFromQueueToAuthorized);
  }

  @Test
  void getPosition_OK() {
    databaseService.addToQueueList(employee);
    Employee employee2 = new Employee("456");
    databaseService.addToQueueList(employee2);

    Assertions.assertEquals(1, databaseService.getPosition(employee).getPosition());
    Assertions.assertEquals(2, databaseService.getPosition(employee2).getPosition());
  }

  @Test
  void getPosition_NULL_Employee() {
    Assertions.assertEquals(-1, databaseService.getPosition(null).getPosition());
  }

  @Test
  void dailyReset_Same_Day() {
    Employee employee2 = new Employee("456");
    databaseService.addToAuthorizedList(employee);
    databaseService.addToQueueList(employee2);
    databaseService.dailyReset();

    Assertions.assertTrue(databaseService.isInAuthorizedList(employee));
    Assertions.assertTrue(databaseService.isInQueueList(employee2));
  }
}

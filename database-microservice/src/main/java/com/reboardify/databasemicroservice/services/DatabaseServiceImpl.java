package com.reboardify.databasemicroservice.services;

import com.reboardify.databasemicroservice.models.Employee;
import com.reboardify.databasemicroservice.models.Position;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService {

  private final LinkedList<String> authorized = new LinkedList<>();
  private final LinkedList<String> queue = new LinkedList<>();
  private Date date = new Date();

  @Override
  public LinkedList<String> getAuthorizedList() {
    return authorized;
  }

  @Override
  public LinkedList<String> getQueueList() {
    return queue;
  }

  @Override
  public Boolean isInAuthorizedList(Employee employee) {
    return authorized.contains(employee.getId());
  }

  @Override
  public Boolean isInQueueList(Employee employee) {
    return queue.contains(employee.getId());
  }

  @Override
  public void addToAuthorizedList(Employee employee) {
    if (employee != null) {
      authorized.add(employee.getId());
    }
  }

  @Override
  public void addToQueueList(Employee employee) {
    if (employee != null) {
      queue.add(employee.getId());
    }
  }

  @Override
  public void removeFromAuthorizedList(Employee employee) {
    if (employee != null) {
      authorized.remove(employee.getId());
    }
  }

  @Override
  public void moveFirstFromQueueToAuthorized() {
    if (queue.size() > 0) {
      authorized.add(queue.getFirst());
      queue.removeFirst();
    }
  }

  @Override
  public Position getPosition(Employee employee) {
    if (employee != null) {
      return new Position(queue.indexOf(employee.getId()) + 1);
    }

    return new Position(-1);
  }

  @Override
  public void dailyReset() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    Date newDate = new Date();
    cal.setTime(newDate);
    int newDay = cal.get(Calendar.DAY_OF_MONTH);

    if (day != newDay) {
      resetAuthorizedList();
      resetQueueList();
      date = newDate;
    }
  }

  private void resetAuthorizedList() {
    authorized.clear();
  }

  private void resetQueueList() {
    queue.clear();
  }
}

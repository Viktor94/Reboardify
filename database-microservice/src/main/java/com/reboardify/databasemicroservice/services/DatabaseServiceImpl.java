package com.reboardify.databasemicroservice.services;

import com.reboardify.databasemicroservice.models.Employee;
import com.reboardify.databasemicroservice.models.Position;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService {

  /**
   * This list is used to store the authorized employees.
   */
  private final LinkedList<String> authorized = new LinkedList<>();
  /**
   * This list is used to store the employees that are in queue.
   */
  private final LinkedList<String> queue = new LinkedList<>();
  /**
   * This variable is used for the daily reset.
   */
  private Date date = new Date();

  @Override
  public LinkedList<String> getAuthorizedList() {
    return authorized;
  }

  /**
   * <p>This method is used to get the queue list</p>
   *
   * @return it returns the queu list
   */
  @Override
  public LinkedList<String> getQueueList() {
    return queue;
  }

  /**
   * <p>It checks if the employee is in the authorized list</p>
   *
   * @param employee employee contains the id of the employee
   * @return it returns true if the employee is in the list
   */
  @Override
  public Boolean isInAuthorizedList(Employee employee) {
    return authorized.contains(employee.getId());
  }

  /**
   * <p>This method checks if the employee is in the queue list</p>
   *
   * @param employee employee contains the id of the employee
   * @return it returns true if the employee is in the list
   */
  @Override
  public Boolean isInQueueList(Employee employee) {
    return queue.contains(employee.getId());
  }

  /**
   * <p>If the employee is not null it adds it to the authorized list</p>
   *
   * @param employee employee contains the id of the employee
   */
  @Override
  public void addToAuthorizedList(Employee employee) {
    if (employee != null) {
      authorized.add(employee.getId());
    }
  }

  /**
   * <p>If the employee is not null it adds the employee to the queue list</p>
   *
   * @param employee contains the id of the employee
   */
  @Override
  public void addToQueueList(Employee employee) {
    if (employee != null) {
      queue.add(employee.getId());
    }
  }

  /**
   * <p>If the employee is not null it removes it frm the authorized list</p>
   *
   * @param employee contains the id of the employee
   */
  @Override
  public void removeFromAuthorizedList(Employee employee) {
    if (employee != null) {
      authorized.remove(employee.getId());
    }
  }

  /**
   * <p>This method is used after the /exit endpoint and takes the first employee from the
   * queue list and puts it into the authorized list.</p>
   */
  @Override
  public void moveFirstFromQueueToAuthorized() {
    if (queue.size() > 0) {
      authorized.add(queue.getFirst());
      queue.removeFirst();
    }
  }

  /**
   * <p>This method is used to get the position of an employee.</p>
   *
   * @param employee it contains the id of the employee
   * @return if the employee is not null it returns the position of it
   */
  @Override
  public Position getPosition(Employee employee) {
    if (employee != null) {
      return new Position(queue.indexOf(employee.getId()) + 1);
    }

    return new Position(-1);
  }

  /**
   * <p>After registration it compares the current day to the classe's date field
   * if it is different is resets the authorized and the queue list.</p>
   */
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

  /**
   * <p>This method resets the authorized list</p>
   */
  private void resetAuthorizedList() {
    authorized.clear();
  }

  /**
   * <p>This method resets the queue list</p>
   */
  private void resetQueueList() {
    queue.clear();
  }
}

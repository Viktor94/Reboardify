package com.reboardify.databasemicroservice.controllers;

import com.reboardify.databasemicroservice.models.AccessStatus;
import com.reboardify.databasemicroservice.models.Employee;
import com.reboardify.databasemicroservice.models.Position;
import com.reboardify.databasemicroservice.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Database Controller is used to store and modify authorized and in queue employees
 */
@RestController
public class DatabaseController {

  /**
   * Database service contains the business logic to modify and get authorized and queue lists
   */
  private final DatabaseService databaseService;

  @Autowired
  public DatabaseController(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }

  /**
   * <p>This method adds the employee to the authorized or to the queue list.</p>
   *
   * @param employee contains the id of the employee
   * @return It returns a bodiless response entity after the registration
   */
  @PostMapping("/employee")
  public ResponseEntity<?> registerUserDetails(@RequestBody Employee employee) {
    databaseService.dailyReset();

    if (employee == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    int maxCapacity = Integer.parseInt(System.getenv("CAPACITY"));
    int maxAllowedEmployee = 250 * maxCapacity / 100;

    if (databaseService.isInQueueList(employee) || databaseService.isInAuthorizedList(employee)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    if (databaseService.getAuthorizedList().size() >= maxAllowedEmployee) {
      databaseService.addToQueueList(employee);
    } else {
      databaseService.addToAuthorizedList(employee);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * <p>This method returns the position of the employee</p>
   *
   * @param id The id of the employee
   * @return It returns a response entity with a position in the body after the position request
   */
  @GetMapping("/position/{id}")
  public ResponseEntity<?> employeePosition(@PathVariable String id) {
    Position position;
    Employee employee = new Employee(id);
    if (databaseService.isInAuthorizedList(employee)) {
      position = new Position(0);
    } else if (databaseService.isInQueueList(employee)) {
      position = databaseService.getPosition(employee);
    } else {
      position = new Position(-1);
    }
    return ResponseEntity.ok(position);
  }

  /**
   * <p>This method returns the access status of an employee</p>
   *
   * @param id The id of the employee
   * @return It returns a response entity after the entry request with the access status in the body
   */
  @GetMapping("/entry/{id}")
  public ResponseEntity<?> entry(@PathVariable String id) {
    if (databaseService.isInAuthorizedList(new Employee(id))) {
      return new ResponseEntity<>(new AccessStatus(id, true), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new AccessStatus(id, false), HttpStatus.OK);
    }
  }

  /**
   * <p>This method removes the employee from the authorized list.</p>
   *
   * @param employee The id of the employee
   * @return It returns a response entity after the exit request with the access status in the body
   */
  @PostMapping("/exit")
  public ResponseEntity<?> exit(@RequestBody Employee employee) {
    if (databaseService.isInAuthorizedList(employee)) {
      databaseService.removeFromAuthorizedList(employee);
      databaseService.moveFirstFromQueueToAuthorized();
      return new ResponseEntity<>(new AccessStatus(employee.getId(), true), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new AccessStatus(employee.getId(), false), HttpStatus.OK);
    }
  }
}

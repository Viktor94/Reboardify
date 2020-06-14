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

@RestController
public class DatabaseController {

  private DatabaseService databaseService;

  @Autowired
  public DatabaseController(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }

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

  @GetMapping("/entry/{id}")
  public ResponseEntity<?> entry(@PathVariable String id) {
    if (databaseService.isInAuthorizedList(new Employee(id))) {
      return new ResponseEntity<>(new AccessStatus(id, true), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new AccessStatus(id, false), HttpStatus.OK);
    }
  }

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

package com.reboardify.databasemicroservice.controllers;

import com.reboardify.databasemicroservice.models.Employee;
import com.reboardify.databasemicroservice.models.Position;
import java.util.LinkedList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

  private final LinkedList<String> authorized = new LinkedList<>();
  private final LinkedList<String> queue = new LinkedList<>();

  @PostMapping("/employee")
  public ResponseEntity<?> registerUserDetails(@RequestBody Employee employee) {
    int maxCapacity = Integer.parseInt(System.getenv("CAPACITY"));
    int maxAllowedEmployee = 250 * maxCapacity / 100;

    if (queue.contains(employee.getId()) || authorized.contains(employee.getId())) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    if (authorized.size() >= maxAllowedEmployee) {
      queue.add(employee.getId());
    } else {
      authorized.add(employee.getId());
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/position/{id}")
  public ResponseEntity<?> employeePosition(@PathVariable String id) {
    Position position;
    if (authorized.contains(id)) {
      position = new Position(0);
    } else if(queue.contains(id)){
      position = new Position(queue.indexOf(id) + 1);
    } else {
      position = new Position(-1);
    }
      return ResponseEntity.ok(position);
  }

  @GetMapping("/entry/{id}")
  public ResponseEntity<?> entry(@PathVariable String id) {
    if (authorized.contains(id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @PostMapping("/exit")
  public ResponseEntity<?> exit(@RequestBody Employee employee) {
    if (authorized.contains(employee.getId())) {
      authorized.remove(employee.getId());

      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}

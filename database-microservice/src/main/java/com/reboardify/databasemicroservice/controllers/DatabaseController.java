package com.reboardify.databasemicroservice.controllers;

import com.reboardify.databasemicroservice.models.Employee;
import com.reboardify.databasemicroservice.models.Position;
import java.util.LinkedList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

  LinkedList<String> authorized = new LinkedList<>();
  LinkedList<String> queue = new LinkedList<>();

  @PostMapping("/employee")
  public ResponseEntity<?> registerUserDetails(@RequestBody Employee employee) {
    int maxCapacity = Integer.parseInt(System.getenv("CAPACITY"));
    int maxAllowedEmployee = 250 * maxCapacity / 100;

    if (queue.contains(employee.getId()) || authorized.contains(employee.getId())) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    if (authorized.size() > maxAllowedEmployee) {
      queue.add(employee.getId());
    } else {
      authorized.add(employee.getId());
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/position")
  public ResponseEntity<?> employeePosition(@RequestBody Employee employee) {
    if (authorized.contains(employee.getId())) {
      Position position = new Position(0);

      return ResponseEntity.ok(position);
    } else {
      Position position = new Position(queue.indexOf(employee.getId()) + 1);

      return ResponseEntity.ok(position);
    }
  }
}

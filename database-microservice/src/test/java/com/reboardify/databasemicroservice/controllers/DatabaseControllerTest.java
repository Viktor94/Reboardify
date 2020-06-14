package com.reboardify.databasemicroservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reboardify.databasemicroservice.models.AccessStatus;
import com.reboardify.databasemicroservice.models.Employee;
import com.reboardify.databasemicroservice.models.Position;
import com.reboardify.databasemicroservice.services.DatabaseService;
import com.reboardify.databasemicroservice.services.DatabaseServiceImpl;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class DatabaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final DatabaseService databaseService = new DatabaseServiceImpl();

  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      StandardCharsets.UTF_8);
  private ObjectMapper objectMapper;
  private Employee employee;

  @BeforeEach
  public void setup() {
    this.mockMvc = standaloneSetup(new DatabaseController(databaseService)).build();
    employee = new Employee("123");
    objectMapper = new ObjectMapper();
    System.out.println("asd");
  }

  @Test
  public void employee_New_Employee() throws Exception {
    mockMvc.perform(post("/employee")
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(employee)))
        .andExpect(status().isOk());
  }

  @Test
  public void employee_AlreadyRegistered_Employee() throws Exception {
    databaseService.addToAuthorizedList(employee);

    mockMvc.perform(post("/employee")
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(employee)))
        .andExpect(status().isConflict());
  }

  @Test
  public void employee_Null_Employee() throws Exception {
    mockMvc.perform(post("/employee")
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(null)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void position_Authorized_Employee() throws Exception {
    mockMvc.perform(post("/employee")
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(employee)))
        .andExpect(status().isOk());

    MvcResult result = mockMvc.perform(get("/position/" + employee.getId()))
        .andExpect(status().isOk())
        .andReturn();

    Position position = objectMapper
        .readValue(result.getResponse().getContentAsString(), Position.class);

    Assertions.assertEquals(0, position.getPosition());
  }

  @Test
  public void position_Unauthorized_Employee() throws Exception {
    MvcResult result = mockMvc.perform(get("/position/" + employee.getId()))
        .andExpect(status().isOk())
        .andReturn();

    Position position = objectMapper
        .readValue(result.getResponse().getContentAsString(), Position.class);

    Assertions.assertEquals(-1, position.getPosition());
  }

  @Test
  public void position_InQueue_Employee_Capacity_1percent() throws Exception {
    registerEmployee(employee);
    registerEmployee(new Employee("456"));
    registerEmployee(new Employee("789"));

    MvcResult result = mockMvc.perform(get("/position/789"))
        .andExpect(status().isOk())
        .andReturn();

    Position position = objectMapper
        .readValue(result.getResponse().getContentAsString(), Position.class);

    Assertions.assertEquals(1, position.getPosition());
  }

  @Test
  public void entry_Authorized_Employee() throws Exception {
    registerEmployee(employee);

    MvcResult result = mockMvc.perform(get("/entry/" + employee.getId()))
        .andExpect(status().isOk())
        .andReturn();

    AccessStatus accessStatus = objectMapper
        .readValue(result.getResponse().getContentAsString(), AccessStatus.class);

    Assertions.assertTrue(accessStatus.getAuthorized());
  }

  @Test
  public void entry_UnAuthorized_Employee() throws Exception {
    MvcResult result = mockMvc.perform(get("/entry/" + employee.getId()))
        .andExpect(status().isOk())
        .andReturn();

    AccessStatus accessStatus = objectMapper
        .readValue(result.getResponse().getContentAsString(), AccessStatus.class);

    Assertions.assertFalse(accessStatus.getAuthorized());
  }

  private void registerEmployee(Employee employee) throws Exception {
    mockMvc.perform(post("/employee")
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(employee)))
        .andExpect(status().isOk());
  }

  @Test
  public void exit_Authorized_Employee() throws Exception {
    registerEmployee(employee);

    mockMvc.perform(get("/entry/" + employee.getId()))
        .andExpect(status().isOk());

    MvcResult result = mockMvc.perform(post("/exit")
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(employee)))
        .andExpect(status().isOk())
        .andReturn();

    AccessStatus accessStatus = objectMapper
        .readValue(result.getResponse().getContentAsString(), AccessStatus.class);

    Assertions.assertTrue(accessStatus.getAuthorized());
  }

  @Test
  public void exit_UnAuthorized_Employee() throws Exception {
    mockMvc.perform(get("/entry/" + employee.getId()))
        .andExpect(status().isOk());

    MvcResult result = mockMvc.perform(post("/exit")
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(employee)))
        .andExpect(status().isOk())
        .andReturn();

    AccessStatus accessStatus = objectMapper
        .readValue(result.getResponse().getContentAsString(), AccessStatus.class);

    Assertions.assertFalse(accessStatus.getAuthorized());
  }
}

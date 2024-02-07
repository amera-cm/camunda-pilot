package io.cmt.camunda_pilot.boot.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

  @GetMapping("/")
  public String hello() {
    return "HOLA MUNDO!!!";
  }
}

package io.cmt.camunda_pilot.boot.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Api {

  @GetMapping("/hello")
  public String hello() {
    return "HOLA MUNDO!!!";
  }
}

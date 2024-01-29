package io.cmt.camunda_pilot.web.pages;

import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
@ParametersAreNonnullByDefault
public class PublicPages {

  @GetMapping("/hello-world")
  public String helloWorld(Model model){
    model.addAttribute("message", "Hello World!");
    return "hello-world";
  }
}

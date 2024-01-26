package io.cmt.camunda_pilot.spring;

import lombok.Data;

/** IAM Config Data. */
@Data
public class IamConfigData {
  private String realm;
  private String authServerUrl;
}

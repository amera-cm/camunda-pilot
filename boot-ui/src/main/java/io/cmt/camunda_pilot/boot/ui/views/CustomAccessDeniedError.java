package io.cmt.camunda_pilot.boot.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.server.HttpStatusCode;
import jakarta.annotation.security.PermitAll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Tag(Tag.DIV)
@PermitAll
public class CustomAccessDeniedError extends Component
    implements HasErrorParameter<AccessDeniedException> {

  private static final Logger logger = LogManager.getLogger(CustomAccessDeniedError.class);

  @Override
  public int setErrorParameter(
      BeforeEnterEvent event, ErrorParameter<AccessDeniedException> parameter) {
    logger.warn(parameter.getException().getMessage());
    getElement().setText("Access denied.");
//    parameter.getException().printStackTrace();
    return HttpStatusCode.UNAUTHORIZED.getCode();
  }
}

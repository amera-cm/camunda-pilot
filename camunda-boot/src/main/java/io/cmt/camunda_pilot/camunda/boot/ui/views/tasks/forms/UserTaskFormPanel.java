package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class UserTaskFormPanel extends VerticalLayout {

  public Registration addTaskCompletedListener(
      ComponentEventListener<TaskCompletedEvent> listener) {
    return addListener(TaskCompletedEvent.class, listener);
  }

  public void fireTaskCompletedEvent() {
    fireEvent(new TaskCompletedEvent(this, false));
  }

  public static class TaskCompletedEvent extends ComponentEvent<UserTaskFormPanel> {

    public TaskCompletedEvent(UserTaskFormPanel source, boolean fromClient) {
      super(source, fromClient);
    }
  }
}

package it.sevenbits.taskmanager.core.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Task {

    String getId();
    String getText();
    TaskStatus getStatus();
    void setStatus(TaskStatus newStatus);
    Task clone();
    boolean update(ObjectNode fields);

}

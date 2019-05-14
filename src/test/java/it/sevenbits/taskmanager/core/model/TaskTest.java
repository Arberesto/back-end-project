package it.sevenbits.taskmanager.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.UUID;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TaskTest {

    private Task task;

    @Test
    public void updateTest1() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("text","secondNode");
        node.put("status","done");
        TaskFactory factory = new TaskFactory();
        String id = UUID.randomUUID().toString();
        Task expectedTask = factory.getNewTask(id,"secondNode",TaskStatus.done);
        task = factory.getNewTask(id,"firstTask", TaskStatus.inbox);
        expectedTask.setCreatedAt(task.getCreatedAt());

        assertTrue(task.update(node));

        expectedTask.setChangedAt(task.getChangedAt());
        assertEquals(expectedTask,task);
    }
}

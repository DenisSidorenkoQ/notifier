package com.exmple.task.unit.service;

import com.exmple.task.entity.TaskStatus;
import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;
import com.exmple.task.service.TaskService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @Test
    public void shouldCreateTask() {
        Task task = Task.builder()
                .text("Text")
                .time(LocalDateTime.now())
                .build();
        Task returnableTask = Task.builder()
                .id(1).text("Text").time(LocalDateTime.now()).build();
        given(taskRepository.save(any(Task.class))).willReturn(returnableTask);

        long taskId = taskService.createTask(task);

        assertThat(taskId, is(1L));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldGetTaskByMail() {
        String mail = "test@gmail.com";
        List<Task> tasks = new ArrayList<>();
        tasks.add(Task.builder().build());
        tasks.add(Task.builder().build());
        when(taskRepository.findTasksByUserMail(mail)).thenReturn(tasks);

        List<Task> result = taskService.getTaskByMail(mail);

        Assertions.assertEquals(tasks, result);
        verify(taskRepository, times(1)).findTasksByUserMail(mail);
    }

    @Test
    public void testUpdateTaskTextWhenExists() {
        Task task = Task.builder().id(1).text("testText").status(TaskStatus.ACTIVE).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.updateTaskTextById(task.getId(), task.getText());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateTaskTextWhenNotExists() {
        Task task = Task.builder().id(1).text("testText").build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        taskService.updateTaskTextById(task.getId(), task.getText());

        verify(taskRepository, never()).updateTask(anyInt(), anyString(), anyString(), any(Date.class));
    }

    @Test
    public void testUpdateTitleWhenExists() {
        Task task = Task.builder().id(1).title("testText").status(TaskStatus.ACTIVE).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.updateTaskTitleById(task.getId(), task.getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateTaskTitleWhenNotExists() {
        Task task = Task.builder().id(1).title("testText").build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        taskService.updateTaskTitleById(task.getId(), task.getTitle());

        verify(taskRepository, never()).updateTask(anyInt(), anyString(), anyString(), any(Date.class));
    }

    @Test
    public void testUpdateTaskTimeWhenExists() {
        Task task = Task.builder().id(1).time(LocalDateTime.now()).status(TaskStatus.ACTIVE).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.updateTaskTimeById(task.getId(), task.getTime());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateTaskTimeWhenNotExists() {
        Task task = Task.builder().id(1).time(LocalDateTime.now()).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        taskService.updateTaskTimeById(task.getId(), task.getTime());

        verify(taskRepository, never()).saveAndFlush(task);
    }

    @Test
    public void testDeleteByIdWhenDeleted() {
        long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.of(Task.builder().build()));
        doNothing().when(taskRepository).deleteById(id);

        taskService.deleteById(id);

        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdWhenNotExists() {
        long id = 1L;

        taskService.deleteById(id);

        verify(taskRepository, times(0)).deleteById(id);
    }

    @Test
    public void testUpdateTaskStatusWhenExists() {
        Task task = Task.builder().id(1).time(LocalDateTime.now()).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.updateTaskStatus(task.getId(), TaskStatus.ACTIVE);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateTaskStatusWhenNotExists() {
        Task task = Task.builder().id(1).time(LocalDateTime.now()).build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        taskService.updateTaskStatus(task.getId(), TaskStatus.ACTIVE);
    }
}

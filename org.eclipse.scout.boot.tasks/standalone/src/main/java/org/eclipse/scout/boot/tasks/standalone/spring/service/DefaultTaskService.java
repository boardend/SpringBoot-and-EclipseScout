package org.eclipse.scout.boot.tasks.standalone.spring.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.scout.boot.tasks.commons.data.TaskRepository;
import org.eclipse.scout.boot.tasks.commons.data.entity.TaskEntity;
import org.eclipse.scout.boot.tasks.commons.model.Task;
import org.eclipse.scout.boot.tasks.commons.model.service.TaskService;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultTaskService implements TaskService, MapperService<Task, TaskEntity> {

  @Autowired
  private TaskRepository taskRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Task> getAll() {
    return taskRepository.findAll()
        .stream()
        .map(task -> convertToModel(task, Task.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> getInbox(String userId) {
    List<Task> inboxList = new ArrayList<>();

    for (Task task : getUserTasks(userId)) {
      if (!task.isAccepted()) {
        inboxList.add(task);
      }
    }

    return inboxList;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> getOwn(String userId) {
    List<Task> ownList = new ArrayList<>();

    for (Task task : getUserTasks(userId)) {
      if (task.isAccepted()) {
        ownList.add(task);
      }
    }

    return ownList;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> getToday(String userId) {
    List<Task> todaysList = new ArrayList<>();

    for (Task task : getUserTasks(userId)) {
      if (!task.isAccepted() || task.isDone()) {
        continue;
      }

      if (isToday(task.getDueDate()) || isToday(task.getReminder())) {
        todaysList.add(task);
      }
    }

    return todaysList;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean exists(UUID taskId) {
    return taskRepository.exists(taskId);
  }

  @Override
  @Transactional(readOnly = true)
  public Task get(UUID taskId) {
    return convertToModel(taskRepository.findOne(taskId), Task.class);
  }

  @Override
  @Transactional
  public void save(Task task) {
    taskRepository.save(convertToEntity(task, TaskEntity.class));
  }

  protected List<Task> getUserTasks(String userId) {
    return taskRepository.findByResponsible(userId)
        .stream()
        .map(task -> convertToModel(task, Task.class))
        .collect(Collectors.toList());
  }

  protected boolean isToday(Date date) {
    if (date == null) {
      return false;
    }

    return DateUtility.isSameDay(new Date(), date);
  }

}

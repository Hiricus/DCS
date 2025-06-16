package com.hiricus.dcs.service;

import com.hiricus.dcs.dto.request.TaskCreationRequest;
import com.hiricus.dcs.dto.task.TaskSubjectDto;
import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.exception.RoleViolationException;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.task.TaskObject;
import com.hiricus.dcs.model.object.task.TaskType;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.TaskRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       GroupRepository groupRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public List<TaskObject> getAllOwnedTasks(Integer curatorId, Boolean includeCompleted) {
        if (!userRepository.isUserExistsById(curatorId)) {
            throw new EntityNotFoundException("User not found");
        }

        List<TaskObject> tasks = includeCompleted ?
                taskRepository.findAllByAuthorId(curatorId) :
                taskRepository.findAllUncompletedByAuthorId(curatorId);
        return tasks;
    }

    @Transactional
    public List<TaskObject> getAllAssignedToHead(Integer headId) {
        if (!userRepository.isUserExistsById(headId)) {
            throw new EntityNotFoundException("User not found");
        }

        Optional<GroupObject> groupObject = userRepository.getUsersGroup(headId);
        if (groupObject.isEmpty()) {
            throw new RoleViolationException("Curator not in group");
        }

        return taskRepository.findAllUncompletedByGroupId(groupObject.get().getId());
    }

    // TODO: Сделать логику для создания задач на сбор данных
    @Transactional
    public Integer createTask(TaskCreationRequest request, Integer authorId) {
        // Проверяем существует ли связанная группа
        if (!groupRepository.isGroupExistsById(request.getGroupId())) {
            throw new EntityNotFoundException("Group does not exist");
        }

        // Создаём объект задачи
        TaskType type = TaskType.valueOf(request.getTaskType());
        TaskObject task = new TaskObject(request.getTaskName(), type, request.getGroupId());

        // Назначаем автора
        task.setTaskAuthor(new UserObject(authorId));

        // Добавляем задачу в БД
        Integer taskId = taskRepository.createTask(task).get();

        // Привязываем к задаче всех пользователей из группы
        List<UserObject> groupMembers = groupRepository.getGroupMembers(request.getGroupId());
        taskRepository.addTaskSubjects(taskId, groupMembers);

        return taskId;
    }

    @Transactional
    public void setCompleted(Integer taskId, Boolean completed) {
        if (!taskRepository.isTaskExistsById(taskId)) {
            throw new EntityNotFoundException("Task with id " + taskId + " not found");
        }

        taskRepository.setCompleted(taskId, completed);
    }

    @Transactional
    public void deleteTask(Integer id) {
        taskRepository.deleteTaskById(id);
    }

    @Transactional
    public List<TaskSubjectDto> getTaskSubjects(Integer taskId) {
        if (!taskRepository.isTaskExistsById(taskId)) {
            throw new EntityNotFoundException("Task with id " + taskId + " not found");
        }

        // Собираем выполнивших и невыполнивших пользователей
        List<UserDataObject> checkedSubjects = taskRepository.getAllTaskSubjectsWithChecking(taskId, true);
        List<UserDataObject> uncheckedSubjects = taskRepository.getAllTaskSubjectsWithChecking(taskId, false);

        // Создаём массив для ответа
        List<TaskSubjectDto> taskSubjects = new ArrayList<>();
        // Добавляем выполнивших пользователей
        taskSubjects.addAll(checkedSubjects.stream()
                .map(checkedSubjectData -> new TaskSubjectDto(checkedSubjectData, true))
                .toList());
        // Добавляем невыполнивших пользоватлей
        taskSubjects.addAll(uncheckedSubjects.stream()
                .map(uncheckedSubjectData -> new TaskSubjectDto(uncheckedSubjectData, false))
                .toList());

        return taskSubjects;
    }

    @Transactional
    public void updateSubjectsCompletionStatus(Integer taskId, List<TaskSubjectDto> taskSubjects) {
        if (!taskRepository.isTaskExistsById(taskId)) {
            throw new EntityNotFoundException("Task with id " + taskId + " not found");
        }

        // Сортируем задействованных студентов на выполнивших и невыполнивших
        List<Integer> checkedSubjectIds = taskSubjects.stream()
                .filter(subject -> subject.getChecked() == true)
                .map(TaskSubjectDto::getId)
                .toList();
        List<Integer> uncheckedSubjectIds = taskSubjects.stream()
                .filter(subject -> subject.getChecked() == false)
                .map(TaskSubjectDto::getId)
                .toList();

        taskRepository.updateCheckedForTaskSubjects(taskId, checkedSubjectIds, true);
        taskRepository.updateCheckedForTaskSubjects(taskId, uncheckedSubjectIds, false);
    }
}

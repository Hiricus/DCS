package com.hiricus.dcs.model.object.task;

import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserObject;
import lombok.ToString;
import org.jooq.Record;

import java.util.ArrayList;
import java.util.List;

import static com.hiricus.dcs.generated.public_.Tables.TASK;

@ToString
public class TaskObject {
    private int id;
    private String name;
    private TaskType type;
    private boolean hasDocuments;

    private UserObject taskAuthor;
    private GroupObject relatedGroup;

    private List<UserObject> taskSubjects = new ArrayList<>();
    private List<DocumentObject> linkedDocuments = new ArrayList<>();


    // constructors
    public TaskObject(String name, TaskType type, boolean hasDocuments) {
        this.name = name;
        this.type = type;
        this.hasDocuments = hasDocuments;
    }
    public TaskObject(int id, String name, TaskType type, boolean hasDocuments) {
        this(name, type, hasDocuments);
        this.id = id;
    }
    public TaskObject() {}

    public TaskObject(Record record) {
        this.id = record.get(TASK.ID);
        this.name = record.get(TASK.TASK_NAME);
        this.type = TaskType.valueOf(record.get(TASK.TASK_TYPE));
        this.hasDocuments = record.get(TASK.HAS_DOCUMENTS);
    }

    // getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public TaskType getType() {
        return type;
    }
    public boolean isHasDocuments() {
        return hasDocuments;
    }
    public UserObject getTaskAuthor() {
        return taskAuthor;
    }
    public GroupObject getRelatedGroup() {
        return relatedGroup;
    }
    public List<UserObject> getTaskSubjects() {
        return taskSubjects;
    }
    public List<DocumentObject> getLinkedDocuments() {
        if (!hasDocuments) {
            return new ArrayList<>();
        } else {
            return linkedDocuments;
        }
    }

    // setters
    public void setHasDocuments(boolean hasDocuments) {
        this.hasDocuments = hasDocuments;
    }
    public void setTaskAuthor(UserObject taskAuthor) {
        this.taskAuthor = taskAuthor;
    }
    public void setTaskContractor(GroupObject taskGroup) {
        this.relatedGroup = taskGroup;
    }
    public void setTaskSubjects(List<UserObject> taskSubjects) {
        this.taskSubjects = taskSubjects;
    }
    public void setLinkedDocuments(List<DocumentObject> linkedDocuments) {
        if (hasDocuments) {
            this.linkedDocuments = linkedDocuments;
        }
    }
}

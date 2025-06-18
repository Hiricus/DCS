package com.hiricus.dcs.service.template.replacing;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseReplacingStrategy extends ReplacingStrategy {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public CourseReplacingStrategy(@Value("COURSE") String keyword,
                                   GroupRepository groupRepository,
                                   UserRepository userRepository) {
        super(keyword);
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String getReplacement(Integer studentId) {
        Optional<GroupObject> optionalGroup = userRepository.getUsersGroup(studentId);
        if (optionalGroup.isEmpty()) {
            throw new EntityNotFoundException("User " + studentId + " does not belong to any group");
        }

        Integer course = groupRepository.findGroupById(optionalGroup.get().getId())
                .get().getCourse();
        return String.valueOf(course);
    }
}

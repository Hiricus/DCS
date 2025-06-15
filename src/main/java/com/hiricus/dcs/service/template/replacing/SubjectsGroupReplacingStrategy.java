package com.hiricus.dcs.service.template.replacing;

import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SubjectsGroupReplacingStrategy extends ReplacingStrategy {
    private final UserRepository userRepository;

    public SubjectsGroupReplacingStrategy(@Value("SUBJECTS_GROUP") String keyword, UserRepository userRepository) {
        super(keyword);
        this.userRepository = userRepository;
    }

    @Override
    public String getReplacement(Integer studentId) {
        Optional<GroupObject> groupOptional = userRepository.getUsersGroup(studentId);
        if (groupOptional.isEmpty()) {
            return "";
        } else {
            return groupOptional.get().getName();
        }
    }
}

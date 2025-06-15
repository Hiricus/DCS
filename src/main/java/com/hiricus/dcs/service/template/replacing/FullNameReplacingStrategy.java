package com.hiricus.dcs.service.template.replacing;

import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FullNameReplacingStrategy extends ReplacingStrategy {
    private final UserDataRepository userDataRepository;

    public FullNameReplacingStrategy(@Value("FULL_NAME") String keyword, UserDataRepository userDataRepository) {
        super(keyword);
        this.userDataRepository = userDataRepository;
    }

    @Override
    public String getReplacement(Integer studentId) {
        UserDataObject userData = userDataRepository.findUserDataByUserId(studentId).get();
        return userData.getSurname() + " " + userData.getName() + " " + userData.getPatronymic();
    }
}

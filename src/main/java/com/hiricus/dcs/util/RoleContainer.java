package com.hiricus.dcs.util;

import com.hiricus.dcs.model.object.user.RoleObject;
import com.hiricus.dcs.model.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoleContainer {
    private final RoleRepository roleRepository;

    private Map<Integer, String> roleMap;

    @Autowired
    public RoleContainer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        roleMap = new HashMap<>();
    }


    @PostConstruct
    public void setup() {
        List<RoleObject> roleObjects = roleRepository.findAll();
        roleMap = roleObjects.stream().collect(
                Collectors.toMap(RoleObject::getId, RoleObject::getRoleName)
        );
    }

    public String getRoleName(Integer id) {
        return roleMap.get(id);
    }
}

package org.example.breakoutdrop.Services.DomainServices.ServerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.Entities.Role;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor

public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new NotFound404("Роль не найдена"));
    }

}

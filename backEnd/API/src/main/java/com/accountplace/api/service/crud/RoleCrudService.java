package com.accountplace.api.service.crud;

import com.accountplace.api.dto.crud.create.RoleCreateDTO;
import com.accountplace.api.dto.crud.pub.PublicRoleDTO;
import com.accountplace.api.dto.crud.update.RoleUpdateDTO;
import com.accountplace.api.entity.RoleEntity;
import com.accountplace.api.repositories.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.stereotype.Service;

@Service
public class RoleCrudService {

    private final RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;



    @Autowired
    public RoleCrudService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public PublicRoleDTO create(RoleCreateDTO roleCreateDTO) {
        RoleEntity roleEntity = this.convertCreateDTOToEntity(roleCreateDTO);
        return this.convertEntityToDTO(this.roleRepository.save(roleEntity));
    }

    public PublicRoleDTO update(RoleUpdateDTO roleUpdateDTO) {
        return this.roleRepository.findById(roleUpdateDTO.getId()).map(
                roleEntity -> {
                    roleEntity.setName(roleUpdateDTO.getName());
                    return this.convertEntityToDTO(this.roleRepository.save(roleEntity));
                }
        ).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public String delete(int id) {
        this.roleRepository.deleteById(id);
        return "Role with "+ id +" deleted";
    }
    public PublicRoleDTO convertEntityToDTO(RoleEntity role) {
        return new PublicRoleDTO(
            role.getId(),
            role.getName()
        );
    }

    public RoleEntity convertCreateDTOToEntity(RoleCreateDTO dto) {
       return new RoleEntity(
            dto.getName()
        );
    }
}

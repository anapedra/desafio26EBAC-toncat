package org.anasantana.services.impl;

import jakarta.transaction.Transactional;
import org.anasantana.daos.generics.IUserDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.dtos.ClientDTO;
import org.anasantana.model.User;
import org.anasantana.services.IUserService;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.ResourceNotFoundException;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements IUserService {

    private final IUserDAO userRepository;

    public UserServiceImpl(IUserDAO userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PageResult<ClientDTO> findAll(int page, int size) {
        PageResult<User> result = userRepository.findAllPaged(page, size);
        return new PageResult<>(
                result.getContent().stream().map(ClientDTO::new).collect(Collectors.toList()),
                result.getPage(),
                result.getSize(),
                result.getTotalElements()
        );
    }

    @Override
    @Transactional
    public ClientDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));
        return new ClientDTO(entity);
    }

    @Override
    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);
        user = userRepository.save(user);
        return new ClientDTO(user);
    }

    @Override
    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        try {
            User user = userRepository.getOne(id);
            copyDtoToEntity(dto, user);
            user = userRepository.save(user);
            return new ClientDTO(user);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Id not found " + id);
        }

        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBaseException("Integrity violation: " + e.getMessage(), e);
        }
    }

    @Override
    public ClientDTO getProfile() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void copyDtoToEntity(ClientDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setMainPhone(dto.getPhone());
        entity.setMomentRegistration(Instant.now());
        entity.setMomentUpdate(Instant.now());
        entity.setRegistrationEmail(dto.getEmail());
    }
}

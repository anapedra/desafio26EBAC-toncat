package org.anasantana.services;

import org.anasantana.dtos.ClientDTO;
import org.anasantana.daos.pagination.PageResult;

public interface IUserService {

    PageResult<ClientDTO> findAll(int page, int size);

    ClientDTO findById(Long id);

    ClientDTO insert(ClientDTO dto);

    ClientDTO update(Long id, ClientDTO dto);

    void deleteById(Long id);

    ClientDTO getProfile();
}

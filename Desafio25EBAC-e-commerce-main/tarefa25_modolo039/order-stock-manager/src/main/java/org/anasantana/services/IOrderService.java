package org.anasantana.services;


import org.anasantana.daos.pagination.PageResult;
import org.anasantana.daos.pagination.Pageable;
import org.anasantana.dtos.OrderDTO;

public interface IOrderService {

    PageResult<OrderDTO> find(Long clientId, String nameClient, String cpfClient, String minDate, String maxDate, Pageable pageable);

    OrderDTO findById(Long id);

    OrderDTO insert(OrderDTO dto);

    OrderDTO update(Long id, OrderDTO dto);

    void delete(Long id);
}

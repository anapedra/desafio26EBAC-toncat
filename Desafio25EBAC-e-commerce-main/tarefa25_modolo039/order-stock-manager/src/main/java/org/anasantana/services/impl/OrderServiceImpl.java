package org.anasantana.services.impl;

import jakarta.transaction.Transactional;
import org.anasantana.daos.generics.IOrderDAO;
import org.anasantana.daos.generics.IOrderItemDAO;
import org.anasantana.daos.generics.IProductDAO;
import org.anasantana.daos.generics.IUserDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.daos.pagination.Pageable;
import org.anasantana.dtos.OrderDTO;
import org.anasantana.dtos.OrderItemDTO;
import org.anasantana.model.Order;
import org.anasantana.model.OrderItem;
import org.anasantana.model.Product;
import org.anasantana.model.User;
import org.anasantana.services.IOrderService;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.EntityNotFoundException;
import org.anasantana.services.exceptions.ResourceNotFoundException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;



public class OrderServiceImpl implements IOrderService {


    private final IOrderDAO orderRepository;
    private final IUserDAO userRepository;
    private final IOrderItemDAO orderItemRepository;
    private final IProductDAO productRepository;
   // private final StockService stockService;

    public OrderServiceImpl(IOrderDAO orderRepository, IUserDAO userRepository, IOrderItemDAO orderItemRepository,
                            IProductDAO productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;

    }

    @Override
    @Transactional
    public PageResult<OrderDTO> find(Long clientId, String nameClient, String cpfClient, String minDate, String maxDate, Pageable pageable) {
        User client = (clientId == 0) ? null : userRepository.getOne(clientId);

        LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDate min = (minDate == null || minDate.isEmpty()) ? today.minusDays(365) : LocalDate.parse(minDate);
        LocalDate max = (maxDate == null || maxDate.isEmpty()) ? today : LocalDate.parse(maxDate);

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        PageResult<Order> result = orderRepository.find(client, nameClient, cpfClient, min, max, page, size);

        orderRepository.findOrder(result.getContent());

        return new PageResult<>(
                result.getContent().stream().map(OrderDTO::new).toList(),
                result.getPage(),
                result.getSize(),
                result.getTotalElements()
        );
    }


    @Override
    @Transactional
    public OrderDTO findById(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new OrderDTO(entity, entity.getItems());
    }

    @Override
    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order entity = new Order();
        entity.setDateOrder(LocalDate.now());
        entity.setMoment(Instant.now());

        copyDtoToEntity(dto, entity);
/*
        for (OrderItem item : entity.getItems()) {
            stockService.decreaseStock(item.getProduct(), item.getQuantity());
        }

 */

        entity = orderRepository.save(entity);
        return new OrderDTO(entity);
    }


    @Override
    @Transactional
    public OrderDTO update(Long id, OrderDTO dto) {
        try {
            Order entity = orderRepository.getOne(id);
/*
            for (OrderItem oldItem : entity.getItems()) {
                stockService.increaseStock(oldItem.getProduct(), oldItem.getQuantity());
            }

 */

            entity.getItems().clear();
            copyDtoToEntity(dto, entity);
/*
            for (OrderItem newItem : entity.getItems()) {
                stockService.decreaseStock(newItem.getProduct(), newItem.getQuantity());
            }

 */

            entity = orderRepository.save(entity);
            return new OrderDTO(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }


    @Override
    public void delete(Long id) {
        if (orderRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Id not found " + id);
        }

        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBaseException("Integrity violation: " + e.getMessage(), e);
        }
    }

    private void copyDtoToEntity(OrderDTO dto, Order entity) {
        entity.setDateOrder(LocalDate.now());
        entity.setMoment(Instant.now());
        entity.setStatus(dto.getStatus());

        User user = new User();
        user.setId(dto.getClient().getId());
        entity.setClient(user);

        entity.getItems().clear();
        for (OrderItemDTO orderItemDTO : dto.getItems()) {
            Product product = productRepository.getOne(orderItemDTO.getProductId());
            OrderItem orderItem = new OrderItem(entity, product, orderItemDTO.getQuantity(), product.getInitialPrice());
            entity.getItems().add(orderItem);


        }

       // orderRepository.save(entity);
        orderItemRepository.saveAll(new ArrayList<>(entity.getItems()));

    }
}

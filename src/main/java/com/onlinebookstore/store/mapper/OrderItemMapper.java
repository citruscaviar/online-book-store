package com.onlinebookstore.store.mapper;

import com.onlinebookstore.store.config.MapperConfig;
import com.onlinebookstore.store.dto.order.item.OrderItemResponseDto;
import com.onlinebookstore.store.model.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDtos(Set<OrderItem> orderItems);
}

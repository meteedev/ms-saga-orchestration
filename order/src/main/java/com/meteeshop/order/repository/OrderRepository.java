package com.meteeshop.order.repository;

import com.meteeshop.order.entitiy.OrderEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

@Registered
public interface OrderRepository extends ReactiveCrudRepository<OrderEntity, UUID> {

}

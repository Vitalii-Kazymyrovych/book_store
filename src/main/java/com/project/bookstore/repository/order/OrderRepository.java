package com.project.bookstore.repository.order;

import com.project.bookstore.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o "
            + "JOIN o.user u "
            + "LEFT JOIN FETCH o.orderItems "
            + "WHERE u.email = :email")
    Page<Order> findAllWithItemsByUserEmail(
            @Param("email") String email,
            Pageable pageable);

    @Query("SELECT o FROM Order o "
            + "LEFT JOIN FETCH o.orderItems "
            + "JOIN FETCH o.user "
            + "WHERE o.id = :id")
    Optional<Order> findWithItemsAndUserById(@Param("id") Long id);
}

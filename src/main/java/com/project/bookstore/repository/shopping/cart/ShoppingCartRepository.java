package com.project.bookstore.repository.shopping.cart;

import com.project.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT sc FROM ShoppingCart sc "
            + "JOIN sc.user u "
            + "WHERE u.email = :email")
    Optional<ShoppingCart> findByUserEmail(@Param("email") String email);

    @Query("SELECT sc FROM ShoppingCart sc "
            + "JOIN sc.user u "
            + "LEFT JOIN FETCH sc.cartItems ci "
            + "LEFT JOIN FETCH ci.book b "
            + "WHERE u.email = :email")
    Optional<ShoppingCart> findWithCartItemsByUserEmail(@Param("email") String email);
}

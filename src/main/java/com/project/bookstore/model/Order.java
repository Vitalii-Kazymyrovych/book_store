package com.project.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "orders")
@Setter
@Getter
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar")
    private Status status;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public enum Status {
        NEW,
        PAID,
        PROCESSING,
        DELIVERED,
        CANCELLED
    }
}

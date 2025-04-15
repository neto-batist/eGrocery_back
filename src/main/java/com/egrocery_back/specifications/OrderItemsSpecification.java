package com.egrocery_back.specifications;

import com.egrocery_back.models.OrderItemsEntity;
import org.springframework.data.jpa.domain.Specification;

public class OrderItemsSpecification {

    public static Specification<OrderItemsEntity> orderIdEquals(Integer orderId) {
        return (root, query, cb) ->
                orderId == null
                        ? null
                        : cb.equal(root.get("ordersByOrderId").get("id"), orderId); // Acessa o ID do pedido
    }

    public static Specification<OrderItemsEntity> productIdEquals(Integer productId) {
        return (root, query, cb) ->
                productId == null
                        ? null
                        : cb.equal(root.get("productsByProductId").get("id"), productId); // Acessa o ID do produto
    }

    public static Specification<OrderItemsEntity> quantityGreaterThanOrEqual(Integer minQuantity) {
        return (root, query, cb) ->
                minQuantity == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
    }

    public static Specification<OrderItemsEntity> quantityLessThanOrEqual(Integer maxQuantity) {
        return (root, query, cb) ->
                maxQuantity == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("quantity"), maxQuantity);
    }

    public static Specification<OrderItemsEntity> priceGreaterThanOrEqual(Double minPrice) {
        return (root, query, cb) ->
                minPrice == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<OrderItemsEntity> priceLessThanOrEqual(Double maxPrice) {
        return (root, query, cb) ->
                maxPrice == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
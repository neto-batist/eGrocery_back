package com.egrocery_back.controllers;

import com.egrocery_back.dto.CartDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Adicionar item ao carrinho
    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody @Valid CartDTO cartDTO) throws NotFound {
        CartDTO savedCart = cartService.addItemToCart(cartDTO);
        return ResponseEntity.ok(savedCart);
    }

    // Remover item do carrinho
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable @Min(1) Integer cartId) throws NotFound {
        cartService.removeItemFromCart(cartId);
        return ResponseEntity.noContent().build();
    }

    // atualizar
    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCartItem(
            @PathVariable("id") @Min(1) Integer id,
            @RequestBody @Valid CartDTO cartDTO) throws NotFound {
        CartDTO updatedCart = cartService.updateItemInCart(id, cartDTO);
        return ResponseEntity.ok(updatedCart);
    }
    // Calcular total do carrinho
    @GetMapping("/{cartId}/total")
    public ResponseEntity<Double> calculateTotal(@PathVariable @Min(1) Integer cartId) throws NotFound {
        double total = cartService.calculateTotal(cartId);
        return ResponseEntity.ok(total);
    }
}

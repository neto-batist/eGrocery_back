package com.egrocery_back.services;

import com.egrocery_back.dto.CartDTO;
import com.egrocery_back.dto.CartFilterDTO;
import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.CartEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.repositories.CartRepository;
import com.egrocery_back.repositories.ProductRepository;
import com.egrocery_back.repositories.UsersRepository;
import com.egrocery_back.specifications.CartSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UsersRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UsersRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Mapeia o DTO para a entidade CartEntity
    private CartEntity mapToEntity(CartDTO dto) {
        CartEntity entity = new CartEntity();
        entity.setId(dto.getId());
        entity.setQuantity(dto.getQuantity());

        // Preenche a data atual se não estiver presente
        if (dto.getCreatedAt() == null) {
            entity.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        } else {
            entity.setCreatedAt(dto.getCreatedAt());
        }

        // Mapeamento do produto
        ProductsEntity product = productRepository.findById(dto.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + dto.getProduct().getId()));
        entity.setProductsByProductId(product);

        // Mapeamento do usuário
        if (dto.getUser() != null && dto.getUser().getId() != null) {
            UsersEntity user = userRepository.findById(dto.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getUser().getId()));
            entity.setUsersByUserId(user);
        }

        return entity;
    }

    // Mapeia a entidade CartEntity para o DTO
    private CartDTO mapToDTO(CartEntity entity) {
        CartDTO dto = new CartDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setProduct(mapProductToDTO(entity.getProductsByProductId()));
        return dto;
    }

    // Método auxiliar para mapear ProductEntity para ProductDTO
    private ProductDTO mapProductToDTO(ProductsEntity productEntity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(productEntity.getId());
        dto.setName(productEntity.getName());
        dto.setPrice(productEntity.getPrice());
        dto.setStockQuantity(productEntity.getStockQuantity());
        return dto;
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(CartDTO cartDTO) {
        CartEntity cartEntity = mapToEntity(cartDTO);
        CartEntity savedCart = cartRepository.save(cartEntity);
        return mapToDTO(savedCart);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Integer cartId) throws NotFound {
        CartEntity existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFound("Carrinho", cartId));
        cartRepository.delete(existingCart);
    }

    @Override
    public double calculateTotal(Integer cartId) throws NotFound {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFound("Carrinho", cartId));

        ProductsEntity product = cart.getProductsByProductId();

        if (product == null || product.getPrice() == null) {
            throw new RuntimeException("Produto ou preço inválido para o carrinho com ID: " + cartId);
        }

        BigDecimal total = product.getPrice()
                .multiply(new BigDecimal(cart.getQuantity()));

        return total.doubleValue();
    }

    @Override
    @Transactional
    public CartDTO updateItemInCart(Integer cartId, CartDTO cartDTO) throws NotFound {
        CartEntity existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFound("Carrinho", cartId));

        // Atualiza campos permitidos
        existingCart.setQuantity(cartDTO.getQuantity());

        // Atualiza o produto, se informado
        if (cartDTO.getProduct() != null && cartDTO.getProduct().getId() != null) {
            ProductsEntity product = productRepository.findById(cartDTO.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + cartDTO.getProduct().getId()));
            existingCart.setProductsByProductId(product);
        }

        // Atualiza o usuário, se informado
        if (cartDTO.getUser() != null && cartDTO.getUser().getId() != null) {
            UsersEntity user = userRepository.findById(cartDTO.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + cartDTO.getUser().getId()));
            existingCart.setUsersByUserId(user);
        }

        CartEntity updatedCart = cartRepository.save(existingCart);
        return mapToDTO(updatedCart);
    }

    @Override
    public Page<CartDTO> getAllCarts(CartFilterDTO filter, Pageable pageable) {
        return cartRepository.findAll(CartSpecification.filter(filter), pageable)
                .map(this::mapToDTO);
    }
}

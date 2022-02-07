package com.meli.bootcamp.integrativeproject.unit;

import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderProductRequest;
import com.meli.bootcamp.integrativeproject.dto.request.PurchaseOrderRequest;
import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.mocks.PurchaseOrderServiceMocks;
import com.meli.bootcamp.integrativeproject.repositories.*;
import com.meli.bootcamp.integrativeproject.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PurchaseOrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @Mock
    private WarehouseSectionRepository warehouseSectionRepository;

    private PurchaseOrderService service;

    private Long existentBuyerId = 1L;
    private Long existentPurchaseOrderId = 1L;

    private PurchaseOrderRequest purchaseOrderRequest;

    private List<PurchaseOrderProductRequest> listOfPurchaseOrderProductRequest = new ArrayList<>();

    private List<Product> listOfFakeProducts = new ArrayList<>();

    private List<CartProduct> listOfFakeCartsProducts = new ArrayList<>();

    private Buyer fakeBuyer;

    private WarehouseSection fakeWarehouseSection;

    private Cart fakeCart;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        purchaseOrderRequest = PurchaseOrderServiceMocks.makeFakePurchaseOrderRequest();
        listOfPurchaseOrderProductRequest = PurchaseOrderServiceMocks.makeFakeListPurchaseOrderProductRequest(2);
        fakeBuyer = PurchaseOrderServiceMocks.makeFakeBuyer();
        fakeCart = PurchaseOrderServiceMocks.makeFakeCart();
        listOfFakeProducts = PurchaseOrderServiceMocks.makeFakeListOfProduct(2);
        listOfFakeCartsProducts = PurchaseOrderServiceMocks.makeFakeListOfCartsProduct(2);
        fakeWarehouseSection = PurchaseOrderServiceMocks.makeFakeWarehouseSection();

        service = new PurchaseOrderService(productRepository, buyerRepository, cartRepository, cartProductRepository, warehouseSectionRepository);
    }

    @Test
    public void shouldBeThrowIfBuyerNotExistsWhenTrySave() {
        Long notExistentBuyerId = 999L;

        purchaseOrderRequest.setBuyerId(notExistentBuyerId);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.save(purchaseOrderRequest));

        assertEquals("Buyer not exists!", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfProductDoesNotHaveEnoughStockWhenTrySave() {
        var requestProductOne = listOfPurchaseOrderProductRequest.get(0);
        var fakeProductOne = listOfFakeProducts.get(0);

        requestProductOne.setProductId(1L);
        requestProductOne.setQuantity(20);

        purchaseOrderRequest.setBuyerId(existentBuyerId);
        purchaseOrderRequest.setProducts(Arrays.asList(requestProductOne));

        fakeProductOne.setName("Salsicha");
        fakeProductOne.setQuantity(10);

        when(buyerRepository.findById(anyLong())).thenReturn(Optional.of(fakeBuyer));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(fakeProductOne));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(purchaseOrderRequest));

        assertEquals("Product " + listOfFakeProducts.get(0).getName() + " does not have enough stock for this quantity!", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfProductHasAnExpirationDateOfLessThan3WeeksWhenTrySave() {
        var requestProductOne = listOfPurchaseOrderProductRequest.get(0);
        var fakeProductOne = listOfFakeProducts.get(0);

        requestProductOne.setProductId(1L);
        requestProductOne.setQuantity(1);

        purchaseOrderRequest.setBuyerId(existentBuyerId);
        purchaseOrderRequest.setProducts(Arrays.asList(requestProductOne));

        fakeProductOne.setName("Salsicha");
        fakeProductOne.setQuantity(20);
        fakeProductOne.setDueDate(LocalDate.now().plusWeeks(2));

        when(buyerRepository.findById(anyLong())).thenReturn(Optional.of(fakeBuyer));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(fakeProductOne));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(purchaseOrderRequest));

        assertEquals("Product " + fakeProductOne.getName() + " has an expiration date of less than 3 weeks!", exception.getMessage());
    }

    @Test
    public void shouldBeCreatedSuccessfullyPurchaseOrder() {
        listOfPurchaseOrderProductRequest.get(0).setProductId(1L);
        listOfPurchaseOrderProductRequest.get(0).setQuantity(3);

        listOfPurchaseOrderProductRequest.get(1).setProductId(2L);
        listOfPurchaseOrderProductRequest.get(1).setQuantity(6);

        purchaseOrderRequest.setBuyerId(existentBuyerId);
        purchaseOrderRequest.setProducts(listOfPurchaseOrderProductRequest);

        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(0).setQuantity(10);
        listOfFakeProducts.get(0).setPrice(9.99);

        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeProducts.get(1).setQuantity(10);
        listOfFakeProducts.get(1).setPrice(8.88);

        fakeWarehouseSection.setSize(100);
        fakeWarehouseSection.setTotalProducts(40);

        when(buyerRepository.findById(anyLong())).thenReturn(Optional.of(fakeBuyer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(listOfFakeProducts.get(0)));
        when(productRepository.findById(2L)).thenReturn(Optional.of(listOfFakeProducts.get(1)));
        when(warehouseSectionRepository.findWarehouseSectionByProductId(anyLong())).thenReturn(fakeWarehouseSection);

        var response = service.save(purchaseOrderRequest);

        assertEquals(BigDecimal.valueOf((3 * 9.99) + (6 * 8.88)), response.getTotalPrice());
        assertEquals(7, listOfFakeProducts.get(0).getQuantity());
        assertEquals(4, listOfFakeProducts.get(1).getQuantity());
        assertEquals(31, fakeWarehouseSection.getTotalProducts());
    }

    @Test
    public void shouldBeThrowIfCartNotExistsWhenTryUpdate() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(purchaseOrderRequest, existentPurchaseOrderId));

        assertEquals("Cart not exists!", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfProductDoesNotExistsInTheCartWhenTryUpdate() {
        var productIdNotExistentInTheCart = 999L;
        listOfPurchaseOrderProductRequest.get(0).setProductId(1L);
        listOfPurchaseOrderProductRequest.get(1).setProductId(productIdNotExistentInTheCart);

        purchaseOrderRequest.setProducts(listOfPurchaseOrderProductRequest);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(fakeCart));
        when(cartProductRepository.existsByProduct_IdAndCart_Id(anyLong(), anyLong()))
                .thenReturn(true)
                .thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.update(purchaseOrderRequest, existentPurchaseOrderId));

        assertEquals("This product does not exist in this cart!", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfOrderedQuantityIsGreaterThanWhatIsInStockWhenTryUpdate() {
        listOfPurchaseOrderProductRequest.get(0).setProductId(1L);
        listOfPurchaseOrderProductRequest.get(0).setQuantity(15);

        listOfPurchaseOrderProductRequest.get(1).setProductId(2L);
        listOfPurchaseOrderProductRequest.get(1).setQuantity(21);

        purchaseOrderRequest.setProducts(listOfPurchaseOrderProductRequest);

        listOfFakeProducts.get(0).setId(1L);
        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(0).setQuantity(10);
        listOfFakeCartsProducts.get(0).setProduct(listOfFakeProducts.get(0));
        listOfFakeCartsProducts.get(0).setQuantity(5);

        listOfFakeProducts.get(1).setId(2L);
        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeProducts.get(1).setQuantity(10);
        listOfFakeCartsProducts.get(1).setProduct(listOfFakeProducts.get(1));
        listOfFakeCartsProducts.get(1).setQuantity(5);

        fakeCart.setCartsProducts(listOfFakeCartsProducts);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(fakeCart));
        when(cartProductRepository.existsByProduct_IdAndCart_Id(anyLong(), anyLong()))
                .thenReturn(true)
                .thenReturn(true);
        when(warehouseSectionRepository.findWarehouseSectionByProductId(anyLong())).thenReturn(fakeWarehouseSection);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.update(purchaseOrderRequest, existentPurchaseOrderId));

        assertEquals("Ordered quantity to product " + listOfFakeProducts.get(1).getName() + " is greater than what is in stock!", exception.getMessage());
    }

    @Test
    public void shouldBeUpdatedSuccessfullyCartWithDecreaseTotalProductsFromWarehouseSection() {
        listOfPurchaseOrderProductRequest.get(0).setProductId(1L);
        listOfPurchaseOrderProductRequest.get(0).setQuantity(7);

        listOfPurchaseOrderProductRequest.get(1).setProductId(2L);
        listOfPurchaseOrderProductRequest.get(1).setQuantity(9);

        purchaseOrderRequest.setProducts(listOfPurchaseOrderProductRequest);

        listOfFakeProducts.get(0).setId(1L);
        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(0).setQuantity(5);
        listOfFakeProducts.get(0).setPrice(3.45);
        listOfFakeCartsProducts.get(0).setProduct(listOfFakeProducts.get(0));
        listOfFakeCartsProducts.get(0).setQuantity(5);

        listOfFakeProducts.get(1).setId(2L);
        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeProducts.get(1).setQuantity(5);
        listOfFakeProducts.get(1).setPrice(7.32);
        listOfFakeCartsProducts.get(1).setProduct(listOfFakeProducts.get(1));
        listOfFakeCartsProducts.get(1).setQuantity(5);

        fakeCart.setCartsProducts(listOfFakeCartsProducts);

        fakeWarehouseSection.setSize(100);
        fakeWarehouseSection.setTotalProducts(10);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(fakeCart));
        when(cartProductRepository.existsByProduct_IdAndCart_Id(anyLong(), anyLong()))
                .thenReturn(true)
                .thenReturn(true);
        when(warehouseSectionRepository.findWarehouseSectionByProductId(anyLong())).thenReturn(fakeWarehouseSection);

        var response = service.update(purchaseOrderRequest, existentPurchaseOrderId);

        assertEquals(BigDecimal.valueOf((7 * 3.45) + (9 * 7.32)), response.getTotalPrice());
        assertEquals(3, fakeCart.getCartsProducts().get(0).getProduct().getQuantity());
        assertEquals(1, fakeCart.getCartsProducts().get(1).getProduct().getQuantity());
        assertEquals(4, fakeWarehouseSection.getTotalProducts());
    }

    @Test
    public void shouldBeUpdatedSuccessfullyCartWithIncreaseTotalProductsFromWarehouseSection() {
        listOfPurchaseOrderProductRequest.get(0).setProductId(1L);
        listOfPurchaseOrderProductRequest.get(0).setQuantity(3);

        listOfPurchaseOrderProductRequest.get(1).setProductId(2L);
        listOfPurchaseOrderProductRequest.get(1).setQuantity(1);

        purchaseOrderRequest.setProducts(listOfPurchaseOrderProductRequest);

        listOfFakeProducts.get(0).setId(1L);
        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeProducts.get(0).setQuantity(5);
        listOfFakeProducts.get(0).setPrice(3.45);
        listOfFakeCartsProducts.get(0).setProduct(listOfFakeProducts.get(0));
        listOfFakeCartsProducts.get(0).setQuantity(5);

        listOfFakeProducts.get(1).setId(2L);
        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeProducts.get(1).setQuantity(5);
        listOfFakeProducts.get(1).setPrice(7.32);
        listOfFakeCartsProducts.get(1).setProduct(listOfFakeProducts.get(1));
        listOfFakeCartsProducts.get(1).setQuantity(5);

        fakeCart.setCartsProducts(listOfFakeCartsProducts);

        fakeWarehouseSection.setSize(100);
        fakeWarehouseSection.setTotalProducts(10);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(fakeCart));
        when(cartProductRepository.existsByProduct_IdAndCart_Id(anyLong(), anyLong()))
                .thenReturn(true)
                .thenReturn(true);
        when(warehouseSectionRepository.findWarehouseSectionByProductId(anyLong())).thenReturn(fakeWarehouseSection);

        var response = service.update(purchaseOrderRequest, existentPurchaseOrderId);

        assertEquals(BigDecimal.valueOf((3 * 3.45) + (1 * 7.32)), response.getTotalPrice());
        assertEquals(7, fakeCart.getCartsProducts().get(0).getProduct().getQuantity());
        assertEquals(9, fakeCart.getCartsProducts().get(1).getProduct().getQuantity());
        assertEquals(16, fakeWarehouseSection.getTotalProducts());
    }

    @Test
    public void shouldBeThrowIfNoOrdersWereFoundForTheCartId() {
        Long notExistentCartId = 999L;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findByCartId(notExistentCartId));

        assertEquals("No orders were found for the given id", exception.getMessage());
    }

    @Test
    public void shouldBeReturnListOfCartProductsIfOrdersWereFoundForTheCartId() {
        Long existentCartId = 1L;

        listOfFakeProducts.get(0).setId(1L);
        listOfFakeProducts.get(0).setName("Salsicha");
        listOfFakeCartsProducts.get(0).setProduct(listOfFakeProducts.get(0));

        listOfFakeProducts.get(1).setId(2L);
        listOfFakeProducts.get(1).setName("Frango");
        listOfFakeCartsProducts.get(1).setProduct(listOfFakeProducts.get(1));

        when(cartProductRepository.findByCartId(anyLong())).thenReturn(listOfFakeCartsProducts);

        var response = service.findByCartId(existentCartId);

        assertEquals(2, response.size());
        assertEquals("Salsicha", response.get(0).getProduct().getName());
        assertEquals("Frango", response.get(1).getProduct().getName());
    }
}

package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(new ProductRepository());
    }

    @Test
    void testUpdateProduct_positive_idExists_shouldUpdateNameAndQuantity() {

        Product p = new Product();
        p.setProductId("p1");
        p.setProductName("Old Name");
        p.setProductQuantity(10);
        productService.create(p);

        Product updated = new Product();
        updated.setProductId("p1");
        updated.setProductName("New Name");
        updated.setProductQuantity(99);

        productService.update(updated);

        Product result = productService.findById("p1");

        assertNotNull(result);
        assertEquals("New Name", result.getProductName());
        assertEquals(99, result.getProductQuantity());
    }

    @Test
    void testUpdateProduct_negative_idNotExists_shouldNotCreateNewProduct() {

        Product updated = new Product();
        updated.setProductId("does-not-exist");
        updated.setProductName("X");
        updated.setProductQuantity(1);

        productService.update(updated);

        List<Product> all = productService.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testDeleteProduct_positive_idExists_shouldRemoveProduct() {

        Product p = new Product();
        p.setProductId("p1");
        p.setProductName("To Delete");
        p.setProductQuantity(1);
        productService.create(p);

        productService.deleteById("p1");

        assertNull(productService.findById("p1"));
        assertTrue(productService.findAll().isEmpty());
    }

    @Test
    void testDeleteProduct_negative_idNotExists_shouldNotAffectExistingProducts() {

        Product p = new Product();
        p.setProductId("p1");
        p.setProductName("Keep Me");
        p.setProductQuantity(5);
        productService.create(p);

        productService.deleteById("does-not-exist");

        Product stillThere = productService.findById("p1");

        assertNotNull(stillThere);
        assertEquals("Keep Me", stillThere.getProductName());
        assertEquals(5, stillThere.getProductQuantity());
        assertEquals(1, productService.findAll().size());
    }
}

package com.picaxo.drool.rest;

import com.picaxo.drool.model.Product;
import com.picaxo.drool.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleController {
    private final ProductService productService;

    @Autowired
    public RuleController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/get-discount", method = RequestMethod.GET, produces = "application/json")
    public Product getQuestions(@RequestParam(required = true) String type) {

        Product product = new Product();
        product.setType(type);
        productService.getProductDiscount(product);
        return product;
    }
}

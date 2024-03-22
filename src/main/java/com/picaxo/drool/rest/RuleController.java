package com.picaxo.drool.rest;

import com.picaxo.drool.model.Passport;
import com.picaxo.drool.model.Product;
import com.picaxo.drool.model.VisaApplication;
import com.picaxo.drool.service.PassportService;
import com.picaxo.drool.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RuleController {

    private final ProductService productService;
    private final PassportService passportService;


    @Autowired
    public RuleController(ProductService productService, PassportService passportService) {
        this.productService = productService;
        this.passportService = passportService;
    }


    @RequestMapping(value = "/get-discount", method = RequestMethod.GET, produces = "application/json")
    public Product getQuestions(@RequestParam(required = true) String type) {

        Product product = new Product();
        product.setType(type);
        productService.getProductDiscount(product);
        return product;
    }

    @RequestMapping(value = "/get-validate-passports", method = RequestMethod.GET, produces = "application/json")
    public List<Passport> getValidatePassports(@RequestParam(value = "key") String key) {
        return passportService.getValidatePassports(key);
    }

    @RequestMapping(value = "/get-stateful-validate-passports", method = RequestMethod.GET, produces = "application/json")
    public List<Passport> getStatefulValidatePassports(@RequestParam(value = "key") String key) {
        return passportService.getStatefulValidatePassports(key);
    }

    @RequestMapping(value = "/get-validate-visa-applications", method = RequestMethod.GET, produces = "application/json")
    public List<VisaApplication> getValidateVisaApplications(@RequestParam(value = "key") String key) {
        return passportService.getValidateVisaApplications(key);
    }
}

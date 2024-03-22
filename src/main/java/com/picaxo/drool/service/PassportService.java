package com.picaxo.drool.service;

import com.picaxo.drool.Common;
import com.picaxo.drool.model.Passport;
import com.picaxo.drool.model.Product;
import com.picaxo.drool.model.VisaApplication;
import com.picaxo.drool.repository.ApplicationRepository;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassportService {
    private final KieContainer kieContainer;

    @Autowired
    public PassportService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public List<Passport> getValidatePassports(String key) {
        List<Passport> passports = ApplicationRepository.getPassports();
        StatelessKieSession kieSession = kieContainer.newStatelessKieSession("StatelessPassportValidation" + key);
        System.out.println("==== DROOLS SESSION START ==== ");
        kieSession.execute(passports);
        System.out.println("==== DROOLS SESSION END ==== ");

        System.out.println("==== PASSPORTS AFTER DROOLS SESSION === ");
        passports.forEach(passport -> {
            System.out.println(passport + " verdict: " + passport.getValidation() + ", cause: " + passport.getCause());
        });

        return passports;
    }

    public List<Passport> getStatefulValidatePassports(String key) {
        KieSession kieSession = kieContainer.newKieSession("StatefulPassportValidation" + key);
        List<Passport> passports = ApplicationRepository.getPassports();

        passports.forEach(kieSession::insert);

        System.out.println("==== DROOLS SESSION START ==== ");
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("==== DROOLS SESSION END ==== ");

        System.out.println("==== PASSPORTS AFTER DROOLS SESSION === ");
        passports.forEach(passport -> {
            System.out.println(passport + " verdict: " + passport.getValidation() + ", cause: " + passport.getCause());
        });

//        if (Common.disposeSession) {
//            kieSession.dispose();
//        }

        return passports;
    }

    public List<VisaApplication> getValidateVisaApplications(String key) {
        KieSession kieSession = kieContainer.newKieSession("VisaApplicationValidation" + key);

        List<Passport> passports = ApplicationRepository.getPassports();
        passports.forEach(kieSession::insert);

        List<VisaApplication> visaApplications = ApplicationRepository.getVisaApplications();
        for (VisaApplication visaApplication : visaApplications) {
            kieSession.insert(visaApplication);
        }

        System.out.println("==== DROOLS SESSION START ==== ");
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("==== DROOLS SESSION END ==== ");

        System.out.println("==== PASSPORTS AFTER DROOLS SESSION === ");
        passports.forEach(passport -> {
            System.out.println(passport + " verdict: " + passport.getValidation() + ", cause: " + passport.getCause());
        });

        System.out.println("==== APPLICATIONS STATE AFTER DROOLS SESSION === ");
        visaApplications.forEach(visaApplication ->
                System.out.println(visaApplication + " verdict: " + visaApplication.getValidation())
        );

        return visaApplications;
    }
}

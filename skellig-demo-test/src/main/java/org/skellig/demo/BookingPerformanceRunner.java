package org.skellig.demo;

import org.jetbrains.annotations.NotNull;
import org.skellig.performance.runner.service.SkelligPerformanceServiceRunner;
import org.skellig.teststep.runner.context.SkelligTestContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class BookingPerformanceRunner extends SkelligPerformanceServiceRunner {

    @NotNull
    @Override
    protected String getConfigFileName() {
        return "skellig-demo-${test.profile}.conf";
    }

    @NotNull
    @Override
    protected String[] getTestSteps() {
        return new String[]{"tests", "org.skellig.demo"};
    }

    @NotNull
    @Override
    protected SkelligTestContext getContext() {
        return new SkelligDemoContext();
    }

    public static void main(String[] args) {
        SpringApplication.run(BookingPerformanceRunner.class, args);
    }

}
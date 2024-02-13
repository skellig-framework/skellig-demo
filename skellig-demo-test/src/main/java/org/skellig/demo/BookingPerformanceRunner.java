package org.skellig.demo;

import org.jetbrains.annotations.NotNull;
import org.skellig.performance.runner.service.SkelligPerformanceServiceRunner;
import org.springframework.boot.SpringApplication;

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

    public static void main(String[] args) {
        SpringApplication.run(BookingPerformanceRunner.class, args);
    }

}
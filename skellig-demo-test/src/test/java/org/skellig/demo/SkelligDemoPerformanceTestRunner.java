package org.skellig.demo;

import org.junit.runner.RunWith;
import org.skellig.runner.SkelligRunner;
import org.skellig.runner.annotation.SkelligOptions;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RunWith(SkelligRunner.class)
@SkelligOptions(
        features = {"tests/performance.sf"},
        testSteps = {"tests", "org.skellig.demo"},
        context = SkelligDemoContext.class,
        config = "skellig-demo-${test.profile}.conf")
public class SkelligDemoPerformanceTestRunner {
}

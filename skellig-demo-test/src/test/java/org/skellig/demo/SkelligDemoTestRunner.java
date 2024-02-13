package org.skellig.demo;

import org.junit.runner.RunWith;
import org.skellig.runner.SkelligRunner;
import org.skellig.runner.annotation.SkelligOptions;

@RunWith(SkelligRunner.class)
@SkelligOptions(
        features = {"tests"},
        testSteps = {"tests", "org.skellig.demo"},
        config = "skellig-demo-${test.profile}.conf")
public class SkelligDemoTestRunner {
}

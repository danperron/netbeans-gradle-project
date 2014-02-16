package org.netbeans.gradle.project.tasks;

import org.openide.util.Lookup;

public final class TestTaskName {
    public static final String DEFAULT_TEST_TASK_NAME = "test";
    public static final String DEFAULT_CLEAN_TEST_TASK_NAME = "cleanTest";

    private final String taskName;

    public TestTaskName(String taskName) {
        if (taskName == null) throw new NullPointerException("taskName");

        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public static String getTaskName(Lookup lookup) {
        String result = tryGetTaskName(lookup);
        return result != null ? result : DEFAULT_TEST_TASK_NAME;
    }

    public static String tryGetTaskName(Lookup lookup) {
        TestTaskName name = lookup.lookup(TestTaskName.class);
        return name != null ? name.taskName : null;
    }
}

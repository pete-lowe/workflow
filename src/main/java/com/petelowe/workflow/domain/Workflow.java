package com.petelowe.workflow.domain;

import java.util.List;

public record Workflow(
        String workflowName,
        String workflowId,
        List<Task> tasks
) {}

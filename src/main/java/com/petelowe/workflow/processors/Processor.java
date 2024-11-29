package com.petelowe.workflow.processors;

import com.petelowe.workflow.domain.Task;

public interface Processor {
    void execute(Task task);

}

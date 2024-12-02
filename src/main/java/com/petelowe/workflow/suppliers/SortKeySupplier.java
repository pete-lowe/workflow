package com.petelowe.workflow.suppliers;


import com.petelowe.workflow.domain.TaskType;

public class SortKeySupplier {

    public static String getWorkflowSortKey() {
        return "DataType#WORKFLOW#";
    }

    public static String getTaskDefinitionSortKey() {
        return "DataType#TASK";
    }

    public static String getWorkflowTaskSortKey(TaskType taskType) {
        return String.format("DataType#WORKFLOW_TASK#TaskType#%s", taskType);
    }

}

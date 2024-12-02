package com.petelowe.workflow.suppliers;

public class PartitionKeySupplier {

    public static String getWorkflowPartitionKey(String workflowId) {
        return String.format("WorkflowId#%s", workflowId);
    }

    public static String getTaskDefinitionPartitionKey(String workflowId) {
        return String.format("TaskId#%s", workflowId);
    }


}

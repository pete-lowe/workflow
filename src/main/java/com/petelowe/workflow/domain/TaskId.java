package com.petelowe.workflow.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public record TaskId(@JsonValue String id) {}

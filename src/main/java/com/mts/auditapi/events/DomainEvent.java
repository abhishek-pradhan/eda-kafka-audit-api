package com.mts.auditapi.events;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

// DomainEvent is based on DDD's Domain Event and
//  AWS EventBridge event pattern: https://docs.aws.amazon.com/eventbridge/latest/userguide/eb-events.html
@Data
public class DomainEvent {
    private String version;
    private UUID id;
    private String detailType;
    private String source;
    private Date date;
    private boolean isError;
    private String message;

    // each Event will have specific Domain's data, in this case it's json result from outbound city api
    private DetailEntity detailEntity;
}

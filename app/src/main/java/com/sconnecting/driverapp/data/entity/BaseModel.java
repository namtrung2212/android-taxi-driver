
package com.sconnecting.driverapp.data.entity;

import java.util.Date;


public interface BaseModel {

    String getId();
    Date getRetrieveAt();
    Date getCreatedAt();
    Date getUpdatedAt();
    Date getUsedAt();
    void setRetrieveAt(Date value);
    void setUsedAt(Date value);
    boolean isNew();
}

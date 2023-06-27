package com.hun.security.api.model;

import lombok.Data;

/**
 * 用户详细信息
 */
@Data
public class HunUser {

    /**
     * 用户ID
     */
    private String userId;

    private String bizUserId;

    private Boolean enabled;

    /**
     * 自提点Id
     */
    private Long stationId;

    /**
     * 店铺Id
     */
    private Long shopId;
}

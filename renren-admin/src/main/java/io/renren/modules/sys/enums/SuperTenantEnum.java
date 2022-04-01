/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.enums;

/**
 * 租户管理员枚举
 *
 * @author Mark sunlightcs@gmail.com
 */
public enum SuperTenantEnum {
    YES(1),
    NO(0);

    private int value;

    SuperTenantEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
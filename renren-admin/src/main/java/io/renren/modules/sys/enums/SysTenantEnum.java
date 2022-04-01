/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.enums;

/**
 * 系统租户枚举
 *
 * @author Mark sunlightcs@gmail.com
 */
public enum SysTenantEnum {
    YES(1),
    NO(0);

    private int value;

    SysTenantEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
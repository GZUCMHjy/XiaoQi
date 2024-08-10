package com.louis.springbootinit.utils;

import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.exception.BusinessException;

import java.lang.reflect.Method;

/**
 * @author namego
 */
public class EnumUtils {
    public static <T extends Enum<T>> T getById(Class<T> enumClass, int id) {
        try {
            Method getIdMethod = enumClass.getMethod("getId");
            for (T enumConstant : enumClass.getEnumConstants()) {
                if ((int) getIdMethod.invoke(enumConstant) == id) {
                    return enumConstant;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有当前枚举");
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有当前枚举");
    }
}

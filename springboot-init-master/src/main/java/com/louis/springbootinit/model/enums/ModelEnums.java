package com.louis.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/25 21:16
 */
public enum ModelEnums {
    MODEL1("5","产业发展专题大模型", "7e128837311143f7bee6d4c5b61124cd","sk-d3e9c210958d426faceda15da1e408f8"),
    MODEL2("0","临床辅助专题大模型", "56ffd07f51d0443bb5b8191089216841","sk-d3e9c210958d426faceda15da1e408f8"),
    MODEL3("4","行业管理专题大模型", "5d946e77233c4dc7afc02e91818cae0f","sk-d3e9c210958d426faceda15da1e408f8"),
    MODEL4("2","个人健康管理大模型", "447cf51cce3e4882adb9acdd9b409a1a","sk-d3e9c210958d426faceda15da1e408f8"),
    MODEL5("1","科研创新专题大模型", "5aa00e0331324fafbac31b9514ebb25e","sk-d3e9c210958d426faceda15da1e408f8"),
    MODEL6("3","医院管理专题大模型", "1f2642f8b00149e9960e6a0433575c62","sk-d3e9c210958d426faceda15da1e408f8");
    private String modelName;
    private String modelId;
    private String appKey;
    private String apiKey;
    ModelEnums(String modelId,String modelName, String appKey, String apiKey){
        this.apiKey = apiKey;
        this.modelName = modelName;
        this.appKey = appKey;
        this.modelId = modelId;
    }
    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ModelEnums getEnumByModelName(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ModelEnums anEnum : ModelEnums.values()) {
            if (anEnum.modelName.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
    public static ModelEnums getEnumByModelId(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ModelEnums anEnum : ModelEnums.values()) {
            if (anEnum.modelId.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
    public String getModelName(){
        return modelName;
    }
    public String getAppKey(){
        return appKey;
    }
    public String getApiKey(){
        return apiKey;
    }
    public String getModelId(){
        return modelId;
    }
}

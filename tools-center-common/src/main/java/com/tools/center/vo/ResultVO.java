package com.tools.center.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tools.center.api.ErrorType;
import com.tools.center.enums.SystemEnum;
import com.tools.center.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class ResultVO<T> implements Serializable {

    public static final Integer SUCCESSFUL_CODE = 2000;
    public static final String SUCCESSFUL_MSG = "处理成功";

    @ApiModelProperty(value = "处理结果code", required = true)
    private Integer code;
    @ApiModelProperty(value = "处理结果描述信息")
    private String msg;
    @ApiModelProperty(value = "请求结果生成时间戳")
    private String time;
    @ApiModelProperty(value = "处理结果数据信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResultVO() {
        this.time = DateUtil.getCurrentDateTime();
    }

    /**
     * @param errorType
     */
    public ResultVO(ErrorType errorType) {
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
        this.time = DateUtil.getCurrentDateTime();
    }

    public ResultVO(ErrorType errorType, T data) {
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
        this.data = data;
        this.time = DateUtil.getCurrentDateTime();
    }



    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param msg
     * @param data
     */
    private ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.time = DateUtil.getCurrentDateTime();
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param msg
     */
    private ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.time = DateUtil.getCurrentDateTime();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @return Result
     */
    public static ResultVO success() {
        return new ResultVO<>(SUCCESSFUL_CODE, SUCCESSFUL_MSG);
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static ResultVO success(Object data) {
        return new ResultVO<>(SUCCESSFUL_CODE, SUCCESSFUL_MSG, data);
    }


    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static ResultVO fail() {
        return new ResultVO(SystemEnum.SYSTEM_ERROR);
    }

    public static ResultVO fail(SystemEnum errorEnum,Object object) {
        return new ResultVO(errorEnum,object);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static ResultVO error(SystemEnum errorEnum) {
        return new ResultVO(errorEnum);
    }

    @JsonIgnore
    public boolean isSuccess(){
        return SUCCESSFUL_CODE.equals(this.code);
    }

    @JsonIgnore
    public boolean isFail(){
        return !isSuccess();
    }
}

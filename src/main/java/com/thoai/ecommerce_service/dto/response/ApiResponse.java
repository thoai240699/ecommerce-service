package com.thoai.ecommerce_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

// JsonInclude: Để chỉ định cách mà các thuộc tính của đối tượng sẽ được bao gồm trong JSON. Quy ước không bao gồm các thuộc tính có giá trị null trong JSON
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    // Quy định mã trạng thái cho phản hồi API, thành công là 200
    private int code = 200;
    private String message;
    // T là kiểu dữ liệu tổng quát, có thể là bất kỳ kiểu dữ liệu nào
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

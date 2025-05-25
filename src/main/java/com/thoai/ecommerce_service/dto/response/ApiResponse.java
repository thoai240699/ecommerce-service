package com.thoai.ecommerce_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

// JsonInclude: Để chỉ định cách mà các thuộc tính của đối tượng sẽ được bao gồm trong JSON. Quy ước không bao gồm các thuộc tính có giá trị null trong JSON
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ApiResponse <T> {
    // Quy định mã trạng thái cho phản hồi API, thành công là 200
    int code;
    String message;
    // T là kiểu dữ liệu tổng quát, có thể là bất kỳ kiểu dữ liệu nào
    T result;
}

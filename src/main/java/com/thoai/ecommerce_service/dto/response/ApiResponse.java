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
    @Builder.Default
    int code=200;
    String message;
    T result;
}

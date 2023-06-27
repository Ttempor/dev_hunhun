package com.hun.bean.dto;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;

@Data
public class DeleteIdDto {
    @Id
    private Long id;
}

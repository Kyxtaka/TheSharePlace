package com.accountplace.api.dto.review;

import lombok.*;

@Data
@AllArgsConstructor
public class PlatformDto {
    private Integer plateformId;
    private String plateformName;
    private String url;
    private String imgRef;
}

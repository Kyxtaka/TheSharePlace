package com.accountplace.api.dto.crud.update;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PlatformUpdateDTO implements Serializable {
    /**
     * The unique identifier for the platform.
     */
    private Integer plateformId;

    /**
     * The name of the platform.
     */
    private String plateformName;

    /**
     * The URL associated with the platform.
     */
    private String url;

    /**
     * A reference to the image associated with the platform.
     */
    private String imgRef;
}

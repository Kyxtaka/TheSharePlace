package com.accountplace.api.dto.crud.create;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PlatformCreateDTO implements Serializable {
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

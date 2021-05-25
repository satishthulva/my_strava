package com.satish.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DatabaseConfig {
    private String url;
    private String driver;
    private String userName;
    private String password;
    private int minPoolSize;
    private int maxPoolSize;
}

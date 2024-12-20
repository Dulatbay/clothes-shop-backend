package com.example.shop.clothesshop.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValueConstants {
    public static final String UPLOADED_FOLDER = "upload-dir/";
    public static final ZoneId ZONE_ID = ZoneId.of("UTC+00:00");
}

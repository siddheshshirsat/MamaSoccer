package com.mama.soccer.data.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CardData {
    private String title;
    private String tileImage;
    private String ctaHttpUrl;
}

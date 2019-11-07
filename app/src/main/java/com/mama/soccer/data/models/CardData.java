package com.mama.soccer.data.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CardData {
    private String title;
    private String team1Image;
    private String team2Image;
    private String ctaHttpUrl;
}

package com.mama.soccer.data.fetch;

import android.util.Log;

import com.mama.soccer.ui.main.CardTopic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Callable;

import lombok.Value;

@Value
public class EventLinkParserTask implements Callable<Boolean> {
    private String url;
    private CardTopic cardTopic;

    @Override
    public Boolean call() {
        try {
            Document eventDoc = Jsoup.connect(url).get();
            Elements eventStreamLinks = eventDoc.select("a[href]");

            Elements titleElements = eventDoc.select("title");
            String title = titleElements.get(0).html();

            Elements teamImageRows = eventDoc.select("tr");
            Elements teamImageElements = teamImageRows.get(0).select("img[src]");

            for (Element eventStreamLink : eventStreamLinks) {
                String eventStreamLinkText = eventStreamLink.attr("href");
                if (eventStreamLinkText.contains("http://www.mamacdn.com/link.php?asad=")) {
                    com.mama.soccer.data.models.CardData cardData = com.mama.soccer.data.models.CardData.builder()
                            .title(title)
                            .team1Image(teamImageElements.get(0).attr("src"))
                            .team2Image(teamImageElements.get(1).attr("src"))
                            .ctaHttpUrl(eventStreamLinkText.substring("http://www.mamacdn.com/link.php?asad=".length()))
                            .build();
                    cardTopic.getPublishSubject().onNext(cardData);
                }
            }
        } catch(IOException e) {
            Log.e("MainFragment", "Failed to call event url", e);
        }
        return true;
    }
}

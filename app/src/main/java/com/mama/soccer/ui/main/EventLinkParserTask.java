package com.mama.soccer.ui.main;

import android.util.Log;

import com.mama.soccer.data.models.CardData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Callable;

import lombok.Value;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/8/16.
 */
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

            for (Element eventStreamLink : eventStreamLinks) {
                String eventStreamLinkText = eventStreamLink.attr("href");
                if (eventStreamLinkText.contains("http://www.mamacdn.com/link.php?asad=")) {
                    CardData cardData = CardData.builder()
                            .title(title)
                            .tileImage("https://talksport.com/wp-content/uploads/sites/5/2019/10/NINTCHDBPICT000535500603.jpg")
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

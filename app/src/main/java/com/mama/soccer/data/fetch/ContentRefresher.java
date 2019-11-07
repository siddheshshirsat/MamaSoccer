package com.mama.soccer.data.fetch;

import android.util.Log;

import com.mama.soccer.ui.main.CardTopic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import lombok.Value;

@Value
public class ContentRefresher  {
    ExecutorService executorService;
    List<CardTopic> cardTopics;

    public void refresh() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://mamahd.live/upcoming/").get();
                    Elements links = doc.select("a[href]");

                    for(Element link: links) {
                        String linkText = link.attr("href");
                        for(int i=0; i<cardTopics.size(); i++) {
                            if(linkText.contains("https://mamahd.live/" + cardTopics.get(i).getUrlSuffix())) {
                                executorService.submit(new EventLinkParserTask(linkText, cardTopics.get(i)));
                                break;
                            }
                        }
                    }
                } catch(IOException e) {
                    Log.e("MainFragment", "Failed to call main url", e);
                }
            }
        });
    }
}

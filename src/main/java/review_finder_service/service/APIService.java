package review_finder_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by leviathan on 2017.01.03..
 */
public class APIService {
    private static final Logger logger = LoggerFactory.getLogger(APIService.class);
    private static final String API_URL = "https://www.google.com/search";
    private static APIService INSTANCE;

    private APIService(){}

    public static APIService getInstance() {
        if (INSTANCE == null) {
            logger.info("Creating new APIService.");
            INSTANCE = new APIService();
        }
        logger.info("Using existing APIService");
        return INSTANCE;
    }

    /*

     */
    /**
     * Uses the Jsoup library to parse the html
     * Returns a String[5][2] Array containing the top 5 search results as<br>
     * returned[x][0]: title of the search result<br>
     * returned[x][1]: URL of the search result<br>
     * (where x in range(0, 5))
     * @param title the name of the product to search reviews for
     * @return      5x2 String array containing the titles and URLs
     */
    public String[][] findReviews(String title) throws IOException {
        logger.info("Building URL, trying to connect.");
        String searchURL = API_URL + "?q=" + title + "+review&num=5";
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
        String[][] reviews = new String[5][2];
        int idx = 0;

        Elements results = doc.select("h3.r > a");
        logger.info("Connection successful, parsing URLs");
        for (Element result : results) {
            String linkText = result.text();
            reviews[idx][0] = linkText;
            String linkHref = result.attr("href");
            reviews[idx][1] = linkHref.substring(7, linkHref.indexOf("&"));
            idx++;
            logger.info(idx + ". URL parsed.");
            logger.info("Text::" + linkText + ", URL::" + linkHref.substring(7, linkHref.indexOf("&")));
            if (idx == 5){
                break;
            }
        }
        return reviews;
    }
}

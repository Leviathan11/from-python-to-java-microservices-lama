package review_finder_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import review_finder_service.controller.ReviewAPIController;
import review_finder_service.service.APIService;

import java.net.URISyntaxException;

import static spark.Spark.*;

/**
 * Created by leviathan on 2017.01.03..
 */
public class ReviewFinderService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewFinderService.class);

    private ReviewAPIController controller;

    /**
     * The main method of the microservice, this is meant to run
     * @param args port number
     */
    public static void main(String[] args) {
        logger.debug("Starting " + ReviewFinderService.class.getName() + "...");

        setup(args);

        ReviewFinderService application = new ReviewFinderService();

        application.controller = new ReviewAPIController(APIService.getInstance());

        // --- MAPPINGS ---
        get("/api/review", application.controller::getReviews);

        // --- EXCEPTION HANDLING ---
        exception(URISyntaxException.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("URI building error, maybe wrong format? : %s", exception.getMessage()));
            logger.error("Error while processing request", exception);
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("Unexpected error occurred: %s", exception.getMessage()));
            logger.error("Error while processing request", exception);
        });
    }

    /**
     * Setting up port
     * @param args port number
     */
    private static void setup(String[] args){
        if(args == null || args.length == 0){
            logger.error("Port must be given as the first argument.");
            System.exit(-1);
        }

        try {
            port(Integer.parseInt(args[0]));
        } catch (NumberFormatException e){
            logger.error("Invalid port given '{}', it should be number.", args[0]);
            System.exit(-1);
        }
    }
}

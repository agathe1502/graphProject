import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import models.ShortestPathAlgorithmModel;
import models.VRP1Model;

import java.io.File;

public class MyApiVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyApiVerticle.class);
    private final AlgorithmService algorithmService = new AlgorithmService();

    private GsonBuilder builder;
    private Gson gson;


    // Quand le verticle se lance
    @Override
    public void start() {
        LOGGER.info("Démarrage du serveur");
        builder = new GsonBuilder();
        gson = builder.create();
        // Création du routeur
        Router router = Router.router(vertx);
        // Handling erreurs
        router.errorHandler(500, rc -> {
            System.err.println("Handling failure");
            Throwable failure = rc.failure();
            if (failure != null) {
                failure.printStackTrace();
            }
        });

        // Définition des routes
        router.get("/api/dijkstra/:dmax/:popmin/:srcVertex/:endVertex")
                .handler(this::dijkstra);
        router.get("/api/dijkstraFibo/:dmax/:popmin/:srcVertex/:endVertex")
                .handler(this::dijkstraFibo);
        router.get("/api/astar/:dmax/:popmin/:srcVertex/:endVertex")
                .handler(this::astar);
        router.get("/api/createFile/:dmax/:popmin")
                .handler(this::createFile);
        router.get("/api/vrp1")
                .handler(this::VRP1);

        // Lancement du serveur
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    // Quand le verticle s'arrête
    @Override
    public void stop() {
        LOGGER.info("Fermeture du serveur");
    }


    private int[] getParamShortestPathAlgorithm(RoutingContext routingContext) {
        final int dmax = Integer.parseInt(routingContext.request().getParam("dmax"));
        final int popmin = Integer.parseInt(routingContext.request().getParam("popmin"));
        if(routingContext.request().getParam("srcVertex") != null){
            final int srcVertex = Integer.parseInt(routingContext.request().getParam("srcVertex"));
            final int endVertex = Integer.parseInt(routingContext.request().getParam("endVertex"));
            return new int[]{dmax, popmin, srcVertex, endVertex};
        }
        return new int[]{dmax, popmin};

    }

    private void dijkstra(RoutingContext routingContext) {
        int[] params = getParamShortestPathAlgorithm(routingContext);
        final ShortestPathAlgorithmModel model = algorithmService.runDijkstra(params[0], params[1], params[2], params[3]);
        setCorsPolicy(routingContext, model);

    }

    private void dijkstraFibo(RoutingContext routingContext) {
        int[] params = getParamShortestPathAlgorithm(routingContext);
        final ShortestPathAlgorithmModel model = algorithmService.runDijkstraFibo(params[0], params[1], params[2], params[3]);
        setCorsPolicy(routingContext, model);

    }

    private void astar(RoutingContext routingContext) {
        int[] params = getParamShortestPathAlgorithm(routingContext);
        final ShortestPathAlgorithmModel model = algorithmService.runAstar(params[0], params[1], params[2], params[3]);
        setCorsPolicy(routingContext, model);
    }

    private void VRP1(RoutingContext routingContext) {

        final VRP1Model vrp1Model = algorithmService.runVRP1();
        setCorsPolicy(routingContext, vrp1Model);

    }

    private void createFile(RoutingContext routingContext) {
        int[] params = getParamShortestPathAlgorithm(routingContext);

        CSVtoTXT fileCSV = new CSVtoTXT();
        String fileName = "src/files/Communes_" + params[0] + "_" + params[1] + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(params[0], params[1], !file.isFile());

        setCorsPolicy(routingContext, fileCSV.getListCitiesModel());


    }

    private void setCorsPolicy(RoutingContext routingContext, Object model){
        routingContext.response()
                .putHeader("content-type", "application/json")
                .putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .setStatusCode(200)
                .putHeader("content-type", "application/json")
                .end(gson.toJson(model));
    }
}
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
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui génère le serveur avec les différenres routes à appeler pour obtenir les résultats sur l'interface graphique
 */
public class ApiClass extends AbstractVerticle {

    /**
     * Instance qui permet d'afficher les Logs
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClass.class);
    /**
     * Instance de la classe AlgorithmService pour pouvoir lancer les algorithmes
     */
    private final AlgorithmService algorithmService = new AlgorithmService();

    /**
     * Builder gson pour transformer un objet en JSON
     */
    private GsonBuilder builder;
    /**
     * Objet transformée en JSON
     */
    private Gson gson;


    /**
     * Méthode appelée au démarrage du serveur
     * Définition des différentes routes dans cette méthode
     */
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
        router.get("/api/vrp2/:popmin/:srcVertex")
                .handler(this::VRP2);

        // Lancement du serveur
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    /**
     * Appelée à la fermeture du serveur
     */
    @Override
    public void stop() {
        LOGGER.info("Fermeture du serveur");
    }


    /**
     * Méthode qui permet de récupérer les paramètres d'une URL en GET
     * @param routingContext le routingContext
     * @return la liste des paramètres
     */
    private List<Integer> getParamShortestPathAlgorithm(RoutingContext routingContext) {
        List<Integer> params = new ArrayList<>();
        if(routingContext.request().getParam("dmax") != null) {
            final int dmax = Integer.parseInt(routingContext.request().getParam("dmax"));
            params.add(dmax);
        }
        final int popmin = Integer.parseInt(routingContext.request().getParam("popmin"));
        params.add(popmin);
        if(routingContext.request().getParam("srcVertex") != null){
            final int srcVertex = Integer.parseInt(routingContext.request().getParam("srcVertex"));
            params.add(srcVertex);
        }
        if(routingContext.request().getParam("endVertex") != null){
            final int endVertex = Integer.parseInt(routingContext.request().getParam("endVertex"));
            params.add(endVertex);
        }
        return params;

    }

    /**
     * Méthode qui lance l'algorithme de Dijkstra (à l'appel de la bonne route)
     * @param routingContext le routing Context
     */
    private void dijkstra(RoutingContext routingContext) {
        List<Integer> params = getParamShortestPathAlgorithm(routingContext);
        final ShortestPathAlgorithmModel model = algorithmService.runDijkstra(params.get(0), params.get(1), params.get(2), params.get(3));
        setCorsPolicy(routingContext, model);

    }

    /**
     * Méthode qui lance l'algorithme de Dijkstra avec tas de Fibonacci (à l'appel de la bonne route)
     * @param routingContext le routing Context
     */
    private void dijkstraFibo(RoutingContext routingContext) {
        List<Integer> params = getParamShortestPathAlgorithm(routingContext);
        final ShortestPathAlgorithmModel model = algorithmService.runDijkstraFibo(params.get(0), params.get(1), params.get(2), params.get(3));
        setCorsPolicy(routingContext, model);

    }

    /**
     * Méthode qui lance l'algorithme A* (à l'appel de la bonne route)
     * @param routingContext le routing Context
     */
    private void astar(RoutingContext routingContext) {
        List<Integer> params = getParamShortestPathAlgorithm(routingContext);
        final ShortestPathAlgorithmModel model = algorithmService.runAstar(params.get(0), params.get(1), params.get(2), params.get(3));
        setCorsPolicy(routingContext, model);
    }

    /**
     * Méthode qui lance l'algorithme du VRP1 (à l'appel de la bonne route)
     * @param routingContext le routing Context
     */
    private void VRP1(RoutingContext routingContext) {

        final VRP1Model vrp1Model = algorithmService.runVRP1();
        setCorsPolicy(routingContext, vrp1Model);

    }

    /**
     * Méthode qui lance l'algorithme du VRP2 (à l'appel de la bonne route)
     * @param routingContext le routing Context
     */
    private void VRP2(RoutingContext routingContext) {
        List<Integer> params = getParamShortestPathAlgorithm(routingContext);

        final ShortestPathAlgorithmModel vrp2Model = algorithmService.runVRP2(params.get(0), params.get(1));
        setCorsPolicy(routingContext, vrp2Model);

    }

    /**
     * Méthode qui crée le fichier du graphe (à l'appel de la bonne route)
     * @param routingContext le routing Context
     */
    private void createFile(RoutingContext routingContext) {
        List<Integer> params = getParamShortestPathAlgorithm(routingContext);
        CSVtoTXT fileCSV = new CSVtoTXT();
        String fileName = "src/files/Communes_" + params.get(0) + "_" + params.get(1) + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(params.get(0), params.get(1), !file.isFile());

        setCorsPolicy(routingContext, fileCSV.getListCitiesModel());


    }

    /**
     * Méthode qui autorise les appels API et qui envoie la répons en JSON
     * @param routingContext le routing Context
     * @param model le model à transformer en JSON
     */
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
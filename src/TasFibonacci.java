public class TasFibonacci {

    private Vertex[] tas; // Structure de tas
    private int[] position; // indique la position d'un élément dans le tas (fonction reciproque)
    private int taille; // nombre d'éléments dans le tas


    /**
     * Crée un Tas Binaire de maximum {@code n} éléments
     * @param n nombre d'éléments maximum dans le tas
     */
    public TasFibonacci(int n) {
        tas = new Vertex[n + 1]; // structure du tas commence à l'indice 1
        position = new int[n];
        taille = 0;
    }

    public int taille() {
        return taille;
    }

    /**
     * ajoute le sommet {@code s} au tas binaire
     *
     * @param s est le sommet ajouté au tas binaire
     *          pour l'instant, s est ajouté en dernière position et la taille du tas est incrémentée
     */
    public void ajouter(Vertex s) {
        // TODO

        if (taille >= tas.length - 1) throw new IllegalArgumentException("le tas est plein");

        taille++;
        tas[taille] = s;
        position[s.getId()] = taille;
    }

    /** retire du tas le sommet de plus petite valeur
     *
     * @return le sommet de plus petite valeur
     */
    public Vertex retirer() {
        // TODO

        if (taille == 0) throw new IllegalArgumentException("le tas est vide");

        Vertex sMin = tas[1];
        int indiceMin = 1;

        for (int i = 2; i <= taille; i++) {
            if (sMin.getId() > tas[i].getId()) {
                indiceMin = i;
                sMin = tas[i];
            }
        }

        //enleve le min du tas
        position[tas[indiceMin].getId()] = 0 ;

        // déplace l'élément en dernière position dans le tas à la place du min qui a été rétiré
        if (taille > 1 && indiceMin != taille) {
            position[tas[taille].getId()] = indiceMin;
            tas[indiceMin] = tas[taille];
        }
        taille--;
        return sMin;
    }
}

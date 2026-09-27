
        package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

public class App {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hibernate-demo");

        insererDonnees(emf);

        mettreAJourPrix(emf, 1L, new BigDecimal("850.00"));

        rechercherParPlageDePrix(
                emf,
                new BigDecimal("200.00"),
                new BigDecimal("600.00")
        );

        supprimerProduit(emf, 3L);

        afficherTousLesProduits(emf);

        emf.close();
    }

    private static void insererDonnees(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Categorie electronique = new Categorie("Électronique");
            em.persist(electronique);

            Produit p1 = new Produit(
                    "Laptop",
                    new BigDecimal("999.99"),
                    electronique
            );

            Produit p2 = new Produit(
                    "Smartphone",
                    new BigDecimal("499.99"),
                    electronique
            );

            Produit p3 = new Produit(
                    "Tablette",
                    new BigDecimal("299.99"),
                    electronique
            );

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            // Validation de la transaction
            em.getTransaction().commit();

            System.out.println(
                    "--- Catégorie et produits insérés avec succès ---"
            );

        } finally {
            // Fermeture de l'EntityManager
            em.close();
        }
    }

    // Méthode pour modifier le prix d'un produit
    public static void mettreAJourPrix(
            EntityManagerFactory emf,
            Long id,
            BigDecimal nouveauPrix) {

        EntityManager em = emf.createEntityManager();

        try {
            // Début de la transaction
            em.getTransaction().begin();

            // Recherche du produit par son identifiant
            Produit p = em.find(Produit.class, id);

            if (p != null) {

                // Modification du prix
                p.setPrix(nouveauPrix);

                // Validation de la transaction
                em.getTransaction().commit();

                System.out.println(
                        "\n--- Prix du produit (ID=" + id
                                + ") mis à jour : " + nouveauPrix + " ---"
                );

            } else {

                System.out.println(
                        "\nLe produit n'existe pas."
                );
            }

        } finally {
            // Fermeture de l'EntityManager
            em.close();
        }
    }

    // Méthode pour supprimer un produit
    public static void supprimerProduit(
            EntityManagerFactory emf,
            Long id) {

        EntityManager em = emf.createEntityManager();

        try {
            // Début de la transaction
            em.getTransaction().begin();

            // Recherche du produit par son identifiant
            Produit p = em.find(Produit.class, id);

            if (p != null) {

                // Suppression du produit
                em.remove(p);

                // Validation de la transaction
                em.getTransaction().commit();

                System.out.println(
                        "\n--- Produit supprimé avec succès (ID="
                                + id + ") ---"
                );

            } else {

                System.out.println(
                        "\nLe produit n'existe pas."
                );
            }

        } finally {
            // Fermeture de l'EntityManager
            em.close();
        }
    }

    // Méthode pour rechercher les produits selon une plage de prix
    public static void rechercherParPlageDePrix(
            EntityManagerFactory emf,
            BigDecimal min,
            BigDecimal max) {

        EntityManager em = emf.createEntityManager();

        try {

            // Requête JPQL pour rechercher les produits
            String query =
                    "SELECT p FROM Produit p "
                            + "WHERE p.prix BETWEEN :minPrix AND :maxPrix";

            // Exécution de la requête
            List<Produit> resultats =
                    em.createQuery(query, Produit.class)
                            .setParameter("minPrix", min)
                            .setParameter("maxPrix", max)
                            .getResultList();

            System.out.println(
                    "\n--- Produits dont le prix est compris entre "
                            + min + " et " + max + " ---"
            );

            for (Produit p : resultats) {
                System.out.println(p);
            }

        } finally {

            em.close();
        }
    }

    private static void afficherTousLesProduits(
            EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();

        try {

            // Requête JPQL pour récupérer tous les produits
            List<Produit> liste =
                    em.createQuery(
                            "SELECT p FROM Produit p",
                            Produit.class
                    ).getResultList();

            System.out.println(
                    "\n--- Liste finale des produits dans la base ---"
            );

            for (Produit p : liste) {
                System.out.println(p);
            }

        } finally {

            em.close();
        }
    }
}

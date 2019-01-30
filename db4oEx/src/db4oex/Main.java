/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db4oex;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import ioc.dam.m6.exemples.db4o.Article;
import ioc.dam.m6.exemples.db4o.Envas;
import ioc.dam.m6.exemples.db4o.Magatzem;
import ioc.dam.m6.exemples.db4o.Producte;
import ioc.dam.m6.exemples.db4o.ProducteAGranel;
import ioc.dam.m6.exemples.db4o.ProducteEnvasat;
import ioc.dam.m6.exemples.db4o.UnitatDeMesura;
import java.util.List;

/**
 *
 * @author berna
 */
public class Main {

    public static void main(String[] args) {
//        Article articleaigua = new Article("1", "Aigua");
//        Producte aigua1 = new Producte(articleaigua, "Lanjarón", 1.99);
//        Producte aigua2 = new Producte(articleaigua, "FontVella", 0.99);
//        Producte platans = new Producte(new Article("3", "Platans"), "PlatanodeCanarias", 5.99);
//        Producte xocolata = new Producte(new Article("4", "Xocolata"), "Milka", 2.99);
//
//        Magatzem mag1 = new Magatzem("1", "Magatzem1");
//        mag1.assignarEstoc(aigua1, 10.0);
//        mag1.assignarEstoc(aigua2, 20.0);
//        mag1.assignarEstoc(platans, 50.0);
//        mag1.assignarEstoc(xocolata, 30.0);
//

        GestorProductes gest = new GestorProductes();
        try {

            instanciar(gest);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            gest.tancar();
        }
    }

    public static void instanciar(GestorProductes gest) {

        Article art1 = new Article("1", "Aigua");
        Article art2 = new Article("2", "Ous");
        Article art3 = new Article("3", "Pa");
        Article art4 = new Article("4", "Llet");
        Article art5 = new Article("5", "Taronjas");
        Article art6 = new Article("6", "Formatge");

        gest.obtenirObjecte(art1);
        gest.obtenirObjecte(art2);
        gest.obtenirObjecte(art3);
        gest.obtenirObjecte(art4);
        gest.obtenirObjecte(art5);
        gest.obtenirObjecte(art6);

        UnitatDeMesura un1 = new UnitatDeMesura("l", "Llitres");
        UnitatDeMesura un2 = new UnitatDeMesura("kg", "Kilograms");
        UnitatDeMesura un3 = new UnitatDeMesura("g", "Grams");

        gest.obtenirObjecte(un1);
        gest.obtenirObjecte(un2);
        gest.obtenirObjecte(un3);

        Envas env1 = new Envas("tetrabrick", 0, un1);
        Envas env2 = new Envas("paquet", 0, un3);
        Envas env3 = new Envas("al buit", 0, un3);

        gest.obtenirObjecte(env1);
        gest.obtenirObjecte(env2);
        gest.obtenirObjecte(env3);

        ProducteAGranel p1 = new ProducteAGranel(art5, 2.00, un2);
        ProducteEnvasat p2 = new ProducteEnvasat(art1, "Lanjarón", 6.00, env2);
        Producte p3 = new Producte(art3, "Hacendado", 5.00);
        ProducteAGranel p4 = new ProducteAGranel(art5, 3.00, un2);
        ProducteEnvasat p5 = new ProducteEnvasat(art1, "FontVella", 1.00, env2);
        Producte p6 = new Producte(art3, "Bimbo", 15.00);
        ProducteAGranel p7 = new ProducteAGranel(art2, 5.00, un2);
        ProducteEnvasat p8 = new ProducteEnvasat(art6, "García Baquero", 10.00, env2);
        Producte p9 = new Producte(art4, "Puleva", 3.00);
        ProducteEnvasat p10 = new ProducteEnvasat(art1, "Nestlé", 0.2, env2);

        gest.obtenirObjecte(p1);
        gest.obtenirObjecte(p2);
        gest.obtenirObjecte(p3);
        gest.obtenirObjecte(p4);
        gest.obtenirObjecte(p5);
        gest.obtenirObjecte(p6);
        gest.obtenirObjecte(p7);
        gest.obtenirObjecte(p8);
        gest.obtenirObjecte(p9);
        gest.obtenirObjecte(p10);

        Magatzem mag = new Magatzem("1", "Magatzem1");
        mag.assignarEstoc(p1, 10.0);
        mag.assignarEstoc(p2, 10.0);
        mag.assignarEstoc(p3, 10.0);
        mag.assignarEstoc(p4, 10.0);
        mag.assignarEstoc(p5, 10.0);
        mag.assignarEstoc(p6, 10.0);
        mag.assignarEstoc(p7, 10.0);
        mag.assignarEstoc(p8, 10.0);
        mag.assignarEstoc(p9, 10.0);
        mag.assignarEstoc(p10, 10.0);

        gest.gest(mag);

    }

    public static void productesMagatzem(GestorProductes gp, String mId, int max,int min) {
        List<Producte> llistaProductes = gp.obtenirProductePerPreuNom(min,max,mId);
        Magatzem m = (Magatzem) gp.gest(new Magatzem(mId, null));

        for (Producte p : llistaProductes) {
            System.out.println(p.getArticle().getDescripcio() + " "
                    + p.getMarca() + " " + m.getEstoc(p).getQuantitat());
        }
    }

    private static void modificarPreu(GestorProductes gp, Article a) {

        List<Producte> productes = gp.obtenirProductePerArticle(a);

        for (int pos = 0; pos < productes.size(); pos++) {
            System.out.println("El preu del article " + a.getDescripcio() + " "
                    + productes.get(pos).getMarca() + " abans era de "
                    + productes.get(pos).getPreu() + "€");

            productes.get(pos).setPreu(productes.get(pos).getPreu() * 1.05);

            System.out.println("El preu del article " + a.getDescripcio() + " "
                    + productes.get(pos).getMarca() + " ara es de "
                    + productes.get(pos).getPreu() + "€ \n");
        }

        gp.actualitzar();
    }

    private static void addStock(GestorProductes gp, Magatzem m,
            List<Producte> p, double increment) {

        for (Producte producte : p) {
            System.out.println("L'estoc de l'article "
                    + m.getEstoc(producte).getProducte().getArticle().getDescripcio() + " "
                    + m.getEstoc(producte).getProducte().getMarca() + " "
                    + "es de: " + m.getEstoc(producte).getQuantitat());

            m.incrementarEstocProducte(producte, increment);

            System.out.println("L'estoc de l'article "
                    + m.getEstoc(producte).getProducte().getArticle().getDescripcio() + " "
                    + m.getEstoc(producte).getProducte().getMarca() + " "
                    + "ara es de: " + m.getEstoc(producte).getQuantitat() + "\n");
        }
        gp.actualitzar();
    }

    private static void reduceStock(GestorProductes gp, Magatzem m,
            List<Producte> p, double decrement) {
        for (Producte producte : p) {

            System.out.println("L'estoc de l'article "
                    + m.getEstoc(producte).getProducte().getArticle().getDescripcio() + " "
                    + m.getEstoc(producte).getProducte().getMarca() + " "
                    + "es de: " + m.getEstoc(producte).getQuantitat());

            m.decrementarEstocProducte(producte, decrement);

            System.out.println("L'estoc de l'article "
                    + m.getEstoc(producte).getProducte().getArticle().getDescripcio() + " "
                    + m.getEstoc(producte).getProducte().getMarca() + " "
                    + "ara es de: " + m.getEstoc(producte).getQuantitat() + "\n");
        }
    }

}

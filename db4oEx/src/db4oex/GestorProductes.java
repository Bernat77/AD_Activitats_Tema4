/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db4oex;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import ioc.dam.m6.exemples.db4o.*;
import java.util.List;

/**
 *
 * @author berna
 */
public class GestorProductes {

    private String DB4OFILENAME = "conf.db4o";
    public ObjectContainer db;
    private EmbeddedConfiguration config;

    public GestorProductes() {

        // accessDb4o
        config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(Magatzem.class).cascadeOnDelete(true);
        config.common().activationDepth(10);
        db = Db4oEmbedded.openFile(config, DB4OFILENAME);
    }

    public void obrir() {
        if (db.ext().isClosed()) {
            db = Db4oEmbedded.openFile(config, DB4OFILENAME);
        }

    }

    public void tancar() {
        if (!db.ext().isClosed()) {
            db.commit();
            db.close();
        }

    }

    public void actualitzar() {
        db.commit();
    }

    public void elminar(Object obj) {
        try {
            db.delete(obj);
        } catch (Db4oIOException ex) {
            db.rollback();
        }
        db.commit();

    }

    public Object obtenirObjecte(Object obj) {
        ObjectSet objset = db.queryByExample(obj);
        if (objset.hasNext()) {
            obj = objset.next();
            if (objset.hasNext()) {
                throw new Db4oIOException("Poc descriptiu");
            }
        } else {
            System.out.println("No existent; Afegit");
            db.store(obj);
            actualitzar();
        }
        return obj;
    }

    public Article obtenirObjecte(Article art) {
        return (Article) obtenirObjecte((Object)art);
    }

    public List<Article> obtenirArticles() {
        return db.queryByExample(new Article());
    }

    public Envas obtenirObjecte(Envas art) {

        return (Envas) obtenirObjecte((Object)art);
    }

    public List<Envas> obtenirEnvasos() {
        return db.queryByExample(new Envas());
    }

    public Magatzem gest(Magatzem art) {

        return (Magatzem) obtenirObjecte((Object)art);
    }

    public List<Magatzem> obtenirMagatzems() {
        return db.queryByExample(new Magatzem());
    }

    public Producte obtenirObjecte(Producte art) {

        return (Producte) obtenirObjecte((Object)art);
    }

    public List<Producte> obtenirProductes() {
        return db.queryByExample(new Producte());
    }

    public List<Producte> obtenirProductePerArticle(Article art) {
        Query query = db.query();
        query.constrain(Producte.class);
        query.descend("article").descend("id").constrain(art.getId());
        return query.execute();

    }

    public List<Producte> obtenirProductePerPreuNom(int min, int max, String prefix) {
        Query query = db.query();
        query.constrain(Producte.class);
        query.descend("preu").constrain(min).greater();
        query.descend("preu").constrain(max).smaller();
        query.descend("article").descend("descripcio").constrain(prefix).startsWith(true);

        return query.execute();
    }

    public List<Producte> obtenirProductePerMagatzem(String Id, double quantitat) {

        ObjectSet ret = null;
        try {
            Magatzem mag = gest(new Magatzem(Id));
            ret = db.query(new Predicate<Producte>() {
                @Override
                public boolean match(Producte producte) {
                    return mag.getEstoc(producte).getQuantitat() <= quantitat;
                }
            });

        } catch (Db4oIOException ex) {
            System.out.println(ex.getMessage());
        }
        return ret;

    }

    public UnitatDeMesura obtenirObjecte(UnitatDeMesura art) {

        return (UnitatDeMesura) obtenirObjecte((Object)art);
    }

    public List<UnitatDeMesura> obteniUnitatDeMesures() {
        return db.queryByExample(new UnitatDeMesura());
    }

    public static void listResult(List<?> result) {
        System.out.println(result.size());
        for (Object o : result) {
            System.out.println(o);
        }
    }

}

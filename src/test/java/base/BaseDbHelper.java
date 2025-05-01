package base;

import base.manager.MyPUI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.io.IOException;
import java.util.List;

public abstract class BaseDbHelper {
    private static BaseProperties properties;
    private final EntityManager entityManager;

    {
        try {
            properties = new BaseProperties();
            entityManager = getEntityManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BaseProperties getProperties() {
        return properties;
    }

    public EntityManager getEntityManager() {
        PersistenceUnitInfo myPUI = new MyPUI(properties.getProperties());

        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider
                .createContainerEntityManagerFactory(myPUI, myPUI.getProperties());

            return entityManagerFactory.createEntityManager();
    }

    public <T> T findEntityById(Class<T> clazz, int id) {
        return entityManager.find(clazz, id);
    }

    public  <T, E> List<T> getListOfEntityByParam(String entityName, Class<T> clazz, String paramName, E paramValue) {
        TypedQuery<T> query = entityManager
                .createQuery("SELECT ce FROM " + entityName + " ce WHERE ce." + paramName + " = :" + paramName, clazz);
        query.setParameter(paramName, paramValue);

        return query.getResultList();
    }

    public <T> void createNewEntity(T entityName) {
        entityManager.getTransaction().begin();
        entityManager.persist(entityName);
        entityManager.getTransaction().commit();
    }

    public <T> void removeEntity(T entityName) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityName);
        entityManager.getTransaction().commit();
    }
}

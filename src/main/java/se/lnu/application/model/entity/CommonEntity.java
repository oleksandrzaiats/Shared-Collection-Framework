package se.lnu.application.model.entity;

import java.io.Serializable;

/**
 * Interface for all database entities.
 * <p>
 * We are using code-first approach. Database is created from classes with @{@link javax.persistence.Entity} annotation.
 * All changes in these classes will be automatically reflect to database structure.
 */
public interface CommonEntity extends Serializable {
}

package org.emu.membership.elastic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A EsField.
 */
@Entity
@Table(name = "es_field")
public class EsField implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "field_name")
    private String fieldName;


    @Column(name = "enabled")
    private boolean enabled;

    @ManyToOne
    @JsonIgnoreProperties(value = "esFields", allowSetters = true)
    private EsIndex esIndex;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public EsField fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public EsIndex getEsIndex() {
        return esIndex;
    }

    public EsField esIndex(EsIndex esIndex) {
        this.esIndex = esIndex;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public EsField enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEsIndex(EsIndex esIndex) {
        this.esIndex = esIndex;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EsField)) {
            return false;
        }
        return id != null && id.equals(((EsField) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsField{" +
                "id=" + getId() +
                ", fieldName='" + getFieldName() + "'" +
                ", enabled='" + isEnabled() + "'" +
                "}";
    }
}

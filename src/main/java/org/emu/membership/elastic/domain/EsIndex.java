package org.emu.membership.elastic.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A EsIndex.
 */
@Entity
@Table(name = "es_index")
public class EsIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "index_name")
    private String indexName;

    @Column(name = "full_qualified_name")
    private String fullQualifiedName;

    @Column(name = "java_name")
    private String javaName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "esIndex")
    private Set<EsField> esFields = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public EsIndex indexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getFullQualifiedName() {
        return fullQualifiedName;
    }

    public EsIndex fullQualifiedName(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
        return this;
    }

    public void setFullQualifiedName(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
    }

    public String getJavaName() {
        return javaName;
    }

    public EsIndex javaName(String javaName) {
        this.javaName = javaName;
        return this;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public Set<EsField> getEsFields() {
        return esFields;
    }

    public EsIndex esFields(Set<EsField> esFields) {
        this.esFields = esFields;
        return this;
    }

    public EsIndex addEsField(EsField esField) {
        this.esFields.add(esField);
        esField.setEsIndex(this);
        return this;
    }

    public EsIndex removeEsField(EsField esField) {
        this.esFields.remove(esField);
        esField.setEsIndex(null);
        return this;
    }

    public void setEsFields(Set<EsField> esFields) {
        this.esFields = esFields;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EsIndex)) {
            return false;
        }
        return id != null && id.equals(((EsIndex) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsIndex{" +
                "id=" + getId() +
                ", indexName='" + getIndexName() + "'" +
                ", fullQualifiedName='" + getFullQualifiedName() + "'" +
                ", javaName='" + getJavaName() + "'" +
                "}";
    }
}

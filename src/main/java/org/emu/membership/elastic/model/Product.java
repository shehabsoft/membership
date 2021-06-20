package org.emu.membership.elastic.model;

import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;


@Builder
@Entity
@Table(name = "PRODUCT")
@Document(indexName = "productindex")
public class Product {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long id;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Field(type = FieldType.Text, name = "name")
    @Column
    private String name;

    @Field(type = FieldType.Double, name = "price")
    @Column
    private Double price;

    @Field(type = FieldType.Integer, name = "quantity")
    @Column
    private Integer quantity;

    @Field(type = FieldType.Keyword, name = "category")
    @Column
    private String category;

    @Field(type = FieldType.Text, name = "desc")
    @Column
    private String description;

    @Field(type = FieldType.Keyword, name = "manufacturer")
    @Column
    private String manufacturer;


    public Product() {
    }

    public Product(String id, String name, Double price, Integer quantity, String category, String description, String manufacturer) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
        this.manufacturer = manufacturer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}

package org.fjorum.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "cat_name")
    private String name;
    @Column(name = "order_id")
    private int orderId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
    @OrderColumn(name = "order_id")
    private List<Category> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    protected Category parent;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<Topic> topics = new ArrayList<>();


    protected Category() {
    }

    public Category(String name, Category parent) {
        setName(name);
        if (parent == null) {
            setOrderId(0);
        } else {
            List<Category> children = parent.getChildren();
            setOrderId(children.isEmpty() ? 0 : children.get(children.size() - 1).orderId + 1);
            setParent(parent);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return id != null && parent == null;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}

package com.hibernate_concepts.spring_jpa_mapping.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties("hibernateLazyInitializer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tutorials")
public class Tutorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "published")
    private boolean published;

    @ManyToMany(fetch=FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tutorial_tags",
    joinColumns = {@JoinColumn(name = "tutorial_id")}, inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getTutorials().add(this);
    }

    public void removeTag(long tagId) {
        Tag tag = this.tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
        if(tag != null) {
            this.tags.remove(tag);
            tag.getTutorials().remove(this);
        }
    }

    public Tutorial(String title, String description, boolean published) {
        this.title = title;
        this.description = description;
        this.published = published;
    }

    @Override
    public String toString() {
        return "Tutorial id=[" + this.id +
                "title=" + this.title +
                "description=" + this.description +
                "published=" + this.published + "]";
    }
}

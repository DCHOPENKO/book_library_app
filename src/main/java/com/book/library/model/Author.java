package com.book.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode (exclude = "books")
@ToString (exclude = "books")
public class Author implements EntityClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstname;

    private String lastname;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH},
            mappedBy = "author", orphanRemoval = true)
    @Builder.Default
    private List<Book> books = new ArrayList<>();
}

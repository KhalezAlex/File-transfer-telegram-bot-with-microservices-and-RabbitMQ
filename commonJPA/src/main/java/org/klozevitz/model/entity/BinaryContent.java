package org.klozevitz.model.entity;


import lombok.*;

import javax.persistence.*;

/**
 * The byte[] field will map to common bytea format. Almost equals @BLOB
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Builder
@Entity
@Table(name = "binary_content")
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] fileAsByteArray;
}

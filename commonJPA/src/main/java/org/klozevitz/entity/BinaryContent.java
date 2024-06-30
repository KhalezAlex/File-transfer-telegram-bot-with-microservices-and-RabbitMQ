package org.klozevitz.entity;


import lombok.*;

import javax.persistence.*;

/**
 * Поле типа byte[] будет иметь формат "bytea", аналогичный формату, диктуемому аннотацией @BLOB
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

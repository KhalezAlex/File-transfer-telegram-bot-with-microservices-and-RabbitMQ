package org.klozevitz.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * This entity is common to ApplicationDocument entity except of 2 fields
 * cause photo does not have this fields in telegram context
 * */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Builder
@Entity
@Table(name = "app_photo")
public class ApplicationPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String telegramFileId;
    @OneToOne
    private BinaryContent binaryContent;
    private Integer fileSize;
}

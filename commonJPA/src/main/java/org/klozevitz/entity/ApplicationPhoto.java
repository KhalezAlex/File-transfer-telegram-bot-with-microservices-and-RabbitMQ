package org.klozevitz.entity;

import lombok.*;
import org.klozevitz.entity.BinaryContent;

import javax.persistence.*;

/**
 * Сущность создана на основе сущности ApplicationDocument, но удалены 2 поля,
 * тк для фото они из телеграмма не приходят
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

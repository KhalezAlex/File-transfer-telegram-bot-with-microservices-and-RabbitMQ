package org.klozevitz.model.repositories;

import org.klozevitz.model.entity.ApplicationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationPhotoRepository extends JpaRepository<ApplicationPhoto, Long> {
}

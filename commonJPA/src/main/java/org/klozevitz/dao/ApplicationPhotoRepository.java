package org.klozevitz.dao;

import org.klozevitz.entity.ApplicationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationPhotoRepository extends JpaRepository<ApplicationPhoto, Long> {
}

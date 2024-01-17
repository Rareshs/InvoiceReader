package org.scrum.domain.repositories;

import org.scrum.domain.models.FurnizorMdl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface FurnizorRepo extends JpaRepository<FurnizorMdl, Long> {

    Optional<FurnizorMdl> findByName(String name);
}

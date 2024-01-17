package org.scrum.domain.repositories;

import org.scrum.domain.models.ClientMdl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepo extends JpaRepository<ClientMdl, Long> {
    //custom querry sa gasim un client dupa nume...maybe add some more?
    Optional<ClientMdl> findByName(String name);
}




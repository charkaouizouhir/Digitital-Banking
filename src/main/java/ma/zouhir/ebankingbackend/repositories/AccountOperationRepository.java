package ma.zouhir.ebankingbackend.repositories;

import ma.zouhir.ebankingbackend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

}

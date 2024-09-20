package ma.zouhir.ebankingbackend.repositories;

import ma.zouhir.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

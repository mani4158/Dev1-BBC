package com.example.dev1.Repository;
import com.example.dev1.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByCustId(String custId);
}

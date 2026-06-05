package az.myapp.retroride.dao.repository;

import az.myapp.retroride.dao.utility.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository
        extends JpaRepository<Car,Long> {
    // Aktiv elanları getir — main sehifeycun
    List<Car> findByStatus(Car.CarStatus status);

    // userlerin elanları — profil üçün
    List<Car> findByUserId(Long userId);

    // Marka axtarış — search ucun
    List<Car> findByBrandContainingIgnoreCase(String brand);
}

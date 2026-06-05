package az.myapp.retroride.dao.repository;

import az.myapp.retroride.dao.utility.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {

    // Maşının bütün şəkillərini sıra ilə gətir
    List<CarImage> findByCarIdOrderBySortOrderAsc(Long carId);

    // Maşının şəkil sayı
    int countByCarId(Long carId);

    // Maşının bütün şəkillərini sil (elan silinəndə)
    void deleteAllByCarId(Long carId);
}
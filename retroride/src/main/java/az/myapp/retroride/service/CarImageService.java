package az.myapp.retroride.service;

import az.myapp.retroride.dao.repository.CarImageRepository;
import az.myapp.retroride.dao.repository.CarRepository;
import az.myapp.retroride.dao.utility.Car;
import az.myapp.retroride.dao.utility.CarImage;
import az.myapp.retroride.dao.utility.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
@Service
@RequiredArgsConstructor
public class CarImageService {

    private final CarImageRepository carImageRepository;
    private final CarRepository carRepository;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    public CarImage uploadImage(Long carId, MultipartFile file) throws IOException {


        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Elan tapılmadı"));

        checkOwnership(car);


        int currentCount = carImageRepository.countByCarId(carId);
        if (currentCount >= 5) {
            throw new RuntimeException("Maksimum 5 şəkil yükləyə bilərsiniz");
        }


        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Yalnız şəkil faylı yükləyə bilərsiniz");
        }


        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".jpg";
        String fileName = UUID.randomUUID() + extension;


        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);


        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        CarImage image = CarImage.builder()
                .car(car)
                .fileName(fileName)
                .sortOrder(currentCount) // növbəti sıra
                .build();

        return carImageRepository.save(image);
    }


    public List<Map<String, Object>> getImageUrls(Long carId) {
        return carImageRepository.findByCarIdOrderBySortOrderAsc(carId)
                .stream()
                .map(img -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", img.getId());
                    map.put("url", "/api/images/" + img.getFileName());
                    return map;
                })
                .toList();
    }


    public void deleteImage(Long imageId) throws IOException {
        CarImage image = carImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Şəkil tapılmadı"));

        checkOwnership(image.getCar());


        Path filePath = Paths.get(uploadDir).resolve(image.getFileName());
        Files.deleteIfExists(filePath);


        carImageRepository.delete(image);
    }

    //  bu elan bu istifadecinindi?
    private void checkOwnership(Car car) {
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (!car.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bu elan sizin deyil");
        }
    }
}
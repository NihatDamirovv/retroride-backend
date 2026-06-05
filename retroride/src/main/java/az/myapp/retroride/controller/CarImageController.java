package az.myapp.retroride.controller;

import az.myapp.retroride.service.CarImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CarImageController {

    private final CarImageService carImageService;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    // POST /api/cars/5/images — şəkil yüklə
    // multipart/form-data ilə göndərilir — form-dan fayl seçəndə belə olur
    @PostMapping("/api/cars/{carId}/images")
    public ResponseEntity<Map<String, String>> uploadImage(
            @PathVariable Long carId,
            @RequestParam("file") MultipartFile file) {
        try {
            carImageService.uploadImage(carId, file);
            return ResponseEntity.ok(Map.of("message", "Şəkil uğurla yükləndi"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Şəkil saxlanılarkən xəta baş verdi"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/cars/5/images — maşının şəkil URL-lərini gətir
    @GetMapping("/api/cars/{carId}/images")
    public ResponseEntity<List<Map<String, Object>>> getImages(@PathVariable Long carId) {
        return ResponseEntity.ok(carImageService.getImageUrls(carId));
    }

    // DELETE /api/images/{imageId} — şəkili sil
    @DeleteMapping("/api/images/{imageId}")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable Long imageId) {
        try {
            carImageService.deleteImage(imageId);
            return ResponseEntity.ok(Map.of("message", "Şəkil silindi"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Şəkil silinərkən xəta baş verdi"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/images/abc123.jpg — şəkili göstər (brauzerdə açılır)
    // GET /api/images/abc123.jpg — İndi istifadəçini birbaşa S3-ə yönləndirir (Redirect edir)
    @GetMapping("/api/images/{fileName:.+}")
    public ResponseEntity<Void> serveImage(@PathVariable String fileName) {
        String s3BucketUrl = "https://retroride-sekiller-nihat.s3.eu-north-1.amazonaws.com/";
        return ResponseEntity.status(org.springframework.http.HttpStatus.FOUND)
                .location(java.net.URI.create(s3BucketUrl + fileName))
                .build();
    }
}
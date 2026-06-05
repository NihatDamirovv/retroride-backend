package az.myapp.retroride.dao.utility;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hansı maşına aid olduğu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    // Şəkil DB-də saxlanılır (base64 deyil, fayl olaraq)
    @Column(name = "file_name", nullable = false)
    private String fileName;

    // Neçənci şəkil olduğu — sıralama üçün
    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;
}
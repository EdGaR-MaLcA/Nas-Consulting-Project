package art.gallery.demo.Picture.Repository;

import art.gallery.demo.Picture.Entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}

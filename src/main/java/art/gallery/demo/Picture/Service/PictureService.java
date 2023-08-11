package art.gallery.demo.Picture.Service;

import art.gallery.demo.Picture.Entity.Picture;
import art.gallery.demo.Picture.Repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final PictureRepository pictureRepository;

    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    public Optional<Picture> getPictureById(Long id) {
        return pictureRepository.findById(id);
    }

    public Picture createPicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    public Picture updatePicture(Long id, Picture updatedPicture) {
        Picture picture = pictureRepository.findById(id).orElse(null);
        if (picture != null) {
            picture.setTitle(updatedPicture.getTitle());
            picture.setArtist(updatedPicture.getArtist());
            picture.setCity(updatedPicture.getCity());
            picture.setUrl(updatedPicture.getUrl());
            picture.setDescription(updatedPicture.getDescription());
            picture.setVotes(updatedPicture.getVotes());
            return pictureRepository.save(picture);
        }
        return null;
    }

    public void deletePicture(Long id) {
        pictureRepository.deleteById(id);
    }

    public Picture voteForPicture(Long id) {
        Optional<Picture> optionalPicture = pictureRepository.findById(id);
        if (optionalPicture.isPresent()) {
            Picture picture = optionalPicture.get();
            picture.setVotes(picture.getVotes() + 1);
            return pictureRepository.save(picture);
        }
        return null;
    }
}
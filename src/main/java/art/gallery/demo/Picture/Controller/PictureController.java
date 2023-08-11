package art.gallery.demo.Picture.Controller;

import art.gallery.demo.Picture.Entity.Picture;
import art.gallery.demo.Picture.Service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import art.gallery.demo.auth.AuthService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/votacion")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://resonant-salmiakki-5e751d.netlify.app")
public class PictureController {

    private final PictureService pictureService;
    private final AuthService authService;

    @GetMapping("/pictures")
    public ResponseEntity<List<Picture>> getAllPictures() {
        List<Picture> pictures = pictureService.getAllPictures();
        return new ResponseEntity<>(pictures, HttpStatus.OK);
    }

    @GetMapping("/pictures/{id}")
    public ResponseEntity<Picture> getPictureById(@PathVariable Long id) {
        Optional<Picture> picture = pictureService.getPictureById(id);
        return picture.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/pictures")
    public ResponseEntity<?> createPicture(@RequestBody Picture picture) {
        if (isAdminUser()) {
            Picture createdPicture = pictureService.createPicture(picture);
            return new ResponseEntity<>(createdPicture, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Only admin users can delete pictures", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/pictures/{id}")
    public ResponseEntity<?> updatePicture(@PathVariable Long id, @RequestBody Picture updatedPicture) {
        if (isAdminUser()) {
            Picture updated = pictureService.updatePicture(id, updatedPicture);
            if (updated != null) {
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Only admin users can update pictures", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/pictures/{id}")
    public ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        if (isAdminUser()) {
            pictureService.deletePicture(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private boolean isAdminUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ADMIN"));
    }

    @PostMapping("/pictures/{id}/vote")
    public ResponseEntity<?> voteForPicture(@PathVariable Long id) {
        Picture votedPicture = pictureService.voteForPicture(id);
        if (votedPicture != null) {UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            authService.updateUserStatusAfterVoting(userDetails);
            return new ResponseEntity<>(votedPicture, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    }



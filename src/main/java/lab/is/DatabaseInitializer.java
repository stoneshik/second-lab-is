package lab.is;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Album;
import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.bd.entities.Nomination;
import lab.is.bd.entities.Studio;
import lab.is.repositories.AlbumRepository;
import lab.is.repositories.CoordinatesRepository;
import lab.is.repositories.MusicBandRepository;
import lab.is.repositories.NominationRepository;
import lab.is.repositories.StudioRepository;
import lab.is.security.bd.entities.Role;
import lab.is.security.bd.entities.RoleEnum;
import lab.is.security.bd.entities.User;
import lab.is.security.repositories.RoleRepository;
import lab.is.security.repositories.UserRepository;
import lab.is.services.musicband.MusicBandService;
import lombok.RequiredArgsConstructor;

@Component
@Profile({"dev", "helios", "jmeter"})
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final CoordinatesRepository coordinatesRepository;
    private final AlbumRepository albumRepository;
    private final StudioRepository studioRepository;
    private final MusicBandRepository musicBandRepository;
    private final MusicBandService musicBandService;
    private final NominationRepository nominationRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (musicBandRepository.count() > 0) return;

        Role userRole = Role.builder().name(RoleEnum.ROLE_USER).build();
        Role adminRole = Role.builder().name(RoleEnum.ROLE_ADMIN).build();
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        User admin = User.builder().login("admin").password(encoder.encode("admin")).roles(Set.of(userRole, adminRole)).build();
        userRepository.save(admin);

        Coordinates c1 = Coordinates.builder().x(10.0f).y(20).build();
        Coordinates c2 = Coordinates.builder().x(-5.2f).y(30).build();
        Coordinates c3 = Coordinates.builder().x(0f).y(0).build();
        Coordinates c4 = Coordinates.builder().x(12.3f).y(-7).build();
        Coordinates c5 = Coordinates.builder().x(3.3f).y(15).build();
        coordinatesRepository.save(c1);
        coordinatesRepository.save(c2);
        coordinatesRepository.save(c3);
        coordinatesRepository.save(c4);
        coordinatesRepository.save(c5);

        Album a1 = Album.builder().name("Echoes of Silence").length(45).build();
        Album a2 = Album.builder().name("Midnight Dreams").length(60).build();
        Album a3 = Album.builder().name("Golden Horizons").length(50).build();
        Album a4 = Album.builder().name("Lost in Time").length(40).build();
        Album a5 = Album.builder().name("Neon Lights").length(55).build();
        albumRepository.save(a1);
        albumRepository.save(a2);
        albumRepository.save(a3);
        albumRepository.save(a4);
        albumRepository.save(a5);

        Studio s1 = Studio.builder().name("Sunset Sound").address("123 Sunset Blvd").build();
        Studio s2 = Studio.builder().name("Maple Leaf Studios").address("456 Maple St").build();
        Studio s3 = Studio.builder().name("River Side Studio").address("789 River Rd").build();
        studioRepository.save(s1);
        studioRepository.save(s2);
        studioRepository.save(s3);

        MusicBand b1 = MusicBand.builder()
                .name("The Echoes")
                .coordinates(c1)
                .creationDate(LocalDateTime.of(2022, 3, 10, 10, 0))
                .genre(MusicGenre.PROGRESSIVE_ROCK)
                .numberOfParticipants(5L)
                .singlesCount(3L)
                .description("Группа прогрессивного рока с мелодичными гармониями")
                .bestAlbum(a1)
                .albumsCount(2)
                .establishmentDate(LocalDate.of(2018, 5, 12))
                .studio(s1)
                .build();

        MusicBand b2 = MusicBand.builder()
                .name("Midnight Pulse")
                .coordinates(c2)
                .creationDate(LocalDateTime.of(2021, 6, 5, 12, 0))
                .genre(MusicGenre.POST_PUNK)
                .numberOfParticipants(4L)
                .singlesCount(2L)
                .description("Experimental post-punk band")
                .bestAlbum(a2)
                .albumsCount(1)
                .establishmentDate(LocalDate.of(2019, 9, 1))
                .studio(s2)
                .build();

        MusicBand b3 = MusicBand.builder()
                .name("Golden Horizons")
                .coordinates(c3)
                .creationDate(LocalDateTime.of(2020, 1, 20, 9, 0))
                .genre(MusicGenre.BRIT_POP)
                .numberOfParticipants(6L)
                .singlesCount(5L)
                .description("Брит-поп ансамбль с мелодичным вокалом")
                .bestAlbum(a3)
                .albumsCount(3)
                .establishmentDate(LocalDate.of(2015, 2, 15))
                .studio(null)
                .build();

        MusicBand b4 = MusicBand.builder()
                .name("Lost Frequencies")
                .coordinates(c4)
                .creationDate(LocalDateTime.of(2023, 2, 14, 14, 30))
                .genre(MusicGenre.PROGRESSIVE_ROCK)
                .numberOfParticipants(5L)
                .singlesCount(1L)
                .description("Experimental rock with layered synths")
                .bestAlbum(a4)
                .albumsCount(1)
                .establishmentDate(LocalDate.of(2020, 6, 20))
                .studio(s1)
                .build();

        MusicBand b5 = MusicBand.builder()
                .name("Neon Waves")
                .coordinates(c5)
                .creationDate(LocalDateTime.of(2022, 7, 1, 11, 0))
                .genre(MusicGenre.POST_PUNK)
                .numberOfParticipants(3L)
                .singlesCount(4L)
                .description("Dynamic post-punk trio")
                .bestAlbum(a5)
                .albumsCount(2)
                .establishmentDate(LocalDate.of(2019, 3, 10))
                .studio(s2)
                .build();

        MusicBand b6 = MusicBand.builder()
                .name("Silver Lining")
                .coordinates(c1)
                .creationDate(LocalDateTime.of(2019, 5, 5, 10, 0))
                .genre(MusicGenre.PROGRESSIVE_ROCK)
                .numberOfParticipants(4L)
                .singlesCount(2L)
                .bestAlbum(a1)
                .albumsCount(2)
                .establishmentDate(LocalDate.of(2017, 5, 5))
                .studio(s3)
                .build();

        MusicBand b7 = MusicBand.builder()
                .name("Crimson Tide")
                .coordinates(c2)
                .creationDate(LocalDateTime.of(2018, 8, 8, 15, 0))
                .genre(MusicGenre.POST_PUNK)
                .numberOfParticipants(5L)
                .singlesCount(3L)
                .bestAlbum(a2)
                .albumsCount(3)
                .establishmentDate(LocalDate.of(2018, 8, 8))
                .studio(s3)
                .build();

        MusicBand b8 = MusicBand.builder()
                .name("Blue Horizon")
                .coordinates(c3)
                .creationDate(LocalDateTime.of(2016, 2, 2, 12, 0))
                .genre(MusicGenre.BRIT_POP)
                .numberOfParticipants(6L)
                .singlesCount(5L)
                .bestAlbum(a3)
                .albumsCount(2)
                .establishmentDate(LocalDate.of(2016, 2, 2))
                .studio(null)
                .build();

        MusicBand b9 = MusicBand.builder()
                .name("Amber Sky")
                .coordinates(c4)
                .creationDate(LocalDateTime.of(2019, 9, 9, 10, 0))
                .genre(MusicGenre.PROGRESSIVE_ROCK)
                .numberOfParticipants(4L)
                .singlesCount(1L)
                .bestAlbum(a4)
                .albumsCount(1)
                .establishmentDate(LocalDate.of(2019, 9, 9))
                .studio(s1)
                .build();

        MusicBand b10 = MusicBand.builder()
                .name("Silver Moon")
                .coordinates(c5)
                .creationDate(LocalDateTime.of(2020, 10, 10, 11, 0))
                .genre(MusicGenre.POST_PUNK)
                .numberOfParticipants(5L)
                .singlesCount(2L)
                .bestAlbum(a5)
                .albumsCount(3)
                .establishmentDate(LocalDate.of(2020, 10, 10))
                .studio(s2)
                .build();

        musicBandService.create(b1);
        musicBandService.create(b2);
        musicBandService.create(b3);
        musicBandService.create(b4);
        musicBandService.create(b5);
        musicBandService.create(b6);
        musicBandService.create(b7);
        musicBandService.create(b8);
        musicBandService.create(b9);
        musicBandService.create(b10);

        nominationRepository.save(
                Nomination.builder().musicBand(b1).musicGenre(MusicGenre.PROGRESSIVE_ROCK).build()
        );
        nominationRepository.save(
                Nomination.builder().musicBand(b2).musicGenre(MusicGenre.POST_PUNK).build()
        );
        nominationRepository.save(
                Nomination.builder().musicBand(b3).musicGenre(MusicGenre.BRIT_POP).build()
        );
        nominationRepository.save(
                Nomination.builder().musicBand(b6).musicGenre(MusicGenre.PROGRESSIVE_ROCK).build()
        );
        nominationRepository.save(
                Nomination.builder().musicBand(b10).musicGenre(MusicGenre.POST_PUNK).build()
        );
    }
}

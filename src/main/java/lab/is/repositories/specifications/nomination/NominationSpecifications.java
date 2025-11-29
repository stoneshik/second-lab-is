package lab.is.repositories.specifications.nomination;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import lab.is.bd.entities.MusicGenre;
import lab.is.bd.entities.Nomination;
import lab.is.repositories.specifications.MySpecification;
import lab.is.repositories.specifications.musicband.MusicBandFieldNames;

@Component
public final class NominationSpecifications extends MySpecification<Nomination> {
    public Specification<Nomination> musicBandIdEquals(Long musicBandId) {
        return fieldValueFromEntityEquals(
            NominationFieldNames.MUSIC_BAND,
            NominationFieldNames.MUSIC_BAND_ID,
            musicBandId
        );
    }

    public Specification<Nomination> genreEquals(MusicGenre fieldValue) {
        if (fieldValue == null) return null;
        return fieldValueEquals(
            MusicBandFieldNames.GENRE,
            fieldValue.name()
        );
    }

    public Specification<Nomination> nominatedAfterOrEq(LocalDateTime from) {
        return fieldDatetimeValueAfterOrEq(
            NominationFieldNames.NOMINATED_AT,
            from
        );
    }

    public Specification<Nomination> nominatedBeforeOrEq(LocalDateTime to) {
        return fieldDatetimeValueBeforeOrEq(
            NominationFieldNames.NOMINATED_AT,
            to
        );
    }
}

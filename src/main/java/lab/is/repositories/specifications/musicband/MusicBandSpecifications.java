package lab.is.repositories.specifications.musicband;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.repositories.specifications.MySpecification;

@Component
public final class MusicBandSpecifications extends MySpecification<MusicBand> {
    public Specification<MusicBand> nameLike(String fieldValue) {
        return fieldStringValueLike(
            MusicBandFieldNames.NAME,
            fieldValue
        );
    }

    public Specification<MusicBand> genreEquals(MusicGenre fieldValue) {
        if (fieldValue == null) return null;
        return fieldValueEquals(
            MusicBandFieldNames.GENRE,
            fieldValue.name()
        );
    }

    public Specification<MusicBand> descriptionLike(String fieldValue) {
        return fieldStringValueLike(
            MusicBandFieldNames.DESCRIPTION,
            fieldValue
        );
    }

    public Specification<MusicBand> bestAlbumNameLike(String fieldValue) {
        return fieldStringValueFromEntityWithJoinLike(
            MusicBandFieldNames.BEST_ALBUM,
            MusicBandFieldNames.BEST_ALBUM_NAME,
            fieldValue
        );
    }

    public Specification<MusicBand> studioNameLike(String fieldValue) {
        return fieldStringValueFromEntityWithJoinLike(
            MusicBandFieldNames.STUDIO,
            MusicBandFieldNames.STUDIO_NAME,
            fieldValue
        );
    }

    public Specification<MusicBand> studioAddressLike(String fieldValue) {
        return fieldStringValueFromEntityWithJoinLike(
            MusicBandFieldNames.STUDIO,
            MusicBandFieldNames.STUDIO_ADDRESS,
            fieldValue
        );
    }
}


package lab.is.util;

import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.Nomination;
import lab.is.dto.responses.nomination.NominationResponseDto;

public class NominationMapper {
    private NominationMapper() {}

    public static NominationResponseDto toDtoFromEntity(Nomination nominationEntity) {
        MusicBand musicBand = nominationEntity.getMusicBand();
        return NominationResponseDto.builder()
            .id(nominationEntity.getId())
            .musicBandId(musicBand.getId())
            .musicBandName(musicBand.getName())
            .musicGenre(musicBand.getGenre())
            .nominatedAt(nominationEntity.getNominatedAt())
            .build();
    }
}

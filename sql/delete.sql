DROP TABLE IF EXISTS
    albums,
    coordinates,
    insertion_histories,
    music_bands,
    nominations,
    refresh_token,
    roles,
    studios,
    user_roles,
    users
CASCADE;
DROP SEQUENCE IF EXISTS
    album_seq,
    coordinates_seq,
    music_band_seq,
    nominations_seq,
    refresh_token_seq,
    role_seq,
    studio_seq,
    insertion_histories_seq,
    user_seq
CASCADE;

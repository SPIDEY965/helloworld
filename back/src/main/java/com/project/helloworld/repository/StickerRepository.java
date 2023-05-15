package com.project.helloworld.repository;

import com.project.helloworld.domain.Sticker;
import com.project.helloworld.dto.response.BoardCategoryCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StickerRepository extends JpaRepository<Sticker,Long> {
    @Query(value="SELECT * FROM sticker where user_seq = :userSeq and board_seq = :boardSeq and type=:type;", nativeQuery = true)
    Sticker stickerByBoardAndUser(@Param("userSeq") Long userSeq,@Param("boardSeq") Long boardSeq, @Param("type") Integer type);
}

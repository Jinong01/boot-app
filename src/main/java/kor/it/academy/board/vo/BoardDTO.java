package kor.it.academy.board.vo;
import kor.it.academy.board.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter@Setter@Builder@NoArgsConstructor@AllArgsConstructor
public class BoardDTO {

    private Integer seq;
    private String title;
    private String writer;
    private String contents;
    private Integer readCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Set<BoardFileDTO> boardFiles = new LinkedHashSet<>();

    public static BoardDTO of(BoardEntity entity){
        Set<BoardFileDTO> boardFiles =
                entity.getBoardFiles().stream().map(BoardFileDTO::of)
                        .collect(Collectors.toSet());

        return BoardDTO.builder()
                .seq(entity.getSeq())
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .contents(entity.getContents())
                .readCount(entity.getReadCount())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .boardFiles(boardFiles).build();
    }

}

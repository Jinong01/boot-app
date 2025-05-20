package kor.it.academy.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Integer seq;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "writer", nullable = false, length = 50)
    private String writer;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "read_count")
    private Integer readCount;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "board")
    private Set<BoardFileEntity> boardFiles = new LinkedHashSet<>();

}